package coop.lab10.minerva.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coop.lab10.minerva.BuildConfig
import coop.lab10.minerva.activities.MainActivity
import coop.lab10.minerva.R
import kotlinx.android.synthetic.main.activity_welcome.*

// Activity which take care of user onboarding and launching proper activity for the user.
// Deep links
// Login, if not logged in
// etc...
class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO not in used yet. On welcome screen probably we would have auth method to unlock the
        // wallet so only user can open it. This view would handle that if it would be enabled.

        if (checkFirstRun()) {
            setContentView(R.layout.activity_welcome)
            start_button.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(EXTRAS_FIRST_RUN, true)
                finish()
                startActivity(intent)
            }
            restore_button.setOnClickListener {
                val intent = Intent(this, RestoreActivity::class.java)
                startActivity(intent)
            }

        } else {
            setTheme(R.style.AppThemeNoActionBar)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    // TODO make it much more generic so any part of the app can refer to it
    // keep in mind that once run next time it won't get same result.
    // so maybe is not wise to give access to it within the app afterwards.
    private fun checkFirstRun(): Boolean {

        val prefsName = applicationContext.packageName + "app_shared_pref"
        val prefVersionCodeKey = "version_code"
        val valueDoesNotExist = -1

        // Get current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(prefVersionCodeKey, valueDoesNotExist)

        // Check for first run or upgrade
        return when {
            currentVersionCode == savedVersionCode -> // This is just a normal run
                false
            savedVersionCode == valueDoesNotExist -> {
                true
            }
            currentVersionCode > savedVersionCode -> {
                false
            }
            else -> {
                false
            }
        }
    }

    companion object {
        const val EXTRAS_FIRST_RUN = "FIRST_RUN"
        const val EXTRAS_MNEMONIC = "MNEMONIC"
    }
}
