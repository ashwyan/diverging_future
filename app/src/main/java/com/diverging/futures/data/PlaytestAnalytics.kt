package com.diverging.futures.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object PlaytestAnalytics {
    private const val TAG = "PlaytestAnalytics"
    
    // THE SCRIPT URL
    private const val WEBHOOK_URL = "https://script.google.com/a/macros/berkeley.edu/s/AKfycbwKsaFlUfIVbCEyJ0rXrXSE-T9FoJgCNIimjx1e13K_ccr4O0R6xG8taEcLnWE1haMe0A/exec"

    fun logLensSelection(siteId: String, lensType: LensType) {
        Log.d(TAG, "LENS_SELECTED: Site=$siteId, Lens=${lensType.label}")
    }

    fun logVote(siteId: String, lensType: LensType) {
        sendDataToRemote(mapOf(
            "user" to "VOTE",
            "choice" to "${lensType.label} (Site: $siteId)"
        ))
    }

    fun logSignature(name: String, affiliation: String, email: String) {
        sendDataToRemote(mapOf(
            "user" to "SIGNATURE: $name",
            "choice" to "$affiliation ($email)"
        ))
    }

    private fun sendDataToRemote(data: Map<String, String>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Use JSONObject for safe JSON construction
                val json = JSONObject().apply {
                    data.forEach { (key, value) -> put(key, value) }
                }.toString()

                Log.d(TAG, "Attempting to send: $json")

                var currentUrl = WEBHOOK_URL
                var redirects = 0
                val maxRedirects = 5

                while (redirects < maxRedirects) {
                    val url = URL(currentUrl)
                    val conn = url.openConnection() as HttpURLConnection
                    
                    // Google Apps Script requires manual redirect handling to maintain data
                    conn.requestMethod = if (redirects == 0) "POST" else "GET"
                    conn.doOutput = (conn.requestMethod == "POST")
                    conn.instanceFollowRedirects = false 
                    conn.connectTimeout = 15000
                    conn.readTimeout = 15000
                    conn.setRequestProperty("Content-Type", "application/json")
                    
                    if (conn.requestMethod == "POST") {
                        conn.outputStream.use { it.write(json.toByteArray()) }
                    }

                    val responseCode = conn.responseCode
                    Log.d(TAG, "Response Code: $responseCode for $currentUrl")

                    // Handle Redirect (302 is standard for GAS)
                    if (responseCode == 301 || responseCode == 302 || responseCode == 303 || responseCode == 307 || responseCode == 308) {
                        val newUrl = conn.getHeaderField("Location")
                        if (newUrl != null) {
                            Log.d(TAG, "Redirecting to: $newUrl")
                            currentUrl = newUrl
                            redirects++
                            conn.disconnect()
                            continue
                        }
                    }

                    // Log success or error details
                    if (responseCode in 200..299) {
                        val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                        Log.d(TAG, "SUCCESS: Server responded: $responseText")
                    } else {
                        val errorText = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "No error body"
                        Log.e(TAG, "ERROR ($responseCode): $errorText")
                        if (responseCode == 401) {
                            Log.e(TAG, "PERMISSION DENIED: Ensure your GAS Deployment is set to 'Who has access: Anyone'.")
                        }
                    }
                    
                    conn.disconnect()
                    break
                }
            } catch (e: Exception) {
                Log.e(TAG, "Network Error: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }
}
