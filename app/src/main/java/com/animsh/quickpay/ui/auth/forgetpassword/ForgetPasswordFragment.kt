package com.animsh.quickpay.ui.auth.forgetpassword

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.animsh.quickpay.databinding.FragmentForgetpasswordBinding
import com.animsh.quickpay.utils.LoadingDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * Created by animsh on 3/25/2021.
 */
class ForgetPasswordFragment : BottomSheetDialogFragment(), KodeinAware {

    private var _binding: FragmentForgetpasswordBinding? = null
    private val binding get() = _binding!!

    override val kodein by closestKodein()
    private val factory: ForgetPasswordViewModelFactory by instance()
    private lateinit var viewModel: ForgetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(ForgetPasswordViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetpasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            val loadingDialog: LoadingDialog = LoadingDialog(requireActivity())

            sendEmailButton.setOnClickListener {

                loadingDialog.showLoadingDialog()

                val uEmail = editTextEmailID.text

                if (uEmail.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter email address!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingDialog.dismissDialog()
                    return@setOnClickListener
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()) {
                        Toast.makeText(
                            requireContext(),
                            "Please enter valid email address!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingDialog.dismissDialog()
                        return@setOnClickListener
                    }
                }

                viewModel.forgetPassword(uEmail.toString()).invokeOnCompletion {
                    viewModel.forgetPasswordLiveData.observe(viewLifecycleOwner, { result ->
                        if (result.isSuccess) {
                            Toast.makeText(
                                requireContext(),
                                "Please check your email for password reset link!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadingDialog.dismissDialog()
                            dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                result.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                            loadingDialog.dismissDialog()
                            dismiss()
                        }
                    })
                }
            }
        }
    }

}