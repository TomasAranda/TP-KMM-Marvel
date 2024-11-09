package ar.edu.unlam.shared

import PUBLIC_KEY
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class CharacterRepositoryClient(driverFactory: DatabaseDriverFactory) {

    private val charactersDB = MarvelDatabase(driverFactory.createDriver())
    private val query: CharactersDBQueries = charactersDB.charactersDBQueries

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Logging) {
            level = LogLevel.INFO
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "HttpClient", message = message)
                }
            }
            logger
        }
    }

    suspend fun getAllCharacters(timestamp: Long, md5: String): CharactersResponse {
        val charactersResponse = httpClient.get {
            url("https://gateway.marvel.com/v1/public/characters")
            parameter("apikey", value = PUBLIC_KEY)
            parameter("ts", timestamp)
            parameter("hash", md5)
        }.body<CharactersResponse>()

        return charactersResponse.also {
            // refresh cache on successful api response
            deleteAllCharacters()
            insertCharacters(it.characters.list.mapToCharacterDBModel())
        }
    }

    fun getCharactersFromCache(): List<Character> {
        return query.getAllCharacters(::mapToCharacter).executeAsList()
    }

    private fun insertCharacters(characters: List<Character>) {
        charactersDB.transaction {
            for (character in characters)
                query.insertCharacter(
                    character.id,
                    character.name,
                    character.description,
                    character.thumbnailUrl
                )
        }
    }

    private fun deleteAllCharacters() {
        query.deleteAllCharacters()
    }

    private fun mapToCharacter(
        id: Long,
        name: String,
        description: String?,
        thumbnailUrl: String?
    ): Character {
        return Character(id, name, description ?: "", thumbnailUrl ?: "")
    }
}

private fun List<CharacterResult>.mapToCharacterDBModel(): List<Character> = map {
    Character(it.id, it.name, it.description, it.thumbnail.toUrl())
}
