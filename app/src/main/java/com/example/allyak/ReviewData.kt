package com.example.allyak

data class ReviewData (
    var pillSeq: String? = null,
    var review: Map<String, UserData> = emptyMap()
)
data class UserData (
    var uid: String? = null,
    var sym0: Int = 0, //good
    var sym1: Int = 0,
    var sym2: Int = 0,
    var sym3: Int = 0,
    var sym4: Int = 0,
    var sym5: Int = 0,
    var sym6: Int = 0,
    var sym7: Int = 0,
    var sym8: Int = 0,
    var sym9: Int = 0,
    var sym10: Int = 0,
    var sym11: Int = 0
)