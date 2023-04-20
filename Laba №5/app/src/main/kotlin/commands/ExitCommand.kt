package commands

import ongoing


class ExitCommand(): Command {

    /**
     * Make ongoing:Boolean = false (stop program)
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            ongoing = false
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}