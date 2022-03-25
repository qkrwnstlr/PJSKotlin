package com.parkjunsik.randomwords

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import com.parkjunsik.randomwords.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var db: WordDatabase
    lateinit var data:List<Word>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = WordDatabase.getInstance(applicationContext)!!
        refreshDb()

        with(binding){
            homeTitle.setOnClickListener {
                Toast.makeText(this@MainActivity,"$data",Toast.LENGTH_SHORT).show()
            }
            Exam.setOnClickListener {
                val dialog = ExamDialog(this@MainActivity)
                refreshDb()
                dialog.startDialog(data)
            }
            addWord.setOnClickListener {
                val dialog = AddDialog(this@MainActivity)
                dialog.startDialog()
                dialog.setOnClickedListener(object:AddDialog.ButtonClickListener{
                    override fun onClicked(newWord: String,newMeaning: String){
                        insert(Word(newWord,newMeaning,Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))
                    }
                })
            }
        }
    }
    private fun refreshDb(){
        CoroutineScope(Dispatchers.Main).launch {
            data = CoroutineScope(Dispatchers.IO).async {
                db.DatabaseDao().loadAllWords()
            }.await()
        }
    }
    private fun insert(word: Word){
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                db.DatabaseDao().insert(word)
            }.await()
        }
    }
}

class ExamDialog(context: Context){
    private val dialog = Dialog(context)
    fun startDialog(dictionary:List<Word>){
        dialog.setContentView(R.layout.dialog_exam)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        val qWord = dialog.findViewById<TextView>(R.id.questionWord)
        val qMeaning = dialog.findViewById<EditText>(R.id.questionMeaning)
        val buttonCheck = dialog.findViewById<Button>(R.id.Button_Check)
        val test = dictionary.shuffled()
        var idx = 0
        qWord.text = test[idx].word
        buttonCheck.setOnClickListener {
            if(idx + 1 >= test.size) Log.d("clear","clear")
            else if(test[idx].meaning == qMeaning.text.toString()) {
                qWord.text = test[++idx].word
                qMeaning.text = null
            }
            else Log.d("false","false")
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