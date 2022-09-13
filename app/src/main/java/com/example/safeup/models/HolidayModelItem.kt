package com.example.safeup.models

data class HolidayModelItem (
    var counties: Any?,
    var countryCode: String?,
    var date: String?,
    var fixed: Boolean?,
    var global: Boolean?,
    var launchYear: Int?,
    var localName: String?,
    var name: String?,
    var types: List<String>?


) {
    override fun equals(other: Any?): Boolean {
        if (other is HolidayModelItem ){
            return name.equals(other.name) && localName.equals(other.localName)
        }else return false
    }

}