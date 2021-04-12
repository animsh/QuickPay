package com.animsh.quickpay.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.animsh.quickpay.ui.auth.AuthRepository

/**
 * Created by animsh on 4/12/2021.
 */
@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(repository) as T
    }
}