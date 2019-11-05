package coop.lab10.minerva.activities

import android.Manifest
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.annotation.RequiresApi
import android.widget.Toast
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import androidx.core.app.ActivityCompat
import coop.lab10.minerva.Minerva
import coop.lab10.minerva.*
import coop.lab10.minerva.features.identities.CardViewDataAdapter
import coop.lab10.minerva.features.models.*
import coop.lab10.minerva.utils.FingerprintLoginHandler
import coop.lab10.minerva.utils.FingerprintSigningHandler
import coop.lab10.minerva.utils.FingerprintViewHandler
import kotlinx.android.synthetic.main.activity_signing.*
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey


class SigningActivity : AppCompatActivity() {

    var cipher: Cipher? = null
    var keyStore: KeyStore? = null
    val KEY_NAME: String = "SecretKey"


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signing)

        cancel_button.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        var requestData = arrayListOf<Payload>()



        // TODO unify that for 2FA it does not have to be payment
        if (intent.action == Minerva.CONFIRM_ACTION)
            if (intent.extras != null) {

                var payload = intent.getStringExtra(Intent.EXTRA_TEXT)
                if (payload != null) {

                    var payload = intent.getStringExtra(Intent.EXTRA_TEXT)
                    var title = intent.getStringExtra(Intent.EXTRA_TITLE)
                    var serviceName = intent.getStringExtra(Minerva.CP_SERVICE_NAME)
                    requestData.add(SignPayload(title, payload, serviceName))

                } else {
                    // TODO handle missing data which could not come
                    var amount = "€" + intent.getStringExtra(Minerva.CP_AMOUNT)
                    var iban = intent.getStringExtra(Minerva.CP_IBAN)
                    var paymentMethodType = intent.getStringExtra(Minerva.CP_RECIPIENT)
                    var title = intent.getStringExtra(Intent.EXTRA_TITLE)
                    var serviceName = intent.getStringExtra(Minerva.CP_SERVICE_NAME)
                    requestData.add(PaymentPayload(title, amount, iban, paymentMethodType, null, serviceName))
                }
            }


        if (intent.action == Minerva.MIX_ACTION) {
            if (intent.extras != null) {
                var listOfActions = intent.getStringArrayListExtra(Minerva.ACTION_LIST)
                listOfActions.forEach() { action ->
                    when (action) {
                        Minerva.IDENTITY_REQUEST_ACTION -> {
                            // TODO move to IdentityRequestPayload directly
                            var title = resources.getString(R.string.request_identity_title)
                            var name = "Thomas Zeinzinger"
                            var address = "Strauchergasse 13\n" +
                                    "8020 Graz\n" +
                                    "Austria"
                            var serviceName = intent.getStringExtra(Minerva.AUTH_SERVICE_NAME)
                            service_provider_name.text = serviceName
                            requestData.add(IdentityRequestPayload(title, name, address))
                        }
                        Minerva.AUTH_ACTION -> {
                            var title = resources.getString(R.string.request_authorization_title)
                            var serviceName = intent.getStringExtra(Minerva.AUTH_SERVICE_NAME)
                            requestData.add(AuthPayload(title, serviceName))
                        }
                    }
                }
            }
        }

        if (intent.action == Minerva.IDENTITY_REQUEST_ACTION) {
            if (intent.extras != null) {
                // TODO handle missing data which could not come

                var title = intent.getStringExtra(Intent.EXTRA_TITLE)
                var name = intent.getStringExtra(Minerva.IR_NAME)
                var address = intent.getStringExtra(Minerva.IR_ADDRESS)
                var serviceName = intent.getStringExtra(Minerva.AUTH_SERVICE_NAME)
                requestData.add(IdentityRequestPayload(title, name, address))
            }
        }

        if (intent.action == Minerva.AUTH_ACTION) {
            if (intent.extras != null) {
                var title = intent.getStringExtra(Intent.EXTRA_TITLE)
                var serviceName = intent.getStringExtra(Minerva.AUTH_SERVICE_NAME)
                requestData.add(AuthPayload(title, serviceName))

            }
        }


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerCardView.setHasFixedSize(true)

        // use a linear layout manager
        var mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this);
        recyclerCardView.setLayoutManager(mLayoutManager);

        var mAdapter = CardViewDataAdapter(requestData);
        recyclerCardView.adapter = mAdapter;

        val fingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

        var keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        //Check whether the device has a fingerprint sensor//
        if (!fingerprintManager.isHardwareDetected()) {
            // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
            Toast.makeText(this, "Your device doesn't support fingerprint authentication", Toast.LENGTH_LONG);
        }
        //Check whether the user has granted your app the USE_FINGERPRINT permission//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            // If your app doesn't have this permission, then display the following text//
            Toast.makeText(this, "Please enable the fingerprint permission", Toast.LENGTH_LONG);
        }

        //Check that the user has registered at least one fingerprint//
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            // If the user hasn’t configured any fingerprints, then display the following message//
            Toast.makeText(this, "No fingerprint configured. Please register at least one fingerprint in your device's Settings", Toast.LENGTH_LONG);

        }

        //Check that the lockscreen is secured//
        if (!keyguardManager.isKeyguardSecure()) {
            // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
            Toast.makeText(this, "Please enable lockscreen security in your device's Settings", Toast.LENGTH_LONG);

        } else {
            try {
                generateKey();
            } catch (e: FingerprintException) {
                e.printStackTrace();
            }

            if (initCipher()) {
                //If the cipher is initialized successfully, then create a CryptoObject instance//
                var cryptoObject = FingerprintManager.CryptoObject(cipher);


                when(intent.action) {
                    Minerva.AUTH_ACTION -> {
                        var helper = FingerprintLoginHandler(this)
                        helper.startAuth(fingerprintManager, cryptoObject)
                    }

                    Intent.ACTION_VIEW -> {

                        val uri = intent.data
                        val serviceDID = uri!!.pathSegments.first()
                        val serviceName = uri.getQueryParameter("service_name")
                        val action = uri!!.lastPathSegment
                        val callbackUrl = uri.getQueryParameter("callback_url")
                        val identityFields = uri.getQueryParameter("identity")
                        val amount = uri.getQueryParameter("value")

                        var title = resources.getString(R.string.request_identity_title)
                        service_provider_name.text = serviceName
                        var name = "Thomas Zeinzinger"
                        var address = "Strauchergasse 13\n" +
                                "8020 Graz\n" +
                                "Austria"
                        requestData.add(IdentityRequestPayload(title, name, address))

                        title = resources.getString(R.string.request_payment_title)
                        requestData.add(PaymentPayload(title, amount, "IBAN", serviceName, null, serviceName))

                        var helper = FingerprintViewHandler(this, callbackUrl)
                        helper.startAuth(fingerprintManager, cryptoObject)

                    }
                    else -> {
                        // This class will be responsible
                        // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                        var helper = FingerprintSigningHandler(this)
                        helper.startAuth(fingerprintManager, cryptoObject)

                    }
                }
            }
        }
    }

    //Create the generateKey method that we’ll use to gain access to the Android keystore and generate the encryption key//

    @Throws(FingerprintException::class)
    private fun generateKey() {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore")

            //Generate the key//
            var keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            //Initialize an empty KeyStore//
            keyStore!!.load(null)

            //Initialize the KeyGenerator//
            keyGenerator.init(
                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                            //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                            .setUserAuthenticationRequired(true)
                            .setEncryptionPaddings(
                                    KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .build())

            //Generate the key//
            keyGenerator.generateKey()

        } catch (exc: KeyStoreException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: NoSuchAlgorithmException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: NoSuchProviderException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: InvalidAlgorithmParameterException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: CertificateException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: IOException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        }

    }


    //Create a new method that we’ll use to initialize our cipher//
    fun initCipher(): Boolean {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore!!.load(
                    null)
            val key = keyStore!!.getKey(KEY_NAME, null) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            //Return true if the cipher has been initialized successfully//
            return true
        } catch (e: KeyPermanentlyInvalidatedException) {

            //Return false if cipher initialization failed//
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }

    }

    private inner class FingerprintException(e: Exception) : Exception(e)


}
