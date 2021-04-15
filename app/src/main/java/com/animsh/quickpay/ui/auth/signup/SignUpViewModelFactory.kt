package com.animsh.quickpay.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.animsh.quickpay.data.AuthRepository
import com.animsh.quickpay.data.DataStoreRepository

/**
 * Created by animsh on 4/12/2021.
 */
@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory(
    private val repository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(repository, dataStoreRepository) as T
    }
}