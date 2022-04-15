package kr.ac.tuk.planyourday

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kr.ac.tuk.planyourday.todo.Todo

class MonthListAdapter(val context: Context, todoList: List<Todo>) : BaseAdapter() {

    var list = todoList

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return list[p0].id
    }

    fun update(newList: List<Todo>) {
        list = emptyList()
        this.list = newList
        notifyDataSetChanged()
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, listView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(parent!!.context).inflate(R.layout.month_todo_list, null)
        val title = view.findViewById<TextView>(R.id.todoItem)
        val content = view.findViewById<TextView>(R.id.content)
        val timestamp = view.findViewById<TextView>(R.id.timestamp)
        val todo = list[position]
        title.text = todo.title + "   "
        content.text = todo.timestamp
        timestamp.text = todo.content + "         "
        return view
    }
}