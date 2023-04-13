package Commands

import BaseClasses.HumanBeing
import currentId
import listOfData
import listOfHumanBeing

class AddCommand() : Command{

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
            val unit = HumanBeing(map)
            currentId += 1
            listOfData.add(unit.makeMapByUnit())
            listOfHumanBeing.add(unit)
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }

        return Result(success, message)
    }
}