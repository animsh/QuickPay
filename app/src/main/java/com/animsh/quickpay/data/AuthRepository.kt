package com.animsh.quickpay.data

import androidx.lifecycle.MutableLiveData
import com.animsh.quickpay.entities.FAuth
import com.animsh.quickpay.entities.GAuth
import com.animsh.quickpay.entities.User
import com.google.firebase.auth.AuthCredential
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
                fAuth.value = FAuth(msg = msg)
            }
        }
        return fAuth
    }

    fun signUp(user: User): MutableLiveData<FAuth> {
        val fAuth: MutableLiveData<FAuth> = MutableLiveData()
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val isSuccess = it.result?.user?.uid != null
                    val msg = it.result?.user?.uid.toString()
                    fAuth.value = FAuth(isSuccess, msg)
                } else {
                    val msg = it.exception?.message.toString()
                    fAuth.value = FAuth(msg = msg)
                }
            }

        return fAuth
    }

    fun forgetPassword(uEmail: String): MutableLiveData<FAuth> {
        val fAuth: MutableLiveData<FAuth> = MutableLiveData()
        firebaseAuth.sendPasswordResetEmail(uEmail).addOnCompleteListener {
            if (it.isSuccessful) {
                val isSuccess = true
                val msg = it.exception?.message.toString()
                fAuth.value = FAuth(isSuccess, msg)
            } else {
                val msg = it.exception?.message.toString()
                fAuth.value = FAuth(msg = msg)
            }
        }
        return fAuth
    }

    fun googleAuthentication(authCredential: AuthCredential): MutableLiveData<GAuth> {
        val gAuth: MutableLiveData<GAuth> = MutableLiveData()
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                val isNewUser = it.result?.additionalUserInfo?.isNewUser ?: false
                val currentUser = currentUser()
                if (currentUser != null) {
                    val id = currentUser.uid
                    val uName = currentUser.displayName?.toString()
                    val uEmail = currentUser.email?.toString()
                    val uMobileNumber = currentUser.phoneNumber?.toString()
                    gAuth.value = GAuth(
                        isSuccess = true,
                        isNew = isNewUser,
                        uid = id,
                        name = uName.toString(),
                        email = uEmail.toString(),
                        mobile = uMobileNumber.toString()
                    )
                } else {
                    gAuth.value = GAuth(message = it.exception?.message.toString())
                }
            } else {
                gAuth.value = GAuth(message = it.exception?.message.toString())
            }
        }
        return gAuth
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}