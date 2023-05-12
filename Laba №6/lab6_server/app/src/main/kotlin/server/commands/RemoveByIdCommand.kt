package server.commands

import server.listOfData
import server.listOfHumanBeing
import server.exceptions.NoSuchIdException

class RemoveByIdCommand(): Command {

    /**
     * Check if id exists then remove
     *
     * @return 'true' if unit with such id exists (delete him) and 'false' if not
     */
    private fun  checkIfIdExistsThenRemove(id: Int) :Boolean{
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
        var result: String
        val id = (map["id"] as Number).toInt()
        val removed = checkIfIdExistsThenRemove(id)
        if (removed){
            try {
                listOfData.removeIf { it["id"] == id }
                result = "Success"
            } catch (e: Exception){
                success = false
                message = e.message.toString()
                result = "Error $message"
            }
        }
        else {
            success = false
            message = NoSuchIdException().message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }

}