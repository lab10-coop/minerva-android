package coop.lab10.minerva

import android.app.Application
import android.os.Build
import androidx.work.*
import coop.lab10.minerva.features.values.StreemWorker
import coop.lab10.minerva.utils.NotificationChannelsManager
import me.uport.sdk.Uport
import java.util.concurrent.TimeUnit

class MinervaWallet : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = Uport.Configuration()
                .setApplicationContext(this)

        Uport.initialize(config)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelsManager.createChannels(this)
        }

        runStreemWorker()

    }


    // Run periodic worker to check open streems.
    // Worker can survive app kill and is manage by the OS
    private fun runStreemWorker() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val checkStreem =
                PeriodicWorkRequestBuilder<StreemWorker>(15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build()
    }


}
