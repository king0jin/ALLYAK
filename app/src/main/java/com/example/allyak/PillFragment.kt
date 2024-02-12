package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PillFragment : Fragment() {
    lateinit var tb : Toolbar
    lateinit var calendar : CalendarView
    lateinit var allyakschedule : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pill, container, false)
        calendar = view.findViewById(R.id.calendar)
        allyakschedule = view.findViewById(R.id.pillnAlramcheck)
        val database = FirebaseDatabase.getInstance()
        val alarmRef = database.getReference("alarms")

        var ClickTime : Long = 0
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val now = System.currentTimeMillis()
            if(now - ClickTime < 200) {
                //캘린더에서 날짜를 더블터치하면 알람액티비티로 넘어가서 알람 설정을 함
                val intent = Intent(requireContext(), AddAlramActivity::class.java)
                intent.putExtra("selectedYear", year)
                intent.putExtra("selectedMonth", month)
                intent.putExtra("selectedDayOfMonth", dayOfMonth)
                startActivity(intent)
            }
            //선택한 날짜 정보 로그로 확인
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            Log.d("Calendar", "Selected date: $selectedDate")
        }
        //데이터 베이스에서 데이터 읽기
        alarmRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val alarmname = postSnapshot.child("mediName").getValue(String::class.java)
                    val alarmhour = postSnapshot.child("hour").getValue(String::class.java)
                    val alarmminute = postSnapshot.child("minute").getValue(String::class.java)
                    val alarmYear = postSnapshot.child("year").getValue(String::class.java)
                    val alarmMonth = postSnapshot.child("month").getValue(String::class.java)
                    val alarmDay = postSnapshot.child("day").getValue(String::class.java)
                    if (alarmname != null && alarmhour != null && alarmminute != null &&
                        alarmYear != null && alarmMonth != null && alarmDay != null) {
                        // 알람 이름 표시
                        allyakschedule = TextView(requireContext())
                        allyakschedule.text = "$alarmname $alarmhour:$alarmminute"
                        // 캘린더에 작은 점으로 표시
                        //allyakschedule.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dot, 0, 0, 0)
                        // 체크박스 추가
                        val checkBox = CheckBox(requireContext())
                        // 체크박스에 체크 이벤트 리스너 추가
                        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                // 체크되면 캘린더에 작은 점으로 표시

                            } else {
                                // 체크가 해제되면 작은 점 삭제

                            }
                        }
                        // 레이아웃에 추가
                        //calendarLayout.addView(checkBox)
                        //calendarLayout.addView(textView)
                    }

                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG", "Failed to read value.", databaseError.toException())
            }
        })

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

}