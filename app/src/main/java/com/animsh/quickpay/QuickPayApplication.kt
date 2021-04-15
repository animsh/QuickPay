package com.animsh.quickpay

import android.app.Application
import com.animsh.quickpay.data.AuthRepository
import com.animsh.quickpay.data.DataStoreRepository
import com.animsh.quickpay.ui.auth.forgetpassword.ForgetPasswordViewModelFactory
import com.animsh.quickpay.ui.auth.login.LoginViewModelFactory
import com.animsh.quickpay.ui.auth.signup.SignUpViewModelFactory
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
        bind() from singleton { DataStoreRepository() }
        bind() from provider { LoginViewModelFactory(instance(), instance()) }
        bind() from provider { SignUpViewModelFactory(instance(), instance()) }
        bind() from provider { ForgetPasswordViewModelFactory(instance()) }
    }

}