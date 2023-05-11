package commands

import listOfData
import listOfHumanBeing


class ClearCommand(): Command {

    /**
     * Make listOfData and listOfHumanBeing empty
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
            listOfData.clear()
            listOfHumanBeing.clear()
            result = "Success"
        } catch (e: Exception){
            success = false
            message = e.message.toString()
            result ="Error $message"
        }
        return Result(success, message, result)
    }

}