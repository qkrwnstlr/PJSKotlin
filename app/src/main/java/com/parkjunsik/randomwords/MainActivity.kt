package com.parkjunsik.randomwords

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parkjunsik.randomwords.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val dictionary:HashMap<String,String> = hashMapOf<String,String>()

        with(binding){
            homeTitle.setOnClickListener {
                Toast.makeText(this@MainActivity,"$dictionary",Toast.LENGTH_SHORT).show()
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