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

class CharacterRepositoryClient {

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
        val charactersResponse = httpClient.get{
                url("https://gateway.marvel.com/v1/public/characters")
                parameter("apikey", value = PUBLIC_KEY)
                parameter("ts", timestamp)
                parameter("hash", md5)
            }.body<CharactersResponse>()
        return charactersResponse
    }
}