package com.moodi.tagsearch

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.moodi.tagsearch.util.addCallbacks

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById(R.id.search) as TagSearch

        searchView
                .apply {
                    setTagColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                    setTagText("mango")
                    setTagRadius(25.0f)
                    setSearchHint("Wow")
                }
                .addCallbacks(
                        {}, {}, {}, {}, {}
                )


    }
}
