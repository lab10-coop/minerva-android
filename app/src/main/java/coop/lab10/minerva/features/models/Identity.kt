package coop.lab10.minerva.features.models

open class Identity(val name: String, val type: Type, val did: String ) {

    companion object {
        /**
         * Type of Identity supported by Minerva
         *
         *  DEFAULT - it is default DID base identity
         *  VC - it is Verfifiable Credential type of "identity"
         *  WALLET - it is pair of the keys representing crypto wallet e.g.
         *
         * @property value
         */
        enum class Type(val value: Int) {
            DEFAULT(1),
            VC(2), // Verifiable Credential
            VC2(3), //Temporary used for dummy data
            WALLET(4), // Any type of key pair public/private key representing e.g. crypto wallet
        }
    }
}

