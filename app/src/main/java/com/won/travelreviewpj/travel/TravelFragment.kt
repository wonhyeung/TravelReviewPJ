package com.won.travelreviewpj.travel

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding4.view.clicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.THROTTLE_TIME
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.databinding.FragmentTravelBinding
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


/**
 * Travel fragment
 *
 * 여행지 추천
 */
class TravelFragment : ViewBindingBaseFragment<FragmentTravelBinding>(FragmentTravelBinding::inflate) {

    private val viewModel: TravelViewModel by viewModels()
    private var currentTravelEntity: TravelEntity? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTravelData()
        fieldSetup()
    }

    private fun fieldSetup() {

        val dp = 1024
        val metrics: DisplayMetrics = resources.displayMetrics
        val px: Int = (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.travelData.collect { travelEntity ->
                    currentTravelEntity = travelEntity
                    binding.apply {
                        tvTravelDestination.text = travelEntity?.travelTitle
                        tvTravelLocation.text = travelEntity?.travelAddress
                        Glide.with(requireContext())
                            .load(travelEntity?.travelImage)
                            .centerCrop()
                            .override(px, px)
                            .placeholder(R.drawable.image_basic)
                            .into(ivTravel)

                        with(bsTravel) {
                            tvBottomTravelOverview.text = travelEntity?.travelOverview
                            Glide.with(requireContext())
                                .load(travelEntity?.travelImage)
                                .centerCrop()
                                .override(px, px)
                                .placeholder(R.drawable.image_basic)
                                .into(ivBottomTravel)
                        }
                    }
                }
            }
        }
        with(binding) {
            compositeDisposable.add(
                Observable.merge(
                    ibTravelWish.clicks().throttleFirst(THROTTLE_TIME, TimeUnit.MILLISECONDS),
                    bsTravel.ibBottomTravelWish.clicks().throttleFirst(THROTTLE_TIME, TimeUnit.MILLISECONDS)
                ).observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        dialogShow()
                    }
            )
            compositeDisposable.add(
                ibTravelWishlist.clicks()
                    .subscribe {
                        val action =
                            TravelFragmentDirections.actionFragmentTravelToFragmentTravelWishlist()
                        findNavController().navigate(action)
                    }
            )

            mbTravelChange
                .clicks()
                .observeOn(Schedulers.io())
                .throttleFirst(THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewModel.getResetData() }

        }
    }

    private fun dialogShow() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.save_wishlist)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.insertTravelWishlist(
                    TravelWishlist(
                        wishlistTitle = currentTravelEntity?.travelTitle ?: "",
                        wishlistAddress = currentTravelEntity?.travelAddress ?: "",
                        wishlistImage = currentTravelEntity?.travelImage ?: "",
                        wishlistOverview = currentTravelEntity?.travelOverview ?: ""
                    )
                )
                viewModel.getResetData()
            }.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}



