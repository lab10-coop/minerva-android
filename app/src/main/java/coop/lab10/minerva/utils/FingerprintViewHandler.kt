package coop.lab10.minerva.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import android.os.StrictMode





@TargetApi(Build.VERSION_CODES.M)
class FingerprintViewHandler(private val context: Context, val callbackUrl: String) : FingerprintCommonHandler(context) {

    override//onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    fun onAuthenticationSucceeded(
            result: FingerprintManager.AuthenticationResult) {

        // TODO don't leave it like that this is not recommended and done only for DEMO purposes
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var intent = Intent()

        var mediaType = MediaType.parse("application/json; charset=utf-8");

        var client = OkHttpClient();

        val json = JSONObject()
        val manJson = JSONObject()
        manJson.put("name", "Thomas")
        manJson.put("email", "thomas.zeinzinger@lab10.coop")
        manJson.put("age", "45")
        manJson.put("address", "Strauchergasse 13, 8020 Graz")
        json.put("data", manJson)
        json.put("signature", "120dj9dj012d0j12d")
        var body = RequestBody.create(mediaType, json.toString());
        var request = Request.Builder()
                    .url(callbackUrl)
                    .post(body)
                    .build();
        var response = client.newCall(request).execute();
        if (response.isSuccessful) {
            (context as Activity).setResult(Activity.RESULT_OK, intent)
            context.finish()
        } else {
            (context as Activity).setResult(Activity.RESULT_CANCELED, intent)
            context.finish()
        }
    }
}