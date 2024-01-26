package com.example.mywhatsapp.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mywhatsapp.R
import com.example.mywhatsapp.databinding.ActivityMainBinding
import com.example.mywhatsapp.domain.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IAuthProgressStatus  {

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private lateinit var binding: ActivityMainBinding
    private val authenticationViewModel : AuthenticationViewModel by viewModels()

    val otpValue = MutableStateFlow<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationViewModel.setIAuthProgressStatus(this)

        binding.proceed.setOnClickListener {
            mainScope.launch {
                val signInJob = authenticationViewModel.signInWithPhoneNumber(
                    binding.etNumber.text.toString(),
                    this@MainActivity
                )

                val user: User =
                    User(userName = "John", userNumber = binding.etNumber.text.toString())
                authenticationViewModel.createUserProfile(user)
            }
        }
        if(checkAuthenticationStatus()){

        }
        else {

        }
    }


    fun checkAuthenticationStatus() :Boolean {
        return false
    }

    private fun openHomePageFragment(){
        // starting background task to update product
        // starting background task to update product
        val fp = Intent(applicationContext, HomeActivity::class.java)
        startActivity(fp)
    }

    fun showBottomSheet () {
        var otpFragment = OTPFragment()
        supportFragmentManager.beginTransaction().add(otpFragment, "bottomSheetOtpFragment").commit()
    }

    override fun showError() {
        Toast.makeText(
            this,
            "Failed to create user profile \uD83D\uDE28",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun dismissOtpFragmentBottomSheetDialog() {
        var fragment = supportFragmentManager.findFragmentByTag("bottomSheetOtpFragment")
        fragment?.let {
            (it as BottomSheetDialogFragment).dismiss()
        }
        openHomePageFragment()
    }

    override fun hideProgressBar() {
        super.hideProgressBar()
        binding.progressBar.visibility = View.GONE
        Toast.makeText(
            this,
            "User Profile Created \uD83C\uDF89",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun changeViewVisibilty() {
        binding.progressBar.visibility = View.GONE

    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        Toast.makeText(
            this,
            "Creating user profile 〰️",
            Toast.LENGTH_LONG
        ).show()
    }
}