package commands

import listOfHumanBeing
import base_classes.Mood

class CountLessThanMoodCommand(): Command {

    /**
     * Get count of units with the mood less than mood from User
     *
     * @return Count: Int of units with the same value of field 'mood'
     */
    private fun getCountLessThanMood(mood : Mood):Int{
        return listOfHumanBeing.stream()
            .filter{it.mood != null && it.mood < mood}
            .count().toInt()
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
        var result: String
        try {
            result = getCountLessThanMood(map["Mood"] as Mood).toString()
        } catch (e : Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }
}