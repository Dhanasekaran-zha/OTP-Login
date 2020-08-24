package com.example.otpsample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth?=null
    var mCallback: OnVerificationStateChangedCallbacks?= null
    var verificationCode: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StartOTP()
        generatebtn.setOnClickListener {
            val phoneNumber=phoneEdt.text.toString()
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60,java.util.concurrent.TimeUnit.SECONDS,this,mCallback!!)}

        signinbtn.setOnClickListener {
            val otp=otpedt.otp.toString()
            val credential = PhoneAuthProvider.getCredential(verificationCode!!,otp)
            SignInWithPhone(credential)

        }
    }

    private fun SignInWithPhone(credential: PhoneAuthCredential) {
        auth!!.signInWithCredential(credential).addOnCompleteListener(object :OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
                if (p0.isSuccessful){
                    startActivity(Intent(this@MainActivity,MainActivity2::class.java))
                    finish()
                }else{
                    Toast.makeText(this@MainActivity,"Incorrect OTP",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun StartOTP() {
        auth= FirebaseAuth.getInstance()
        auth!!.useAppLanguage()
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(this@MainActivity,"verification completed",Toast.LENGTH_SHORT).show();
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@MainActivity,"verification fialed",Toast.LENGTH_SHORT).show();

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationCode=p0
                Toast.makeText(this@MainActivity,"Code sent",Toast.LENGTH_SHORT).show();

            }
        }


    }
}