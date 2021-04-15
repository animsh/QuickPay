package com.animsh.quickpay.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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
import com.animsh.quickpay.utils.Constants.RC_SIGN_IN
import com.animsh.quickpay.utils.Constants.WEB_KEY
import com.animsh.quickpay.utils.LoadingDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
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

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
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
            loadingDialog = LoadingDialog(requireActivity())

            loginButton.setOnClickListener {
                loadingDialog.showLoadingDialog()
                val uEmail = editTextEmailID.text
                val uPassword = editTextPassword.text

                if (uEmail.isNullOrEmpty() || uPassword.isNullOrEmpty()) {
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
                            if (result.isSuccess) {
                                saveLoginState()
                                loadingDialog.dismissDialog()
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                                activity?.finish()
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

            loginWithGoogleButton.setOnClickListener {
                loadingDialog.showLoadingDialog()
                initGoogleSignInClient()
                val signInIntent: Intent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }

            registerButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }

            forgetPasswordButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
            }
        }
    }

    private fun saveLoginState() {
        val sharedPref =
            activity?.getSharedPreferences(
                getString(R.string.app_name),
                Context.MODE_PRIVATE
            )
        with(sharedPref?.edit()) {
            this?.putBoolean("userLogin", true)
            this?.apply()
        }
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_KEY)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount: GoogleSignInAccount? =
                    task.getResult(ApiException::class.java)
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount)
                } else {
                    loadingDialog.dismissDialog()
                }
            } catch (e: ApiException) {
                Log.e("TAGTAG", "onActivityResult: " + e.localizedMessage)
                loadingDialog.dismissDialog()
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        viewModel.googleAuthentication(googleAuthCredential).invokeOnCompletion {
            viewModel.googleAuthenticationLiveData.observe(viewLifecycleOwner, {
                if (it.isSuccess) {
                    if (it.isNew) {
                        viewModel.createUser(
                            User(
                                uid = it.uid,
                                email = it.email,
                                password = "",
                                name = it.name,
                                mobile = ""
                            )
                        ).invokeOnCompletion {
                            viewModel.dataStoreLiveData.observe(
                                viewLifecycleOwner,
                                { dataResult ->
                                    if (dataResult.isSuccess) {
                                        saveLoginState()
                                        val intent =
                                            Intent(requireContext(), MainActivity::class.java)
                                        startActivity(intent)
                                        activity?.finish()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            dataResult.msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    loadingDialog.dismissDialog()
                                })
                        }
                    } else {
                        saveLoginState()
                        val intent =
                            Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                loadingDialog.dismissDialog()
            })
        }
    }
}