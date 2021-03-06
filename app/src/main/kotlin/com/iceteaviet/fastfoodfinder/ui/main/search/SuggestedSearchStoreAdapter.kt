package com.iceteaviet.fastfoodfinder.ui.main.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.iceteaviet.fastfoodfinder.R
import com.iceteaviet.fastfoodfinder.data.remote.store.model.Store
import com.iceteaviet.fastfoodfinder.ui.main.ItemTouchHelperAdapter
import com.iceteaviet.fastfoodfinder.ui.main.ItemTouchHelperViewHolder
import kotlinx.android.synthetic.main.item_recently_location.view.*
import java.util.*

/**
 * Created by MyPC on 11/16/2016.
 */
class SuggestedSearchStoreAdapter internal constructor() : RecyclerView.Adapter<SuggestedSearchStoreAdapter.RecentlySearchStoreViewHolder>(), ItemTouchHelperAdapter {
    private val storeList: MutableList<Store>
    private var mOnItemClickListener: OnItemClickListener? = null

    init {
        storeList = ArrayList()
    }

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): RecentlySearchStoreViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_suggestion, parent, false)
        return RecentlySearchStoreViewHolder(itemView)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        return false
    }

    override fun onItemDismiss(position: Int) {
        storeList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun addStores(destinations: List<Store>) {
        val pos = storeList.size
        storeList.addAll(destinations)
        notifyItemRangeInserted(pos, destinations.size)
    }

    fun addStore(destination: Store) {
        storeList.add(destination)
        notifyItemRangeInserted(storeList.size, 1)
    }

    fun setStores(destinations: List<Store>) {
        storeList.clear()
        storeList.addAll(destinations)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecentlySearchStoreViewHolder, position: Int) {
        val store = storeList[position]
        holder.setData(store)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    interface OnItemClickListener {
        fun onClick(store: Store)
    }

    inner class RecentlySearchStoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {
        var txtTitle: TextView = itemView.tv_item_title
        var txtAddress: TextView = itemView.tv_item_address

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val store = storeList[position]
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onClick(store)
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

        fun setData(store: Store) {
            txtTitle.text = store.title
            txtAddress.text = store.address
        }
    }

}
