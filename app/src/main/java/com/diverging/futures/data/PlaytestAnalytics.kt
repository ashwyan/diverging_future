package com.diverging.futures.data

import android.util.Log

object PlaytestAnalytics {
    private const val TAG = "PlaytestAnalytics"

    fun logLensSelection(siteId: String, lensType: LensType) {
        // In a real app, this would send to Firebase or a custom backend
        Log.d(TAG, "LENS_SELECTED: Site=$siteId, Lens=${lensType.label}")
    }

    fun logVote(lensType: LensType) {
        Log.d(TAG, "VOTE_SUBMITTED: ChosenFuture=${lensType.label}")
    }
}
