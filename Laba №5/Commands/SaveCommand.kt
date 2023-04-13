package Commands

import HelpingFunctions.writeInJSONFile
import listOfHumanBeing
import pathToCollection
import java.util.*

class SaveCommand() : Command{

    /**
     * Save listOfHumanBeing in file.json (path to it is pathToCollection)
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            val list = LinkedList<Map<String, Any?>>()
            for (unit in listOfHumanBeing){ list.add(unit.makeMapByUnit())}
            writeInJSONFile(pathToCollection, list)
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}