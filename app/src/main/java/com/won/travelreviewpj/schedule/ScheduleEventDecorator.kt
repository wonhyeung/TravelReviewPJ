package com.won.travelreviewpj.schedule

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

/**
 * Schedule event decorator
 *
 * @param day 여행한 날짜
 * 방문한 여행지 날짜 Decorator
 */
class ScheduleEventDecorator(day: CalendarDay) : DayViewDecorator {

    private val travelDays: CalendarDay = day
    override fun shouldDecorate(selectedTravelDay: CalendarDay?): Boolean {
        return selectedTravelDay?.equals(travelDays)!!
    }
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(10f, Color.RED))
    }
}