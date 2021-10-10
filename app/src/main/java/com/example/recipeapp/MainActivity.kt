package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    var receipesList =  mutableListOf<DataItem>()
    lateinit var fab: FloatingActionButton
    lateinit var rvMain: RecyclerView
    lateinit var adapter: RecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)
        rvMain = findViewById(R.id.rvMain)
        rvMain.layoutManager = LinearLayoutManager(this@MainActivity)


        if(receipesList.size == 0){
            CoroutineScope(IO).launch {
                fetchData()
            }
        }
        fab.setOnClickListener {
            startActivity(Intent(this, AddRecipe::class.java))
        }
    }

    fun updateData(newList: MutableList<DataItem>){
        val diffUtil = DiffUtilClass(receipesList, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        receipesList = newList
        result.dispatchUpdatesTo(adapter)
    }



    fun fetchData(){
        try {
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dojo-recipes.herokuapp.com/")
                .build().create(ApiInterface::class.java)

            val retrofitData = retrofitBuilder.getData()
            retrofitData.enqueue(object : Callback<List<DataItem>?> {
                override fun onResponse(
                        call: Call<List<DataItem>?>,
                        response: Response<List<DataItem>?>
                ) {
                    for (d in response.body()!!)
                        receipesList.add(DataItem(d.author, d.ingredients, d.instructions, d.title))
                    adapter = RecyclerViewAdapter(receipesList)
                    rvMain.adapter = adapter

                }

                override fun onFailure(call: Call<List<DataItem>?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: $t", Toast.LENGTH_LONG).show()
                }
            })
        }catch (e: Exception){
            Toast.makeText(this, "Error: $e", Toast.LENGTH_LONG).show()
        }
    }
}