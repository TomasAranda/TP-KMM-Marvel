package ar.edu.unlam.marvel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.edu.unlam.shared.CharactersService
import ar.edu.unlam.shared.DatabaseDriverFactory

class CharactersViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val charactersService = CharactersService(DatabaseDriverFactory(context))
        return CharactersViewModel(charactersService) as T
    }
}