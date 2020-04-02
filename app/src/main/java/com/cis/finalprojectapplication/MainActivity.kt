package com.cis.finalprojectapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
                return@setOnClickListener
            }
            if (password.isEmpty()){
                Toast.makeText(this,"กรุณากรอกรหัสผ่าน", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            mAuth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    task ->
                if (!task.isSuccessful){
                    if (password.length < 6 ){
                        password_login.error = "รหัสผ่านน้อยกว่า 6 ตัวอักษร กรุณากรองรหัสผ่านใหม่อีกครั้ง"
                    }else{
                        Toast.makeText(this,"Autentication Failed"+task.exception, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity,HomeActivity::class.java))
                    finish()
                }
            }
        }

    }
}
