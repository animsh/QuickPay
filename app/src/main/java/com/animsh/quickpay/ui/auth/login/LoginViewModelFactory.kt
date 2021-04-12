package com.animsh.quickpay.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.animsh.quickpay.ui.auth.AuthRepository

/**
 * Created by animsh on 4/11/2021.
 */
@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}