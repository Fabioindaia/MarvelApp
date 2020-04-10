package com.uolinc.marvelapp.ui.characterlist.mapper

import com.uolinc.marvelapp.ui.characterlist.presentation.DataCharacterPresentation
import com.uolinc.marvelapp.model.response.DataResponse

class DataCharacterMapper {

    fun fromResponse(response: DataResponse) = with(response) {
        DataCharacterPresentation(
                characters = data.characters.map { CharacterMapper().fromResponse(it) }
        )
    }
}