package com.animsh.quickpay.ui.auth.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import com.animsh.quickpay.R
import com.animsh.quickpay.databinding.FragmentSignupBinding
import com.animsh.quickpay.entities.User
import com.animsh.quickpay.utils.LoadingDialog
import com.google.android.material.transition.MaterialFadeThrough
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * Created by animsh on 3/25/2021.
 */
class SignUpFragment : Fragment(R.layout.fragment_signup), KodeinAware {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override val kodein by closestKodein()
    private val factory: SignUpViewModelFactory by instance()
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        viewModel = ViewModelProvider(requireActivity(), factory).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            val loadingDialog: LoadingDialog = LoadingDialog(requireActivity())

            signUpButton.setOnClickListener {

                loadingDialog.showLoadingDialog()

                val uEmail = editTextEmailID.text
                val uFullName = editTextFullName.text
                val uMobileNumber = editTextMobileNumber.text
                val uPassword = editTextPassword.text

                if (uEmail.isNullOrEmpty() || uFullName.isNullOrEmpty() || uMobileNumber.isNullOrEmpty() || uPassword.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the details!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingDialog.dismissDialog()
                    return@setOnClickListener
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()) {
                        Toast.makeText(
                            requireContext(),
                            "Please use valid email address!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingDialog.dismissDialog()
                        return@setOnClickListener
                    }
                    if (!uMobileNumber.matches(Regex("^[+]?[0-9]{10}\$"))) {
                        Toast.makeText(
                            requireContext(),
                            "Please use valid Mobile Number!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingDialog.dismissDialog()
                        return@setOnClickListener
                    }
                }

                viewModel.signUpUser(
                    User(
                        email = uEmail.toString(),
                        password = uPassword.toString(),
                        name = uFullName.toString()
                    )
                ).invokeOnCompletion {
                    viewModel.authenticatedUserLiveData.observe(viewLifecycleOwner, { result ->
                        if (result.isSuccess) {
                            viewModel.createUser(
                                User(
                                    uid = result.msg,
                                    email = uEmail.toString(),
                                    password = uPassword.toString(),
                                    name = uFullName.toString(),
                                    mobile = uMobileNumber.toString()
                                )
                            ).invokeOnCompletion {
                                viewModel.dataStoreLiveData.observe(
                                    viewLifecycleOwner,
                                    { dataResult ->
                                        if (dataResult.isSuccess) {
                                            Toast.makeText(
                                                requireContext(),
                                                "Login using email & password!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            loadingDialog.dismissDialog()
                                            findNavController().popBackStack(
                                                R.id.loginFragment,
                                                true
                                            )
                                            findNavController().navigate(R.id.loginFragment)
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                dataResult.msg,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            loadingDialog.dismissDialog()
                                        }

                                    })
                            }

                        } else {
                            Toast.makeText(
                                requireContext(),
                                result.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                            loadingDialog.dismissDialog()
                        }
                    })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}