package coop.lab10.minerva.features.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

/**
 * Representation of generic network which holds valuables.
 * For example DLT networks but as well different type of networks handling payments
 */
@Entity
open class Network {
    @Id
    var id: Long = 0

    // TODO move that to specific network imlpementation as not all network will support tokens
    lateinit var tokens: ToMany<Token>

}