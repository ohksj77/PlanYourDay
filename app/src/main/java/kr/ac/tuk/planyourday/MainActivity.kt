package kr.ac.tuk.planyourday

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kr.ac.tuk.planyourday.databinding.ActivityMainBinding
import kr.ac.tuk.planyourday.month.MonthActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAll.setOnClickListener {
            val intent = Intent(this, AllActivity::class.java)
            startActivity(intent)
        }
        binding.btnWeek.setOnClickListener {
            val intent = Intent(this, WeekActivity::class.java)
            startActivity(intent)
        }
        binding.btnMonth.setOnClickListener {
            val intent = Intent(this, MonthActivity::class.java)
            startActivity(intent)
        }
        fab = binding.fab
        binding.fab.setOnClickListener {
            Toast.makeText(this, "개발자 메일: ohksj77@naver.com", Toast.LENGTH_LONG).show()
        }
    }
}