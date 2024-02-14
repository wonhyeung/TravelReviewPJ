package com.won.travelreviewpj.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.common.currentDateFormat
import com.won.travelreviewpj.databinding.FragmentScheduleBinding
import com.won.travelreviewpj.record.diary.RecordDiary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Schedule fragment
 *
 * 방문한 여행지 날짜 확인
 */
class ScheduleFragment : ViewBindingBaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {

    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var adapter: ScheduleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDiaries()
        fieldSetup()
    }

    private fun fieldSetup(recordDiary: List<RecordDiary> = emptyList()) {
        adapter = ScheduleAdapter(R.layout.item_travel)
        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        with(binding) {
            rvScheduleTravel.layoutManager = manager
            adapter.replaceScheduleInfo(recordDiary)
            mcSchedule.setOnDateChangedListener { _, date, _ ->
                lifecycleScope.launch {
                    viewModel.getSelectedDateDiaries(date)
                }
            }
            rvScheduleTravel.adapter = adapter

        }
    }
    private fun setCalendarDays(recordDiary: List<RecordDiary> = emptyList()) {

        recordDiary.forEach { diary ->
            val formatter = currentDateFormat()

            var startDate = formatter.parse(diary.startDate.substring(0, 13))
            val endDate = formatter.parse(diary.endDate.substring(0, 13))

            while (!startDate.after(endDate)) {
                val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(startDate)
                val month = SimpleDateFormat("MM", Locale.getDefault()).format(startDate)
                val day = SimpleDateFormat("dd", Locale.getDefault()).format(startDate)

                val addDay: CalendarDay = CalendarDay.from(year.toInt(), month.toInt(), day.toInt())
                binding.mcSchedule.addDecorators(ScheduleEventDecorator(addDay))
                startDate = Date(startDate.time + TimeUnit.DAYS.toMillis(1))
            }
        }
    }

    private fun observeDiaries() {

        viewModel.allDiaries.observe(viewLifecycleOwner) { allDiaries ->
            adapter.replaceScheduleInfo(allDiaries)
            setCalendarDays(allDiaries)
        }

        viewModel.selectedDiaries.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.replaceScheduleInfo(it)
            }
        }
    }

}