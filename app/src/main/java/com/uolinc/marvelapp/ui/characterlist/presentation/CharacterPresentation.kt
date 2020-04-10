package com.uolinc.marvelapp.ui.characterlist.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterPresentation (
    var name: String = "",
    var description: String = "",
    var imageUrl: String = ""
) : Parcelable