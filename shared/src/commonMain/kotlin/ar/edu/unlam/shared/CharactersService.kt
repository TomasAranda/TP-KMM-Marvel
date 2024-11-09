package ar.edu.unlam.shared

import PRIVATE_KEY
import PUBLIC_KEY
import ar.edu.unlam.shared.HashGenerator.md5
import io.ktor.util.date.*
import okio.ByteString.Companion.encodeUtf8

class CharactersService(driverFactory: DatabaseDriverFactory){
    private val charactersRepository = CharacterRepositoryClient(driverFactory)

    suspend fun getCharacters(): List<Character> = try {
        val timestamp = getTimeMillis()
        val characters = charactersRepository.getAllCharacters(
            timestamp,
            md5(timestamp.toString() + PRIVATE_KEY + PUBLIC_KEY)
        )
        sort(characters.toModel())
    } catch (e: Exception) {
        // return cached results on api call failure
        charactersRepository.getCharactersFromCache()
    }

    private fun sort(characters: List<Character>): List<Character> {
        return characters.sortedWith(CharacterComparator())
    }

    private fun CharactersResponse.toModel(): List<Character> {
        return this.characters.list.map {
            Character(
                id = it.id,
                name = it.name,
                description = it.description,
                thumbnailUrl = it.thumbnail.toUrl()
            )
        }
    }

    /**
     * Los personajes se ordenan de la siguiente manera:
     * - Primero los que tienen descripción, y luego los que no.
     * - Los que tienen descripción a su vez se ordenan ascendentemente por su id.
     * - Los que NO tienen descripción se ordenan descendentemente por su id.
     */
    class CharacterComparator : Comparator<Character> {
        override fun compare(c1: Character, c2: Character): Int {
            if (c1.description.isEmpty() && c2.description.isEmpty()) {
                return c2.id.compareTo(c1.id)
            }
            if (c1.description.isNotEmpty() && c2.description.isNotEmpty()) {
                return c1.id.compareTo(c2.id)
            }
            if (c1.description.isNotEmpty() && c2.description.isEmpty()) {
                return -1
            }
            return 1
        }

    }
}
object HashGenerator {
    fun md5(string: String) : String {
        return try {
            val byteString = string.encodeUtf8()
            byteString.md5().hex()
        } catch (e: Exception) {
            ""
        }
    }
}
