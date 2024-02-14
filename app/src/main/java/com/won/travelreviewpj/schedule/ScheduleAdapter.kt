package com.won.travelreviewpj.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.won.travelreviewpj.databinding.ItemTravelBinding
import com.won.travelreviewpj.record.diary.RecordDiary

/**
 * Schedule adapter
 *
 * @property recordItemLayout 여행지 이미지, 여행지, 주소
 * 방문한 여행지 날짜 확인
 */
class ScheduleAdapter(
    private val recordItemLayout: Int,
) : ListAdapter<RecordDiary, ScheduleAdapter.ItemHolder>(diffUtil) {

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

    inner class ItemHolder(val binding: ItemTravelBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recordDiary: RecordDiary) {
            recordDiary.let {
                with(binding) {
                    Glide.with(root.context)
                        .load(it.image)
                        .centerCrop()
                        .into(ivItemTravelWishlist)
                    tvItemTravelWishlistTitle.text = it.name
                    tvItemTravelWishlistAddress.text = it.address
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ItemTravelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun replaceScheduleInfo(recordDiary: List<RecordDiary>) {
        submitList(recordDiary)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}