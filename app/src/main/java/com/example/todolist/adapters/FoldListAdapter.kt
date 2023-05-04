package com.example.todolist.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.activities.EditTaskActivity
import com.example.todolist.viewmodels.EditPageViewModel

/*
* 在 RecyclerView 的 Adapter 中，您需要定义两个 ViewHolder 类型：HeaderViewHolder 和 ChildViewHolder。
* 在调用 onCreateViewHolder() 时，需要根据 ViewGroup的类型判断当前 View 是 Header 还是 Child ，并返回相应的 ViewHolder。
* 对于 ParentViewHolder，只需返回相应的视图即可（例如布局文件中的 LinearLayout、TextView等）。
* 对于 ChildViewHolder，需要将子项数据适配到对应的视图，并在需要时添加单击事件处理器。
* getChildCount() 返回每个 Header 对应的 Child 项数目；getGroupCount() 则返回 Header 数量。
* 如果希望实现折叠/展开样式，则需要为 Header 添加一个点击侦听器，并设置 isExpand 变量以标记相关节点是否已折叠或展开。
* 在折叠状态下，childViewCount 被视为 0.
* onBindViewHolder() 方法中，需要将数据适配到每个 ViewHolder 对应的视图中。
* 用一个 List<Object> 类型的变量来存储您所有的 Header 和 Child 数据
* 在构造方法中对数据进行分组，以便适配器知道哪些项目是 Header 并将相应的 Child 添加到列表视图中。
* getItemViewType() 方法需要返回不同的值才能区分当前视图是否为 Header 或 Child。
* 这样 onCreateViewHolder() 就能够正确地创建不同的 ViewHolder。
* 使用 notifyItemInserted()、notifyDataSetChanged() 等 RecyclerView.Adapter 的通知函数进行数据操作时，
* 需要注意考虑 Header 和 Child 的position保持和dataList中一致
* */


// 可折叠展示列表的adapter
class FoldListAdapter(private var data: List<FoldData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_PARENT = 0x001
        const val TYPE_CHILD = 0x002
    }

    fun setNewDatas(new: List<FoldData>) {
        data = new
        notifyDataSetChanged()
    }

    sealed class FoldData(val type: Int) {
        data class ParentBean(
            var title: String,
            var isExpand: Boolean = false,
            var isFinished: Boolean = false,
            var children: List<ChildBean>? = null,
            val id: Long
        ) : FoldData(TYPE_PARENT)

        data class ChildBean(
            var title: String,
            var isFinished: Boolean = false,
            val id: Long,
            val parent: Long
        ) : FoldData(TYPE_CHILD)
    }

    sealed class Holder(root: View) : RecyclerView.ViewHolder(root)
    inner class ParentHolder(itemView: View) : Holder(itemView) {
        private val titleView = itemView.findViewById<TextView>(R.id.parent_task_title)
        private val addChild = itemView.findViewById<ImageView>(R.id.create_child_task)
        private val isExpand = itemView.findViewById<ImageView>(R.id.parent_task_state)
        private val isFinished = itemView.findViewById<CheckBox>(R.id.parent_is_finished_state)

        init {
            itemView.setOnClickListener {
                val currentPosition = adapterPosition
                // 当前position可见
                if (currentPosition != RecyclerView.NO_POSITION) {
                    (data[currentPosition] as FoldData.ParentBean).apply {
                        isExpand = !isExpand
                        if (!children.isNullOrEmpty()) {
                            if (isExpand) {
                                expandItem(currentPosition)
                            } else {
                                closeItem(currentPosition)
                            }
                        }
                        notifyItemChanged(currentPosition)
                    }
                }
            }

            addChild.setOnClickListener {
                // 跳转Edit界面 新建 子级task
                addChild.context.let {
                    val intent = Intent(it, EditTaskActivity::class.java)
                    intent.putExtra("mode", "create_task")
//                    intent.putExtra("isFinished", (data[adapterPosition] as FoldData.ParentBean).isFinished)
//                    intent.putExtra("id", (data[adapterPosition] as FoldData.ParentBean).id)
//                    intent.putExtra("title", (data[adapterPosition] as FoldData.ParentBean).title)
                    intent.putExtra("parent", (data[adapterPosition] as FoldData.ParentBean).id)
                    it.startActivity(intent)
                }
            }
        }

        private fun closeItem(position: Int) {
            (data[position] as FoldData.ParentBean).children?.let {
                val arr = (data as ArrayList<FoldData>)
                for (i in it.size downTo 1) {
                    arr.removeAt(position + i)
                }

                notifyItemRangeRemoved(position + 1, it.size)
                notifyItemRangeChanged(position + 1, it.size)
//                notifyDataSetChanged()
            }
        }

        private fun expandItem(position: Int) {
            (data[position] as FoldData.ParentBean).children?.let {
                (data as ArrayList<FoldData>).addAll(position + 1, it)
                notifyItemRangeInserted(position + 1, it.size)
                notifyItemRangeChanged(position + 1, it.size)
//                notifyDataSetChanged()
            }
        }

        fun bind(task: FoldData.ParentBean, position: Int) {
            titleView.text = task.title
            // 旋转parentView的箭头
            isExpand.rotation = if (task.isExpand) 0f else 90f
            isFinished.isChecked = task.isFinished
        }
    }

    inner class ChildHolder(itemView: View) : Holder(itemView) {
        private val childDrawer = itemView.findViewById<DrawerLayout>(R.id.child_drawer_layout)
        private val contentView = itemView.findViewById<CardView>(R.id.child_content_view)

        private val titleView = itemView.findViewById<TextView>(R.id.child_task_title)
        private val isFinished = itemView.findViewById<CheckBox>(R.id.child_is_finished_state)

        private val delete = itemView.findViewById<Button>(R.id.task_delete_view)
        private val top = itemView.findViewById<Button>(R.id.task_sticky_view)

        init {
            titleView.setOnClickListener {
                // 跳转Edit界面
                contentView.context.let {
                    val intent = Intent(it, EditTaskActivity::class.java)
                    intent.putExtra("mode", "change")
                    intent.putExtra(
                        "isFinished",
                        (data[adapterPosition] as FoldData.ChildBean).isFinished
                    )
                    intent.putExtra("id", (data[adapterPosition] as FoldData.ChildBean).id)
                    intent.putExtra("title", (data[adapterPosition] as FoldData.ChildBean).title)
                    intent.putExtra("parent", (data[adapterPosition] as FoldData.ChildBean).parent)
                    it.startActivity(intent)
                }
            }

            childDrawer.setScrimColor(Color.TRANSPARENT)
            childDrawer.addDrawerListener(object : DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    contentView.translationX = (-(drawerView.measuredWidth) * slideOffset)
                }

                override fun onDrawerOpened(drawerView: View) {

                }

                override fun onDrawerClosed(drawerView: View) {

                }

                override fun onDrawerStateChanged(newState: Int) {

                }

            })
            top.setOnClickListener {

            }
            delete.setOnClickListener {
                (data as ArrayList<FoldData>).removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition, 1)
                childDrawer.closeDrawers()
            }
        }

        fun bind(task: FoldData.ChildBean, position: Int) {
            titleView.text = task.title
            isFinished.isChecked = task.isFinished
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_PARENT -> ParentHolder(inflater.inflate(R.layout.test_parent, parent, false))
            else -> ChildHolder(inflater.inflate(R.layout.test_child, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ParentHolder -> {
                holder.bind(data[position] as FoldData.ParentBean, position)
            }

            is ChildHolder -> {
                holder.bind(data[position] as FoldData.ChildBean, position)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

}