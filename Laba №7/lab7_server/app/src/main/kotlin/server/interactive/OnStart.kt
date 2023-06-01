package server.interactive

import server.*
import server.base_classes.HumanBeing
import server.base_classes.getDescriptionOfHumanBeingFields
import java.sql.Connection
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.logging.Level


fun createCollection(connection: Connection) : ConcurrentLinkedDeque<HumanBeing> {
    val collection = ConcurrentLinkedDeque<HumanBeing>()
    val statement = connection.prepareStatement("SELECT * FROM collection")
    try {
        val resultSet = statement.executeQuery()
        val fields = getDescriptionOfHumanBeingFields()
        while (resultSet.next()){
            val unit = mutableMapOf<String, Any?>()
            for (i in fields){
                if (!i.key.startsWith("coordinate"))
                    unit[i.key] = convertToNeededType(resultSet.getString(i.key), i.value)
            }
            unit["coordinates"] = listOf(
                convertToNeededType(resultSet.getString("coordinate_x"), fields["coordinate_x"]),
                convertToNeededType(resultSet.getString("coordinate_y"), fields["coordinate_y"])
            )
            collection.add(HumanBeing(unit))
        }
    } catch (e: Exception){
        logger.log(Level.WARNING, "There is error with downloading data from db")
    }
    return collection
}