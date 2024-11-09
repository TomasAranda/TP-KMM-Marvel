package ar.edu.unlam.marvel

import androidx.recyclerview.widget.RecyclerView
import ar.edu.unlam.marvel.databinding.ListItemCharacterBinding
import com.squareup.picasso.Picasso
import ar.edu.unlam.shared.Character

class CharacterViewHolder(private val binding: ListItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(character: Character) {
        binding.name.text = character.name
        binding.description.text = character.description
        if (character.thumbnailUrl.isNotEmpty()) {
            Picasso.get()
                .load(character.thumbnailUrl)
                .into(binding.image)
        } else {
            binding.image.setImageURI(null)
        }
    }

}
