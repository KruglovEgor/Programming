package commands

import base_classes.HumanBeing
import listOfHumanBeing
import helping_functions.pullInDataToSend

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
        try {
            val unitToRemove = listOfHumanBeing.stream()
                .sorted(Comparator.comparing(HumanBeing::name))
                .findFirst()
                .orElse(null)
            if(unitToRemove != null){
                pullInDataToSend(unitToRemove)
                val mapOfId = mapOf("id" to unitToRemove.id)
                val removeById = RemoveByIdCommand()
                removeById.execute(mapOfId)
            }
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}