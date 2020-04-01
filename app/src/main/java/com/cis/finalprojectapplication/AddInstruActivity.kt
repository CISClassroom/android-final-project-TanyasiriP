package com.cis.finalprojectapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import kotlinx.android.synthetic.main.activity_add_instru.*

class AddInstruActivity : AppCompatActivity() {



    private  val TAG:String = "Add Instru Activity"

    lateinit var instr_name: EditText
    lateinit var instr_detail: EditText
    lateinit var  instr_save: Button
    lateinit var instr_type: Spinner

    lateinit var select_photo_button: Button //
    lateinit var select_photo_imageviwe: ImageView //
    var mStorageRef: StorageReference? = null
    private var uploadTask: StorageTask<*>? = null
    var imguri: Uri? = null

    var valueofspinner = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_instru)

        mStorageRef = FirebaseStorage.getInstance().getReference("Images")
        select_photo_button = findViewById<View>(R.id.select_photo_button) as Button //
        select_photo_imageviwe = findViewById<View>(R.id.select_photo_imageviwe) as ImageView //

        instr_name = findViewById(R.id.instru_name) as EditText
        instr_detail = findViewById(R.id.instru_detail) as EditText
        instr_save = findViewById(R.id.save__instru_button) as Button
        instr_type = findViewById(R.id.type_instru_spinner) as Spinner

        select_photo_button = findViewById(R.id.select_photo_button) as Button //
        select_photo_imageviwe = findViewById(R.id.select_photo_imageviwe) as ImageView //

        val t = resources.getStringArray(R.array.type)
        val adapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,t)
        instr_type.adapter = adapter

        instr_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                valueofspinner = adapterView.getItemAtPosition(i).toString()
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        save__instru_button.setOnClickListener {
            saveInstrument()

            val intent = Intent(this,HomeActivity::class.java)
            intent.putExtra("instr_name",""+ instru_name.text.toString())
            intent.putExtra("instr_detail",""+instru_detail.text.toString())

            if(instru_name.text.isNotEmpty() && instru_detail.text.isNotEmpty()){
                startActivity(intent)
            }
        }
    }
    private fun saveInstrument(){
        val instrumentName = instru_name.text.toString().trim()
        if (instrumentName.isEmpty()){
            Toast.makeText(this,"กรุณาใส่ชื่อเครื่องดนตรี", Toast.LENGTH_SHORT).show()
            return
        }

        val instrumentDetail = instru_detail.text.toString()
        if (instrumentDetail.isEmpty()){
            Toast.makeText(this,"กรุณาใส่คำอธิบายเครื่องดนตรี", Toast.LENGTH_SHORT).show()
            return
        }

        val instrumentType =  valueofspinner.toString()
        if (instrumentType.isEmpty()){
            Toast.makeText(this,"กรุณาเลือกด้านกิจกรรม",Toast.LENGTH_SHORT).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("instruments")

        val instrumentId = ref.push().key

        val select_instrument = instrumentId?.let { Instrument(it,instrumentName, instrumentDetail,instrumentType)}

        if (instrumentId != null) {
            ref.child(instrumentId).setValue(select_instrument).addOnCompleteListener {
                Toast.makeText(applicationContext,"บันทึกเครื่องดนตรีแล้ว", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imguri = data.data
            select_photo_imageviwe!!.setImageURI(imguri)
        }
    }

}
