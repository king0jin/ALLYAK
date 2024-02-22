package com.example.allyak

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class PharmacyApiData {
    private val apiUrl ="http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown"
    private val apiKey = "Bht6UBoql5Zuh2tvMckFU8%2FADjJBctoibZLwRZxojBgR3aOBeRyIjdD33DwP4teYhTAEIYs1fuQHt7eplCkv0w%3D%3D"

    fun getData(): ArrayList<Pharmacy> {
        val dataArr = ArrayList<Pharmacy>()

        //네트워킹 작업은 메인스레드에서 처리X 따로 스레드를 만들어 처리.
        val t = object : Thread() {
            override fun run() {
                try {
                    //url관련부분
                    val fullurl = "$apiUrl?serviceKey=$apiKey"
                    val url = URL(fullurl)
                    val isStream = url.openStream()

                    //xml 파싱
                    val xmlFactory = XmlPullParserFactory.newInstance()
                    val parser = xmlFactory.newPullParser()
                    parser.setInput(isStream, "UTF-8")

                    //xml과 관련된 변수
                    var eventType = parser.eventType
                    var bdutyName = false
                    var bdutyAddr = false
                    var bdutyTel1 = false
                    var bwgs84Lon = false
                    var bwgs84Lat = false
                    var bdutyTime1c = false
                    var bdutyTime1s = false
                    var bdutyTime2c = false
                    var bdutyTime2s = false
                    var bdutyTime3c = false
                    var bdutyTime3s = false
                    var bdutyTime4c = false
                    var bdutyTime4s = false
                    var bdutyTime5c = false
                    var bdutyTime5s = false
                    var bdutyTime6c = false
                    var bdutyTime6s = false
                    var bdutyTime7c = false
                    var bdutyTime7s = false
                    var bdutyTime8c = false
                    var bdutyTime8s = false

                    var dutyName = ""
                    var dutyAddr = ""
                    var dutyTel1 = ""
                    var wgs84Lon = ""
                    var wgs84Lat = ""
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

                    //본격적인 파싱
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        when (eventType) {
                            XmlPullParser.START_TAG -> {
                                val tagName = parser.name
                                when (tagName) {
                                    "item" -> {
                                        for (i in 0 until parser.attributeCount) {
                                            when (parser.getAttributeName(i)) {
                                                "기관명" -> bdutyName = true
                                                "주소" -> bdutyAddr = true
                                                "대표전화1" -> bdutyTel1 = true
                                                "병원경도" -> bwgs84Lon = true
                                                "병원위도" -> bwgs84Lat = true
                                                "진료시간(월요일)C" -> bdutyTime1c = true
                                                "진료시간(월요일)S" -> bdutyTime1s = true
                                                "진료시간(화요일)C" -> bdutyTime2c = true
                                                "진료시간(화요일)S" -> bdutyTime2s = true
                                                "진료시간(수요일)C" -> bdutyTime3c = true
                                                "진료시간(수요일)S" -> bdutyTime3s = true
                                                "진료시간(목요일)C" -> bdutyTime4c = true
                                                "진료시간(목요일)S" -> bdutyTime4s = true
                                                "진료시간(금요일)C" -> bdutyTime5c = true
                                                "진료시간(금요일)S" -> bdutyTime5s = true
                                                "진료시간(토요일)C" -> bdutyTime6c = true
                                                "진료시간(토요일)S" -> bdutyTime6s = true
                                                "진료시간(일요일)C" -> bdutyTime7c = true
                                                "진료시간(일요일)S" -> bdutyTime7s = true
                                                "진료시간(공휴일)C" -> bdutyTime8c = true
                                                "진료시간(공휴일)S" -> bdutyTime8s = true
                                            }
                                        }
                                    }
                                }
                            }
                            XmlPullParser.TEXT -> {
                                val text = parser.text
                                if (bdutyName) {
                                    dutyName = text
                                    bdutyName = false
                                } else if (bdutyAddr) {
                                    dutyAddr = text
                                    bdutyAddr = false
                                } else if (bdutyTel1) {
                                    dutyTel1 = text
                                    bdutyTel1 = false
                                } else if (bwgs84Lon) {
                                    wgs84Lon = text
                                    bwgs84Lon = false
                                } else if (bwgs84Lat) {
                                    wgs84Lat = text
                                    bwgs84Lat = false
                                } else if (bdutyTime1c) {
                                    dutyTime1c = text
                                    bdutyTime1c = false
                                } else if (bdutyTime1s) {
                                    dutyTime1s = text
                                    bdutyTime1s = false
                                } else if (bdutyTime2c) {
                                    dutyTime2c = text
                                    bdutyTime2c = false
                                } else if (bdutyTime2s) {
                                    dutyTime2s = text
                                    bdutyTime2s = false
                                } else if (bdutyTime3c) {
                                    dutyTime3c = text
                                    bdutyTime3c = false
                                } else if (bdutyTime3s) {
                                    dutyTime3s = text
                                    bdutyTime3s = false
                                } else if (bdutyTime4c) {
                                    dutyTime4c = text
                                    bdutyTime4c = false
                                } else if (bdutyTime4s) {
                                    dutyTime4s = text
                                    bdutyTime4s = false
                                } else if (bdutyTime5c) {
                                    dutyTime5c = text
                                    bdutyTime5c = false
                                } else if (bdutyTime5s) {
                                    dutyTime5s = text
                                    bdutyTime5s = false
                                } else if (bdutyTime6c) {
                                    dutyTime6c = text
                                    bdutyTime6c = false
                                } else if (bdutyTime6s) {
                                    dutyTime6s = text
                                    bdutyTime6s = false
                                } else if (bdutyTime7c) {
                                    dutyTime7c = text
                                    bdutyTime7c = false
                                } else if (bdutyTime7s) {
                                    dutyTime7s = text
                                    bdutyTime7s = false
                                } else if (bdutyTime8c) {
                                    dutyTime8c = text
                                    bdutyTime8c = false
                                } else if (bdutyTime8s) {
                                    dutyTime8s = text
                                    bdutyTime8s = false
                                }
                            }
                            XmlPullParser.END_TAG -> {
                                if (parser.name == "item") {
                                    dataArr.add(
                                        Pharmacy(
                                            wgs84Lon.toDouble(), wgs84Lat.toDouble(),
                                            dutyName, dutyAddr, dutyTel1,
                                            dutyTime1c, dutyTime1s,
                                            dutyTime2c, dutyTime2s,
                                            dutyTime3c, dutyTime3s,
                                            dutyTime4c, dutyTime4s,
                                            dutyTime5c, dutyTime5s,
                                            dutyTime6c, dutyTime6s,
                                            dutyTime7c, dutyTime7s,
                                            dutyTime8c, dutyTime8s
                                        )
                                    )
                                }
                            }
                        }
                        eventType = parser.next()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        try {
            t.start()
            t.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dataArr
    }
}