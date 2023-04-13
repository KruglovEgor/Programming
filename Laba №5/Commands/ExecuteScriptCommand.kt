package Commands

import HelpingFunctions.printResults
import HelpingFunctions.readFromFile
import Interactive.checkIfCommandExists
import listenToUser

class ExecuteScriptCommand() :Command{

    /**
     * Execute commands from file (pathToScript)
     *
     * @param map with 1 pair ('path' : 'path to the file')
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            val commands = readFromFile(map["path"] as String).trim().split("\r\n")
            var metExecuteScript = false
            for(i in commands.indices){
                if (commands[i] == "execute_script"){
                    metExecuteScript = true
                }
                else{
                    if (checkIfCommandExists(commands[i])){
                        printResults("\nIn file was command '${commands[i]}'")
                        listenToUser(commands[i])
                    }
                    else{
                        printResults("There was unknown command '${commands[i]}'. Be careful, in file.txt must be commands only from 'help' list. Each command must be on new line without coma, dot or space around it.")
                    }
                }
            }
            if (metExecuteScript) printResults("\nIn file was command execute_script. It may cause the infinity loop, so please enter it by yourself if tou want to execute it.")
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }

}