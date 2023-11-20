package com.won.travelreviewpj.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordBinding
import com.won.travelreviewpj.travel.TravelFragmentDirections
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import com.won.travelreviewpj.travel.wishlist.TravelWishlistAdapter

class RecordFragment :
    ViewBindingBaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {

    private fun item() = mutableListOf<Record>().apply {
        add(Record(R.drawable.empty_folder_blue, "111"))
        add(Record(R.drawable.empty_folder_blue, "112"))
        add(Record(R.drawable.empty_folder_blue, "113"))
        add(Record(R.drawable.empty_folder_blue, "114"))
        add(Record(R.drawable.empty_folder_blue, "115"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        with(binding) {
            rvRecordSelect.layoutManager = manager
            rvRecordSelect.adapter = RecordAdapter(item())
        }
    }
}


