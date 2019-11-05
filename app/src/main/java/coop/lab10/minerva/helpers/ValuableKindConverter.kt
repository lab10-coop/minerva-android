package coop.lab10.minerva.helpers

import coop.lab10.minerva.features.models.Valuable
import io.objectbox.converter.PropertyConverter

class ValuableKindConverter : PropertyConverter<Valuable.Kind, Int> {

    override fun convertToEntityProperty(databaseValue: Int) : Valuable.Kind? {
        if (databaseValue == null) {
            return null
        }
        for (kind in Valuable.Kind.values()) {
            if (kind.id == databaseValue) {
                return kind
            }
        }

        return Valuable.Kind.DEFAULT
    }

    override fun convertToDatabaseValue(entityProperty: Valuable.Kind?): Int? {
        return entityProperty?.id
    }

}