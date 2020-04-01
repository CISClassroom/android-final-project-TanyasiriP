package com.cis.finalprojectapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_show_all.*

class ShowAllActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    private val TAG:String = "Show Activity"
    lateinit var arrayAdapter: ArrayAdapter<*>
    var toDoItemList: MutableList<ToDo>? = null
    lateinit var adapter: InstrumentAdapter.ToDoItemAdapter
    private var listViewItems: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_all)

        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)

        listViewItems = findViewById<View>(R.id.show_all_listview) as ListView

        toDoItemList = mutableListOf()
        adapter = InstrumentAdapter.ToDoItemAdapter(this, toDoItemList!!)
        listViewItems!!.setAdapter(adapter)


        show_all_listview.setOnItemClickListener{ parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            val intent =  Intent(this@ShowAllActivity,ShowDetailActivity::class.java)
            val selectedItem = parent?.getItemAtPosition(position) as ToDo
            intent.putExtra("name",selectedItem.name)
            intent.putExtra("id",selectedItem.object_id)
            startActivity(intent)
        }

    }
    var itemListener: ValueEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // call function
            addDataToList(dataSnapshot.child("instruments"))
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, display log a message
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
        adapter.notifyDataSetChanged()
    }
}
