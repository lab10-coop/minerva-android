package coop.lab10.minerva.domain

import android.content.Context
import com.uport.sdk.signer.UportHDSigner
import com.uport.sdk.signer.encryption.KeyProtection

/* Class responsible for handling secure storage for seed/mnemonic and other secrets
 * Currently we are using uPort library for handling all but in the future we need to
 * extend it to be more generic. Probably use LOX from Aries.
 */
class SecureStorage {

    // TODO find out how to separate Android context from this domain code.
    // Seems that the storage would need to be within presentation layer as it use
    //
    companion object {
        fun createSeed(context: Context, callback: (err: Exception?, address: String?, pubKey: String?) -> Unit) {
            return if (UportHDSigner().hasSeed(context)) {
                // Master seed exist do nothing
                callback(null, null, null)
            } else {
                UportHDSigner().createHDSeed(context, KeyProtection.Level.PROMPT, callback)
            }
        }

        fun loadSeed(context: Context, phrase: String, callback: (err: Exception?, address: String?, pubKey: String?) -> Unit) {
            return if (UportHDSigner().hasSeed(context)) {
                // Master seed exist do nothing
                callback(null, null, null)
            } else {
                UportHDSigner().importHDSeed(context, KeyProtection.Level.PROMPT, phrase, callback)
            }
        }
    }
}