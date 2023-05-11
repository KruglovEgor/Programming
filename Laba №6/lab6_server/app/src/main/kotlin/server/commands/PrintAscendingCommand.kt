package commands

import base_classes.HumanBeing
import listOfHumanBeing
import helping_functions.pullInDataToSend
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
        try {
            val txt = listOfHumanBeing.stream()
                .sorted(Comparator.comparing { it.name })
                .map(HumanBeing::toString)
                .collect(Collectors.joining())
            pullInDataToSend(txt)
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}