package coop.lab10.minerva.features.services

import android.app.Notification.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import android.util.Log
import android.app.NotificationManager
import android.os.Build
import android.content.Context
import android.app.PendingIntent
import android.content.Intent
import android.text.format.DateFormat
import coop.lab10.minerva.R
import coop.lab10.minerva.utils.NotificationChannelsManager
import coop.lab10.minerva.utils.StreemUtils
import coop.lab10.minerva.utils.ValueFormatter
import org.web3j.crypto.WalletUtils
import kotlin.random.Random


/// Handles remotely initiated push notifications
// TODO: less hardcoded values
class PushNotificationService : FirebaseMessagingService() {

    // TODO: specify the supported actions (incl. params)
    private val REMOTE_ACTION_START_PAYMENT = "payment"
    private val REMOTE_ACTION_STOP_PAYMENT = "stopPayment"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (null != remoteMessage && remoteMessage.data != null) {
            Log.d(TAG, remoteMessage.data.toString())
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            val longBody = remoteMessage.data["longBody"]
            val action = remoteMessage.data["action"]
            val amount = remoteMessage.data["amount"]
            val timeUnit = remoteMessage.data["timeUnit"] ?: "s"
            val receiverWalletAddress = remoteMessage.data["receiverWalletAddress"]
            val currencySymbol = remoteMessage.data["currencySymbol"] ?: "€"
            val serviceProviderName = remoteMessage.data["serviceProviderName"]

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            lateinit var builder: NotificationCompat.Builder

            if (action == REMOTE_ACTION_START_PAYMENT) {
                if (amount != null && title != null && body != null) {
                    if (amount.toLong() > 0 && receiverWalletAddress != null && WalletUtils.isValidAddress(receiverWalletAddress)) {
                        builder = buildPaymentRequestNotification(this, title, body, longBody, amount.toLong(), timeUnit, currencySymbol, receiverWalletAddress, serviceProviderName)
                        builder.setChannelId(NotificationChannelsManager.PAYMENT_NOTIFICATION_CHANNEL_ID)
                    }
                }
                manager.notify(PAYMENT_NOTIFICATION_ID, builder.build())

            } else if (action == REMOTE_ACTION_STOP_PAYMENT) {
                // TODO: also ensure that the streem was actually stopped
                builder = buildPaymentStoppedNotification(this)
                // TODO handle multiple streems, currently just remove any payment notification
                manager.cancel(PAYMENT_NOTIFICATION_ID)
                manager.notify(STOPPED_PAYMENT_NOTIFICATION, builder.build())
            }
        }
    }

    companion object {
        val TAG = PushNotificationService::class.java.simpleName

        const val USER_ACTION_CONFIRM = "confirm"
        const val USER_ACTION_DENY = "deny"
        const val USER_ACTION_STOP = "stop"
        const val EXTRA_PAYMENT_AMOUNT = "coop.lab10.minerva.EXTRA_PAYMENT_AMOUNT"
        const val EXTRA_RECEIVER_WALLET_ADDRESS = "coop.lab10.minerva.EXTRA_RECEIVER_WALLET_ADDRESS"
        const val EXTRA_SERVICE_PROVIDER_NAME = "coop.lab10.minerva.EXTRA_SERVICE_PROVIDER_NAME"
        const val EXTRA_SERVICE_CURRENCY_SYMBOL = "coop.lab10.minerva.EXTRA_CURRENCY_SYMBOL"
        const val EXTRA_SERVICE_TIME_UNIT = "coop.lab10.minerva.EXTRA_TIME_UNIT"
        const val EXTRA_SERVICE_PAID_AMOUNT = "coop.lab10.minerva.EXTRA_SERVICE_PAID_AMOUNT"
        const val EXTRA_SERVICE_SECONDS_SINCE_START = "coop.lab10.minerva.EXTRA_SERVICE_SECONDS_SINCE_START"

        // not sure if this belongs here. Used as key for persistence only
        const val EXTRA_SERVICE_START_TIMESTAMP = "coop.lab10.minerva.EXTRA_SERVICE_STARTTIME"

        const val PAYMENT_NOTIFICATION_ID = 101
        const val STOPPED_PAYMENT_NOTIFICATION = 102

        const val SHARED_PREFS_NAME = "temporary_notification_data"


        fun buildPaymentRequestNotification(context: Context, title: String, body: String, longBody: String?, amount: Long, timeUnit: String, currencySymbol: String, receiverWalletAddress: String, serviceProviderName: String?): NotificationCompat.Builder {
            // TODO: build the body string from the params instead of taking from remote
            val confirmIntent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(EXTRA_NOTIFICATION_ID, PAYMENT_NOTIFICATION_ID)
                putExtra(EXTRA_RECEIVER_WALLET_ADDRESS, receiverWalletAddress)
                putExtra(EXTRA_PAYMENT_AMOUNT, amount)
                putExtra(EXTRA_SERVICE_PROVIDER_NAME, serviceProviderName)
                putExtra(EXTRA_SERVICE_TIME_UNIT, timeUnit)
                putExtra(EXTRA_SERVICE_CURRENCY_SYMBOL, currencySymbol)
            }
            confirmIntent.action = USER_ACTION_CONFIRM

            val denyIntent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(EXTRA_NOTIFICATION_ID, PAYMENT_NOTIFICATION_ID)
            }
            denyIntent.action = USER_ACTION_DENY

            val confirmPendingIntent: PendingIntent =
                    PendingIntent.getBroadcast(context, Random.nextInt(), confirmIntent, 0)

            val denyPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, Random.nextInt(), denyIntent, 0)

            val channelId = NotificationChannelsManager.PAYMENT_NOTIFICATION_CHANNEL_ID
            val builder = getBuilder(context, channelId)
                    .setSmallIcon(coop.lab10.minerva.R.drawable.minerva_statusbar_icon)
                    .setContentTitle(title)
                    .setContentText(body)

                    .setPriority(NotificationCompat.PRIORITY_MAX)
            if (longBody != null) {
                builder.setStyle(NotificationCompat.BigTextStyle()
                        .bigText(longBody))
            }

            val confirmAction = NotificationCompat.Action.Builder(R.drawable.ic_artis_logo, "Confirm", confirmPendingIntent).build()
            val denyAction = NotificationCompat.Action.Builder(R.drawable.ic_artis_logo, "Deny", denyPendingIntent).build()

            builder.addAction(confirmAction)
            builder.addAction(denyAction)
            return builder
        }

        fun buildPaymentNotification(context: Context, title: String, body: String) : NotificationCompat.Builder {
            val channelId = NotificationChannelsManager.PAYMENT_NOTIFICATION_CHANNEL_ID
            val builder = getBuilder(context, channelId)
                    .setSmallIcon(R.drawable.minerva_statusbar_icon)
                    .setContentTitle(title)
                    .setContentText(body).setAutoCancel(true)
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
            return builder
        }

        // TODO: should probably get an object or object id/reference instead of detail params
        fun buildPaymentStartedNotification(context: Context, title: String, body: String, alreadyPaid: Double, secondsSinceStart: Long, currencySymbol: String, receiverWalletAddress: String): NotificationCompat.Builder {
            val stopIntent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(EXTRA_NOTIFICATION_ID, PAYMENT_NOTIFICATION_ID)
                putExtra(EXTRA_RECEIVER_WALLET_ADDRESS, receiverWalletAddress)
                putExtra(EXTRA_SERVICE_PAID_AMOUNT, alreadyPaid)
                putExtra(EXTRA_SERVICE_CURRENCY_SYMBOL, currencySymbol)
                putExtra(EXTRA_SERVICE_SECONDS_SINCE_START, secondsSinceStart)

            }
            stopIntent.action = USER_ACTION_STOP

            val stopPendingIntent: PendingIntent = PendingIntent.getBroadcast(context, Random.nextInt(), stopIntent, 0)

            val channelId = NotificationChannelsManager.PAYMENT_NOTIFICATION_CHANNEL_ID
            val builder = getBuilder(context, channelId)
                    .setSmallIcon(coop.lab10.minerva.R.drawable.minerva_statusbar_icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setOnlyAlertOnce(true)
                    .setProgress(0,0,true)
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)

            val stopAction = NotificationCompat.Action.Builder(R.drawable.minerva_statusbar_icon, "Stop payment", stopPendingIntent).build()

            builder.addAction(stopAction)
            return builder
        }

        /// builds a "continuous payment stopped" notification based on persisted values
        fun buildPaymentStoppedNotification(context: Context): NotificationCompat.Builder {
            val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            // TODO: should fail instead of showing default values
            val serviceProviderName = sharedPrefs.getString(EXTRA_SERVICE_PROVIDER_NAME, "")
            val amountPerTimeUnit = sharedPrefs.getLong(EXTRA_PAYMENT_AMOUNT, 0)
            val timeUnit = sharedPrefs.getString(EXTRA_SERVICE_TIME_UNIT, "")
            val currencySymbol = sharedPrefs.getString(EXTRA_SERVICE_CURRENCY_SYMBOL, "€")
            val startTimestamp = sharedPrefs.getLong(EXTRA_SERVICE_START_TIMESTAMP, 0)

            val amountPerSecond = StreemUtils.calculateAmountPerSecond(amountPerTimeUnit, timeUnit)
            val secondsSinceStart = (System.currentTimeMillis() - startTimestamp) / 1000
            val paidAmount = amountPerSecond * secondsSinceStart

            val title = "Continuous Payment Finished"

            val paidString = ValueFormatter.currency((paidAmount / 100), currencySymbol)
            val timePeriod = ValueFormatter.timeCounter(secondsSinceStart)

            val body = "$serviceProviderName received $paidString in $timePeriod for charging"


            val channelId = NotificationChannelsManager.PAYMENT_NOTIFICATION_CHANNEL_ID
            val builder = getBuilder(context, channelId)
                    .setSmallIcon(coop.lab10.minerva.R.drawable.minerva_statusbar_icon)
                    .setContentTitle(title)
                    .setContentText(body).setAutoCancel(true)
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
            return builder
        }

        fun buildOngoingPaymentNotification(context: Context) {
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

            val secondsSinceStart = (System.currentTimeMillis() - startedTimestamp) / 1000
            val paidAmount = amountPerSecond * secondsSinceStart

            var string = context.resources.getString(R.string.notification_payment_ongoing_body)
            var titleString = context.resources.getString(R.string.notification_payment_ongoing_title)

            // TODO add support for multiple streems at once
            Thread(
                    Runnable {
                        var ongogingPayment = true
                        var seconds = secondsSinceStart
                        var alreadyPaid = paidAmount
                        while (ongogingPayment) {
                            val notifications = manager.getActiveNotifications()
                            for (notification in notifications) {
                                if (notification.id == PushNotificationService.PAYMENT_NOTIFICATION_ID) {
                                    // Payment Notification still active continue updating

                                    var body = String.format(string, (amountPerTimeUnit / 100).toFloat(), currencySymbol, timeUnit, serviceProviderName, startedAt)

                                    var title = if (alreadyPaid > 0) {
                                        // TODO value should be taken from local DB and passing should come from model
                                        String.format(titleString, ValueFormatter.timeCounter(seconds), ValueFormatter.currency((alreadyPaid / 100), currencySymbol) )
                                    } else {
                                        String.format(titleString, ValueFormatter.timeCounter(seconds), ValueFormatter.currency((alreadyPaid), currencySymbol))
                                    }
                                    val builder = buildPaymentStartedNotification(context, title, body, alreadyPaid, seconds, currencySymbol, receiverWalletAddress)
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


        private fun getBuilder(context: Context, channelId: String): NotificationCompat.Builder {

            val builder = NotificationCompat.Builder(context, channelId)

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.minerva_statusbar_icon);
                builder.setColor(context.resources.getColor(R.color.minervaBgColor));
            } else {
                builder.setSmallIcon(R.drawable.minerva_notification_icon);
            }

            return builder
        }
    }
}