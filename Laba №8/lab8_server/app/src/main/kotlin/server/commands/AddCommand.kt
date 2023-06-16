package server.commands

import server.listOfHumanBeing
import server.base_classes.HumanBeing
import server.data_base.workWithDataInDB
import java.sql.Connection

class AddCommand(private val connection: Connection) : Command {

    /**
     * Add new unit with parameters from mapWithParams: LinkedTreeMap<String, Any?>
     *
     * @param map - parameters of HumanBeing's unit
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        map.filterKeys { it != "id" }
        println(map)
        val unit = HumanBeing(map + ("id" to getId(connection)))
        println(unit)
        try {
            listOfHumanBeing.add(unit)
        } catch (e: Exception){
            return Result(false, e.message.toString(), "Error: ${e.message}")
        }
        val added = workWithDataInDB("add", unit,  connection)
        if (!added.success) listOfHumanBeing.removeIf { it.id == unit.id }

        return added
    }
}


fun getId(connection: Connection) : Int{
    val query = "SELECT nextval('collection_id_seq')"
    val statement = connection.prepareStatement(query)
    val resultSet = statement.executeQuery()
    if (resultSet.next()) {
        val id = resultSet.getInt(1)
        return id
    }
    return 0
}