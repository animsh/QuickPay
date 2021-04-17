package com.animsh.quickpay.ui.main.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.animsh.quickpay.R
import com.animsh.quickpay.databinding.FragmentScannerBinding

/**
 * Created by animsh on 4/17/2021.
 */

class ScannerFragment : Fragment(R.layout.fragment_scanner) {
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}