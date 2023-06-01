package server.interactive

import server.base_classes.Mood
import server.helping_functions.convertJSONtoMapOfStringAndAny
import server.commands.*
import java.sql.Connection


fun getClassesOfCommands(connection: Connection) : Map<String, Command>{
    val commandClasses = mapOf(
        "help" to HelpCommand(),
        "info" to InfoCommand(),
        "show" to ShowCommand(),
        "add" to AddCommand(connection),
        "update" to UpdateCommand(connection),
        "remove_by_id" to RemoveByIdCommand(connection),
        "clear" to ClearCommand(connection),
        "execute_script" to ExecuteScriptCommand(),
        "remove_first" to RemoveFirstCommand(connection),
        "remove_head" to RemoveHeadCommand(connection),
        "add_if_max" to AddIfMaxCommand(connection),
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


/**
 * Converts String value to its type
 *
 * @param input : String - data for converting
 *
 * @param type : String - type for converting
 *
 * @return input in format of type
 */
fun convertToNeededType(input: String?, type: String?): Any?{
    var convertedInput = input
    if (input != null) {
        if (input.isEmpty()) convertedInput = null
    }

    val output = when(type){
        "String" -> convertedInput
        "Double" -> convertedInput?.toDouble()
        "Int" -> convertedInput?.toDouble()?.toInt()
        "Mood" -> Mood.valueOf(convertedInput as String)
        "Long" -> convertedInput?.toDouble()?.toLong()
        "Float" -> convertedInput?.toFloat()
        "Boolean" -> when(input){
                        "t" -> true
                        "f" -> false
                        else -> convertedInput?.toBoolean() }
        "Double?" -> convertedInput?.toDouble()
        "String?" -> convertedInput
        "Mood?" ->  convertedInput?.let { Mood.valueOf(it) }
        else -> throw IllegalArgumentException("Unsupported type: $type")
    }
    return output
}