package commands

import listOfHumanBeing
import base_classes.Mood
import helping_functions.printResults

class CountLessThanMoodCommand(): Command {

    /**
     * Get count of units with the mood less than mood from User
     *
     * @return Count: Int of units with the same value of field 'mood'
     */
    private fun getCountLessThanMood(mood : Mood):Int{
        var count = 0
        for (unit in listOfHumanBeing){
            if(unit.mood != null){
                if(unit.mood < mood){
                    count++
                }
            }
        }
        return count
    }

    /**
     * Printing count of units with the same value of field 'mood' using printResults()
     *
     * @param map with 1 pair('mood' : 'value')
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            printResults(getCountLessThanMood(map["Mood"] as Mood))
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}