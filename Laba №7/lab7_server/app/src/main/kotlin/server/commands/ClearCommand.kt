package server.commands

import server.base_classes.HumanBeing
import server.data_base.workWithDataInDB
import server.listOfHumanBeing
import java.sql.Connection

class ClearCommand(val connection: Connection): Command {

    /**
     * Make server.getListOfData and server.getListOfHumanBeing empty
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>): Result {
        try {
            workWithDataInDB("clear", HumanBeing(map), connection)
            listOfHumanBeing.removeIf { it.creator == map["creator"] }
            return Result(true, "", "Success")
        } catch (e: Exception) {
            return Result(false, e.message.toString(), "Error: ${e.message}")
        }
    }
}