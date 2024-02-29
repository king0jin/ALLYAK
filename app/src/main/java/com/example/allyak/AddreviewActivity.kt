package com.example.allyak

import android.animation.ObjectAnimator
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.allyak.databinding.ActivityAddreviewBinding


class AddreviewActivity: AppCompatActivity() {
    private val binding by lazy{ ActivityAddreviewBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.syms.visibility = View.GONE
        binding.symgood.setOnClickListener {
            if(binding.symbad.isChecked){
                binding.symbad.isChecked = false
                symsgone()
            }
        }
        binding.symbad.setOnClickListener {
            if(binding.symbad.isChecked){ symsgone() }
            else {
                ObjectAnimator.ofFloat(binding.sym0, "translationY", -330f).apply { start() }
                val animator = ObjectAnimator.ofFloat(binding.syms, "alpha", 0f, 1f)
                animator.duration = 300 // 애니메이션 지속 시간 (300ms로 설정)
                animator.start()
                binding.symgood.isChecked = false
                binding.syms.visibility = View.VISIBLE
            }
        }
        binding.addBtn.setOnClickListener {
            //데이터 저장
            //제품 일련 번호를 키로 저장
            if(binding.symgood.isChecked == false && binding.symbad.isChecked == false){
                Toast.makeText(this, "증상을 추가해주세요", Toast.LENGTH_SHORT).show()
            }
            else{
                val itemNum = intent.getStringExtra("itemSeq")
                if(binding.symgood.isChecked == true){
                    //잘맞아요 추가
                }
                else{
                    //부작용이 있는 경우
                }
            }
        }
        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
    private fun symsgone(){
            ObjectAnimator.ofFloat(binding.sym0, "translationY", 0f).apply { start() }
            val animator = ObjectAnimator.ofFloat(binding.syms, "alpha", 1f, 0f)
            animator.duration = 200 // 애니메이션 지속 시간 (100ms로 설정)
            animator.start()
            animator.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    binding.syms.visibility = View.GONE //천천히 사라짐
                }
            })
    }
}