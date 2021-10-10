package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val edTitle = findViewById<EditText>(R.id.ed_title)
        val edAuthor = findViewById<EditText>(R.id.ed_author)
        val edIngredents = findViewById<EditText>(R.id.ed_ingredents)
        val edInstruction = findViewById<EditText>(R.id.ed_instruction)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnView = findViewById<Button>(R.id.btn_view)

        val listItem = MainActivity().receipesList!!




        btnSave.setOnClickListener {
            if (edTitle.text.isNotEmpty() && edAuthor.text.isNotEmpty() && edIngredents.text.isNotEmpty() && edInstruction.text.isNotEmpty() ){
                try {
                    val retrofitBuilder = Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl("https://dojo-recipes.herokuapp.com/")
                            .build().create(ApiInterface::class.java)

                    val retrofitData = retrofitBuilder.postData(DataItem(edAuthor.text.toString(), edIngredents.text.toString(), edInstruction.text.toString(), edTitle.text.toString()))

                    if(retrofitData != null){
                        retrofitData.enqueue(object : Callback<Data?> {
                            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                                listItem.add(DataItem(edAuthor.text.toString(), edIngredents.text.toString(), edInstruction.text.toString(), edTitle.text.toString()))
                                Toast.makeText(this@AddRecipe, "Added successfully", Toast.LENGTH_LONG).show()
                            }

                            override fun onFailure(call: Call<Data?>, t: Throwable) {
                                Toast.makeText(this@AddRecipe, "Error: $t", Toast.LENGTH_LONG).show()
                            }
                        })
                    }

                }catch (e: Exception){

                }
            }
        }

        btnView.setOnClickListener {
            MainActivity().updateData(listItem)
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
}