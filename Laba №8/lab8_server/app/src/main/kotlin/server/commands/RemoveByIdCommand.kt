package server.commands

import server.data_base.workWithDataInDB
import server.listOfHumanBeing
import java.sql.Connection


//todo maybe add more information about exceptions
class RemoveByIdCommand(val connection: Connection): Command {

    /**
     * Check if id exists then remove
     *
     * @return 'true' if unit with such id exists (delete him) and 'false' if not
     */
    private fun  removeByIdAndCreator(id: Int, creator: String) :Boolean{
        var removedInDB = false
        listOfHumanBeing.stream()
            .filter{ (it.id == id) and (it.creator == creator)}
            .findFirst()
            .ifPresent {
                removedInDB = (workWithDataInDB("remove", it, connection).success)
            }
        if (removedInDB) {
            listOfHumanBeing.removeIf { it.id == id }
            return true
        }
        else return false
    }

    /**
     * Remove unit by its id
     *
     * @param map with 1 pair ('id' : 'value')
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        val id = (map["id"] as Number).toInt()
        val creator = map["creator"].toString()
        val removed = removeByIdAndCreator(id, creator)
        if (removed){
                return Result(true, "", "Success")
        }
        else {
            return Result(false, "We didn't manage to delete unit", "Error: We didn't manage to delete unit")
        }
    }

}