package com.won.travelreviewpj.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.naver.maps.geometry.LatLng
import com.won.travelreviewpj.common.THROTTLE_TIME
import com.won.travelreviewpj.common.toCoordinate
import com.won.travelreviewpj.databinding.ItemTravelBinding
import com.won.travelreviewpj.record.diary.RecordDiary
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


/**
 * Map adapter
 *
 * @property recordItemLayout 각 아이템의 위도/경도를 맵으로 이동하기 위한 연결 정보
 * @property itemClicked 아이템 클릭 시 이벤트 람다 함수
 * 방문한 여행지 대한 세부 정보
 */
class MapAdapter(
    private val recordItemLayout: Int,
    private val itemClicked: (LatLng) -> Unit
) : ListAdapter<RecordDiary, MapAdapter.ItemHolder>(diffUtil) {

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
                    root.clicks()
                        .throttleFirst(THROTTLE_TIME, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val latLng = LatLng(
                                toCoordinate(recordDiary.mapy.toDouble()),
                                toCoordinate(recordDiary.mapx.toDouble())
                            )
                            itemClicked(latLng)
                        }
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

    fun replaceMapInfo(recordDiary: List<RecordDiary>) {
        submitList(recordDiary)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}