package server.interactive

import server.helping_functions.convertJSONtoMapOfStringAndAny
import server.commands.*


fun getClassesOfCommands() : Map<String, Command>{
    val commandClasses = mapOf(
        "help" to HelpCommand(),
        "info" to InfoCommand(),
        "show" to ShowCommand(),
        "add" to AddCommand(),
        "update" to UpdateCommand(),
        "remove_by_id" to RemoveByIdCommand(),
        "clear" to ClearCommand(),
        "execute_script" to ExecuteScriptCommand(),
        "remove_first" to RemoveFirstCommand(),
        "remove_head" to RemoveHeadCommand(),
        "add_if_max" to AddIfMaxCommand(),
        "count_by_minutes_of_waiting" to CountByMinutesOfWaitingCommand(),
        "count_less_than_mood" to CountLessThanMoodCommand(),
        "print_ascending" to PrintAscendingCommand()
    )
    return commandClasses
}

/**
 * Get parameters of commands
 *
 * @return LinkedTreeMap<String, List<Any>> of pairs 'command' : '['count_of_params', 'type_of_1st_param', 'type_of_2nd_param', ...]'
 */
fun getParametersOfCommands() : Map<String, Any?> {
    val inputStream = object {}.javaClass.getResourceAsStream("/CommandsParametersServer.json")
    val dataFromFile = inputStream.bufferedReader().use { it.readText() }
    return convertJSONtoMapOfStringAndAny(dataFromFile)
}