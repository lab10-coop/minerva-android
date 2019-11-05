package coop.lab10.minerva.features.activities

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import coop.lab10.minerva.R
import coop.lab10.minerva.features.models.Activity

class ActivitiesAdapter(private val items: List<Activity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        val v1 = inflater.inflate(R.layout.activity_card, viewGroup, false)
        viewHolder = ActivitiesViewHolder(v1)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as ActivitiesViewHolder
        viewHolder.time.text = items[position].time
        viewHolder.name.text = items[position].name
        viewHolder.logo.setImageResource(items[position].logoResourceId)
        viewHolder.title.text = items[position].date


    }
}