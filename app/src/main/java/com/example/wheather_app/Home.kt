package com.example.wheather_app


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.wheather_app.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Home : AppCompatActivity() {
    val Url="https://api.openweathermap.org/data/2.5/"
//    lateinit var textview_temp:TextView

    private lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor= Color.TRANSPARENT



//        textview_temp=findViewById(R.id.temp)

        fetchwheatherdata("Lucknow")
        searchCity()
    }

    private fun searchCity() {
         val searchView =binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchwheatherdata(query)
                }
                return  true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                 return true
            }

        })
    }

    private fun fetchwheatherdata(cityName:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Url)
            .build().create(ApiInterface::class.java)

        val response =
            retrofit.getWheatherData(cityName, "efadb41ff1be60ac03217ccbd7f590cd", "metric")

        response.enqueue(object : Callback<wheatherapp> {
            override fun onResponse(call: Call<wheatherapp>, response: Response<wheatherapp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity.toString()
                    val windspeed = responseBody.wind.speed.toString()
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val sealevel = responseBody.main.pressure.toString()
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val minTEMP = responseBody.main.temp_min
                    val maxTEMP = responseBody.main.temp_max
                    val conditionvalacard = responseBody.main.humidity



                    binding.temp.text = "$temperature\u2103"
                    binding.whetherCondition.text = "$condition"
                    binding.MAXTemp.text = "MAX TEMP:$maxTEMP"
                    binding.MINTemp.text = "MIN TEMP:$minTEMP"
                    binding.Humidity.text = "$humidity %"
                    binding.WindSpeed.text = "$windspeed M/S"
                    binding.sea.text = "$sealevel hpa"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text = "${time(sunset)}"
                     binding.Humidity.text="$conditionvalacard"
                    binding.day.text =dayName(System.currentTimeMillis())
                    binding.date.text =date()
                        binding.city.text = "$cityName"

                    changeImages(condition)


                }
            }

            override fun onFailure(call: Call<wheatherapp>, t: Throwable) {

            }
        })

    }

    private fun changeImages(conditions:String) {
        when(conditions){
            "Clear Sky" , "Sunny" , "Clear" ->
            {
                    binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
        }
            "Partly Clouds" , "Clouds" , "Overcast" , "Mist" , "Foggy" ->
            {
                    binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
        }
            "Light Rain" , "Drizzle" , "Moderate Rain" , "Showers" , "Heavy Rain" ->
            {
                    binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
        }
            "Light Snow" , "Moderate Snow" , "Heavy Snow" , "Blizzard" ->
            {
                    binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
        }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
        }

        }

        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String? {
        val sdf=SimpleDateFormat("dd MMM yyy ", Locale.getDefault())
        return sdf.format((Date()))

    }
    private fun time(timeStamp:Long): String? {
        val sdf=SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timeStamp*1000)))

    }


    fun dayName(timeStamp:Long):String{
            val sdf=SimpleDateFormat("EEEE", Locale.getDefault())
            return sdf.format((Date()))
        }
     }
