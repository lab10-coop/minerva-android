package coop.lab10.minerva.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import coop.lab10.minerva.Minerva

@TargetApi(Build.VERSION_CODES.M)
class FingerprintLoginHandler(private val context: Context) : FingerprintCommonHandler(context) {

    override//onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    fun onAuthenticationSucceeded(
            result: FingerprintManager.AuthenticationResult) {

        var intent = Intent()
        var loginData = "Login profile"
        intent.putExtra(Minerva.AUTH_PAYLOAD, loginData)
        (context as Activity).setResult(Activity.RESULT_OK, intent)
        context.finish()
    }
}