package com.cis.finalprojectapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_to_rent.*

class AddToRentActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    var toDoItemList: MutableList<ToDo>? = null
    private val instrument = ArrayList<String>()
    private val resInstrument = ArrayList<String>()
    private var value01 = ""
    private lateinit var mInstrumentReturnAdapter: InstrumentReturnAdapter
    private lateinit var mStudentListAdapter: InstrumentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_rent)

        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)
        toDoItemList = mutableListOf()
        onClick()
    }

    private var itemListener: ValueEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            addDataToList(dataSnapshot.child("instruments"))
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        // Check if current database contains any collection
        // check if the collection has any to do items or not
        while (items.hasNext()) {
            // get current item
            val todo = ToDo.create()
            val currentItem = items.next()
            val map = currentItem.getValue() as HashMap<String, Any>
            // add data to object
            todo.name = map["instrumentName"] as String
            todo.object_id = map["instrumentId"] as String


            toDoItemList!!.add(todo)
        }

        //เพิ่มเข้า spinner


        for (t in toDoItemList!!) {
            instrument.add(t.name!!)
        }
        getValueSpinnerEvent()


    }

    private fun getValueSpinnerEvent() {
        val count = ArrayList<String>()
        val adapters = ArrayAdapter(this, android.R.layout.simple_spinner_item, instrument)
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        select_instrument_spinner?.adapter = adapters

        select_instrument_spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {

                if (i != -1) {
                    resInstrument.add(adapterView.getItemAtPosition(i).toString())

                    mStudentListAdapter = InstrumentListAdapter(applicationContext, resInstrument)
                    selected_instrument.apply {
                        layoutManager = LinearLayoutManager(applicationContext)
                        adapter = mStudentListAdapter
                        mStudentListAdapter.notifyDataSetChanged()

                    }
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

    }
    private fun onClick() {

        val ref = FirebaseDatabase.getInstance().getReference("selected instrument")
        val instruId = ref.push().key
        var selectInstru: SelectInstrument

        save_rent_button.setOnClickListener {
            for (t in resInstrument) {
                selectInstru = instruId?.let { SelectInstrument(value01, resInstrument) }!!

                ref.child(instruId).setValue(selectInstru).addOnCompleteListener {
                    Toast.makeText(applicationContext, "บันทึกการยืมแล้ว", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            finish()
        }
    }
}
