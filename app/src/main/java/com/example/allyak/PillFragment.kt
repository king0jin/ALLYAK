package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

class PillFragment : Fragment() {
    lateinit var tb : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pill, container, false)
        val calendarView = view.findViewById<CalendarView>(R.id.calendar)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // 선택한 날짜 정보를 처리합니다.
            // 여기서 year, month, dayOfMonth는 선택한 날짜에 대한 정보입니다.
            // 이 정보를 원하는 방식으로 활용할 수 있습니다.
            // 예를 들어, 텍스트뷰에 출력하거나 다른 기능에 활용할 수 있습니다.
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            // 여기서는 예시로 선택한 날짜를 로그로 출력합니다.
            println("Selected date: $selectedDate")
        }
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pilllist_menu, menu)
        true
    }

    //메뉴 아이템 클리시 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pilllist_action -> {
                Toast.makeText(requireContext(), "Moving to AddAlarmActivity", Toast.LENGTH_SHORT).show()
                //PillListActivity로 이동
                val intent = Intent(requireContext(), PillListActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> return true
        }
    }

    private fun updateCalendarWithAlarms() {
        //파이어베이스에서 알람 정보 가져오기
        //val UserId = FirebaseAuth.getInstance().currentUser?.uid
        //val database = FirebaseDatabase.getInstance()
        //val alramRef = database.getReference("alarms").child(UserId)


    }

}