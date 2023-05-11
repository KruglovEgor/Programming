package commands

import helping_functions.pullInDataToSend
import listOfHumanBeing
import kotlin.math.max

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
        try {
            val maxImpactSpeed : Long = listOfHumanBeing.stream()
                .mapToLong {it.impactSpeed}
                .max()
                .orElse(-Long.MAX_VALUE + 1)
            if (map["impactSpeed"] as Long > maxImpactSpeed){
                val addCommand = AddCommand()
                addCommand.execute(map)
            }
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        if(success){
            pullInDataToSend("Success")
        } else {
            pullInDataToSend("Error $message")
        }
        return Result(success, message)
    }
}