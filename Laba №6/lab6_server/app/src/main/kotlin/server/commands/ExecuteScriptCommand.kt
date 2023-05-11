package commands

import dataToSend


class ExecuteScriptCommand() : Command {

    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            dataToSend = "Success"
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }

}