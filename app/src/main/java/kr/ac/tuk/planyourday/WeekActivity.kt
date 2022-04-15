package kr.ac.tuk.planyourday

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import kr.ac.tuk.planyourday.databinding.ActivityWeekBinding
import kr.ac.tuk.planyourday.month.EventDecorator
import kr.ac.tuk.planyourday.todo.Todo
import kr.ac.tuk.planyourday.todo.TodoViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class WeekActivity : AppCompatActivity(), OnDateSelectedListener, OnMonthChangedListener {
    private var materialCalendarView: MaterialCalendarView? = null
    private lateinit var binding: ActivityWeekBinding
    lateinit var todoViewModel: TodoViewModel
    lateinit var monthListAdapter: MonthListAdapter
    lateinit var listView: ListView
    private var cal: Calendar? = null
    var myCalendarList: List<Todo> = listOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        cal = Calendar.getInstance()
        materialCalendarView = binding.calendarweek


        val weekLabels = arrayOf("일", "월", "화", "수", "목", "금", "토")
        materialCalendarView!!.setWeekDayLabels(weekLabels)
        materialCalendarView!!.setOnDateChangedListener(this)
        materialCalendarView!!.setOnMonthChangedListener(this)
        materialCalendarView!!.topbarVisible = true
        materialCalendarView!!.isDynamicHeightEnabled = true
        materialCalendarView!!.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2020, 12, 1))
            .setMaximumDate(CalendarDay.from(2030, 4, 30))
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit();
        materialCalendarView!!.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)));
        materialCalendarView!!.setTitleFormatter(TitleFormatter { day ->
            val inputText: Date = day.date
            val calendarHeaderElements = ((inputText.year.toInt() + 1900).toString() + "-" +  (inputText.month.toInt() + 1).toString())
            val calendarHeaderBuilder = StringBuilder()
            calendarHeaderBuilder.append(calendarHeaderElements)
            calendarHeaderBuilder.toString()
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        init()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean,
    ) {
        Executors.newSingleThreadExecutor().execute { myCalendarList = todoViewModel.getAllList() }
        monthListAdapter = MonthListAdapter(this, myCalendarList)
        var list : ArrayList<Todo> = arrayListOf()
        for(i in myCalendarList){
            var date = Date(date.year - 1900, date.month, date.day)
            val format = SimpleDateFormat("yyyy-MM-dd")
            val day = format.format(date).toString()
            var arr = i.content.toString().split(" ")
            if(day == arr[0]) {
                list.add(i)
            }
        }
        cal!![date.year, date.month - 1] = date.day
        listView = binding.todoWeek
        listView.adapter = monthListAdapter
        monthListAdapter.update(list)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay) {
        val cal = Calendar.getInstance(Locale.KOREA)
        cal[date.year, date.month] = date.day
        getData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData() {
        try {
            todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
            Executors.newSingleThreadExecutor().execute { myCalendarList = todoViewModel.getAllList() }
            materialCalendarView!!.removeDecorators()
            if (myCalendarList.size > 0) {
                initDecorators(myCalendarList)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDecorators(list: List<Todo>) {
        if(list.isEmpty()){
            return
        }
        val days = HashSet<CalendarDay>()
        for (myCalendar in list) {
            val calendar = Calendar.getInstance()
            val tmpArr = myCalendar.content!!.split(" ")
            val tmpDate = tmpArr[0].split("-")
            calendar.time = Date(tmpDate[0].toInt(), tmpDate[1].toInt(), tmpDate[2].toInt())
            days.add(CalendarDay.from(Date(tmpDate[0].toInt() - 1900, tmpDate[1].toInt() - 1, tmpDate[2].toInt())))
        }
        materialCalendarView!!.addDecorator(EventDecorator(Color.WHITE, days))
        materialCalendarView!!.invalidateDecorators()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        materialCalendarView!!.selectedDate = CalendarDay.from(cal!![Calendar.YEAR],
            cal!![Calendar.MONTH],
            cal!![Calendar.DAY_OF_MONTH])
        materialCalendarView!!.state().edit()
            .isCacheCalendarPositionEnabled(false)
            .commit()
    }
}