package com.animsh.quickpay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.animsh.quickpay.databinding.ActivitySplashBinding
import com.animsh.quickpay.ui.auth.AuthActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(1000)
            val sharedPref =
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
                    ?: return@launch
            val isUserLogin = sharedPref.getBoolean("userLogin", false)
            Log.d("TAGTAGTAG", "onCreate: $isUserLogin")
            if (isUserLogin) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            }
            finish()
        }
    }
}