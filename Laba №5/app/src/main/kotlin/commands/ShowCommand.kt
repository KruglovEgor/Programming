package commands

import listOfHumanBeing
import helping_functions.printResults

class ShowCommand() : Command {

    /**
     * Get collection as String
     *
     * @return String of units in our collection
     */
    private fun getShow():String{
        val txt = listOfHumanBeing.toString()
        return txt
    }

    /**
     * Printing units in our collection using printResult()
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            printResults(getShow())
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }

        return Result(success, message)
    }
}