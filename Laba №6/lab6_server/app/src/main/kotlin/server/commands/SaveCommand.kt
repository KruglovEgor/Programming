package server.commands

import server.helping_functions.writeInJSONFile
import server.listOfHumanBeing
import java.util.*
import server.listOfData

class SaveCommand() : Command {

    /**
     * Save server.getListOfHumanBeing in file.json (path to it is pathToCollection)
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
            val list = LinkedList<MutableMap<String, Any?>>()
            for (unit in listOfHumanBeing){ list.add(unit.makeMapByUnit())}
            writeInJSONFile("Data.json", list)
            listOfData = list
            result = "Success"
        } catch (e : Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }

        return Result(success, message, result)
    }
}