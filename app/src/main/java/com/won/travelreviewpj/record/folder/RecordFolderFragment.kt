package com.won.travelreviewpj.record.folder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordFolderBinding

class RecordFolderFragment : ViewBindingBaseFragment<FragmentRecordFolderBinding>(FragmentRecordFolderBinding::inflate) {
    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            tbRecordFolder.setOnMenuItemClickListener {item ->
                if(item.itemId == R.id.btn_add) {
                    val action =
                        RecordFolderFragmentDirections.actionRecordFolderFragmentToRecordUpdateFragment()
                    findNavController().navigate(action)
                }
                true
            }
        }
    }


}