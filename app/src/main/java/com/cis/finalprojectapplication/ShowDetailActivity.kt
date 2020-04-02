package com.cis.finalprojectapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_show_detail.*

class ShowDetailActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference

    var instrument = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)
        var id = getIntent().getExtras()!!.getString("id")

        back_to_all.setOnClickListener{
            val intent =  Intent(this@ShowDetailActivity,ShowAllActivity::class.java)
            startActivity(intent)
        }
        delete_button.setOnClickListener {
            mDatabase.child("instruments").child(id!!).removeValue()
            val intent = Intent(this,ShowAllActivity::class.java)
            Toast.makeText(this,"เนื้อเพลงถูกลบออกแล้ว", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }
    var itemListener: ValueEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // call function
            addDataToList(dataSnapshot.child("instruments"))
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, display log a message

        }
    }
    fun addDataToList(dataSnapshot: DataSnapshot) {
        val id = getIntent().getExtras()!!.getString("id")
        val items = dataSnapshot.children.iterator()
        while (items.hasNext()) {
            val currentItem = items.next().getValue() as HashMap<String, Any>
            if (currentItem.get("instrumentId")==id) {
                getname.text = currentItem.get("instrumentName") as String
                get_detail.text = currentItem.get("instrumentDetail") as String
                get_type.text = currentItem.get("instrumentType") as String

                instrument = currentItem["instrumentName"] as String
            }

        }
    }
}
