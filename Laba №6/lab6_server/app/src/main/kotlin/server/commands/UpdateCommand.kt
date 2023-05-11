package commands

import listOfHumanBeing
import base_classes.HumanBeing
import exceptions.NoSuchIdException
import helping_functions.pullInDataToSend
import java.util.LinkedList
import java.util.stream.Collector
import java.util.stream.Collectors

class UpdateCommand() : Command {

    /**
     * Check if id exists
     *
     * @return 'true' if unit with such id exists and 'false' if not
     */
    private fun  checkIfIdExists(id: Int) :Boolean{
        return listOfHumanBeing.stream().anyMatch { it.id == id}
    }

    /**
     * Update parameters of unit using mapWithParams: LinkedTreeMap<String, Any?>
     *
     * @param map - parameters of HumanBeing's unit
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        val id = (map["id"] as Number).toInt()
        if (checkIfIdExists(id)){
            try {
//                for (unit : HumanBeing in listOfHumanBeing){
//                    if(unit.id == map["id"]){
//                        val newUnit = HumanBeing(map)
//                        listOfHumanBeing.add(newUnit)
//                        listOfHumanBeing.remove(unit)
//                        break
//                    }
//                }
                val newUnit = HumanBeing(map)
                listOfHumanBeing = listOfHumanBeing.stream()
                    .map { if (it.id == id) newUnit else it}
                    .collect(Collectors.toCollection { LinkedList<HumanBeing>() })
            } catch (e: Exception){
                success = false
                message = e.message.toString()
            }
        }
        else {
            success = false
            message = NoSuchIdException().message.toString()
        }
        if(success){
            pullInDataToSend("Success")
        } else {
            pullInDataToSend("Error $message")
        }
        return Result(success, message)
    }

}