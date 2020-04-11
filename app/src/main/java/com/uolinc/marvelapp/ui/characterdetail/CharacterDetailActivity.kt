package com.uolinc.marvelapp.ui.characterdetail

import android.os.Bundle
import android.transition.Explode
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso
import com.uolinc.marvelapp.R
import com.uolinc.marvelapp.ui.characterlist.presentation.CharacterPresentation

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var imgCharacter: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)
        initialize()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Explode()
        }
    }

    /**
     * Volta para a tela anterior ao clicar no botão
     *
     * @param menuItem menu de itens
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     * Chama método para carregar os dados do personagem
     */
    private fun initialize() {
        toolbar = findViewById(R.id.toolbar)
        imgCharacter = findViewById(R.id.img_character)
        txtName = findViewById(R.id.tv_name)
        txtDescription = findViewById(R.id.tv_description)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        uploadData()
    }

    /**
     * Carrega os dados do personagem
     */
    private fun uploadData() {
        val character = intent.getParcelableExtra<CharacterPresentation>("character")!!
        with(character) {
            Picasso.get()
                    .load(imageUrl)
                    .into(imgCharacter)
            txtName.text = name
            txtDescription.text = description
        }
    }
}
