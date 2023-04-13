package Commands

import listOfData
import listOfHumanBeing

class ClearCommand():Command{

    /**
     * Make listOfData and listOfHumanBeing empty
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            listOfData.clear()
            listOfHumanBeing.clear()
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }

}