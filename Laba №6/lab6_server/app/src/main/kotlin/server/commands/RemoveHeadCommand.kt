package server.commands

import server.base_classes.HumanBeing
import server.exceptions.NoSuchIdException
import server.listOfHumanBeing

class RemoveHeadCommand(): Command {

    /**
     * Printing unit with the smallest id using printResults() then delete it
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
            val unitToRemove = listOfHumanBeing.stream()
                .sorted(Comparator.comparing(HumanBeing::name))
                .findFirst()
                .orElse(null)
            if(unitToRemove != null){
                result = unitToRemove.toString()
                val mapOfId = mapOf("id" to unitToRemove.id)
                val removeById = RemoveByIdCommand()
                removeById.execute(mapOfId)
            }
            else result = "Error ${NoSuchIdException().message.toString()}"
        } catch (e : Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }
}