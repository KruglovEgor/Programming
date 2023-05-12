package server.commands

import server.listOfHumanBeing

class AddIfMaxCommand() : Command {

    /**
     * Add new unit with parameters from mapWithParams, if its impactSpeed is the biggest
     *
     * @param map with parameters of HumanBeing's unit
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        var result: String
        try {
            val maxImpactSpeed : Long = listOfHumanBeing.stream()
                .mapToLong {it.impactSpeed}
                .max()
                .orElse(-Long.MAX_VALUE + 1)
            if (map["impactSpeed"] as Long > maxImpactSpeed){
                val addCommand = AddCommand()
                addCommand.execute(map)
            }
            result = "Success"
        } catch (e : Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }
}