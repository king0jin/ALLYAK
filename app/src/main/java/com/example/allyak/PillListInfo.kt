package com.example.allyak

data class PillListInfo(
    val key: String,
    val mediName: String,
    val hour: Int,
    val minute: Int,
    val calendarYear: Int,
    val calendarMonth: Int,
    val calendarDay: Int,
    var checked: Boolean
) {
    constructor() : this("", "", 0, 0, 0, 0, 0, false)
}
data class Pill(
    val mediName: String,
    val hour: Int,
    val minute: Int,
    val calendarYear: Int,
    val calendarMonth: Int,
    val calendarDay: Int,
    var checked: Boolean
) {
    constructor() : this("", 0, 0, 0, 0, 0, false)
}
