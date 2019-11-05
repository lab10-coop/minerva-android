package coop.lab10.minerva.features.activities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coop.lab10.minerva.R

class ActivitiesViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

    val logo: ImageView = v.findViewById(R.id.activity_logo)
    val time: TextView = v.findViewById(R.id.activity_time)
    val name: TextView = v.findViewById(R.id.activity_name)
    val title: TextView = v.findViewById(R.id.activity_title)
}
