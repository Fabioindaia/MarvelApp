package com.uolinc.marvelapp.ui.characterlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.uolinc.marvelapp.R
import com.uolinc.marvelapp.network.NetworkState
import com.uolinc.marvelapp.network.Status

class NetworkStateViewHolder(
        itemView: View,
        private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val pageLoad: ProgressBar = itemView.findViewById(R.id.pb_loading)
    private val buttonRetry: AppCompatButton = itemView.findViewById(R.id.btn_retry)

    fun bindTo(networkState: NetworkState?) {
        itemView.run {
            buttonRetry.visibility = if (networkState?.status == Status.FAILED)
                View.VISIBLE else View.GONE
            pageLoad.visibility = if (networkState?.status == Status.LOADING)
                View.VISIBLE else View.GONE

            buttonRetry.setOnClickListener { retryCallback() }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            return NetworkStateViewHolder(view, retryCallback)
        }
    }
}