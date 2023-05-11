package commands

import listOfHumanBeing
import helping_functions.pullInDataToSend

class CountByMinutesOfWaitingCommand() : Command {

    /**
     * Get count of units with the same minutesOfWaiting
     *
     * @return Count: Int of units with the same value of field 'minutesOfWaiting'
     */
    fun getCountByMinutesOfWaiting(minutesOfWaiting : Double) : Int{
        return listOfHumanBeing.stream()
            .filter{ it.minutesOfWaiting == minutesOfWaiting}
            .count().toInt()
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
            pullInDataToSend(getCountByMinutesOfWaiting(map["minutesOfWaiting"] as Double))
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }

}