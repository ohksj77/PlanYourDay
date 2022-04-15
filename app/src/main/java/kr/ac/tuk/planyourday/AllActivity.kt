package kr.ac.tuk.planyourday

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.tuk.planyourday.databinding.ActivityAllBinding
import kr.ac.tuk.planyourday.todo.Todo
import kr.ac.tuk.planyourday.todo.TodoAdapter
import kr.ac.tuk.planyourday.todo.TodoViewModel

class AllActivity : AppCompatActivity() {

    lateinit var todoViewModel: TodoViewModel
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var binding: ActivityAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoAdapter = TodoAdapter(this)
        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        binding.rvTodoList.layoutManager = LinearLayoutManager(this)
        binding.rvTodoList.adapter = todoAdapter
        todoAdapter.setItemClickListener(object : TodoAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getOne(itemId)
                    val intent = Intent(this@AllActivity, EditTodoActivity::class.java).apply{
                        putExtra("type", "EDIT")
                        putExtra("item", todo)
                    }
                    requestActivity.launch(intent)
                }
            }
        })
        todoAdapter.setItemCheckBoxClickListener(object: TodoAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = todoViewModel.getOne(itemId)
                    todo.isChecked = !todo.isChecked
                    todoViewModel.update(todo)
                }
            }
        })
        todoViewModel.todoList.observe(this) {
            todoAdapter.update(it)
        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, EditTodoActivity::class.java).apply {
                putExtra("type", "ADD")
            }
            requestActivity.launch(intent)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_delete -> {
                Toast.makeText(this, "삭제", Toast.LENGTH_SHORT).show()
                todoViewModel.todoList.value?.forEach {
                    if (it.isChecked) {
                        todoViewModel.delete(it)
                    }
                }
                return true
            }
            R.id.item_all_select -> {
                Toast.makeText(this, "전체 선택", Toast.LENGTH_SHORT).show()
                todoViewModel.todoList.value?.forEach {
                    if (!it.isChecked) {
                        it.isChecked = !it.isChecked
                        todoViewModel.update(it)
                    }
                }
                return true
            }
            R.id.item_not_select -> {
                Toast.makeText(this, "전체 선택 해제", Toast.LENGTH_SHORT).show()
                todoViewModel.todoList.value?.forEach {
                    if (it.isChecked) {
                        it.isChecked = !it.isChecked
                        todoViewModel.update(it)
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }
    private var requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val todo = it.data?.getParcelableExtra<Todo>("todo") as Todo

                when (it.data?.getIntExtra("flag", -1)) {
                    0 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            todoViewModel.insert(todo)
                        }
                        Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            todoViewModel.update(todo)
                        }
                        Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
}