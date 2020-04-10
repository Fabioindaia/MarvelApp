package com.uolinc.marvelapp.ui.characterlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uolinc.marvelapp.R
import com.uolinc.marvelapp.ui.characterlist.presentation.CharacterPresentation

class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imgCharacter: AppCompatImageView = itemView.findViewById(R.id.img_character)
    private val txtName: TextView = itemView.findViewById(R.id.txt_name)
    private val txtDescription: TextView = itemView.findViewById(R.id.txt_description)

    fun bindTo(row: CharacterPresentation?, action: (CharacterPresentation) -> Unit) {
        itemView.run {
            Picasso.get()
                    .load(row?.imageUrl)
                    .into(imgCharacter)
            txtName.text = row?.name
            txtDescription.text = row?.description

            setOnClickListener { action.invoke(row!!) }
        }
    }

    companion object {
        fun create(parent: ViewGroup): CharacterViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val view = inflate.inflate(R.layout.item_character, parent, false)
            return CharacterViewHolder(view)
        }
    }
}