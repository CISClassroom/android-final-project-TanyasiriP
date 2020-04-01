package com.cis.finalprojectapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val TAG:String = "Home Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser

        user_email.text = user!!.email

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val users = firebaseAuth.currentUser
            if (users == null) {
                startActivity(Intent(this@HomeActivity,MainActivity::class.java))
                finish()
            }
        }

        return_button.setOnClickListener{
            //startActivity(Intent(this@HomeActivity,ReturnActivity::class.java))
            Toast.makeText(this,"เครื่องดนตรีที่คืนแล้ว", Toast.LENGTH_SHORT).show()
        }

        no_return_button.setOnClickListener{
            //startActivity(Intent(this@HomeActivity,AddToRentActivity::class.java))
            Toast.makeText(this,"เครื่องดนตรีที่ยังไม่คืน",Toast.LENGTH_SHORT).show()
        }

        show_button.setOnClickListener{
            startActivity(Intent(this@HomeActivity,ShowAllActivity::class.java))
            Toast.makeText(this,"เครื่องดนตรีทั้งหมด",Toast.LENGTH_SHORT).show()
        }

        add_button.setOnClickListener{
            startActivity(Intent(this@HomeActivity,AddInstruActivity::class.java))
            Toast.makeText(this,"เพิ่มเครื่องดนตรี",Toast.LENGTH_SHORT).show()
        }

        signout_button.setOnClickListener {
            mAuth!!.signOut()
            Toast.makeText(this,"ออกจากระบบ", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@HomeActivity,MainActivity::class.java))
            finish()
        }
    }
}
