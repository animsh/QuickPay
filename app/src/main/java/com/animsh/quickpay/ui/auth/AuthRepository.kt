package com.animsh.quickpay.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.animsh.quickpay.entities.FAuth
import com.animsh.quickpay.entities.User
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by animsh on 4/11/2021.
 */
class AuthRepository {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun login(user: User): MutableLiveData<FAuth> {
        val fAuth: MutableLiveData<FAuth> = MutableLiveData()
        firebaseAuth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                val currentUser = currentUser()
                val isLogin = currentUser != null
                val msg = it.exception?.message.toString()
                fAuth.value = FAuth(isLogin, msg)
            } else {
                val msg = it.exception?.message.toString()
                fAuth.value = FAuth(errorMsg = msg)
            }
        }
        return fAuth
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}