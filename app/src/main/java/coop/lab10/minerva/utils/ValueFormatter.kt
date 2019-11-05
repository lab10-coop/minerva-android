package coop.lab10.minerva.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ValueFormatter {

    companion object {
        fun currency(value: Double) : String {
            return currency(value, "€")
        }
        fun currency(value: String) : String {
            val formatter = DecimalFormat("###,##0.00")
            return try {
                "€" + formatter.format(value.toFloat())

            } catch (exception: Exception) {
                "€0.00"
            }
        }

        fun currency(value: Double, currencySymbol: String) : String {
            val formatter = DecimalFormat("###,##0.00")
            return currencySymbol + formatter.format(value)
        }

        fun timeCounter(seconds: Long) : String {
            var df = SimpleDateFormat("s' sec'")
            val tz = TimeZone.getTimeZone("UTC")
            if (seconds >= 60 ) {
                df = SimpleDateFormat("m' min' s' sec'")
            }
            if (seconds >= 3600 ) {
                df = SimpleDateFormat("h' h' m' min'")
            }
            df.timeZone = tz
            return df.format(Date(seconds*1000))
        }
    }
}
