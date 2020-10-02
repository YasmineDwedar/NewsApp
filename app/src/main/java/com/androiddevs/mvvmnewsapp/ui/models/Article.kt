package com.androiddevs.mvvmnewsapp.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//tell android studio its a table in out database
@Entity(tableName = "articles")

data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
):Serializable