package com.example.allyak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
        return inflater.inflate(R.layout.fragment_pill, container, false)

        //파이어베이스에서 알람 정보 가져와서 캘린더 뷰 업데이트
        updateCalendarWithAlarms()
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
                //AddAlarmActivity로 이동
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateCalendarWithAlarms() {
        //파이어베이스에서 알람 정보 가져오기
        //val UserId = FirebaseAuth.getInstance().currentUser?.uid
        //val database = FirebaseDatabase.getInstance()
        //val alramRef = database.getReference("alarms").child(UserId)


    }

}