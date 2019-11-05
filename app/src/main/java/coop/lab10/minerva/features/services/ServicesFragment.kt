package coop.lab10.minerva.features.services

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coop.lab10.minerva.R

import coop.lab10.minerva.features.models.Service

class ServicesFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_services, container, false)


        // Set the adapter
        if (view is androidx.recyclerview.widget.RecyclerView) {
            with(view) {
                var services = arrayListOf<Service>()

                services.add(Service("ÖBB", R.drawable.obb_logo))
                services.add(Service("Patreaon", R.drawable.patreon_logo))
                services.add(Service("Energie Steiermark", R.drawable.energie_steiermark_logo))
                services.add(Service("Paybox", R.drawable.paybox_logo))


                //adapter = ServicesAdapter(services);
                //view.services_recycler.adapter = adapter;
            }
        }
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
