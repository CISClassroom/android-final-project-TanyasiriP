package com.cis.finalprojectapplication

class Instrument(val instrumentId:String,val instrumentName:String,val instrumentDetail: String,val instrumentType:String) {}


class SelectInstrument(val SelectInstrumentId:String,val SelectInstrumentName:ArrayList<String>)


class ToDo {
    companion object Factory {
        fun create(): ToDo = ToDo()
    }

    var name: String? = null
    var object_id : String? = null

}