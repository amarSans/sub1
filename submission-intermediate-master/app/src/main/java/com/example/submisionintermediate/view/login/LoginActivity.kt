package com.example.submisionintermediate.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.example.submisionintermediate.data.UserRepository
import com.example.submisionintermediate.databinding.ActivityLoginBinding
import com.example.submisionintermediate.di.Injection
import com.example.submisionintermediate.view.ViewModelFactory
import com.example.submisionintermediate.view.daftar.SignupActivity
import com.example.submisionintermediate.view.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding:ActivityLoginBinding
    private lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepository=Injection.provideRepository(this)
        showLoading(false)
        setupView()
        setupAction()
        playAnimation()
    }
    private fun setupAction() {
        binding.btnDaftar.setOnClickListener { startActivity(Intent(this, SignupActivity::class.java)) }
        binding.btnLogin.setOnClickListener {
            showLoading(true)
            val email=binding.emailEditText.text.toString()
            val password=binding.passwordEditText.text.toString()
        viewModel.viewModelScope.launch {
            val result=viewModel.login(email, password).first()
            if(result.isSuccess){
                val loginResponse=result.getOrNull()
                loginResponse?.loginResult.let { loginResult ->
                    loginResult?.token?.let { token->
                       viewModel.saveSession(token)
                    }
                }
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                finish()
            }else{
                showLoading(false)
                val error=result.exceptionOrNull()
                Toast.makeText(this@LoginActivity,"login failed:${error?.message}",Toast.LENGTH_SHORT).show()
            }
        }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar1.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun playAnimation() {
        val nilai:Long=500
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(nilai)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(nilai)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(nilai)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(nilai)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(nilai)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(nilai)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(nilai)
        val daftar = ObjectAnimator.ofFloat(binding.btnDaftar, View.ALPHA, 1f).setDuration(nilai)

        val button=AnimatorSet().apply { playTogether(login,daftar) }
        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                button
            )
            startDelay = nilai
        }.start()

    }
}