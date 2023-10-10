package com.example.loginexercise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.loginexercise.databinding.ActivityLoginBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginBtnClose.setOnClickListener{ closeApp() }
        binding.loginBtnLogin.setOnClickListener { submitForm() }

        binding.loginInputEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                validateForm()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        binding.loginInputPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                validateForm()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    private fun validateForm(){
        val loginButton = binding.loginBtnLogin
        binding.loginContainerPassword.helperText = validPassword()
        binding.loginContainerEmail.helperText = validEmail()

        loginButton.isEnabled = validPassword() == null && validEmail() == null
    }

    private fun closeApp(){
        finish()
    }

    private fun submitForm(){
        val validEmail = binding.loginContainerEmail.helperText == null
        val validPassword = binding.loginContainerPassword.helperText == null

        if(validEmail && validPassword){
            binding.loginBtnLogin.visibility = View.GONE
            binding.loginProgressIndicatorLoading.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                val email = binding.loginInputEmail.text.toString()
                val password = binding.loginInputPassword.text.toString()
                val user = mockUsers.find{ it.email == email && it.password == password}
                if(user != null){
                    submitData()
                } else {
                    binding.loginBtnLogin.visibility = View.VISIBLE
                    binding.loginProgressIndicatorLoading.visibility = View.GONE
                    invalidForm()
                }
            }, 1000)
        }
    }

    private fun submitData(){
        val rememberSwitch = binding.loginSwitchRemember
        val savedEmail = binding.loginInputEmail.text.toString()
        val savedPassword = binding.loginInputPassword.text.toString()

        val welcomeIntent = Intent(this, WelcomeActivity::class.java)
        if(rememberSwitch.isChecked){
            Log.d("SWITCHING", savedEmail+savedPassword)
            startActivity(welcomeIntent)
        }
        startActivity(welcomeIntent)
    }

    private fun invalidForm(){
        var message = "ESTE USUARIO NO ESTÁ REGISTRADO"

        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Okey"){_,_ -> }.show()
    }

    data class User(
        val username: String,
        val email: String,
        val password: String,
    )

    val mockUsers = listOf(
        User("Pepe","pepegzlez@example.es","a123456B"),
        User("shadowdarkness09","edgyemail@example.es","darks234")
    )

    private fun validEmail(): String?{
        val emailText = binding.loginInputEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Email no válido"
        }

        return null
    }

    private fun validPassword(): String?{
        val passwordText = binding.loginInputPassword.text.toString()
        if(passwordText.length < 6){
            return "Mínimo 6 carácteres"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex())){
            return "Debe tener al menos 1 mayúscula"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
            return "Debe tener al menos 1 minúscula"
        if(!passwordText.matches(".*[0-9].*".toRegex())){
            return "Debe tener al menos 1 número"
        }
        return null
    }

}