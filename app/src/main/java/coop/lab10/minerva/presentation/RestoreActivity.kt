package coop.lab10.minerva.presentation

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import coop.lab10.minerva.R
import coop.lab10.minerva.activities.MainActivity
import kotlinx.android.synthetic.main.activity_restore.*
import kotlinx.android.synthetic.main.activity_welcome.start_button


class RestoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restore)

        start_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(WelcomeActivity.EXTRAS_MNEMONIC, mnemonic.text.toString())
            intent.putExtra(WelcomeActivity.EXTRAS_FIRST_RUN, true)
            finish()
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadClipboard()
        clearClipboard()
    }

    private fun loadClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        try {
            mnemonic.setText(clipboard.primaryClip.getItemAt(0).text)
        } catch (e: Exception) {
            return
        }
    }

    // Simple way to clear the clipboard after pasting it into the text.
    // if mnemonic stays in the clipboard someone cold hijack it.
    // TODO clipboard overall does not seems like secure way to store mnemonic
    // even for short period of time. Much better would be to use intents or QR
    // to pass data.
    private fun clearClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.text = ""
    }
}
