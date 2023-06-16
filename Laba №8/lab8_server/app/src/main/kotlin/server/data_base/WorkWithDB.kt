package server.data_base

import server.base_classes.HumanBeing
import server.commands.Result
import java.sql.Connection


fun workWithDataInDB(type: String, unit:HumanBeing, connection: Connection) : Result{
    val query = when(type){
        "add" -> addData(unit)
        "remove" -> removeData(unit)
        "update" -> updateData(unit)
        "clear" -> clearData(unit)
        else -> return Result(false,  "Unsupported type: $type", "Error: Unsupported type: $type")
    }
    val statement = connection.prepareStatement(query)
    try {
        statement.executeUpdate()
        return Result(true, "", "Success")
    } catch (e: Exception){
        return Result(false, e.message.toString(), "Error: ${e.message}")
    }
}


fun addData(unit: HumanBeing) : String{
    val fields = unit.getParametersForDB()
    var query = "INSERT INTO collection"
    query += "(${fields.keys.joinToString(", ")}) VALUES "
    query += "(${fields.values.joinToString(", ")})"
    return query

}


fun removeData(unit: HumanBeing): String {
    val id = unit.id
    return "DELETE FROM collection WHERE id=$id"
}


fun  updateData(unit: HumanBeing) : String{
    val fields = unit.getParametersForDB()
    var query = "UPDATE collection SET "
    query += fields.entries.joinToString(", ") { (key, value) -> "$key=$value" }
    query += " WHERE id=${unit.id}"
    return query
}


fun clearData(unit: HumanBeing):String{
    return "DELETE FROM collection WHERE creator='${unit.creator}'"
}
