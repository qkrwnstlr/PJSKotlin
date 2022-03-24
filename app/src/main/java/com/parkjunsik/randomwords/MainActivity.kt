package com.parkjunsik.randomwords

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import com.parkjunsik.randomwords.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val dictionary:HashMap<String,String> = hashMapOf()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "wordsDB"
        ).build()
        val dbDao = db.DatabaseDao()
        val words:List<Word> = dbDao.loadAllWords()

        with(binding){
            homeTitle.setOnClickListener {
                Toast.makeText(this@MainActivity,"$dictionary",Toast.LENGTH_SHORT).show()
            }
            Exam.setOnClickListener {
                val dialog = ExamDialog(this@MainActivity)
                dialog.startDialog(dictionary)
            }
            addWord.setOnClickListener {
                val dialog = AddDialog(this@MainActivity)
                dialog.startDialog()
                dialog.setOnClickedListener(object:AddDialog.ButtonClickListener{
                    override fun onClicked(newWord: String,newMeaning: String){
                        dictionary[newWord]=newMeaning
                    }
                })
            }
        }
    }
}

class ExamDialog(context: Context){
    private val dialog = Dialog(context)
    fun startDialog(dictionary:HashMap<String,String>){
        dialog.setContentView(R.layout.dialog_exam)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        val qWord = dialog.findViewById<TextView>(R.id.questionWord)
        val qMeaning = dialog.findViewById<EditText>(R.id.questionMeaning)
        val buttonCheck = dialog.findViewById<Button>(R.id.Button_Check)
        val test = dictionary.keys.toHashSet().toMutableList().shuffled().toList()
        var idx = 0
        qWord.text = test[idx++]
        buttonCheck.setOnClickListener {
            if(dictionary[qWord.text.toString()] == qMeaning.text.toString())
                qWord.text = test[idx++]
        }
        dialog.show()
    }
}

class AddDialog(context: Context){  
    private val dialog = Dialog(context)
    interface ButtonClickListener{
        fun onClicked(newWord:String, newMeaning:String)
    }
    private lateinit var onClickedListener:ButtonClickListener
    fun setOnClickedListener(listener:ButtonClickListener) {
        onClickedListener=listener
    }
    fun startDialog(){
        dialog.setContentView(R.layout.dialog_add)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        val newWord = dialog.findViewById<EditText>(R.id.newWord)
        val newMeaning = dialog.findViewById<EditText>(R.id.newMeaning)
        val buttonAdd = dialog.findViewById<Button>(R.id.Button_addWord)
        buttonAdd.setOnClickListener {
            onClickedListener.onClicked(newWord.text.toString(),newMeaning.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }
}