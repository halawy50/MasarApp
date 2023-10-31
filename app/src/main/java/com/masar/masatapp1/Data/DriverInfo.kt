package com.masar.masatapp1.Data

data class DriverInfo (
    var idDriver : String = "",
    var name : String = "",
    var national_ID : String = "",
    var typeOfLicense : String = "",
    var typeOfVechile : String = "",
    var numberOfVechile : String = "",
    var VechileLicense : String = "",
    var numberOfSeats : Int = 0,
    var approveSeats : Int = 0,
    var LicenseNumberOfLandTransport : String = "",
    var imageDriver : String = "",
    var stateAccount:Boolean=true,
    var idCompany :String = "",
    var nameCompany : String = "",
    var email : String = "",
    var password : String = ""
    )