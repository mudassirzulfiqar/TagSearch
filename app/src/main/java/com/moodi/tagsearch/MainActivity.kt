package com.moodi.tagsearch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById(R.id.search) as TagSearch


        searchView.addTag("mango")
        searchView.tagRadius = 15f


    }
}
