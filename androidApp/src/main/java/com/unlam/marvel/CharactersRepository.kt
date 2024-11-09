package com.unlam.marvel

interface CharactersRepository {
    suspend fun getCharacters(timestamp: Long, md5: String): List<Character>
}