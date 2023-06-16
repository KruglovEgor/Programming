package server.commands

import server.listOfHumanBeing
import server.base_classes.HumanBeing
import server.data_base.updateData
import server.data_base.workWithDataInDB
import server.exceptions.NoSuchIdException
import java.sql.Connection
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.stream.Collectors

class UpdateCommand(val connection: Connection) : Command {

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
        var success = false
        var message = "No such unit"
        var result = "Error: $message"
        val id = (map["id"] as Number).toInt()
        val creator = map["creator"].toString()
        if (checkIfIdExists(id)){
            try {
                listOfHumanBeing.stream()
                    .filter{(it.id == id) and (it.creator == creator)}
                    .findFirst()
                    .ifPresent{
                        val oldUnitParams = it.makeMapByUnit()
                        it.setNewParameters(map)
                        val updatedInDB = workWithDataInDB("update", it, connection)
                        if (updatedInDB.success) {
                            success = true
                            message =""
                            result="Success"
                        } else{
                            it.setNewParameters(oldUnitParams)
                            success = false
                            message = "Problem with updating data in DB"
                            result = "Error: $message"
                        }
                    }
            } catch (e: Exception){
                success = false
                message = e.message.toString()
                result = "Error $message"
            }
        }
        else {
            success = false
            //message = NoSuchIdException().message.toString()
            message = "Can't update this unit!"
            result = "Error $message"
        }

        return Result(success, message, result)
    }

}