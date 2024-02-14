package com.won.travelreviewpj.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.longClicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.databinding.ItemFolderBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

/**
 * Record adapter
 *
 * @property recordItemLayout 방문한 여행지 저장할 폴더
 * 방문한 여행지 저장할 폴더
 */
class RecordAdapter(
    private val recordItemLayout: Int,
    private val recordViewModel: RecordViewModel,
    private val lifecycleOwner: LifecycleOwner,
) : ListAdapter<Record, RecordAdapter.ItemHolder>(diffUtil) {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Record>() {
            override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ItemHolder(val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: Record) {
            record.let {
                with(binding) {
                    ivFolderImage.setImageResource(it.image)
                    tvFolderName.text = it.name
                    compositeDisposable.add(
                        root.clicks()
                            .subscribe {
                                val action =
                                    RecordFragmentDirections.actionRecordFragmentToRecordDiaryFragment(
                                        record.id
                                    )
                                root.findNavController().navigate(action)
                            }
                    )
                    root.longClicks()
                        .subscribe {
                            MaterialAlertDialogBuilder(root.context)
                                .setMessage(R.string.delete_folder)
                                .setNegativeButton(R.string.no) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .setPositiveButton(R.string.yes) { dialog, _ ->
                                    lifecycleOwner.lifecycleScope.launch {
                                        recordViewModel.deleteRecord(record)
                                        dialog.dismiss()
                                    }
                                }.show()
                        }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ItemFolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun replaceRecordInfo(record: List<Record>) {
        submitList(record)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}