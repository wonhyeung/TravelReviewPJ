package com.won.travelreviewpj.record.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.longClicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.databinding.ItemNotepadBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

/**
 * Record diary adapter
 *
 * @property recordItemLayout 여행지 이미지, 여행지, 주소
 * @property recordId  폴더 식별 정보
 * 방문한 여행지 확인
 */
class RecordDiaryAdapter(
    private val recordItemLayout: Int,
    private val recordId: String,
    private val recordViewModel: RecordDiaryViewModel,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<RecordDiary, RecordDiaryAdapter.ItemHolder>(diffUtil) {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecordDiary>() {
            override fun areItemsTheSame(oldItem: RecordDiary, newItem: RecordDiary): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecordDiary, newItem: RecordDiary): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class ItemHolder(var binding: ItemNotepadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recordDiary: RecordDiary) {
            recordDiary.let {
                with(binding) {
                    Glide.with(root.context)
                        .load(it.image)
                        .into(ivNotepadRecord)
                    tvNotepadRecord.text = it.name
                    tvNotepadAddress.text = it.address
                    compositeDisposable.add(
                        root.clicks()
                            .subscribe {
                                val action =
                                    RecordDiaryFragmentDirections.actionFragmentRecordDiaryToRecordDetailFragment(
                                        recordId, recordDiary.id
                                    )
                                root.findNavController().navigate(action)
                            }
                    )
                    root.longClicks()
                        .subscribe {
                            MaterialAlertDialogBuilder(root.context)
                                .setMessage(R.string.delete_diary)
                                .setNegativeButton(R.string.no) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .setPositiveButton(R.string.yes) { dialog, _ ->
                                    lifecycleOwner.lifecycleScope.launch {
                                        recordViewModel.deleteRecordDiary(
                                            recordId,
                                            recordDiary.id
                                        )
                                        dialog.dismiss()
                                    }
                                }
                                .show()
                        }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ItemNotepadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun replaceRecordDiaryInfo(recordDiary: List<RecordDiary>) {
        submitList(recordDiary)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}