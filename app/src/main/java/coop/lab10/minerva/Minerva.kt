package coop.lab10.minerva

import android.app.Activity
import android.content.Intent

class Minerva {

    companion object {
        const val AUTH_PAYLOAD = "authPayload"


        // List of codes which are used to communicate between activities (return codes)
        const val AUTH_REQUEST_CODE  = 102
        const val SIGNED_PAYLOAD = 101

        const val AUTH_PAYLOAD_IDENTITY_NAME = "identityName"

        // ---------------------------------------------------------------------
        // List of potential ACTIONS which Minerva can handle

        // Sign/confirm operation by signing payload and send back
        const val CONFIRM_ACTION = "coop.lab10.minerva.CONFIRM_ACTION"

        // Mix Action - indicate that user requesting one or more ACTION from above list
        const val MIX_ACTION = "coop.lab10.minerva.MIX_ACTION"


        // Request user for providing private data to the application
        const val IDENTITY_REQUEST_ACTION = "coop.lab10.minerva.IDENTITY_REQUEST_ACTION"

        // Requesting payment from Minerva Wallet using their internal assets. Keep in mind that if
        const val PAYMENT_ACTION = "coop.lab10.minerva.PAYMENT_ACTION"

        // Requesting to proove of specific identity, control over specific DID
        const val AUTH_ACTION = "coop.lab10.minerva.AUTH_ACTION"

        // ---------------------------------------------------------------------

        // List of actions which app requesting needed to look up for specific attributes for each action
        // required for MIX_ACTION
        const val ACTION_LIST = "coop.lab10.minerva.ACTION_LIST"

        // List of fields requested within IDENTITY_REQUEST_ACTION
        // Below find a list of all supported fields. This would be needed to be covered by Taxonomy which Paul K. is working on within Dativa
        const val IDENTITY_FIELDS = "coop.lab10.minerva.IDENTITY_FIELDS"

        // ---------------------------------------------------------------------
        // List of fields which can be requested by third party within scope of IDENTITY_REQUEST_ACTION

        const val IF_NAME = "coop.lab10.minerva.IF_NAME"
        const val IF_ADDRESS = "coop.lab10.minerva.IF_ADDRESS"
        const val IF_EMAIL = "coop.lab10.minerva.IF_EMAIL"

        // List of fields which third party app can request from AUTH action
        const val AUTH_FIELDS = "coop.lab10.minerva.AUTH_FIELDS"

        // Payment
        const val CP_AMOUNT = "coop.lab10.minerva.CP_AMOUNT"
        const val CP_RECIPIENT = "coop.lab10.minerva.CP_RECIPIENT"
        const val CP_IBAN = "coop.lab10.minerva.CP_IBAN"
        const val CP_SERVICE_NAME = "coop.lab10.minerva.CP_SERVICE_NAME"

        // Identity Request

        const val IR_NAME = "coop.lab10.minerva.IR_NAME"
        const val IR_ADDRESS = "coop.lab10.minerva.IR_ADDRESS"

        // Login Request
        const val AUTH_REQUEST = "coop.lab10.minerva.AUTH"
        const val AUTH_SERVICE_NAME = "coop.lab10.minerva.AUTH_SERVICE_NAME"

        fun login(activity: Activity, serviceName: String) {
            val sendIntent: Intent = Intent().apply {
                action = AUTH_ACTION
                putExtra(Intent.EXTRA_TITLE, "Identity Request")
                putExtra(Minerva.AUTH_SERVICE_NAME, serviceName)
                type = "text/plain"
            }
            activity.startActivityForResult(sendIntent, Minerva.AUTH_REQUEST_CODE)
        }

        fun sign() {

        }
    }
}
