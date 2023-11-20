package com.won.travelreviewpj.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentScheduleBinding
import com.won.travelreviewpj.map.MapAdapter


class ScheduleFragment :
    ViewBindingBaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {

    private fun item() = mutableListOf<Schedule>().apply {
        add(Schedule(R.drawable.travel_sample, "111", "혼자"))
        add(Schedule(R.drawable.travel_sample, "112", "혼자"))
        add(Schedule(R.drawable.travel_sample, "113", "혼자"))
        add(Schedule(R.drawable.travel_sample, "114", "혼자"))
        add(Schedule(R.drawable.travel_sample, "115", "혼자"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tbSchedule.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.btn_add) {
                    val action =
                        ScheduleFragmentDirections.actionScheduleFragmentToScheduleUpdateFragment()
                    findNavController().navigate(action)
                }
                true
            }
            val manager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
            with(binding) {
                rvScheduleTravel.layoutManager = manager
                rvScheduleTravel.adapter = ScheduleAdapter(item())
            }
        }
    }
}