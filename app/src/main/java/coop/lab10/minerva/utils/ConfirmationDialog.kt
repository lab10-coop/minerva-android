package coop.lab10.minerva.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.view.ContextThemeWrapper
import coop.lab10.minerva.R

class ConfirmationDialog : DialogFragment() {

    var payload = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            val mArgs = getArguments()
            payload= mArgs!!.getString("payload")
        }
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(ContextThemeWrapper(getActivity(), R.style.ConfrimDialog))
        builder.setMessage(payload)
                .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialog, id ->
                    // SIGN and send data back to the banking app
                    var intent = Intent()
                    var signedData = "0x232425"
                    intent.putExtra("signedPayload", signedData)
                    activity!!.setResult(Activity.RESULT_OK, intent)
                    activity!!.finish()
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                    activity!!.setResult(Activity.RESULT_CANCELED)
                    activity!!.finish()
                }).setOnDismissListener() {
                    activity!!.setResult(Activity.RESULT_CANCELED)
                    activity!!.finish()
                }
        // Create the AlertDialog object and return it
        return builder.create()
    }
}