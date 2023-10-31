package com.masar.masatapp1

import com.masar.masatapp1.Data.DriverInfo
import com.masar.masatapp1.Server.CompanyServer
import com.masar.masatapp1.Server.DriverServer
import com.masar.masatapp1.Server.StudentServer

object server {
    var student = StudentServer()
    var company = CompanyServer()
    var driverInfo = DriverInfo()
    var dtiver = DriverServer()
    var namyCompany = ""
    var cityCompany = ""
    var stateBook=0

}