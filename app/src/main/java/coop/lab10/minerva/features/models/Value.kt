package coop.lab10.minerva.features.models

class Value(val name: String, val type: Type) {

    companion object {
        enum class Type(val value: Int) {
            DEFAULT(1),
            ATS(2),
            ETH(3)
        }
    }

}