package Commands

import HelpingFunctions.printResults
import listOfHumanBeing

class PrintAscendingCommand() : Command{

    /**
     * Printing all units in order by its id (increasing)
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            listOfHumanBeing.sort()
            printResults(listOfHumanBeing)
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}