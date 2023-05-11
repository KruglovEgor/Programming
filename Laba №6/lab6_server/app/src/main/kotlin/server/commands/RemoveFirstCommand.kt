package commands

import base_classes.HumanBeing
import helping_functions.pullInDataToSend
import listOfHumanBeing


class RemoveFirstCommand(): Command {

    /**
     * Remove unit with the smallest id
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
//            listOfHumanBeing.sort()
//            val mapOfId = mapOf("id" to listOfHumanBeing[0].id)
//            val removeById = RemoveByIdCommand()
//            removeById.execute(mapOfId)
            listOfHumanBeing.stream()
                .sorted(Comparator.comparing(HumanBeing::name))
                .findFirst()
                .ifPresent {
                    val mapOfId = mapOf("id" to it.id)
                    val removeById = RemoveByIdCommand()
                    removeById.execute(mapOfId)
                }
        } catch (e: Exception){
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