package coop.lab10.minerva.features.values

import android.view.View
import android.widget.TextView
import coop.lab10.minerva.R

class DefaultValueViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

    val valueName: TextView = v.findViewById(R.id.value_name)

}
