package com.androiddevs.mvvmnewsapp.ui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.ui.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase :RoomDatabase(){
abstract fun getArticlesDao():ArticleDAO
  companion object{
      //other threads can see if the instance changed
      @Volatile
      private var  instance:ArticleDatabase? = null
      private val  LOCK = Any()


     /* Elvis operator ?:
     if instance is null do el b3d el operator
        fun invoke is called like constructor keda
    */

      operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
          instance ?: createDatabase(context).also{ instance = it}
      }

      private fun createDatabase(context: Context) =
          Room.databaseBuilder(
             context.applicationContext,
              ArticleDatabase::class.java,
              "article_db.db"
          ).build()


  }
}