package com.animsh.quickpay

import android.app.Application
import com.animsh.quickpay.ui.auth.AuthRepository
import com.animsh.quickpay.ui.auth.login.LoginViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/**
 * Created by animsh on 3/25/2021.
 */
class QuickPayApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@QuickPayApplication))

        bind() from singleton { AuthRepository() }
        bind() from provider { LoginViewModelFactory(instance()) }
    }

}