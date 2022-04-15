package kr.ac.tuk.planyourday

import android.app.Application
import kr.ac.tuk.planyourday.todo.TodoRepository

class ApplicationClass: Application() {
    override fun onCreate() {
        super.onCreate()
        TodoRepository.initialize(this)
    }
}