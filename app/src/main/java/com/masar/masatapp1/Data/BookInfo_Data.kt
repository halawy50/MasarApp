package com.masar.masatapp1.Data

data class BookInfo_Data (
    var idBook :String ="",
    var nameDriver :String= "",
    var idDriver :String= "",
    var name_Person :String="",
    var typeCar :String= "",
    var idPerson: String ="",
    var city :String= "",
    var round :String= "", //المدرية
    var phoneNumber :String= "",
    var go_back :String= "",
    var subscribe :String= "",
    var address_from :String= "",
    var address_to :String="",
    var stateBook:Int = -1,
    var counter :Int=1,
    var idComany: String = "",
    var nameComany:String="",
    var latitude : Double = 0.0,
    var longitude : Double = 0.0
  )