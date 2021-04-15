package com.animsh.quickpay.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.animsh.quickpay.data.AuthRepository
import com.animsh.quickpay.data.DataStoreRepository

/**
 * Created by animsh on 4/11/2021.
 */
@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(
    private val repository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository, dataStoreRepository) as T
    }
}