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
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

private val client = OkHttpClient()
class PharmacyLocationTaskViewModel : ViewModel() {

    private val _pharmacyLocationData = MutableLiveData<List<PharmacyLocation>>()
    private val _pharmacyData = MutableLiveData<List<Pharmacy>>()
    val pharmacyLocationData: LiveData<List<PharmacyLocation>> get() = _pharmacyLocationData
    val pharmacyData: LiveData<List<Pharmacy>> get() = _pharmacyData

    fun getPharmacyLocationData(city: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = fetchPharmacyLocationDataApi(city)

                // 파싱된 데이터를 가져옴
                //약국 위치
                val pharmacies1 = parsePharmacyData1(result)

                //가져온 경도위도정보를 PharmacyList1로 변환
                val pharmacyList1 = pharmacies1.map { pharmacyLatLng ->
                    PharmacyLocation(wgs84Lon = pharmacyLatLng.wgs84Lon, wgs84Lat = pharmacyLatLng.wgs84Lat)
                }
                withContext(Dispatchers.Main) {
                    _pharmacyLocationData.value = pharmacyList1
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }
    private suspend fun parsePharmacyData1(data: String): List<PharmacyLocation> {
        val pharmacies1 = mutableListOf<PharmacyLocation>()
        try {
            val factory1 = XmlPullParserFactory.newInstance()
            factory1.isNamespaceAware = true
            val parser = factory1.newPullParser()
            parser.setInput(StringReader(data))

            var eventType = parser.eventType
            var wgs84Lon = 0.0
            var wgs84Lat = 0.0
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "wgs84Lon" -> {
                                wgs84Lon = parser.nextText().toDoubleOrNull() ?: 0.0
                            }

                            "wgs84Lat" -> {
                                wgs84Lat = parser.nextText().toDoubleOrNull() ?: 0.0
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (parser.name == "pharmacy") {
                            val pharmacyLocation = PharmacyLocation(wgs84Lon, wgs84Lat)
                            pharmacies1.let { pharmacies1.add(pharmacyLocation) }
                        }
                    }
                }
                eventType = parser.next()
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return pharmacies1
    }
    fun getPharmacyData(city:String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = fetchPharmacyLocationDataApi(city)

                // 파싱된 데이터를 가져옴
                //약국 정보
                val pharmacies2 = parsePharmacyData2(result)

                //가져온 약국 정보를 PharmacyList2로 변환
                val pharmacyList2 = pharmacies2.map{ pharmacyInfo ->
                    Pharmacy(
                        pharmacyInfo.dutyAddr, pharmacyInfo.dutyName, pharmacyInfo.dutyTel1,
                        pharmacyInfo.dutyTime1c, pharmacyInfo.dutyTime1s, pharmacyInfo.dutyTime2c, pharmacyInfo.dutyTime2s,
                        pharmacyInfo.dutyTime3c, pharmacyInfo.dutyTime3s, pharmacyInfo.dutyTime4c, pharmacyInfo.dutyTime4s,
                        pharmacyInfo.dutyTime5c, pharmacyInfo.dutyTime5s, pharmacyInfo.dutyTime6c, pharmacyInfo.dutyTime6s,
                        pharmacyInfo.dutyTime7c, pharmacyInfo.dutyTime7s, pharmacyInfo.dutyTime8c, pharmacyInfo.dutyTime8s)
                }
                withContext(Dispatchers.Main) {
                    _pharmacyData.value = pharmacyList2
                }
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
    }
    private suspend fun parsePharmacyData2(data: String): List<Pharmacy> {
        val pharmacies2 = mutableListOf<Pharmacy>()
        try {
            val factory2 = XmlPullParserFactory.newInstance()
            factory2.isNamespaceAware = true
            val parser = factory2.newPullParser()
            parser.setInput(StringReader(data))
            var eventType = parser.eventType
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
            var dutyTime7c = ""
            var dutyTime7s = ""
            var dutyTime8c = ""
            var dutyTime8s = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "dutyAddr" -> dutyAddr = parser.nextText() ?: ""
                            "dutyName" -> dutyName = parser.nextText() ?: ""
                            "dutyTel1" -> dutyTel1 = parser.nextText() ?: ""
                            "dutyTime1c" -> dutyTime1c = parser.nextText() ?: ""
                            "dutyTime1s" -> dutyTime1s = parser.nextText() ?: ""
                            "dutyTime2c" -> dutyTime2c = parser.nextText() ?: ""
                            "dutyTime2s" -> dutyTime2s = parser.nextText() ?: ""
                            "dutyTime3c" -> dutyTime3c = parser.nextText() ?: ""
                            "dutyTime3s" -> dutyTime3s = parser.nextText() ?: ""
                            "dutyTime4c" -> dutyTime4c = parser.nextText() ?: ""
                            "dutyTime4s" -> dutyTime4s = parser.nextText() ?: ""
                            "dutyTime5c" -> dutyTime5c = parser.nextText() ?: ""
                            "dutyTime5s" -> dutyTime5s = parser.nextText() ?: ""
                            "dutyTime6c" -> dutyTime6c = parser.nextText() ?: ""
                            "dutyTime6s" -> dutyTime6s = parser.nextText() ?: ""
                            "dutyTime7c" -> dutyTime7c = parser.nextText() ?: ""
                            "dutyTime7s" -> dutyTime7s = parser.nextText() ?: ""
                            "dutyTime8c" -> dutyTime8c = parser.nextText() ?: ""
                            "dutyTime8s" -> dutyTime8s = parser.nextText() ?: ""
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "pharmacy") {
                            // Pharmacy 객체 생성하여 리스트에 추가
                            val pharmacy = Pharmacy(dutyAddr, dutyName, dutyTel1,
                                                    dutyTime1c, dutyTime1s, dutyTime2c, dutyTime2s,
                                                    dutyTime3c, dutyTime3s, dutyTime4c, dutyTime4s,
                                                    dutyTime5c, dutyTime5s, dutyTime6c, dutyTime6s,
                                                    dutyTime7c, dutyTime7s, dutyTime8c, dutyTime8s
                            )
                            pharmacies2.add(pharmacy)
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pharmacies2
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