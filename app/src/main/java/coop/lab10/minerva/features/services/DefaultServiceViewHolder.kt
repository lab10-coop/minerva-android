package coop.lab10.minerva.features.services

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coop.lab10.minerva.R

class DefaultServiceViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

    var name: TextView = v.findViewById(R.id.identity_name)
    var logo: ImageView = v.findViewById(R.id.service_logo)
}