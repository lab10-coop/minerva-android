package coop.lab10.minerva.features.identities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coop.lab10.minerva.R
import coop.lab10.minerva.features.models.*
import coop.lab10.minerva.utils.ValueFormatter

class CardViewDataAdapter// Provide a suitable constructor (depends on the kind of dataset)
(var mDataset: ArrayList<Payload>) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        when (viewType) {
            Payload.Type.PAYMENT.value -> {
                val v1 = LayoutInflater.from(parent.context).inflate(R.layout.payment_card_view, parent, false)
                return PaymentViewHolder(v1)
            }
            Payload.Type.IDENTITY_REQUEST.value -> {
                val v2 = LayoutInflater.from(parent.context).inflate(R.layout.identity_request_card_view, parent, false)
                return IdentityRequestViewHolder(v2)
            }
            Payload.Type.SIGN.value -> {
                val v2 = LayoutInflater.from(parent.context).inflate(R.layout.authorization_card_view, parent, false)
                return SignRequestViewHolder(v2)
            }
            Payload.Type.AUTH.value -> {
                val v3 = LayoutInflater.from(parent.context).inflate(R.layout.auth_request_card_view, parent, false)
                return AuthViewHolder(v3)
            }
            else -> {
                val v1 = LayoutInflater.from(parent.context).inflate(R.layout.payment_card_view, parent, false)
                return PaymentViewHolder(v1)
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        when (viewHolder.itemViewType) {
            Payload.Type.PAYMENT.value -> {
                viewHolder as PaymentViewHolder
                viewHolder.card_title.text = mDataset[position].title
                var paymentPayload = (mDataset[position] as PaymentPayload)
                viewHolder.paymentMethodLogo.setImageDrawable(viewHolder.paymentMethodLogo.context.getDrawable(R.drawable.ic_artis_logo))
                viewHolder.amount.text = ValueFormatter.currency(paymentPayload.amount)
                viewHolder.paymentMethodType.text = "Continuous transfer"
                viewHolder.amountPeriod.text = "/ month"
                viewHolder.paymentMethodName.text = "Artis"
            }
            Payload.Type.IDENTITY_REQUEST.value -> {
                var identityRequestPayload = (mDataset[position] as IdentityRequestPayload)
                viewHolder as IdentityRequestViewHolder
                viewHolder.cardTitle.text = identityRequestPayload.title
                viewHolder.name.text = identityRequestPayload.name
                viewHolder.address.text = identityRequestPayload.address
                viewHolder.identityName.text = "Citizen"

            }
            Payload.Type.AUTH.value -> {
                var identityRequestPayload = (mDataset[position] as AuthPayload)
                viewHolder as AuthViewHolder
                viewHolder.card_title.text = identityRequestPayload.title
                viewHolder.identity_name.text = identityRequestPayload.serviceName
            }
            Payload.Type.SIGN.value -> {
                var signRequestPayload = (mDataset[position] as SignPayload)
                viewHolder as SignRequestViewHolder
                viewHolder.card_title.text = signRequestPayload.title
                viewHolder.payload.text = signRequestPayload.data
            }
            else -> {

            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun getItemViewType(position: Int): Int {
        return mDataset.get(position).type.value
    }

    // inner class to hold a reference to each item of RecyclerView
    class PaymentViewHolder(itemLayoutView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemLayoutView) {

        var card_title: TextView = itemLayoutView
                .findViewById(R.id.card_title) as TextView
        var paymentMethodLogo: ImageView = itemLayoutView.findViewById(R.id.identity_logo) as ImageView
        var amount: TextView
        var amountPeriod: TextView
        var paymentMethodType: TextView
        var paymentMethodName: TextView = itemLayoutView.findViewById(R.id.payment_method_name)

        init {
            amount = itemLayoutView.findViewById(R.id.amount_value) as TextView
            paymentMethodType = itemLayoutView.findViewById(R.id.payment_method_type) as TextView
            amountPeriod = itemLayoutView.findViewById(R.id.amount_period) as TextView

        }
    }

    class IdentityRequestViewHolder(itemLayoutView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemLayoutView) {
        var cardTitle: TextView = itemLayoutView.findViewById(R.id.card_title) as TextView
        var name: TextView = itemLayoutView.findViewById(R.id.name_value) as TextView
        var address: TextView = itemLayoutView.findViewById(R.id.attr_value) as TextView
        var identityName: TextView = itemLayoutView.findViewById(R.id.identity_name) as TextView
        //var identityLogo: ImageView = itemLayoutView.findViewById(R.id.identity_logo) as ImageView
    }

    class AuthViewHolder(itemLayoutView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemLayoutView) {
        var card_title: TextView
        var identity_name: TextView

        init {
            card_title = itemLayoutView.findViewById(R.id.card_title) as TextView
            identity_name = itemLayoutView.findViewById(R.id.identity_name) as TextView
        }
    }

    class SignRequestViewHolder(itemLayoutView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemLayoutView) {
        var card_title: TextView
        var payload: TextView

        init {
            card_title = itemLayoutView.findViewById(R.id.card_title) as TextView
            payload = itemLayoutView.findViewById(R.id.payload) as TextView
        }
    }

}