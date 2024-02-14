package com.won.travelreviewpj.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentMapBinding
import com.won.travelreviewpj.record.diary.RecordDiary

/**
 * Map fragment
 *
 * 방문한 여행지 위치 확인
 */
class MapFragment : ViewBindingBaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate) {

    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var mapAdapter: MapAdapter
    private lateinit var naverMap: NaverMap
    private var marker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapSetting()
        observeDiaries()
        notifyDiary()
    }

    private fun mapSetting() {
        val mapFragment =
            childFragmentManager.findFragmentById(binding.mapFragment.id) as MapFragment?
        mapFragment?.getMapAsync { naverMap ->
            this.naverMap = naverMap
        }
    }

    private fun notifyDiary(diaries: List<RecordDiary> = emptyList()) {
        mapAdapter = MapAdapter(R.layout.item_travel) { latLng ->
            naverMap.moveCamera(CameraUpdate.scrollTo(latLng))

            marker?.map = null
            marker = Marker().apply {
                position = latLng
                map = naverMap
            }
        }
        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        binding.also{
            with(it.rvMap){
                layoutManager = manager
                mapAdapter.replaceMapInfo(diaries)
                adapter = mapAdapter
            }
        }
    }
    private fun observeDiaries() {
        mapViewModel.allRecordDiary.observe(viewLifecycleOwner) { allDiaries ->
            mapAdapter.replaceMapInfo(allDiaries)
        }
    }
}