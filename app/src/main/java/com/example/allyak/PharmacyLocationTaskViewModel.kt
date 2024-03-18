package com.example.allyak

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class PharmacyLocationTaskViewModel : ViewModel() {
    private val client = OkHttpClient()

    private val _pharmacyLocationData = MutableLiveData<List<Pharmacy>>()
    private val _pharmacyData = MutableLiveData<List<Pharmacy>>()
    val pharmacyLocationData: LiveData<List<Pharmacy>> get() = _pharmacyLocationData

    fun getPharmacyLocationData(city: String, district: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = fetchPharmacyLocationDataApi(city, district)
            Log.i("##INFO", "result = ${result}")

            val pharmacies1 = parsePharmacyData1(result)
            Log.i("##INFO", "pharmacies1 = ${pharmacies1}");
            withContext(Dispatchers.Main) {
                _pharmacyLocationData.value = pharmacies1
            }

        }
    }

    private fun parsePharmacyData1(data: String): List<Pharmacy> {
        val xmlString = data // XML 문자열
        val xmlPullParserFactory = XmlPullParserFactory.newInstance()
        val parser = xmlPullParserFactory.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(xmlString))
        parser.nextTag()

        val pharmacies = mutableListOf<Pharmacy>()
        var eventType = parser.eventType
        var currentPharmacy: Pharmacy? = null

        var dutyAddr = ""
        var dutyName = ""
        var dutyTel1 = ""
        var dutyTime1c = ""
        var dutyTime1s = ""
        var dutyTime2c = ""
        var dutyTime2s = ""
        var dutyTime3c = ""
        var dutyTime3s = ""
        var dutyTime4c = ""
        var dutyTime4s = ""
        var dutyTime5c = ""
        var dutyTime5s = ""
        var dutyTime6c = ""
        var dutyTime6s = ""
        var hpid = ""
        var postCdn1 = ""
        var postCdn2 = ""
        var rnum = ""
        var wgs84Lat = ""
        var wgs84Lon = ""


        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val name = parser.name
                    when (name) {
                        "dutyAddr" -> dutyAddr = parser.nextText()
                        "dutyName" -> dutyName = parser.nextText()
                        "dutyTel1" -> dutyTel1 = parser.nextText()
                        "dutyTime1c" -> dutyTime1c = parser.nextText()
                        "dutyTime1s" -> dutyTime1s = parser.nextText()
                        "dutyTime2c" -> dutyTime2c = parser.nextText()
                        "dutyTime2s" -> dutyTime2s = parser.nextText()
                        "dutyTime3c" -> dutyTime3c = parser.nextText()
                        "dutyTime3s" -> dutyTime3s = parser.nextText()
                        "dutyTime4c" -> dutyTime4c = parser.nextText()
                        "dutyTime4s" -> dutyTime4s = parser.nextText()
                        "dutyTime5c" -> dutyTime5c = parser.nextText()
                        "dutyTime5s" -> dutyTime5s = parser.nextText()
                        "dutyTime6c" -> dutyTime6c = parser.nextText()
                        "dutyTime6s" -> dutyTime6s = parser.nextText()
                        "hpid" -> hpid = parser.nextText()
                        "postCdn1" -> postCdn1 = parser.nextText()
                        "postCdn2" -> postCdn2 = parser.nextText()
                        "rnum" -> rnum = parser.nextText()
                        "wgs84Lat" -> wgs84Lat = parser.nextText()
                        "wgs84Lon" -> wgs84Lon = parser.nextText()
                    }

                    currentPharmacy = Pharmacy(
                        dutyAddr, dutyName, dutyTel1,
                        dutyTime1c, dutyTime1s, dutyTime2c, dutyTime2s,
                        dutyTime3c, dutyTime3s, dutyTime4c, dutyTime4s,
                        dutyTime5c, dutyTime5s, dutyTime6c, dutyTime6s,
                        hpid, postCdn1, postCdn2, rnum, wgs84Lat, wgs84Lon
                    )

                }

                XmlPullParser.END_TAG -> {
                    if (parser.name == "item") {
                        // "item" 태그가 종료되면 pharmacies 리스트에 Pharmacy 객체 추가
                        currentPharmacy?.let { pharmacies.add(it) }
                    }
                }
            }

            eventType = parser.next()
        }
        return pharmacies
    }
    private fun fetchPharmacyLocationDataApi(city: String, district: String): String {
        // API 요청을 보낼 URL 설정
        val apiUrl =
            "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire"
        val apiKey =
            "Bht6UBoql5Zuh2tvMckFU8%2FADjJBctoibZLwRZxojBgR3aOBeRyIjdD33DwP4teYhTAEIYs1fuQHt7eplCkv0w%3D%3D"
//        val apiKey = "Bht6UBoql5Zuh2tvMckFU8/ADjJBctoibZLwRZxojBgR3aOBeRyIjdD33DwP4teYhTAEIYs1fuQHt7eplCkv0w=="
        val requestUrl = "$apiUrl?serviceKey=$apiKey&Q0=$city&Q1=$district" // serviceKey 파라미터명 변경

        // OkHttp 요청 객체 생성
        val request = Request.Builder()
            .url(requestUrl)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string() ?: "Empty Response"
                } else {
                    Log.e("##ERROR", "Error: ${response.code} ${response.message}");
                    "Error: ${response.code} ${response.message}"
                }
            }
        } catch (e: Exception) {
            Log.e("##ERROR", ": error = ${e}");
            "An error occurred: ${e.message}"
        }

    }
}