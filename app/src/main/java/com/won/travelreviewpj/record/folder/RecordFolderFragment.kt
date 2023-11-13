package com.won.travelreviewpj.record.folder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordFolderBinding
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import com.won.travelreviewpj.travel.wishlist.TravelWishlistAdapter

class RecordFolderFragment :
    ViewBindingBaseFragment<FragmentRecordFolderBinding>(FragmentRecordFolderBinding::inflate) {


    private fun item() = mutableListOf<RecordFolder>().apply {
        add(RecordFolder(R.drawable.travel_sample, "222", "222"))
        add(RecordFolder(R.drawable.travel_sample, "223", "223"))
        add(RecordFolder(R.drawable.travel_sample, "224", "224"))
        add(RecordFolder(R.drawable.travel_sample, "225", "225"))
        add(RecordFolder(R.drawable.travel_sample, "226", "226"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        with(binding) {
            tbRecordFolder.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.btn_add) {
                    val action =
                        RecordFolderFragmentDirections.actionRecordFolderFragmentToRecordUpdateFragment()
                    findNavController().navigate(action)
                }
                true
            }

            rvRecordSelect.layoutManager = manager
            rvRecordSelect.adapter = RecordFolderAdapter(item())
        }
    }


}