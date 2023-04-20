package commands

import listOfData
import listOfHumanBeing
import base_classes.HumanBeing
import exceptions.NoSuchIdException

class RemoveByIdCommand(): Command {

    /**
     * Check if id exists then remove
     *
     * @return 'true' if unit with such id exists (delete him) and 'false' if not
     */
    private fun  checkIfIdExistsThenRemove(id: Int) :Boolean{
        for(unit : HumanBeing in listOfHumanBeing){
            if(unit.id == id) {
                listOfHumanBeing.remove(unit)
                return true
            }
        }
        return false
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
        if (checkIfIdExistsThenRemove(map["id"] as Int)){
            try {
                for(unitParam in listOfData){
                    if(unitParam["id"]  == map["id"]){
                        listOfData.remove(unitParam)
                        break
                    }
                }
            } catch (e: Exception){
                success = false
                message = e.message.toString()
            }
        }
        else {
            success = false
            message = NoSuchIdException().message.toString()
        }

        return Result(success, message)
    }

}