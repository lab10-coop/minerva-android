package coop.lab10.minerva.features.models

import coop.lab10.minerva.helpers.ValuableKindConverter
import io.objectbox.annotation.BaseEntity
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Id

@BaseEntity
data class Valuable(
        @Id var id: Long = 0,
        val name: String? = "",
        @Convert(converter = ValuableKindConverter::class, dbType = Int::class)
        val kind: String? = ""
)
{
    enum class Kind constructor(internal val id: Int) {
        DEFAULT(0), NETWORK(1), VOUCHER(2)
    }
}




