package com.parkjunsik.randomwords

import android.content.Context
import androidx.room.*

@Entity(tableName="tb_database")
data class Word(
    @ColumnInfo(name="word") var word:String,
    @ColumnInfo(name="meaning") var meaning:String,
    @ColumnInfo(name="date") var date:Int
){
    @PrimaryKey(autoGenerate = true)var id:Int=0
}

@Dao
interface DatabaseDao{
    @Query("SELECT * FROM tb_database")
    fun loadAllWords():List<Word>
    @Query("SELECT * FROM tb_database WHERE date == :today")
    fun loadTodayWords(today:Int):List<Word>

    @Query("Delete From tb_database Where word == :word")
    fun deleteWord(word: String)

    @Insert
    fun insert(word:Word)
}

@Database(entities = [Word::class], version = 1)
abstract class WordDatabase : RoomDatabase(){
    abstract fun DatabaseDao():DatabaseDao
    companion object{
        private var instance: WordDatabase? = null

        @Synchronized
        fun getInstance(context: Context):WordDatabase?{
            if(instance==null)
                synchronized(WordDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordDatabase::class.java,
                        "word-database"
                    ).build()
                }
            return instance
        }
    }
}