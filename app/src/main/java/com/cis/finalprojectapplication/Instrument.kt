package com.cis.finalprojectapplication

class Instrument(val instrumentId:String,val instrumentName:String,val instrumentDetail: String,val instrumentType:String) {}


//class Students(val student_id:String,val name_student:String){}
//class StudentEvent(val event_name: String,val student_name: ArrayList<String>)

class ToDo {
    companion object Factory {
        fun create(): ToDo = ToDo()
    }

    var name: String? = null
    var object_id : String? = null

}