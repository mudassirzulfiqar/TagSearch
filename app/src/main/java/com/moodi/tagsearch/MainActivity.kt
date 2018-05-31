package com.moodi.tagsearch

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.moodi.tagsearch.util.addCallbacks
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById(R.id.search) as TagSearch

        val tag = listOf("Sports", "Education", "Debate", "Earth Quack")

        val hotTags = ArrayList<String>()
        hotTags.addAll(tag)

        findViewById(R.id.add_tag).setOnClickListener {
            searchView.setTagText("Android tutorials")
        }

        searchView
                .apply {
                    setTagColor(
                            ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                    setTagText("mango")
                    setTagRadius(10.0f)
                    setSearchBarRadius(10.0f)
                    setSearchHint("Wow")
                    addTags(hotTags)
                    setTagTitle("Hot")
                }
                .addCallbacks(
                        {}, {}, {}, {}, {}
                )


    }
}
