package commands

import base_classes.HumanBeing
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
        var result: String
        try {
            listOfHumanBeing.stream()
                .sorted(Comparator.comparing(HumanBeing::name))
                .findFirst()
                .ifPresent {
                    val mapOfId = mapOf("id" to it.id)
                    val removeById = RemoveByIdCommand()
                    removeById.execute(mapOfId)
                }
            result = "Success"
        } catch (e: Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }
}