package com.won.travelreviewpj.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentScheduleBinding


class ScheduleFragment :
    ViewBindingBaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            tbSchedule.setOnMenuItemClickListener {item ->
                if(item.itemId == R.id.btn_add) {
                    val action =
                        ScheduleFragmentDirections.actionScheduleFragmentToScheduleUpdateFragment()
                    findNavController().navigate(action)
                }
                true
            }
        }
    }
}