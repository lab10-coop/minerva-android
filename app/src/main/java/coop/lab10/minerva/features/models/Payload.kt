package coop.lab10.minerva.features.models

import android.graphics.Bitmap

open class Payload(val title: String, val type: Type) {
    enum class Type(val value : Int){
        PAYMENT(1), SIGN(2), IDENTITY_REQUEST(3), AUTH(4)
    }
}
class PaymentPayload(title: String, val amount: String, val iban: String, val recipient: String, val logo: Bitmap?, var service_name: String) : Payload(title, Type.PAYMENT)
class SignPayload(title: String, val data: String, val serviceName: String) : Payload(title, Type.SIGN)
class IdentityRequestPayload(title: String, val name: String, val address: String) : Payload(title, Type.IDENTITY_REQUEST)
class AuthPayload(title: String, val serviceName: String) : Payload(title, Type.AUTH)