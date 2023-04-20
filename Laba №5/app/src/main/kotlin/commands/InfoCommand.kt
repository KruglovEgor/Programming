package commands

import dateOfInitialization
import listOfHumanBeing
import helping_functions.printResults

class InfoCommand() : Command {

    /**
     * Get information about collection
     *
     * @return String with information about collection
     */
    private fun getInfo():String{
        val txt = """
            Class of collection: LinkedList
            Count of units: ${listOfHumanBeing.size}
            Date of initialization: ${dateOfInitialization}
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
        try {
            printResults(getInfo())
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}
