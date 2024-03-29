package coop.lab10.minerva.utils

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import androidx.core.app.ActivityCompat
import android.widget.Toast


@TargetApi(Build.VERSION_CODES.M)
open class FingerprintCommonHandler(private val context: Context) : FingerprintManager.AuthenticationCallback() {

    // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
    // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//

    private var cancellationSignal: CancellationSignal? = null

    //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

    fun startAuth(manager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject) {

        cancellationSignal = CancellationSignal()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override//onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//
    fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {

        //I’m going to display the results of fingerprint authentication as a series of toasts.
        //Here, I’m creating the message that’ll be displayed if an error occurs//
        // Do not display toast if error code is 5 = authorization canceled
        if (errMsgId != 5) {
            Toast.makeText(context, "Authentication error - code $errMsgId\n$errString", Toast.LENGTH_LONG).show()
        }
    }

    override//onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//
    fun onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show()
    }

    override//onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
    //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
    fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        Toast.makeText(context, "Authentication help\n$helpString", Toast.LENGTH_LONG).show()
    }

}