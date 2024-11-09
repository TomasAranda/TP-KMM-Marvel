package ar.edu.unlam.shared

class Character(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnailUrl: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Character

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (thumbnailUrl != other.thumbnailUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + thumbnailUrl.hashCode()
        return result
    }
}