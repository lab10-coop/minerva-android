package coop.lab10.minerva.features.identities

import android.view.View
import android.widget.TextView
import coop.lab10.minerva.R

class DefaultVCViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

    val identityName: TextView = v.findViewById(R.id.identity_name)
    val name: TextView = v.findViewById(R.id.name_value)
    val avatarLatter: TextView = v.findViewById(R.id.identity_avatar)
    val cardHolder: View = v.findViewById(R.id.identity_holder)
    val email: TextView = v.findViewById(R.id.email)

}
