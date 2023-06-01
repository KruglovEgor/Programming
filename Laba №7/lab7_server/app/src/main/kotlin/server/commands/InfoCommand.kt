package server.commands

import server.dateOfInitialization
import server.listOfHumanBeing

class InfoCommand() : Command {

    /**
     * Get information about collection
     *
     * @return String with information about collection
     */
    private fun getInfo():String{
        val txt = """
            Class of collection: ${listOfHumanBeing.javaClass}
            Count of units: ${listOfHumanBeing.size}
            Date of initialization: $dateOfInitialization
            """.trimIndent()
        return txt
    }

    /**
     * Printing information about collection using printResult()\
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        var result: String
        try {
            result = getInfo()
        } catch (e: Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }
}
