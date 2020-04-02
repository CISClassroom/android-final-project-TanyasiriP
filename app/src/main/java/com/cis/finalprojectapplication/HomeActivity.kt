package com.cis.finalprojectapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        show_button.setOnClickListener{
            startActivity(Intent(this@HomeActivity,ShowAllActivity::class.java))
            Toast.makeText(this,"เนื้อเพลงทั้งหมด",Toast.LENGTH_SHORT).show()
        }

        add_button.setOnClickListener{
            startActivity(Intent(this@HomeActivity,AddInstruActivity::class.java))
            Toast.makeText(this,"เพิ่มเนื้อเพลงใหม่",Toast.LENGTH_SHORT).show()
        }

        signout_button.setOnClickListener {
            mAuth!!.signOut()
            Toast.makeText(this,"ออกจากระบบ", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@HomeActivity,MainActivity::class.java))
            finish()
        }
    }
}
