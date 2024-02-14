package com.won.travelreviewpj.travel.wishlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelWishlistBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

/**
 * Travel wishlist fragment
 *
 * 후에 갈 여행지 확인
 */
class TravelWishlistFragment :    ViewBindingBaseFragment<FragmentTravelWishlistBinding>(FragmentTravelWishlistBinding::inflate) {

    private val viewModel: TravelWishlistViewModel by viewModels()
    private lateinit var adapter: TravelWishlistAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetup()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun fieldSetup() {
        adapter = TravelWishlistAdapter(mutableListOf(), viewModel)
        val swipeCallback = TravelWishlistSwipeCallback().apply {
            setClamp(200f)
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTravelWishlist)

        val manager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        with(binding) {
            compositeDisposable.add(
                ibTravelWishlist.clicks()
                    .subscribe { findNavController().popBackStack() }
            )
            rvTravelWishlist.layoutManager = manager
            rvTravelWishlist.adapter = adapter

            rvTravelWishlist.setOnTouchListener { _, _ ->
                swipeCallback.removePreviousClamp(rvTravelWishlist)
                false
            }
        }

        viewModel.getAllTravelWishlist()?.let { flow ->
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    flow.collect { travelWishlist ->
                        adapter.replaceWishlistInfo(travelWishlist)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}

