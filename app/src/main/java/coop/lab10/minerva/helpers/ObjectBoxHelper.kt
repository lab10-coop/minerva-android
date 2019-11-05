package coop.lab10.minerva.helpers

import android.content.Context
import coop.lab10.minerva.features.models.MyObjectBox
import io.objectbox.BoxStore

object ObjectBoxHelper {
    private var boxStore: BoxStore? = null

    fun init(context: Context) {
        if (boxStore != null)
            boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build()
    }

    fun get(): BoxStore? {
        return boxStore
    }
}