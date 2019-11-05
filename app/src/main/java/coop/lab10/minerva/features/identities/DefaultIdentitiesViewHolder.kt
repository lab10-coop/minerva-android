package coop.lab10.minerva.features.identities

import androidx.cardview.widget.CardView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import coop.lab10.minerva.R


class DefaultIdentitiesViewHolder(v: View, viewGroup: ViewGroup) : RecyclerView.ViewHolder(v) {

    val identityName: TextView = v.findViewById(R.id.identity_name)
    val avatarLatter: TextView = v.findViewById(R.id.identity_avatar)
    val cardHolder: View = v.findViewById(R.id.identity_holder)
    val attributesValue: TextView = v.findViewById(R.id.attr_value)
    val detailsView: LinearLayout = v.findViewById(R.id.identity_details)
    val card: CardView = v.findViewById(R.id.card)
    val moreBtn: ImageButton = v.findViewById(R.id.btn_more)
    val viewGroup : ViewGroup = viewGroup

    // Details

    val identityDID: TextView = v.findViewById(R.id.identity_did)

}
