package com.uolinc.marvelapp.ui.characterlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.facebook.drawee.view.SimpleDraweeView
import com.uolinc.marvelapp.R

import butterknife.BindView
import butterknife.ButterKnife
import com.uolinc.marvelapp.model.Result

class CharacterAdapter internal constructor(
        private val resultList: List<Result>,
        private val context: Context
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private val VIEW_TYPE_LOADING = 0
    private var totalRecord: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): CharacterViewHolder {
        return if (i == VIEW_TYPE_LOADING) {
            LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_loading, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_character, parent, false))
        }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.onBind(resultList[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        val VIEW_TYPE_ITEM = 1
        return if (position == resultList.size - 1 && position < totalRecord - 1) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int = resultList.size

    /**
     * Seta a quantidade total de registros da página
     *
     * @param totalRecord total de registros
     */
    internal fun setTotalRecord(totalRecord: Int) {
        this.totalRecord = totalRecord
    }

    internal inner class ViewHolder(view: View) : CharacterViewHolder(view) {

        private val imgCharacter: SimpleDraweeView
        private val txtName: TextView
        private val txtDescription: TextView

        init {
            imgCharacter = view.findViewById(R.id.imgCharacter)
            txtName = view.findViewById(R.id.txtName)
            txtDescription = view.findViewById(R.id.txtDescription)
        }

        override fun clear() {

        }

        override fun onBind(resultRow: Result, position: Int) {
            super.onBind(resultRow, position)
            val urlImage = "${resultRow.thumbnail!!.path}.${resultRow.thumbnail!!.extension}"

            itemView.run {
                imgCharacter.setImageURI(urlImage)
                txtName.setText(resultRow.name)
                txtDescription.setText(resultRow.description)

                setOnClickListener { v: View ->
                    val view = context as MainContrato.View
                    view.showCharacterDetailActivity(resultRow, urlImage)
                }
            }
        }
    }

    inner class LoadingViewHolder internal constructor(view: View) : CharacterViewHolder(view) {

        @BindView(R.id.progressBarLoading)
        internal var progressBarLoading: ProgressBar? = null

        init {
            ButterKnife.bind(this, view)
        }

        override fun clear() {

        }
    }

    abstract inner class CharacterViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        protected abstract fun clear()

        open fun onBind(resultRow: Result, position: Int) {
            clear()
        }
    }
}
