package kr.ac.tuk.planyourday

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kr.ac.tuk.planyourday.databinding.ActivityEditTodoBinding
import kr.ac.tuk.planyourday.todo.Todo
import java.time.LocalDateTime
import java.util.*

class EditTodoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditTodoBinding
    private var todo: Todo? = null
    private val cal: Calendar = Calendar.getInstance()
    private var selectYear: Int = cal.get(Calendar.YEAR)
    private var selectMonth: Int = cal.get(Calendar.MONTH)
    private var selectDay: Int = cal.get(Calendar.DAY_OF_MONTH)
    private var selectAmPm: Int = cal.get(Calendar.AM_PM)
    private  val date: Date = Date(System.currentTimeMillis())
    private var selectHour: Int = date.hours
    private var selectMinute: Int = date.minutes
    private var flag: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.timeSpinner.setIs24HourView(true)
        binding.dateSpinner.updateDate(selectYear, selectMonth, selectDay)
        if(selectAmPm == 0){
            selectHour += 9
            binding.timeSpinner.currentHour = selectHour
        }
        else{
            if(selectHour + 9 < 24){
                selectHour += 9
                binding.timeSpinner.currentHour = selectHour
            }
            else{
                selectHour -= 15
                binding.timeSpinner.currentHour = selectHour
            }
        }
        binding.timeSpinner.currentMinute = selectMinute


        val type = intent.getStringExtra("type")

        if (type.equals("ADD")) {
            binding.btnSave.text = "추가하기"
        } else {
            todo = intent.getParcelableExtra("item") as Todo?
            binding.etTodoTitle.setText(todo!!.title)
            binding.etTodoContent.setText(todo!!.timestamp)
            binding.btnSave.text = "수정하기"
        }

        binding.timeSpinner.setOnTimeChangedListener{view, hour, min ->
            selectHour = hour
            selectMinute = min
        }

        binding.dateSpinner.setOnDateChangedListener{view, year, month, dayOfMonth ->
            selectYear = year
            selectMonth = month + 1
            selectDay = dayOfMonth
            flag = true
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTodoTitle.text.toString()
            val content = binding.etTodoContent.text.toString()
            var dateTime: LocalDateTime? = null
            if(flag == false){
                dateTime = LocalDateTime.of(selectYear, selectMonth + 1, selectDay, selectHour, selectMinute)
            }
            else{
                dateTime = LocalDateTime.of(selectYear, selectMonth, selectDay, selectHour, selectMinute)
            }
            val tmpTime = dateTime.toString().split("T")
            val currentDate = tmpTime[0] + " " + tmpTime[1]

            if (type.equals("ADD")) {
                if (title.isNotEmpty()) {
                    val todo = Todo(0, title, currentDate, content, false)
                    val intent = Intent().apply {
                        putExtra("todo", todo)
                        putExtra("flag", 0)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            } else {
                if (title.isNotEmpty()) {
                    val todo = Todo(todo!!.id, title, currentDate, content, todo!!.isChecked)
                    val intent = Intent().apply {
                        putExtra("todo", todo)
                        putExtra("flag", 1)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}