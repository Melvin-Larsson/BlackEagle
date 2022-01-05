package com.inglarna.blackeagle.ui.cardlist

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SimpleItemTouchHelperCallback: ItemTouchHelper.Callback() {

    var touchHelperAdapter = ItemTouchHelperAdapter {_,_->}
    var clearViewCallback = ClearViewCallback {  }

    fun interface ClearViewCallback{
        fun onClearView()
    }
    fun interface ItemTouchHelperAdapter{
        fun onItemMove(fromPosition: Int, toPosition: Int)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun getMovementFlags(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
        touchHelperAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        clearViewCallback.onClearView()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

}