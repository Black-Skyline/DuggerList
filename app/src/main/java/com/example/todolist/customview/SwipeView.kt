package com.example.todolist.customview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import com.example.todolist.R
import kotlin.math.abs

//class SwipeView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
//
//    // 不同功能的子view
//    // 展示内容
//    private var contentView: View? = null
//
//    // 删除内容
//    private var deleteView: View? = null
//
//    // 置顶内容
//    private var stickyView: View? = null
//
//    // 手势检测器
//    private val gestureDetector = GestureDetectorCompat(context, GestureListener())
//
//    // 手指按下时的坐标
//    private var downX = 0f
//    private var downY = 0f
//
//    // 滑动的最小距离
//    private val minSwipeDistance = context.resources.getDimensionPixelSize(R.dimen.dp_size_75)
//
//    // 是否已经被删除了
//    private var isDeleted = false
//
//    // 是否已经被置顶了
//    private var isInTop = false
//
//    init {
//        val inflater = LayoutInflater.from(context)
//        inflater.inflate(R.layout.test_child, this, true)
//
//        contentView = findViewById(R.id.child_task_title)
//        deleteView = findViewById(R.id.task_delete_view)
//        stickyView = findViewById(R.id.task_sticky_view)
//    }
//
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        return gestureDetector.onTouchEvent(ev)
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        return gestureDetector.onTouchEvent(event)
//    }
//
//    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
//        override fun onDown(e: MotionEvent): Boolean {
//            downX = e?.x ?: 0f
//            downY = e?.y ?: 0f
//            isDeleted = false
//            return super.onDown(e)
//        }
//
//        override fun onScroll(
//            e1: MotionEvent,
//            e2: MotionEvent,
//            distanceX: Float,
//            distanceY: Float
//        ): Boolean {
//            if (isDeleted)
//                return true
//            if (abs(distanceX) > abs(distanceY)) {
//                val deltaX = e2?.x?.minus(downX) ?: 0f
//                if (deltaX > minSwipeDistance) {
//                    // 向右滑动，显示删除 View
//                    showDeleteView()
//                } else if (deltaX < -minSwipeDistance) {
//                    // 向左滑动，隐藏删除 View
//                    hideDeleteView()
//                }
//                return true
//            }
//            // 横向滑动
//            return super.onScroll(e1, e2, distanceX, distanceY)
//        }
//    }
//
//    @SuppressLint("ObjectAnimatorBinding")
//    private fun showDeleteView() {
//        val animator = ObjectAnimator.ofFloat(
//            contentView,
//            "translationX",
//            0f,
//            deleteView?.width?.toFloat() ?: 0f
//        )
//        animator.duration = 200
//        animator.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(p0: Animator) {
//
//            }
//
//            override fun onAnimationEnd(p0: Animator) {
//                isDeleted = true
//            }
//
//            override fun onAnimationCancel(p0: Animator) {
//
//            }
//
//            override fun onAnimationRepeat(p0: Animator) {
//                animator.start()
//            }
//
//        })
//        animator.start()
//    }
//    @SuppressLint("ObjectAnimatorBinding")
//    private fun hideDeleteView() {
//        val animator = ObjectAnimator.ofFloat(contentView, "translationX", deleteView?.width?.toFloat() ?: 0f, 0f)
//        animator.duration = 200
//        animator.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(p0: Animator) {
//            }
//
//            override fun onAnimationEnd(p0: Animator) {
//                isDeleted = false
//            }
//
//            override fun onAnimationCancel(p0: Animator) {
//            }
//
//            override fun onAnimationRepeat(p0: Animator) {
//            }
//
//        })
//        animator.start()
//    }
//
//    fun isDeleted() :Boolean {
//        return isDeleted
//    }
//    fun reset() {
//        contentView?.translationX = 0f
//        isDeleted = false
//    }
//}