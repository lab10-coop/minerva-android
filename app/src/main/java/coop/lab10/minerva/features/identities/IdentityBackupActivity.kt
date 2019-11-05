package coop.lab10.minerva.features.identities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.uport.sdk.signer.UportHDSigner
import coop.lab10.minerva.R
import kotlinx.android.synthetic.main.activity_identity_backup.*
import kotlinx.android.synthetic.main.main_content.custom_title
import kotlinx.android.synthetic.main.main_content.toolbar


class IdentityBackupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity_backup)
        setSupportActionBar(toolbar)
        val mSupportActionBar = supportActionBar
        if (mSupportActionBar != null) {
            mSupportActionBar.setDisplayShowTitleEnabled(false)
            mSupportActionBar.setDisplayHomeAsUpEnabled(true)
            mSupportActionBar.setDisplayShowHomeEnabled(true)
        }
        custom_title.text = resources.getString(R.string.identity_backup)

        val phrase = intent.getStringExtra("PHRASE")
        if (phrase != null) {
            phraseList.text = phrase.replace(" ", "\n")
        } else {
            val hdList = UportHDSigner().allHDRoots(this)
            // TODO support multiple root keys
            // currently we take only first assuming that there should be just one root key
            if (hdList.size > 0) {
                UportHDSigner().showHDSeed(this, hdList.first(), "Unlock your key") { err, phrase ->
                    if (err == null) {
                        phraseList.text = phrase.replace(" ", "\n")
                    } else {
                        finish()
                    }
                }
            }
        }

        copy_to_clipboard.setOnClickListener {
            copyToClipboard(phraseList.text.toString().replace("\n", " "))
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData
                .newPlainText("message", text)
        clipboard.primaryClip = clip
        Toast.makeText(this,
                resources.getString(R.string.notify_copiedtoclipboard),
                Toast.LENGTH_SHORT).show()
    }
}
