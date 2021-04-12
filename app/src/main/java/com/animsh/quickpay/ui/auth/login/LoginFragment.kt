package com.animsh.quickpay.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.animsh.quickpay.MainActivity
import com.animsh.quickpay.R
import com.animsh.quickpay.databinding.FragmentLoginBinding
import com.animsh.quickpay.entities.User
import com.google.android.material.transition.MaterialFadeThrough
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * Created by animsh on 3/25/2021.
 */
class LoginFragment : Fragment(R.layout.fragment_login), KodeinAware {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override val kodein by closestKodein()
    private val factory: LoginViewModelFactory by instance()
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        viewModel = ViewModelProvider(requireActivity(), factory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            loginButton.setOnClickListener {
                val uEmail = editTextEmailID.text
                val uPassword = editTextPassword.text

                if (uEmail.isNullOrEmpty() || uPassword.isNullOrEmpty()) {
                    return@setOnClickListener
                }

                viewModel.loginUser(
                    User(
                        email = uEmail.toString(),
                        password = uPassword.toString()
                    )
                ).invokeOnCompletion {
                    viewModel.authenticatedUserLiveData.observe(
                        viewLifecycleOwner,
                        { result ->
                            if (result.isLogin) {
                                val sharedPref =
                                    activity?.getSharedPreferences(
                                        getString(R.string.app_name),
                                        Context.MODE_PRIVATE
                                    )
                                        ?: return@observe
                                with(sharedPref.edit()) {
                                    putBoolean("userLogin", true)
                                    apply()
                                }
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                                activity?.finish()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    result.errorMsg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            }

            registerButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }

            forgetPasswordButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}