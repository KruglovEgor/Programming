package commands

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
            listOfHumanBeing.sort()
            val mapOfId = mapOf("id" to listOfHumanBeing[0].id)
            val removeById = RemoveByIdCommand()
            removeById.execute(mapOfId)
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}