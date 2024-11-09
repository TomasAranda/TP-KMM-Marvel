package ar.edu.unlam.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform