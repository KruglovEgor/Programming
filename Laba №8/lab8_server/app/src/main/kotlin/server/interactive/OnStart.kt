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
                unit[i.key] = convertToNeededType(resultSet.getString(i.key), i.value)
            }
            collection.add(HumanBeing(unit))
        }
    } catch (e: Exception){
        logger.log(Level.WARNING, "There is error with downloading data from db")
    }
    return collection
}