package com.example.app5.ui.gallery

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app5.databinding.FragmentGalleryBinding
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var city = "Saratov, RU"
    val api = "931d8405f4374a85cb7076256d65fba7"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        weatherTask().execute()
        binding.search.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Выбор города")
            val et = EditText(context)
            builder.setView(et)
            builder.setPositiveButton("Ок"){dialog, i->
                city = et.text.toString()
                weatherTask().execute()
            }
            builder.setNegativeButton("Отмена"){dialog, i->

            }
            builder.show()
        }
        return root
    }
    inner class weatherTask(): AsyncTask<String, Void, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            binding.layout.visibility = View.GONE

        }
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$api")
                    .readText(Charsets.UTF_8)
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Обновлено: " + SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.ENGLISH).format(
                    Date(updatedAt*1000))
                val temp = main.getString("temp")+"ºC"
                val tempMin = "Минимальная: " + main.getString("temp_min")+"ºC"
                val tempMax = "Максимальная: " + main.getString("temp_max")+"ºC"
                val pressure  = main.getString("pressure")
                val humidity  = main.getString("humidity") + "%"
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed") + " м/с"
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+", "+sys.getString("country")

                binding.location.text = address
                binding.updatedAt.text = updatedAtText
                binding.description.text = weatherDescription.capitalize()
                binding.temp.text = temp
                binding.minTemp.text = tempMin
                binding.maxTemp.text = tempMax
                binding.sunrise.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(sunrise*1000))
                binding.sunset.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(sunset*1000))
                binding.wind.text = windSpeed
                binding.pressure.text = pressure
                binding.humidity.text = humidity

                binding.layout.visibility = View.VISIBLE
            }catch (e: Exception){
                Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}