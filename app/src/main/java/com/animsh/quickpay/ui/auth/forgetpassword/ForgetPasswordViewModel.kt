package com.animsh.quickpay.ui.auth.forgetpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.animsh.quickpay.entities.FAuth
import com.animsh.quickpay.ui.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by animsh on 3/25/2021.
 */
class ForgetPasswordViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var forgetPasswordLiveData: MutableLiveData<FAuth> = MutableLiveData()

    fun forgetPassword(uEmail: String) = CoroutineScope(Dispatchers.Main).launch {
        forgetPasswordLiveData = repository.forgetPassword(uEmail)
    }
}