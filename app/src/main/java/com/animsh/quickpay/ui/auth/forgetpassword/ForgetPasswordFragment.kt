package com.animsh.quickpay.ui.auth.forgetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.animsh.quickpay.databinding.FragmentForgetpasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by animsh on 3/25/2021.
 */
class ForgetPasswordFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentForgetpasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetpasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

}