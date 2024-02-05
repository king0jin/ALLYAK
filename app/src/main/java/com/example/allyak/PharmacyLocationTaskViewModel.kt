package com.example.allyak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

private val client = OkHttpClient()
class PharmacyLocationTaskViewModel : ViewModel() {

    private val _pharmacyLocationData = MutableLiveData<List<Pharmacy>>()
    val pharmacyLocationData: LiveData<List<Pharmacy>> get() = _pharmacyLocationData

    fun getPharmacyLocationData(city: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = fetchPharmacyLocationDataApi(city)
                val pharmacies = parsePharmacyData(result) // 파싱된 데이터를 가져옴
                // LatLng 리스트를 Pharmacy 리스트로 변환
                val pharmacyList = pharmacies.map { pharmacyLatLng ->
                    Pharmacy(latitude = pharmacyLatLng.latitude, longitude = pharmacyLatLng.longitude)
                }
                withContext(Dispatchers.Main) {
                    _pharmacyLocationData.value = pharmacyList
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }
    private suspend fun parsePharmacyData(data: String): List<Pharmacy> {
        val pharmacies = mutableListOf<Pharmacy>()

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(StringReader(data))

            var eventType = parser.eventType
            var pharmacy: Pharmacy? = null

//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                when (eventType) {
//                    XmlPullParser.START_TAG -> {
//                        when (parser.name) {
//                            "item" -> {
//                                pharmacy = Pharmacy()
//                            }
//                            "latitude" -> {
//                                pharmacy?.latitude = parser.nextText().toDouble()
//                            }
//                            "longitude" -> {
//                                pharmacy?.longitude = parser.nextText().toDouble()
//                            }
//                            // Add other fields as needed
//                        }
//                    }
//                    XmlPullParser.END_TAG -> {
//                        if (parser.name == "item") {
//                            pharmacy?.let { pharmacies.add(it) }
//                        }
//                    }
//                }
//                eventType = parser.next()
//            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pharmacies
    }
    private suspend fun fetchPharmacyLocationDataApi(city: String): String {
        // API 요청을 보낼 URL 설정
        val apiUrl = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService"
        val apiKey = "Bht6UBoql5Zuh2tvMckFU8%2FADjJBctoibZLwRZxojBgR3aOBeRyIjdD33DwP4teYhTAEIYs1fuQHt7eplCkv0w%3D%3D"
        val requestUrl = "$apiUrl?api_key=$apiKey&city=$city"

        // OkHttp 요청 객체 생성
        val request = Request.Builder()
            .url(requestUrl)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string() ?: "Empty Response"
                } else {
                    "Error: ${response.code} ${response.message}"
                }
            }
        } catch (e : Exception) {
            "An error occurred: ${e.message}"
        }
    }
}