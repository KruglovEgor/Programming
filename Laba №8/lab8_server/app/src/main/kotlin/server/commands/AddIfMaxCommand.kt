package server.commands

import server.listOfHumanBeing
import java.sql.Connection

class AddIfMaxCommand(val connection: Connection) : Command {

    /**
     * Add new unit with parameters from mapWithParams, if its impactSpeed is the biggest
     *
     * @param map with parameters of HumanBeing's unit
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        try {
            val maxImpactSpeed : Long = listOfHumanBeing.stream()
                .mapToLong {it.impactSpeed}
                .max()
                .orElse(-Long.MAX_VALUE + 1)
            if (map["impactSpeed"] as Long > maxImpactSpeed){
                val addCommand = AddCommand(connection)
                return addCommand.execute(map)
            }
            return Result(true, "", "NOTE: No such units")
        } catch (e : Exception){
            return  Result(false, e.message.toString(), "Error: ${e.message}")
        }
    }
}