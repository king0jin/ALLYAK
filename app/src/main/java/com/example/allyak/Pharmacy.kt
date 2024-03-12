package com.example.allyak

data class Pharmacy(
    val dutyAddr: String,
    val dutyName: String,
    val dutyTel1: String,
    val dutyTime1c: String,
    val dutyTime1s: String,
    val dutyTime2c: String,
    val dutyTime2s: String,
    val dutyTime3c: String,
    val dutyTime3s: String,
    val dutyTime4c: String,
    val dutyTime4s: String,
    val dutyTime5c: String,
    val dutyTime5s: String,
    val dutyTime6c: String,
    val dutyTime6s: String,
    val hpid: String,
    val postCdn1: String,
    val postCdn2: String,
    val rnum: String,
    val wgs84Lat: String,
    val wgs84Lon: String
)
//주소, 기관명, 대표전화, 진료시간(공휴일포함)