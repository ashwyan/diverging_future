package com.diverging.futures.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        // Mapping to your script's "user" and "choice" keys
        sendDataToRemote(mapOf(
            "user" to "VOTE",
            "choice" to "${lensType.label} (Site: $siteId)"
        ))
    }

    fun logSignature(name: String, affiliation: String, email: String) {
        // Mapping to your script's "user" and "choice" keys
        sendDataToRemote(mapOf(
            "user" to "SIGNATURE: $name",
            "choice" to "$affiliation ($email)"
        ))
    }

    private fun sendDataToRemote(data: Map<String, String>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Construct JSON to match your data.user and data.choice
                val json = data.entries.joinToString(
                    prefix = "{",
                    postfix = "}",
                    separator = ","
                ) { (key, value) -> 
                    "\"$key\": \"${value.replace("\"", "\\\"")}\"" 
                }

                var currentUrl = WEBHOOK_URL
                var responseCode: Int
                var redirects = 0
                val maxRedirects = 5

                while (redirects < maxRedirects) {
                    val url = URL(currentUrl)
                    val conn = url.openConnection() as HttpURLConnection
                    
                    // IMPORTANT: Google Script redirects POST to GET. 
                    // To keep the data, we must handle the redirect manually.
                    conn.requestMethod = if (redirects == 0) "POST" else "GET"
                    conn.doOutput = redirects == 0
                    conn.instanceFollowRedirects = false 
                    conn.setRequestProperty("Content-Type", "application/json")

                    if (redirects == 0) {
                        conn.outputStream.use { it.write(json.toByteArray()) }
                    }

                    responseCode = conn.responseCode
                    Log.d(TAG, "Status: $responseCode from $currentUrl")

                    // Handle Google's Redirect (302/301/307/308)
                    if (responseCode == 301 || responseCode == 302 || responseCode == 303 || responseCode == 307 || responseCode == 308) {
                        val newUrl = conn.getHeaderField("Location")
                        if (newUrl == null) break
                        currentUrl = newUrl
                        redirects++
                        conn.disconnect()
                        continue
                    }

                    if (responseCode == 401) {
                        Log.e(TAG, "!!! PERMISSION ERROR !!!")
                        Log.e(TAG, "Your Google Script is set to 'Anyone within berkeley.edu'.")
                        Log.e(TAG, "Go to Deploy -> Manage Deployments and set 'Who has access' to 'Anyone'.")
                    }

                    if (responseCode in 200..299) {
                        Log.d(TAG, "SUCCESS: Data visible in Sheet!")
                    }
                    
                    conn.disconnect()
                    break
                }
            } catch (e: Exception) {
                Log.e(TAG, "Network Error: ${e.localizedMessage}")
            }
        }
    }
}
