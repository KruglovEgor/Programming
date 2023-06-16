package server.commands

import server.helping_functions.convertListOfMapsToJson
import server.listOfHumanBeing

class PrintAscendingCommand() : Command {

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
        var result: String
        try {
            val list = mutableListOf<Map<String, Any?>>()
            for(i in listOfHumanBeing){
                list.add(i.makeMapByUnit())
            }
            result = convertListOfMapsToJson(list)
        } catch (e : Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }
}