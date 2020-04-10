package com.uolinc.marvelapp.ui.characterlist.mapper

import com.uolinc.marvelapp.ui.characterlist.presentation.CharacterPresentation
import com.uolinc.marvelapp.model.response.CharacterResponse

class CharacterMapper {

    fun fromResponse(response: CharacterResponse) = with(response) {
        val urlImage = if (thumbnail != null) {
            "${thumbnail.path}.${thumbnail.extension}"
        } else {
            ""
        }
        CharacterPresentation(name = name, description = description, imageUrl = urlImage)
    }
}