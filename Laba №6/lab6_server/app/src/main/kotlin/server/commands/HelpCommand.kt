package commands


class HelpCommand() : Command {

    /**
     * Get commands description
     *
     * @return String with all available commands with their description
     */
    private fun getHelp():String{
        val inputStream = object {}.javaClass.getResourceAsStream("/Help.txt")
        val dataFromFile = inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        inputStream.close()
        return dataFromFile
    }

    /**
     * Printing all available commands with their description using printResult()
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>): Result {
        var success = true
        var message = ""
        var result: String
        try {
            result = getHelp()
        } catch (e: Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }
}