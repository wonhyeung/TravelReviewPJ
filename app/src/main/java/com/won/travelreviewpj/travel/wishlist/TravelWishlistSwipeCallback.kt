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
/**
 * Travel wishlist swipe callback
 *
 * RecyclerView 에 item Swipe 하여 삭제
 */
class TravelWishlistSwipeCallback : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    /**
     * Get movement flags
     * 왼쪽 , 오른쪽 이동
     */
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

    /**
     * Clear view
     * swipe가 취소 되거나 완료 되었을 때 item 원래 위치로 이동
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))
        previousPosition = viewHolder.absoluteAdapterPosition
    }


    /**
     * On selected changed
     * item 선택 상태가 변경될 때 호출
     */
    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.absoluteAdapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    /**
     * Get swipe escape velocity
     * 최대 속도
     */
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    /**
     * Get swipe threshold
     * 손을 떼면 호출
     */
    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 2f
    }

    /**
     * On child draw
     * swipe 하여 뷰에 변화가 생길 경우 호출
     */
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
                return
            }
            currentDx = x
            getDefaultUIUtil().onDraw(
                c, recyclerView, view, dX, dY, actionState, isCurrentlyActive
            )
        }
    }

    /**
     * Clamp view position horizontal
     * item swipe 할 때 삭제 icon 화면에 보이게 고정
     */
    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ): Float {
        /**
         * 오른쪽 방향으로 swipe 막기
         */
        val min: Float = -view.width.toFloat() / 2
        val max = 0f

        /**
         * view가 고정되었을 때 swipe 되는 영역 제한
         */
        val x = if (isClamped) {
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return min(max(min, x), max)
    }

    /**
     * Set tag
     * isClamp를 view의 tag로 관리
     * isClamp = true : 고정 , false : 고정 해제
     */
    private fun setTag(viewHolder: ViewHolder, isClamped: Boolean) { viewHolder.itemView.tag = isClamped }
    private fun getTag(viewHolder: ViewHolder): Boolean { return viewHolder.itemView.tag as? Boolean ?: false }

    private fun getView(viewHolder: ViewHolder): View {
        return (viewHolder as TravelWishlistAdapter.ItemHolder).itemView.findViewById(R.id.ll_item_travel_wishlist)
    }

    /**
     * Set clamp
     * view가 swipe 되었을 때 고정될 크기 설정
     */
    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    /**
     * Remove previous clamp
     * 다른 view가 swipe 되거나 터치되면 고정 해제
     */
    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition)
            return
        /**
         * 이전에 선택한 위치의 view 고정 해제
         */
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }

}