package com.animsh.quickpay.ui.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.animsh.quickpay.entities.FAuth
import com.animsh.quickpay.entities.User
import com.animsh.quickpay.ui.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by animsh on 3/25/2021.
 */
class SignUpViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var authenticatedUserLiveData: MutableLiveData<FAuth> = MutableLiveData()

    fun signUpUser(user: User) = CoroutineScope(Dispatchers.Main).launch {
        authenticatedUserLiveData = repository.signUp(user)
    }

}