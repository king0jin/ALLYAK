package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import java.util.Date


class CommunityFragment : Fragment() {
    // Access a Cloud Firestore instance from your Activity
    lateinit var addPost: FloatingActionButton
    private lateinit var communityRecyclerView: RecyclerView
    private val itemList = mutableListOf<ItemData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPost = view.findViewById(R.id.addPost)
        addPost.setOnClickListener {
            val intent = Intent(this.requireContext(), AddpostActivity::class.java)
            startActivity(intent)
        }
        communityRecyclerView = view.findViewById(R.id.communityRecyclerView)
        communityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        communityRecyclerView.adapter = MyAdapter(requireContext(), itemList)
        //getPostData()
    }
//    private fun getPostData() {
//        val postListener = object : ValueEventListener { //realtime database 가져오기
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onDataChange(snapshot: DataSnapshot) {
//                itemList.clear()
//
//                for (data in snapshot.children) {
//                    val item = data.getValue(ItemData::class.java)
//                    // 리스트에 읽어 온 데이터를 넣어준다.
//                    if (item != null) {
//                        val currentDate = Date()
//                        val itemDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ko", "KR")).parse(item.date)
//
//                        val displayText = if (isSameDate(currentDate, itemDate)) {
//                            // 같은 날이면 시간만 표시
//                            SimpleDateFormat("HH:mm", Locale("ko", "KR")).format(itemDate)
//                        } else {
//                            // 다른 날이면 날짜만 표시
//                            SimpleDateFormat("yyyy/MM/dd", Locale("ko", "KR")).format(itemDate)
//                        }
//
//                        // ItemData 객체의 date 속성을 새로운 형식으로 업데이트
//                        item.date = displayText
//                        itemList.add(item)
//                    }
//                }
//                itemList.reverse()
//                // notifyDataSetChanged()를 호출하여 adapter에게 값이 변경 되었음을 알려준다.
//                (communityRecyclerView.adapter as MyAdapter).notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("Allyakk", "loadPost:onCancelled", error.toException())
//            }
//        }
//        // addValueEventListener() 메서드로 DatabaseReference에 ValueEventListener를 추가한다.
//        PostRef.contentRef.addValueEventListener(postListener)
//    }
    private fun isSameDate(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}