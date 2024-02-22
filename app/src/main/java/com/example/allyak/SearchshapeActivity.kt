package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.allyak.databinding.ActivitySearchshapeBinding

class SearchshapeActivity: AppCompatActivity() {
    lateinit var binding: ActivitySearchshapeBinding
    private val buttonIds = listOf("c_white","c_yellow","c_orange","c_pink","c_red","c_brown","c_ygreen", "c_green",
        "c_bgreen","c_blue","c_navy","c_violet","c_purple","c_gray","c_black","c_none")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchshapeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setOnClickListener{
            finish()
        }
        binding.pillSearchBtn.setOnClickListener {
            val intent = Intent(this, SearchpillActivity::class.java)
            startActivity(intent)
        }
        val colorRadio = findViewById<RadioGroup>(R.id.color_radio)
        val ToggleListener =
            RadioGroup.OnCheckedChangeListener { radioGroup, i ->
                for (j in 0 until radioGroup.childCount) {
                    val view = radioGroup.getChildAt(j) as ToggleButton
                    view.isChecked = view.id == i
                }
            }
        colorRadio.setOnCheckedChangeListener(ToggleListener)
        for (buttonId in buttonIds) {
            val button = findViewById<ToggleButton>(resources.getIdentifier(buttonId, "id", packageName))
            button.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // 토글 버튼이 눌렸을 때
                    val resourceId = resources.getIdentifier(buttonId, "drawable", packageName)
                    if (resourceId != 0) {
                        // 해당 아이디와 같은 이름의 XML 파일이 있는 경우
                        val pressedDrawable = ContextCompat.getDrawable(this, resourceId)
                        button.background = pressedDrawable
                    }

                } else {
                    // 토글 버튼이 해제되었을 때
                    val resourceId = resources.getIdentifier(buttonId+"1", "drawable", packageName)
                    if (resourceId != 0) {
                        // 해당 아이디와 같은 이름의 XML 파일이 있는 경우
                        val defaultDrawable = ContextCompat.getDrawable(this, resourceId)
                        button.background = defaultDrawable
                    }
                }
            }
        }
    }
    fun onToggle(view: View) {
        //(view.getParent() as RadioGroup).check(view.getId())
        // app specific stuff ..
        (view.parent as RadioGroup).check(0)
    }
}