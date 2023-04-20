package commands

import listOfHumanBeing
import base_classes.HumanBeing
import exceptions.NoSuchIdException

class UpdateCommand() : Command {

    /**
     * Check if id exists
     *
     * @return 'true' if unit with such id exists and 'false' if not
     */
    private fun  checkIfIdExists(id: Int) :Boolean{
        for(unit : HumanBeing in listOfHumanBeing){
            if(unit.id == id) {
                return true
            }
        }
        return false
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
        if (checkIfIdExists(map["id"] as Int)){
            try {


                for (unit : HumanBeing in listOfHumanBeing){
                    if(unit.id == map["id"]){
                        val newUnit = HumanBeing(map)
                        listOfHumanBeing.add(newUnit)
                        listOfHumanBeing.remove(unit)
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