package server.commands

import server.currentId
import server.listOfData
import server.listOfHumanBeing
import server.base_classes.HumanBeing


class AddCommand() : Command {

    /**
     * Add new unit with parameters from mapWithParams: LinkedTreeMap<String, Any?>
     *
     * @param map - parameters of HumanBeing's unit
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {

        var success = true
        var message = ""
        var result: String
        try {
            val unit = HumanBeing(map.filterKeys { it != "id" })
            currentId += 1
            listOfData.add(unit.makeMapByUnit())
            listOfHumanBeing.add(unit)
            result = "Success"
        } catch (e: Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"

        }
        return Result(success, message, result)
    }
}