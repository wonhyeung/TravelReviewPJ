package com.won.travelreviewpj.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.won.travelreviewpj.common.currentDateFormat
import com.won.travelreviewpj.record.RecordRepository
import com.won.travelreviewpj.record.diary.RecordDiary

class ScheduleViewModel : ViewModel() {

    private val repository = RecordRepository()
    private val _selectedDiaries = MutableLiveData<List<RecordDiary>?>(emptyList())
    val selectedDiaries: MutableLiveData<List<RecordDiary>?> get() = _selectedDiaries
    val allDiaries: LiveData<List<RecordDiary>> = repository.getAllRecordDiaries()

    fun getSelectedDateDiaries(selectedDay: CalendarDay) {
        val selectedDateStr = "${selectedDay.year}년 ${selectedDay.month}월 ${selectedDay.day}일"
        val selectedDate = currentDateFormat().parse(selectedDateStr)
        val newSchedules = allDiaries.value?.filter { recordDiary ->
            val formatter = currentDateFormat()
            val startDate = formatter.parse(recordDiary.startDate.substring(0, 13))
            val endDate = formatter.parse(recordDiary.endDate.substring(0, 13))

            !selectedDate.before(startDate) && !selectedDate.after(endDate)
        }
        _selectedDiaries.postValue(newSchedules)
    }
}