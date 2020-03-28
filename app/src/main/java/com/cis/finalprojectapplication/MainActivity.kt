package com.cis.finalprojectapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    private  val TAG:String = "Main Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        if (mAuth!!.currentUser != null){
            startActivity(Intent(this@MainActivity,HomeActivity::class.java))
            finish()
        }

        Button_login.setOnClickListener {
            val email = email_login.text.toString().trim{it <= ' '}
            val password = password_login.text.toString().trim { it <= ' ' }

            if (email.isEmpty()){
                Toast.makeText(this,"กรุณากรอก E-mail", Toast.LENGTH_LONG).show()
                Log.d(TAG,"Email was empty!")
                return@setOnClickListener
            }
            if (password.isEmpty()){
                Toast.makeText(this,"กรุณากรอกรหัสผ่าน", Toast.LENGTH_LONG).show()
                Log.d(TAG,"Password was empty!")
                return@setOnClickListener
            }
            mAuth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    task ->
                if (!task.isSuccessful){
                    if (password.length < 6 ){
                        password_login.error = "รหัสผ่านไม่ถูกต้อง กรูณากรองรหัสผ่านใหม่อีกครั้ง"
                        Log.d(TAG,"Enter password less than 6 characters")
                    }else{
                        Toast.makeText(this,"Autentication Failed"+task.exception, Toast.LENGTH_SHORT).show()
                        Log.d(TAG,"Authentication Failed"+task.exception)
                    }
                }else{
                    Toast.makeText(this,"Sign in Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity,HomeActivity::class.java))
                    finish()
                }
            }
        }

    }
}
