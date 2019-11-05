package coop.lab10.minerva.features.services

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import coop.lab10.minerva.Web3jManager
import coop.lab10.minerva.R
import coop.lab10.minerva.events.PaymentStartedEvent
import coop.lab10.minerva.utils.StreemUtils
import coop.lab10.minerva.utils.ValueFormatter
import org.greenrobot.eventbus.Subscribe


/// Handles user interaction with notifications (?)
class NotificationReceiver : BroadcastReceiver() {
    val TAG = NotificationReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationID = intent.extras.getInt(Notification.EXTRA_NOTIFICATION_ID)
        Log.d(TAG, "handling notification with id $notificationID, action ${intent.action}")
        when {
            intent.action == PushNotificationService.USER_ACTION_CONFIRM -> {

                val receiverWalletAddress = intent.extras.getString(PushNotificationService.EXTRA_RECEIVER_WALLET_ADDRESS)
                val amountPerTimeUnit = intent.extras.getLong(PushNotificationService.EXTRA_PAYMENT_AMOUNT)
                val serviceProviderName = intent.extras.getString(PushNotificationService.EXTRA_SERVICE_PROVIDER_NAME, context.resources.getString(R.string.unknown_service_provider))
                val currencySymbol = intent.extras.getString(PushNotificationService.EXTRA_SERVICE_CURRENCY_SYMBOL, "€")
                val timeUnit = intent.extras.getString(PushNotificationService.EXTRA_SERVICE_TIME_UNIT, "s")

                val amountPerHour = StreemUtils.calculateAmountPerHour(amountPerTimeUnit, timeUnit)


                // persist the values we need in final notification (streem stopped)
                // TODO: this is just a temporary solution. Do properly!
                val sharedPrefs = context.getSharedPreferences(PushNotificationService.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
                val prefsEditor = sharedPrefs.edit()
                prefsEditor.putString(PushNotificationService.EXTRA_SERVICE_PROVIDER_NAME, serviceProviderName)
                prefsEditor.putLong(PushNotificationService.EXTRA_PAYMENT_AMOUNT, amountPerTimeUnit)
                prefsEditor.putString(PushNotificationService.EXTRA_SERVICE_TIME_UNIT, timeUnit)
                prefsEditor.putString(PushNotificationService.EXTRA_SERVICE_CURRENCY_SYMBOL, currencySymbol)
                prefsEditor.putString(PushNotificationService.EXTRA_RECEIVER_WALLET_ADDRESS, receiverWalletAddress)
                prefsEditor.putLong(PushNotificationService.EXTRA_SERVICE_START_TIMESTAMP, System.currentTimeMillis())
                prefsEditor.apply()

                val flowRate = ValueFormatter.currency((amountPerHour / 100), currencySymbol) + "/h"
                val title = context.resources.getString(R.string.notification_payment_starting_title)
                val bodyString = context.resources.getString(R.string.notification_payment_starting_body)
                var body = String.format(bodyString, flowRate, serviceProviderName)

                val builder = PushNotificationService.buildPaymentNotification(context, title, body)
                manager.notify(PushNotificationService.PAYMENT_NOTIFICATION_ID, builder.build())

                /*
                TODO: this should not be handled here. Instead there should be a module which handles
                such requests regardless of how they're received (deeplink, push notification, ...)?
                */
                // TODO: what if init fails?
                // TODO: shouldn't be initialized here. Use singleton instead? Or hold in App instance?
                val web3jMgr = Web3jManager()
                Thread(Runnable {
                    web3jMgr.openStreem( context, receiverWalletAddress, amountPerHour.toBigDecimal())
                }).start()
                // TODO: update the notification in (success) callback handler


            }
            intent.action == PushNotificationService.USER_ACTION_DENY -> {
                // TODO add login into activities about rejection of the request
                manager.cancel(notificationID)
            }
            intent.action == PushNotificationService.USER_ACTION_STOP -> {
                manager.cancel(notificationID)

                val sharedPrefs = context.getSharedPreferences(PushNotificationService.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
                val receiverWalletAddress = sharedPrefs.getString(PushNotificationService.EXTRA_RECEIVER_WALLET_ADDRESS, "")
                // TODO: what if init fails?
                // TODO: shouldn't be initialized here. Use singleton instead? Or hold in App instance?
                val web3jMgr = Web3jManager()
                Thread(Runnable {
                    //web3jMgr.sendNativeToken(receiverWalletAddress, BigDecimal.valueOf(0))
                    web3jMgr.closeLastStreem(receiverWalletAddress)
                }).start()
                // TODO: update the notification in (success) callback handler
            }
        }
    }

    @Subscribe
    fun onPaymentStarted(event: PaymentStartedEvent) {
        Log.d("TAG", "Payment already started")
    }
}