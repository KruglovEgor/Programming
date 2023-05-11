package commands

import listOfHumanBeing

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
        var result: String
        try {
            result = (getCountByMinutesOfWaiting(map["minutesOfWaiting"] as Double)).toString()
        } catch (e: Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }

}