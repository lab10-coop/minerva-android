package coop.lab10.minerva.utils

// TODO: can we make this rounding artifact proof? Use enum or types for timeUnit
// TODO: should do calculations with the datatypes expected by web3 (BigInteger / BigDecimal)
// TODO: this is untested. Look for a replacement which is part of a lib we're already using.
class StreemUtils {
    companion object {
        fun calculateAmountPerSecond(amount: Long, timeUnit: String) : Double {
            return when(timeUnit) {
                "s" -> amount / 1.0
                "m" -> amount / 60.0
                "h" -> amount / 3600.0
                "d" -> amount / 86400.0
                "w" -> amount / 604800.0
                else -> amount / 1.0 // TODO: throw
            }
        }

        fun calculateAmountPerHour(amount: Long, timeUnit: String) : Double {
            return when(timeUnit) {
                "s" -> amount * 3600.0
                "m" -> amount * 60.0
                "h" -> amount * 1.0
                "d" -> amount / 24.0
                "w" -> amount / 168.0
                else -> amount / 1.0 // TODO: throw
            }
        }
    }
}