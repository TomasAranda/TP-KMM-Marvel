# TP KMM Marvel

Trabajo práctico de la materia **Taller de Programación** de la Universidad Nacional de La Matanza

Se trata de una app simple que lista datos de personajes de Marvel. El objetivo era migrar de una app android a una app multiplataforma utilizando [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)

Ademas se utilizan las siguientes tecnologias:
- [Ktor](https://ktor.io/): Para realizar llamadas http a la api de Marvel
- [SQLDelight](https://sqldelight.github.io/sqldelight/2.0.2/): Para implementar funcionalidad de cache on error guardando datos en SQLite local del dispositivo

> Para poder consumir la api de Marvel se requiere configurar las api keys agregandolas en el archivo [Constants.kt](shared/src/commonMain/kotlin/ar/edu/unlam/shared/Constants.kt). Las mismas se pueden obtener en el [sitio de la api](https://developer.marvel.com/).
