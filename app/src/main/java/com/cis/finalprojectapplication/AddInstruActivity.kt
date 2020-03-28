package com.cis.finalprojectapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_instru.*

class AddInstruActivity : AppCompatActivity() {

    private  val TAG:String = "Add Instru Activity"

    lateinit var instr_name: EditText
    lateinit var instr_detail: EditText
    lateinit var  instr_save: Button
    lateinit var instr_type: Spinner

    var valueofspinner = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_instru)

        instr_name = findViewById(R.id.instru_name) as EditText
        instr_detail = findViewById(R.id.instru_detail) as EditText
        instr_save = findViewById(R.id.save__instru_button) as Button
        instr_type = findViewById(R.id.type_instru_spinner) as Spinner

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

        val ref = FirebaseDatabase.getInstance().getReference("instrument")

        val instrumentId = ref.push().key

        val events_student = instrumentId?.let { Instrument(it,instrumentName, instrumentDetail,instrumentType)}

        if (instrumentId != null) {
            ref.child(instrumentId).setValue(events_student).addOnCompleteListener {
                Toast.makeText(applicationContext,"บันทึกเครื่องดนตรีแล้ว", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
