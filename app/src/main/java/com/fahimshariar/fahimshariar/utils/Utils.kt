package com.fahimshariar.fahimshariar.utils

import android.app.Application
import com.fahimshariar.fahimshariar.App
import com.ldt.fahimshariar.R
import com.ldt.fahimshariar.interactors.runOnUiThread
import es.dmoral.toasty.Toasty

object Utils {
    @JvmStatic
    fun getApp(): Application {
        return _root_ide_package_.com.fahimshariar.fahimshariar.App.getInstance()
    }

    fun hasFlags(flags: Int, flagsNeedToCheck: Int): Boolean {
        return flags and flagsNeedToCheck != 0
    }

    fun showGeneralErrorToast() {
        runOnUiThread {
            Toasty.normal(getApp(), R.string.str_error_general).show()
        }
    }
}