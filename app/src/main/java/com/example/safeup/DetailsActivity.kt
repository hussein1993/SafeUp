package com.example.safeup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.safeup.models.HolidayModelItem
import com.google.gson.Gson

class DetailsActivity : AppCompatActivity() {
    private lateinit var name:TextView
    private lateinit var localname:TextView
    private lateinit var date:TextView
    private lateinit var counties:TextView
    private lateinit var countryCode:TextView
    private lateinit var fixed:TextView
    private lateinit var global:TextView
    private lateinit var launchYear:TextView
    private lateinit var types:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        var json = Gson()
        val currHoliday = json.fromJson<HolidayModelItem>(intent.getStringExtra("selected_holiday"),HolidayModelItem::class.java)
        name = findViewById(R.id.name)
        localname= findViewById(R.id.local_name)
        date = findViewById(R.id.date)
        counties= findViewById(R.id.counties)
        countryCode= findViewById(R.id.countryCode)
        fixed = findViewById(R.id.fixed)
        global= findViewById(R.id.global)
        launchYear= findViewById(R.id.launchYear)
        types = findViewById(R.id.types)


        name.text = currHoliday.name
        localname.text = currHoliday.localName
        date.text = currHoliday.date
        counties.text = currHoliday.counties.toString()
        countryCode.text = currHoliday.countryCode
        fixed.text = currHoliday.fixed.toString()
        global.text = currHoliday.global.toString()
        launchYear.text = currHoliday.launchYear.toString()
        types.text = currHoliday.types.toString()
    }
}