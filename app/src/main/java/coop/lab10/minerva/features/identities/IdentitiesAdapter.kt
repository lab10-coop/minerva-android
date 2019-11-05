package coop.lab10.minerva.features.identities

import android.app.Activity
import android.content.Intent
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.AccountPicker
import com.uport.sdk.signer.UportHDSigner
import coop.lab10.minerva.R
import coop.lab10.minerva.features.models.Identity


class IdentitiesAdapter(private val items: MutableList<Identity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var mExpandedPosition = RecyclerView.NO_POSITION

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items.get(position).type.value
    }

    fun updateAll(newItems: List<Identity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addIdentity(item: Identity) {
        items.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(viewGroup.context)

        when (viewType) {
            Identity.Companion.Type.DEFAULT.value -> {
                val v1 = inflater.inflate(R.layout.identity_default_card, viewGroup, false)
                viewHolder = DefaultIdentitiesViewHolder(v1, viewGroup)
            }
            Identity.Companion.Type.VC.value, Identity.Companion.Type.VC2.value -> {
                val v1 = inflater.inflate(R.layout.vc_card, viewGroup, false)
                viewHolder = DefaultVCViewHolder(v1)
            }
            else -> {
                val v1 = inflater.inflate(R.layout.identity_default_card, viewGroup, false)
                viewHolder = DefaultIdentitiesViewHolder(v1, viewGroup)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            Identity.Companion.Type.DEFAULT.value -> {
                var isExpanded = (position == mExpandedPosition)
                viewHolder as DefaultIdentitiesViewHolder
                viewHolder.identityName.text = viewHolder.viewGroup.context.getString(R.string.title_default_identity)
                viewHolder.avatarLatter.text = items[position].name.first().toString()
                viewHolder.cardHolder.setBackgroundResource(R.drawable.card_holder_blue)
                // TODO find out how to format DID directly in lib
                viewHolder.identityDID.text = "did:uport:" + items[position].did.removePrefix("0x")
                // TODO load the attributes from DB if there are any
                // Should we allow to attach any attributes to the base key?
                viewHolder.attributesValue.text = "+ DID"

                viewHolder.card.setOnClickListener {
                    TransitionManager.beginDelayedTransition(viewHolder.viewGroup)
                    if (isExpanded) {
                        viewHolder.detailsView.visibility = View.GONE
                        viewHolder.attributesValue.visibility = View.VISIBLE
                        mExpandedPosition = RecyclerView.NO_POSITION
                    } else {
                        viewHolder.detailsView.visibility = View.VISIBLE
                        viewHolder.attributesValue.visibility = View.GONE
                        mExpandedPosition = position
                    }
                    // TODO find out better way to use item changed, currently it flicker when tapped
                    // notifyItemChanged(position)
                    notifyDataSetChanged()
                }
                viewHolder.moreBtn.setOnClickListener { view ->
                    val popupMenu = PopupMenu(viewHolder.viewGroup.context, view)
                    val inflater = popupMenu.menuInflater
                    inflater.inflate(R.menu.identity_item, popupMenu.menu)
                    popupMenu.show()

                    popupMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.menu_identity_card_backup -> {

                                UportHDSigner().showHDSeed(view.context, items[position].did, "Unlock your identity") { err, phrase ->
                                    if (err == null) {
                                        val intent = Intent(view.context, IdentityBackupActivity::class.java).apply {
                                            putExtra("PHRASE", phrase)
                                        }
                                        view.context.startActivity(intent)
                                    }
                                }


                            }
                            R.id.menu_identity_card_delete -> {
                                UportHDSigner().deleteSeed(viewHolder.viewGroup.context, items[position].did)
                                items.remove(items[position])
                                notifyItemRemoved(position)
                            }
                            R.id.menu_identity_connect_account -> {

                                val intent = AccountPicker.newChooseAccountIntent(null, null, null,
                                        false, null, null, null, null);
                                (viewHolder.viewGroup.context as Activity).startActivityForResult(intent, 7)
                            }
                        }
                        true
                    }
                }
            }
            Identity.Companion.Type.VC.value -> {
                val vh1 = viewHolder as DefaultVCViewHolder
                viewHolder.identityName.text = items.get(position).name
                viewHolder.avatarLatter.text = items.get(position).name.first().toString()
                viewHolder.cardHolder.setBackgroundResource(R.drawable.card_holder_red)
                viewHolder.name.text = "Thomas Zeinzinger"
                viewHolder.email.text = "thomas.zeinzinger@lab10.coop"
            }
            Identity.Companion.Type.VC2.value -> {
                val vh1 = viewHolder as DefaultVCViewHolder
                viewHolder.identityName.text = items.get(position).name
                viewHolder.avatarLatter.text = items.get(position).name.first().toString()
                viewHolder.cardHolder.setBackgroundResource(R.drawable.card_holder_red)
                viewHolder.name.text = "Ninja"
                viewHolder.email.text = "ninja1972@gmail.com"
            }
            else -> {
                val vh1 = viewHolder as DefaultIdentitiesViewHolder
            }
        }
    }
}