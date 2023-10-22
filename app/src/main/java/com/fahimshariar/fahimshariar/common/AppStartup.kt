package com.fahimshariar.fahimshariar.common

import androidx.annotation.WorkerThread
import com.ldt.fahimshariar.interactors.AppExecutors

object AppStartup {
    @JvmStatic
    fun onAppStartup() {
        AppExecutors.single().execute {
            initDataAsyncOnStartUp()
        }
    }

    @WorkerThread
    private fun initDataAsyncOnStartUp() {
        MediaManager.loadMediaIfNeeded()
    }
}