package com.won.travelreviewpj.travel.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.THROTTLE_TIME
import com.won.travelreviewpj.databinding.ItemTravelWishlistBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Travel wishlist adapter
 *
 * @property travelWishlists 후에 갈 여행지
 */
class TravelWishlistAdapter(
    private var travelWishlists: MutableList<TravelWishlist>,
    private val viewModel: TravelWishlistViewModel
) : ListAdapter<TravelWishlist, TravelWishlistAdapter.ItemHolder>(diffUtil) {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TravelWishlist>() {

            override fun areItemsTheSame(oldItem: TravelWishlist, newItem: TravelWishlist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TravelWishlist, newItem: TravelWishlist): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ItemHolder(val binding: ItemTravelWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(travelWishlist: TravelWishlist) {
            travelWishlist.let {
                with(binding) {
                    tvItemTravelWishlistTitle.text = it.wishlistTitle
                    tvItemTravelWishlistLocation.text = it.wishlistAddress

                    Glide.with(root.context)
                        .load(it.wishlistImage)
                        .centerCrop()
                        .placeholder(R.drawable.image_basic)
                        .into(ivItemTravelWishlist)

                    compositeDisposable.add(
                        root.clicks()
                            .subscribe {
                                val action =
                                    TravelWishlistFragmentDirections.actionFragmentTravelWishlistToFragmentTravelWishlistDetail(
                                        travelWishlist.id
                                    )
                                root.findNavController().navigate(action)
                            }
                    )

                    ibItemTravelDelete.clicks()
                        .observeOn(Schedulers.io())
                        .throttleFirst(THROTTLE_TIME, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { viewModel.deleteTravelWishlist(travelWishlist.id) }

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(ItemTravelWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun replaceWishlistInfo(travelWishlist: List<TravelWishlist>) {
        submitList(travelWishlist)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}



