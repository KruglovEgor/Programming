package commands

import listOfHumanBeing
import helping_functions.printResults

class CountByMinutesOfWaitingCommand() : Command {

    /**
     * Get count of units with the same minutesOfWaiting
     *
     * @return Count: Int of units with the same value of field 'minutesOfWaiting'
     */
    fun getCountByMinutesOfWaiting(minutesOfWaiting : Double) : Int{
        var count = 0
        for(unit in listOfHumanBeing){
            if(unit.minutesOfWaiting?.equals(minutesOfWaiting) == true){
                count++
            }
        }
        return count
    }

    /**
     * Printing count of units with the same value of field 'minutesOfWaiting' using printResults()
     *
     * @param map with 1 pair('minutesOfWaiting' : 'value')
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            printResults(getCountByMinutesOfWaiting(map["minutesOfWaiting"] as Double))
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }

}