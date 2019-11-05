package coop.lab10.minerva.features.values

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import coop.lab10.minerva.R

class ATSValueViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v){

    // TODO implement view holder
    val valueName: TextView = v.findViewById(R.id.value_name)
    val atsSendBtn: LinearLayout = v.findViewById(R.id.ats_send_btn)
}