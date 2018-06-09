package com.lelloman.read

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.lelloman.read.model.Article
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArticlesAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        for(i in 0 until 100){
            adapter.add(Article(
                    id = i.toLong(),
                    title = "I am the article number $i",
                    sourceId = i.toLong(),
                    sourceName = "source $i",
                    subtitle = "bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla",
                    time = Random().nextLong()
            ))
        }
    }
}
