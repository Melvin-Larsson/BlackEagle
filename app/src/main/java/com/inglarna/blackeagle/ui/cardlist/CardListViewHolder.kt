package com.inglarna.blackeagle.ui.cardlist

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import com.inglarna.blackeagle.R


class CardListViewHolder(val binding: ListItemCardBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener,
    PopupMenu.OnMenuItemClickListener {

    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }


    override fun onClick(p0: View?) {
        /*
        inspektera och edit
         */
    }

    override fun onLongClick(v: View): Boolean {
        Toast.makeText(itemView.context, "long click", Toast.LENGTH_LONG).show()
        val popup = PopupMenu(itemView.context, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.card_long_click_menu)
        popup.show()
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.editCard-> editCard()
            R.id.deleteCard-> delete()
        }
        return true
    }

    private fun editCard() {
        Toast.makeText(itemView.context, "edit", Toast.LENGTH_LONG).show()

    }

    private fun delete(){
        Toast.makeText(itemView.context, "delete", Toast.LENGTH_LONG).show()
    }
}