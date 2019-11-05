package coop.lab10.minerva.features.values

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import coop.lab10.minerva.features.services.PushNotificationService


// StreemWorker use to monitor ongoing streems and update push notification to inform user about current state
class StreemWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    companion object {
        const val NOTIFICATION_ID = "PAYMENT_NOTIFICATION_ID"
    }

    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.

        val notificationId = PushNotificationService.PAYMENT_NOTIFICATION_ID
        // TODO check if there are open streems if so show in Notification
        // do async call to the network to check open streems we can use CountDownLatch for await function

        // TODO override payment notifaiction if exists
//        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val builder = PushNotificationService.buildPaymentStartedNotification(applicationContext, title, body)
//        manager.notify(notificationId, builder.build())

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }
}



