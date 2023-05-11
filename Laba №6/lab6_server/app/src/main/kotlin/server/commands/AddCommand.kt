package commands

import currentId
import listOfData
import listOfHumanBeing
import base_classes.HumanBeing
import helping_functions.pullInDataToSend


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
        try {
            val unit = HumanBeing(map.filterKeys { it != "id" })
            currentId += 1
            listOfData.add(unit.makeMapByUnit())
            listOfHumanBeing.add(unit)
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }
        if(success){
            pullInDataToSend("Success")
        } else {
            pullInDataToSend("Error $message")
        }
        return Result(success, message)
    }
}