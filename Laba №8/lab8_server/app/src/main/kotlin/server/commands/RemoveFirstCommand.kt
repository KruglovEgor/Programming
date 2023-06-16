package server.commands

import server.base_classes.HumanBeing
import server.exceptions.NoSuchIdException
import server.listOfHumanBeing
import java.sql.Connection


class RemoveFirstCommand(val connection: Connection): Command {

    /**
     * Remove unit with the smallest id
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var idOfFirstElement = -1
        listOfHumanBeing.stream()
            .sorted(Comparator.comparing(HumanBeing::name))
            .findFirst()
            .ifPresent {
                idOfFirstElement = it.id
            }
        if (idOfFirstElement >= 0){
            val removeByIdCommand = RemoveByIdCommand(connection)
            return removeByIdCommand.execute(mapOf("id" to idOfFirstElement))
        } else return Result(false, NoSuchIdException().message.toString(), "Error: ${NoSuchIdException().message}" )

    }
}