package com.developerstring.financemanagementapp.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.developerstring.financemanagementapp.R
import com.developerstring.financemanagementapp.databinding.ActivityCreateAccountScreenBinding
import com.developerstring.financemanagementapp.databinding.ActivitySignInEmailBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CreateAccountScreen : AppCompatActivity() {

    // set binding
    private lateinit var binding: ActivityCreateAccountScreenBinding
    // firebase Auth
    private lateinit var mAuth: FirebaseAuth
    // firebase User
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set binding to Activity
        binding = ActivityCreateAccountScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get firebase instance
        mAuth = FirebaseAuth.getInstance()

        // get the current user
        user = mAuth.currentUser!!

        // password edittext
        binding.passwordCreateAccountScreen.doOnTextChanged{text, start, before, count ->
            // set error text invisible
            binding.passConfirmPassNotMatchingText.visibility = View.INVISIBLE
            // set normal background of password edit text
            binding.passwordCreateAccountScreen.setBackgroundResource(R.drawable.sign_in_edit_text)
            // set normal lock icon of password edit text
            binding.passwordCreateAccountScreen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sign_in_edit_text_password_icon,0,0,0)

        }

        // confirm password edittext
        binding.passwordCreateAccountScreen.doOnTextChanged{text, start, before, count ->
            // set error text invisible
            binding.passConfirmPassNotMatchingText.visibility = View.INVISIBLE
            // set normal background of password edit text
            binding.confirmPasswordCreateAccountScreen.setBackgroundResource(R.drawable.sign_in_edit_text)
            // set normal lock icon of password edit text
            binding.confirmPasswordCreateAccountScreen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sign_in_edit_text_password_icon,0,0,0)

        }

        // show password checkbox
        binding.showPasswordCheckboxCreateAccountScreen.setOnClickListener {
            // show password on checkbox checked
            if (binding.showPasswordCheckboxCreateAccountScreen.isChecked){
                binding.passwordCreateAccountScreen.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.confirmPasswordCreateAccountScreen.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

            // hide password if checkbox not checked
            if (!binding.showPasswordCheckboxCreateAccountScreen.isChecked){
                binding.passwordCreateAccountScreen.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.confirmPasswordCreateAccountScreen.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // onclick create account button
        binding.createAccountBtnEmail.setOnClickListener {
            val email: String = binding.emailCreateAccountScreen.text.toString()
            val password: String = binding.passwordCreateAccountScreen.text.toString()
            val confirmPassword: String = binding.confirmPasswordCreateAccountScreen.text.toString()

            // check if password and confirm password are same
            if (password == confirmPassword){
                // make progressbar visible
                binding.progressBarCreateAccountScreen.visibility = View.VISIBLE
                // create account in Firebase
                mAuth.createUserWithEmailAndPassword(email,confirmPassword)
                    .addOnCompleteListener(
                    OnCompleteListener<AuthResult> {task ->
                        // check if the account is create
                        if (task.isSuccessful){
                            // make progressbar invisible
                            binding.progressBarCreateAccountScreen.visibility = View.INVISIBLE
                            // show message account is created
                            Toast.makeText(this,"Account Created Successful...",Toast.LENGTH_SHORT).show()
                            // delay of sometime after creating account
                            Handler().postDelayed({
                                val createProfileIntent = Intent(this@CreateAccountScreen,MainActivity::class.java)
                                startActivity(createProfileIntent)
                                finish()
                            },2000)
                        } else {
                            // get the error while creating the account
                            val e = task.exception.toString()
                            // show the error to the user
                            Toast.makeText(this,e,Toast.LENGTH_SHORT).show()
                            binding.emailCreateAccountScreen.setText(task.toString())
                            // make progressbar invisible
                            binding.progressBarCreateAccountScreen.visibility = View.INVISIBLE
                        }
                    }
                )

            }
            else{
                // set error text visible
                binding.passConfirmPassNotMatchingText.visibility = View.VISIBLE
                // set error background of password edit text
                binding.passwordCreateAccountScreen.setBackgroundResource(R.drawable.sign_in_edit_text_error)
                // set error lock icon of password edit text
                binding.passwordCreateAccountScreen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_error,0,0,0)

                // set error background of confirm password edit text
                binding.confirmPasswordCreateAccountScreen.setBackgroundResource(R.drawable.sign_in_edit_text_error)
                // set error lock icon of confirm password edit text
                binding.confirmPasswordCreateAccountScreen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_error,0,0,0)
            }
        }
    }
}