package com.parkjunsik.randomwords

import androidx.room.*

@Entity(tableName="tb_database")
data class Word(
    @PrimaryKey(autoGenerate = true) val id:Long,
    @ColumnInfo(name="word") var word:String,
    @ColumnInfo(name="meaning") var meaning:String,
    @ColumnInfo(name="date") var date:Int
)

@Dao
interface DatabaseDao{
    @Query("SELECT * FROM tb_database")
    fun loadAllWords():List<Word>
    @Query("SELECT * FROM tb_database WHERE date == :today")
    fun loadTodayWords(today:Int):List<Word>
    @Insert
    fun insert(vararg words:Word)
    @Delete
    fun delete(word:Word)
}

@Database(entities = [Word::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun DatabaseDao():DatabaseDao
}