package coop.lab10.minerva.features.identities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.uport.sdk.signer.UportHDSigner
import coop.lab10.minerva.Web3jManager
import coop.lab10.minerva.R
import coop.lab10.minerva.domain.SecureStorage
import coop.lab10.minerva.features.models.Identity
import kotlinx.android.synthetic.main.fragment_identity_list.view.*

/**
 * A fragment representing a list of Identities.
 * Activities containing this fragment MUST implement the
 * [IdentityFragment.OnListFragmentInteractionListener] interface.
 */
class IdentityFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {


    private lateinit var identitiesAdapter : IdentitiesAdapter

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_identity_list, container, false)

        val recyclerView = view.identities_recycler
        recyclerView.layoutManager = LinearLayoutManager(context)

        (view.swipe_container as SwipeRefreshLayout).setOnRefreshListener(this)
        var identities = arrayListOf<Identity>()

        UportHDSigner().allHDRoots(context!!).forEach {
                identities.add(Identity("Default",  Identity.Companion.Type.DEFAULT, it))
        }
        identitiesAdapter = IdentitiesAdapter(identities)

        recyclerView.adapter = identitiesAdapter
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onRefresh() {
        var identities = arrayListOf<Identity>()

        // TODO
        UportHDSigner().allHDRoots(context!!).forEach {
            identities.add(Identity(resources.getString(R.string.title_default_identity),  Identity.Companion.Type.DEFAULT, it))
        }
        identitiesAdapter.updateAll(identities)
        val mView = view
        if(mView != null)
            mView.swipe_container.isRefreshing = false
    }

    fun addDefault() {
        // Implements adding resource method
        val context = context
        if (context != null) {
            SecureStorage.createSeed(context) { err, address, publicKey ->
                if (address != null)
                    identitiesAdapter.addIdentity(Identity(resources.getString(R.string.title_default_identity), Identity.Companion.Type.DEFAULT, address))
            }

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Identity?)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                IdentityFragment().apply {
                }
    }
}
