package com.uolinc.marvelapp.ui.characterlist.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.uolinc.marvelapp.R
import com.uolinc.marvelapp.network.NetworkState
import com.uolinc.marvelapp.ui.characterlist.presentation.CharacterPresentation

class CharactersAdapter(
        private val retryCallback: () -> Unit,
        private val clickAction: (CharacterPresentation) -> Unit
) : PagedListAdapter<CharacterPresentation, RecyclerView.ViewHolder>(unitDiff) {

    private var networkState: NetworkState? = null
    private val pageSize = 40

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_character -> CharacterViewHolder.create(parent)
            R.layout.item_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_character -> (holder as CharacterViewHolder).bindTo(
                    getItem(position),
                    clickAction
            )
            R.layout.item_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_character
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED && networkState != NetworkState.EMPTY
    }

    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newNetworkState the new network state
     */
    fun setNetworkState(newNetworkState: NetworkState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    companion object {
        val unitDiff = object : DiffUtil.ItemCallback<CharacterPresentation>() {
            override fun areItemsTheSame(oldItem: CharacterPresentation, newItem: CharacterPresentation): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: CharacterPresentation, newItem: CharacterPresentation): Boolean {
                return oldItem == newItem
            }
        }
    }
}