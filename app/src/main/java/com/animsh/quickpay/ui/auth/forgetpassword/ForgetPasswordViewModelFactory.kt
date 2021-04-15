package com.animsh.quickpay.ui.auth.forgetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.animsh.quickpay.data.AuthRepository

/**
 * Created by animsh on 4/12/2021.
 */
@Suppress("UNCHECKED_CAST")
class ForgetPasswordViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForgetPasswordViewModel(repository) as T
    }
}