package coop.lab10.minerva.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import coop.lab10.minerva.R


class NotificationChannelsManager {

    companion object {
        val PAYMENT_NOTIFICATION_CHANNEL_ID = "payment"
        val DEFAULT_NOTIFICATION_CHANNEL_ID= "MINERVA_DEFAULT"

        @RequiresApi(Build.VERSION_CODES.O)
        fun createChannels(context: Context) {
            val channels = arrayListOf<NotificationChannel>()
            val paymentChannel = NotificationChannel(PAYMENT_NOTIFICATION_CHANNEL_ID, "Payment", NotificationManager.IMPORTANCE_HIGH)
            paymentChannel.description = context.resources.getString(R.string.notification_payment_channel_desc)
            paymentChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            paymentChannel.setShowBadge(false)


            val defaultChannel = NotificationChannel(DEFAULT_NOTIFICATION_CHANNEL_ID, "Default", NotificationManager.IMPORTANCE_DEFAULT)
            defaultChannel.description = context.resources.getString(R.string.notification_default_channel_desc)
            defaultChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            defaultChannel.setShowBadge(false)

            channels.add(paymentChannel)
            channels.add(defaultChannel)


            val mNotificationManager = context
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannels(channels)
        }
    }
}