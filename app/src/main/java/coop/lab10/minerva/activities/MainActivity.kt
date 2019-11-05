package coop.lab10.minerva.activities

import android.accounts.AccountManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import coop.lab10.minerva.Web3jManager
import coop.lab10.minerva.BuildConfig
import coop.lab10.minerva.R
import coop.lab10.minerva.domain.SecureStorage
import coop.lab10.minerva.events.ErrorEvent
import coop.lab10.minerva.events.MessageEvent
import coop.lab10.minerva.events.PaymentErrorEvent
import coop.lab10.minerva.events.PaymentStartedEvent
import coop.lab10.minerva.features.activities.ActivitiesFragment
import coop.lab10.minerva.features.identities.IdentityBackupActivity
import coop.lab10.minerva.features.identities.IdentityFragment
import coop.lab10.minerva.features.models.Identity
import coop.lab10.minerva.features.services.PushNotificationService
import coop.lab10.minerva.features.services.ServicesFragment
import coop.lab10.minerva.features.values.ValuesFragment
import coop.lab10.minerva.helpers.ObjectBoxHelper
import coop.lab10.minerva.presentation.SettingsActivity
import coop.lab10.minerva.presentation.WelcomeActivity
import coop.lab10.minerva.utils.StreemUtils
import coop.lab10.minerva.utils.ValueFormatter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.kethereum.bip39.model.MnemonicWords
import org.kethereum.bip39.validate
import org.kethereum.bip39.wordlists.WORDLIST_ENGLISH
import java.io.IOException


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, IdentityFragment.OnListFragmentInteractionListener,
        ValuesFragment.OnFragmentInteractionListener,
        ServicesFragment.OnFragmentInteractionListener,
        ActivitiesFragment.OnFragmentInteractionListener {
    val TAG = MainActivity::class.java.simpleName


    var hideMenu: Boolean = false
    var web3jMgr: Web3jManager? = null

    override fun onFragmentInteraction(uri: Uri) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListFragmentInteraction(item: Identity?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()
        parseIntentForPairing()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent) //must store the new intent unless getIntent() will return the old one
        parseIntentForPairing()

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val switchPref = sharedPref.getBoolean(SettingsActivity.KEY_ALLOW_SCREEN_CAPTURE, false)

        if (switchPref) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }
        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            val token = FirebaseInstanceId.getInstance().token
            Log.e("FCM TOKEN", "Token:" + token)
        }

        ObjectBoxHelper.init(this)

        setSupportActionBar(toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        custom_title.text = resources.getString(R.string.identities_title)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.menu.getItem(0).isChecked = true
        loadFragment(IdentityFragment())

        web3jMgr = Web3jManager()


        val firstRun = intent.extras.getBoolean(WelcomeActivity.EXTRAS_FIRST_RUN, false)
        if (firstRun) {
            val restore = intent.extras.getString(WelcomeActivity.EXTRAS_MNEMONIC, "")
            // To avoid of passing empty string or invalid input we need to verify mnemonic.
            val mnemonic = MnemonicWords(restore)
            if (mnemonic.validate(WORDLIST_ENGLISH)) {
                SecureStorage.loadSeed(this, restore) { _, address, _ ->
                    if (address != null) {
                        Toast.makeText(this, "Key loaded correctly", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Mnemonic is not valid check it and try again", Toast.LENGTH_LONG).show()
            }
            if (restore.isEmpty())
                SecureStorage.createSeed(this) { _, address, _ ->
                    if (address != null) {
                        Toast.makeText(this, "Default identity created", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Key already exist do nothing", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val fragments = supportFragmentManager.backStackEntryCount
            if (fragments == 1) {
                finish();
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.identity, menu)
        if (hideMenu) {
            for (i in 0 until menu.size())
                menu.getItem(i).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.add_button -> {
                val currentFragment = supportFragmentManager.fragments.last()
                if (currentFragment is IdentityFragment)
                    currentFragment.addDefault()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_identities -> {
                custom_title.text = resources.getString(R.string.identities_title)
                hideMenu = false
                invalidateOptionsMenu()
                val fragment = IdentityFragment.newInstance()
                loadFragment(fragment)
            }
            R.id.nav_values -> {
                custom_title.text = resources.getString(R.string.values_title)
                hideMenu = false
                invalidateOptionsMenu()
                val fragment = ValuesFragment()
                loadFragment(fragment)
            }
            R.id.nav_services -> {
                custom_title.text = resources.getString(R.string.services_title)
                hideMenu = false
                invalidateOptionsMenu()
                val fragment = ServicesFragment()
                loadFragment(fragment)
            }
            R.id.nav_activities -> {
                custom_title.text = resources.getString(R.string.activities_title)
                hideMenu = true
                invalidateOptionsMenu()
                val fragment = ActivitiesFragment()
                loadFragment(fragment)
            }
            R.id.nav_backup -> {
                val intent = Intent(this, IdentityBackupActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_web3test -> {
                // TODO: trigger notification or remove
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // On connecting account
        // TODO extract request codes to one place and name them
        if (requestCode == 7 && resultCode == RESULT_OK) {
            val accountName = data!!.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_fragment_container, fragment)
        fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }

    private fun parseIntentForPairing() {
        if (intent.extras != null && intent.action == "coop.lab10.minerva.ACTION_PAIR") {

            var url = intent.extras.getString("callback_url")
            val token = FirebaseInstanceId.getInstance().token
            if (token == null) {
                Toast.makeText(this, "Token is not yet ready try again", Toast.LENGTH_LONG).show()
            } else {
                url += "/$token"

                val httpClient = OkHttpClient()
                val request = Request.Builder()
                        .url(url)
                        .build()

                Log.d(TAG, "onResume with intent: url: $url, token: $token")

                Thread(Runnable {
                    try {
                        val response = httpClient.newCall(request).execute()
                        if (response.isSuccessful) {
                            EventBus.getDefault().post(MessageEvent("Your device is pairing with: $url"))
                        } else {
                            EventBus.getDefault().post(ErrorEvent("Failed to connect: $url"))
                        }
                    } catch (e: IOException) {
                        Log.e("MainActivity", "error in getting response get request okhttp")
                        EventBus.getDefault().post(ErrorEvent("Error in getting response from okhttp"))
                    }
                }).start()
            }
        }
    }

    // TODO make it much more generic so any part of the app can refer to it
    private fun checkFirstRun() {

        val prefsName = applicationContext.packageName + "app_shared_pref"
        val prefVersionCodeKey = "version_code"
        val valueDoesNotExist = -1

        // Get current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(prefVersionCodeKey, valueDoesNotExist)

        // Check for first run or upgrade
        when {
            currentVersionCode == savedVersionCode -> // This is just a normal run
                return
            savedVersionCode == valueDoesNotExist -> {
                // Trigger creating of default master key
                val currentFragment = supportFragmentManager.fragments.last()
                if (currentFragment is IdentityFragment)
                    currentFragment.addDefault()
            }
            currentVersionCode > savedVersionCode -> {
                // TODO Do something if there is an upgrade.
            }
        }
        prefs.edit().putInt(prefVersionCodeKey, currentVersionCode).apply()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onError(event: ErrorEvent) {
        val snack = Snackbar.make(main_fragment_container, event.message, Snackbar.LENGTH_LONG)
        snack.view.setBackgroundColor(resources.getColor(R.color.snackBackgroundError))
        snack.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(event: MessageEvent) {
        Snackbar.make(main_fragment_container, event.message, Snackbar.LENGTH_LONG)
                .show()
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onPaymentStarted(event: PaymentStartedEvent) {
        // persist the values we need in final notification (streem stopped)
        val context = this
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val sharedPrefs = context.getSharedPreferences(PushNotificationService.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val amountPerTimeUnit = sharedPrefs.getLong(PushNotificationService.EXTRA_PAYMENT_AMOUNT, 0)
        val receiverWalletAddress = sharedPrefs.getString(PushNotificationService.EXTRA_RECEIVER_WALLET_ADDRESS, "")
        val currencySymbol = sharedPrefs.getString(PushNotificationService.EXTRA_SERVICE_CURRENCY_SYMBOL, "")
        val timeUnit = sharedPrefs.getString(PushNotificationService.EXTRA_SERVICE_TIME_UNIT, "")
        val serviceProviderName = sharedPrefs.getString(PushNotificationService.EXTRA_SERVICE_PROVIDER_NAME, "")
        val startedTimestamp = sharedPrefs.getLong(PushNotificationService.EXTRA_SERVICE_START_TIMESTAMP, 0)

        val startedAt = DateFormat.format("HH:mm", startedTimestamp).toString()
        val amountPerSecond = StreemUtils.calculateAmountPerSecond(amountPerTimeUnit, timeUnit)

        var string = context.resources.getString(R.string.notification_payment_ongoing_body)
        var titleString = context.resources.getString(R.string.notification_payment_ongoing_title)

        // TODO add support for multiple streems at once
        Thread(
                Runnable {
                    var ongogingPayment = true
                    var seconds = 0L
                    var alreadyPaid = 0.0
                    while (ongogingPayment) {
                        val notifications = manager.getActiveNotifications()
                        for (notification in notifications) {
                            if (notification.id == PushNotificationService.PAYMENT_NOTIFICATION_ID) {
                                // Payment Notification still active continue updating

                                var body = String.format(string, (amountPerTimeUnit / 100).toFloat(), currencySymbol, timeUnit, serviceProviderName, startedAt)

                                var title = if (alreadyPaid > 0) {
                                    // TODO value should be taken from local DB and passing should come from model
                                    String.format(titleString, ValueFormatter.timeCounter(seconds), ValueFormatter.currency((alreadyPaid / 100), currencySymbol))
                                } else {
                                    String.format(titleString, ValueFormatter.timeCounter(seconds), ValueFormatter.currency((alreadyPaid), currencySymbol))
                                }
                                val builder = PushNotificationService.buildPaymentStartedNotification(context, title, body, alreadyPaid, seconds, currencySymbol, receiverWalletAddress)
                                manager.notify(notification.id, builder.build())
                            } else {
                                ongogingPayment = false
                            }
                        }

                        // Sleeps the thread, simulating an operation
                        // that takes time
                        try {
                            // Sleep for 1 seconds
                            Thread.sleep((1000).toLong())
                            seconds += 1
                            alreadyPaid += amountPerSecond
                        } catch (e: InterruptedException) {
                            Log.e(TAG, e.message)
                            // TODO handle error
                        }

                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start()
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onPaymentError(event: PaymentErrorEvent) {
        val context = this
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val title = context.resources.getString(R.string.notification_payment_error_title)
        val body = event.message
        val builder = PushNotificationService.buildPaymentNotification(context, title, body)
        manager.notify(PushNotificationService.PAYMENT_NOTIFICATION_ID, builder.build())
    }
}
