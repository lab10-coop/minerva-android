package coop.lab10.minerva.features.values

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import coop.lab10.minerva.R
import coop.lab10.minerva.features.models.Value

class ValuesAdapter(private val items: List<Value>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TAG = ValuesAdapter::class.java.simpleName

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type.value
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        when (viewType) {
            Value.Companion.Type.DEFAULT.value -> {
                val v1 = inflater.inflate(R.layout.value_default_card, viewGroup, false)
                viewHolder = DefaultValueViewHolder(v1)
            }
            Value.Companion.Type.ATS.value -> {
                val v1 = inflater.inflate(R.layout.artis_value_card, viewGroup, false)
                viewHolder = ATSValueViewHolder(v1)
            }
            Value.Companion.Type.ETH.value -> {
                val v1 = inflater.inflate(R.layout.eth_value_card, viewGroup, false)
                viewHolder = DefaultValueViewHolder(v1)
            }
            else -> { // shouldn't we throw instead?
                val v1 = inflater.inflate(R.layout.value_default_card, viewGroup, false)
                viewHolder = DefaultValueViewHolder(v1)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            Value.Companion.Type.DEFAULT.value, Value.Companion.Type.ETH.value  -> {
                viewHolder as DefaultValueViewHolder
                viewHolder.valueName.text = items[position].name
            }
            Value.Companion.Type.ATS.value -> {
                viewHolder as ATSValueViewHolder
                viewHolder.atsSendBtn.setOnClickListener {
                    Log.d(TAG, "ATS send pressed")
                }
            }
            else -> {
                viewHolder as DefaultValueViewHolder
                viewHolder.valueName.text = items[position].name
            }
        }
    }
}