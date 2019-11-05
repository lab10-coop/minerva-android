package coop.lab10.minerva.utils

import android.annotation.TargetApi
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.app.Activity
import android.content.Intent
import android.os.Build

@TargetApi(Build.VERSION_CODES.M)
class FingerprintSigningHandler(private val context: Context) : FingerprintCommonHandler(context) {

    override//onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    fun onAuthenticationSucceeded(
            result: FingerprintManager.AuthenticationResult) {

        var intent = Intent()
        var signedData = "0x232425"
        intent.putExtra("signedPayload", signedData)
        (context as Activity).setResult(Activity.RESULT_OK, intent)
        context.finish()

    }

}