package com.animsh.quickpay

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

/**
 * Created by animsh on 3/25/2021.
 */
class QuickPayApplication :Application(),KodeinAware {
    override val kodein: Kodein =  Kodein.lazy {
        import(androidXModule(this@QuickPayApplication))
    }

}