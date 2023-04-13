package Commands


interface Command{
    fun execute(map: Map<String, Any?>): Result
}

class Result(
    val success: Boolean,
    val message: String
)

