package commands

import listOfHumanBeing
import helping_functions.pullInDataToSend
import java.util.*

class ShowCommand() : Command {

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
            val printAscending = PrintAscendingCommand()
            printAscending.execute(mapOf("1" to 1))
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }

        return Result(success, message)
    }
}