package commands

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
            var maxImpactSpeed : Long = -Long.MAX_VALUE + 1
            for(unit in listOfHumanBeing){
                maxImpactSpeed = max(maxImpactSpeed, unit.impactSpeed)
            }
            if (map["impactSpeed"] as Long > maxImpactSpeed){
                val addCommand = AddCommand()
                addCommand.execute(map)
            }
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}