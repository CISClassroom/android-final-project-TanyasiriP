package com.cis.finalprojectapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_instru.*

class AddInstruActivity : AppCompatActivity() {



    private  val TAG:String = "Add Instru Activity"

    lateinit var instr_name: EditText
    lateinit var instr_detail: EditText
    lateinit var  instr_save: Button
    lateinit var instr_type: Spinner
    lateinit var imguri: Uri

    lateinit var select_photo_button: Button //
    lateinit var select_photo_imageviwe: ImageView //
    lateinit var mStorageRef: StorageReference
    private var uploadTask: StorageTask<*>? = null

    private val PERMISSION_CODE = 1000;

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
        select_photo_button.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    FileChoose()
                }
            }
            else{
                //system os is < marshmallow
                FileChoose()
            }
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
    private fun getExtension(uri: Uri): String? {
        var cr = contentResolver
        var mimetypemap = MimeTypeMap.getSingleton()
        return mimetypemap.getExtensionFromMimeType(cr.getType(uri))
    }
    private fun FileUploader(){

        var ref = mStorageRef.child(""+System.currentTimeMillis()+"."+getExtension(imguri))
        ref.putFile(imguri)
            .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // Get a URL to the uploaded content
                Toast.makeText(this,"Image success",Toast.LENGTH_LONG).show()
            })
            .addOnFailureListener(OnFailureListener {
            })
    }
    private fun FileChoose(){

        var intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent,1)
    }


    private fun saveInstrument(){
        val instrumentName = instru_name.text.toString().trim()
        if (instrumentName.isEmpty()){
            Toast.makeText(this,"กรุณาใส่ชื่อเพลง", Toast.LENGTH_SHORT).show()
            return
        }

        val instrumentDetail = instru_detail.text.toString()
        if (instrumentDetail.isEmpty()){
            Toast.makeText(this,"กรุณาใส่เนื้อเพลง", Toast.LENGTH_SHORT).show()
            return
        }

        val instrumentType =  valueofspinner.toString()
        if (instrumentType.isEmpty()){
            Toast.makeText(this,"กรุณาเลือกประเภทของเพลง",Toast.LENGTH_SHORT).show()
            return
        }
        val ref = FirebaseDatabase.getInstance().getReference("instruments")

        val instrumentId = ref.push().key

        val select_instrument = instrumentId?.let { Instrument(it,instrumentName, instrumentDetail,instrumentType)}

        if (instrumentId != null) {
            ref.child(instrumentId).setValue(select_instrument).addOnCompleteListener {
                Toast.makeText(applicationContext,"บันทึกเนื้อเพลงแล้ว", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){

            imguri = data.getData()!!
            select_photo_imageviwe.setImageURI(imguri)
        }


    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    FileChoose()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
