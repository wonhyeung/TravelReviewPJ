package com.won.travelreviewpj.record.diary

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentRecordDiaryMapBinding
import com.won.travelreviewpj.record.update.RecordDiaryUpdateFragment

class RecordDiaryMapFragment :
    ViewBindingBaseFragment<FragmentRecordDiaryMapBinding>(FragmentRecordDiaryMapBinding::inflate),
    OnMapReadyCallback, OnSearchResultListener {

    private lateinit var naverMap: NaverMap
    private val repository = RecordDiaryMapRepository()
    private var searchItem: SearchItem? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetup()
        buttonClick()

    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
    }


    override fun onResult(items: List<SearchItem>) {
        Log.e("items", "$items")
        items.firstOrNull()?.let { item ->
            searchItem = item
            val latLng = LatLng(item.mapy.toDouble() / 1e6 / 10, item.mapx.toDouble() / 1e6 / 10)
            val cameraUpdate = CameraUpdate.scrollTo(latLng)
            Log.e("latlng", "$latLng")
            naverMap.moveCamera(cameraUpdate)

            val marker = Marker()
            marker.position = latLng
            marker.map = naverMap
        }
    }

    override fun onError(e: Throwable) {
    }

    fun searchPlaces(query: String?) {
        if (query != null) {
            repository.searchPlaces(query, this)
        }
    }

    private fun fieldSetup() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_record_diary) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_record_diary, it).commit()
            }

        mapFragment.getMapAsync(this)

        binding.sbRecordDiary.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchPlaces(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun buttonClick() {
        binding.mbRecordDiaryMap.setOnClickListener {
            searchItem?.let { item ->
                val cleanTitle = Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY).toString()
                val bundle = Bundle()
                bundle.putString("title",cleanTitle)
                bundle.putString("address", item.address)
                bundle.putString("mapx", item.mapx)
                bundle.putString("mapy", item.mapy)

                val recordId = arguments?.getString("recordId")
                bundle.putString("recordId", recordId)

                val fragment = RecordDiaryUpdateFragment()
                fragment.arguments = bundle

                findNavController().navigate(
                    R.id.action_recordDiaryMapFragment_to_recordDiaryUpdateFragment,
                    bundle
                )
            }
        }
    }

}