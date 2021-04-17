package com.animsh.quickpay.ui.main.bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.animsh.quickpay.R
import com.animsh.quickpay.databinding.FragmentBagBinding

/**
 * Created by animsh on 4/17/2021.
 */
class BagFragment : Fragment(R.layout.fragment_bag) {
    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}