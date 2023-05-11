package commands

import listOfData
import listOfHumanBeing
import base_classes.HumanBeing
import exceptions.NoSuchIdException
import helping_functions.pullInDataToSend

class RemoveByIdCommand(): Command {

    /**
     * Check if id exists then remove
     *
     * @return 'true' if unit with such id exists (delete him) and 'false' if not
     */
    private fun  checkIfIdExistsThenRemove(id: Int) :Boolean{
//        for(unit : HumanBeing in listOfHumanBeing){
//            if(unit.id == id) {
//                listOfHumanBeing.remove(unit)
//                return true
//            }
//        }
//        return false
        return listOfHumanBeing.removeIf { it.id == id }
    }

    /**
     * Remove unit by its id
     *
     * @param map with 1 pair ('id' : 'value')
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        val id = (map["id"] as Number).toInt()
        val removed = checkIfIdExistsThenRemove(id)
        if (removed){
            try {
               listOfData.removeIf { it["id"] == id }
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