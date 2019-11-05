package coop.lab10.minerva.features.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne



@Entity
class Token {
    @Id
    var id: Long = 0

    lateinit var network:ToOne<Network>

}