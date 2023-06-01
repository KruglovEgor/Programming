package server.commands

import server.base_classes.HumanBeing
import server.listOfHumanBeing
import java.util.stream.Collectors

class PrintAscendingCommand() : Command {

    /**
     * Printing all units in order by its id (increasing)
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
            result = listOfHumanBeing.stream()
                .sorted(Comparator.comparing { it.name })
                .map(HumanBeing::toString)
                .collect(Collectors.joining())
        } catch (e : Exception){
            success = false
            message = e.message.toString()
            result = "Error $message"
        }
        return Result(success, message, result)
    }
}