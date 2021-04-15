package com.animsh.quickpay.entities

/**
 * Created by animsh on 4/15/2021.
 */
data class GAuth(
    val isSuccess: Boolean = false,
    val isNew: Boolean = false,
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val mobile: String = "",
    val message: String = ""
)