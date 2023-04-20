package commands

import listOfHumanBeing
import helping_functions.printResults

class RemoveHeadCommand(): Command {

    /**
     * Printing unit with the smallest id using printResults() then delete it
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
            val removeById = RemoveFirstCommand()
            val mapForStart = mapOf("1" to 1)
            if (listOfHumanBeing.size > 0) printResults(listOfHumanBeing[0])
            removeById.execute(mapForStart)
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}