package com.won.travelreviewpj.record.diary.map

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ADDRESS
import com.won.travelreviewpj.common.FOLDER_ID
import com.won.travelreviewpj.common.MAPX
import com.won.travelreviewpj.common.MAPY
import com.won.travelreviewpj.common.RECORD_ID
import com.won.travelreviewpj.common.TITLE
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.common.toCoordinate
import com.won.travelreviewpj.databinding.FragmentRecordDiaryMapBinding
import com.won.travelreviewpj.record.diary.update.RecordDiaryUpdateFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Record diary map fragment
 *
 * 방문한 여행지 위치 설정
 */
class RecordDiaryMapFragment :
    ViewBindingBaseFragment<FragmentRecordDiaryMapBinding>(FragmentRecordDiaryMapBinding::inflate),
    OnMapReadyCallback, OnSearchResultListener {

    private lateinit var naverMap: NaverMap
    private val repository = MapRetrofitManager()
    private var searchItem: SearchItem? = null
    private var marker: Marker? = null
    private val compositeDisposable = CompositeDisposable()

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
        items.firstOrNull()?.let { item ->
            searchItem = item
            val latLng =
                LatLng(toCoordinate(item.mapy.toDouble()), toCoordinate(item.mapx.toDouble()))
            val cameraUpdate = CameraUpdate.scrollTo(latLng)
            naverMap.moveCamera(cameraUpdate)
            marker?.map = null
            marker = Marker().apply {
                position = latLng
                map = naverMap
            }
        }
    }

    override fun onError(e: Throwable) {}

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
        compositeDisposable.add(
            binding.mbRecordDiaryMap.clicks()
                .subscribe {
                    searchItem?.let { item ->
                        val cleanTitle =
                            Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY).toString()
                        val folderId = arguments?.getString(FOLDER_ID) ?: ""
                        val recordId = arguments?.getString(RECORD_ID)

                        val bundle = Bundle()
                        with(bundle) {
                            putString(FOLDER_ID, folderId)
                            putString(RECORD_ID, recordId)
                            putString(TITLE, cleanTitle)
                            putString(ADDRESS, item.address)
                            putString(MAPX, item.mapx)
                            putString(MAPY, item.mapy)
                        }

                        val fragment = RecordDiaryUpdateFragment()
                        fragment.arguments = bundle

                        findNavController().navigate(
                            R.id.action_recordDiaryMapFragment_to_recordDiaryUpdateFragment,
                            bundle
                        )
                    }
                }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}