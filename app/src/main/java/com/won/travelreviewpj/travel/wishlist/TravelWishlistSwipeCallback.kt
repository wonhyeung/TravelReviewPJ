package com.won.travelreviewpj.travel.wishlist

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.won.travelreviewpj.R
import java.lang.Float.max
import java.lang.Float.min

class TravelWishlistSwipeCallback : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        return makeMovementFlags(0, LEFT or RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))
        previousPosition = viewHolder.absoluteAdapterPosition
    }


    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.absoluteAdapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 2f
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)

            if (x == -clamp) {
                getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
            }
            currentDx = x
            getDefaultUIUtil().onDraw(
                c, recyclerView, view, dX, dY, actionState, isCurrentlyActive
            )
        }
    }

    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ): Float {
        val min: Float = -view.width.toFloat() / 2
        // RIGHT 방향으로 swipe 막기
        val max: Float = 0f

        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return min(max(min, x), max)
    }


    private fun setTag(viewHolder: ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: ViewHolder): Boolean {
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    private fun getView(viewHolder: ViewHolder): View {
        return (viewHolder as TravelWishlistAdapter.ItemHolder).itemView.findViewById(R.id.ll_item_travel_wishlist)
    }

    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition)
            return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }

}