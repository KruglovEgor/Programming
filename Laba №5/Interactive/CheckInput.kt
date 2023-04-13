package Interactive

import BaseClasses.Mood
import Commands.*
import HelpingFunctions.printResults


/**
 * Get parameters of commands
 *
 * @return LinkedTreeMap<String, List<Any>> of pairs 'command' : '['count_of_params', 'type_of_1st_param', 'type_of_2nd_param', ...]'
 */
fun getParametersOfCommands() : Map<String, List<Any>> {
    val commandsAndParameters = mapOf<String, List<Any>>(
        "help" to listOf(0),
        "info" to listOf(0),
        "show" to listOf(0),
        "add" to listOf(1, "unit Map"),
        "update" to listOf(2, "id Int", "unit Map"),
        "remove_by_id" to listOf(1, "id Int"),
        "clear" to listOf(0),
        "save" to listOf(0),
        "execute_script" to listOf(1, "path Path"),
        "exit" to listOf(0),
        "remove_first" to listOf(0),
        "remove_head" to listOf(0),
        "add_if_max" to listOf(1, "unit Map"),
        "count_by_minutes_of_waiting" to listOf(1, "minutesOfWaiting Double"),
        "count_less_than_mood" to listOf(1, "mood Mood"),
        "print_ascending" to listOf(0)
    )
    return commandsAndParameters
}


fun getClassesOfCommands() : Map<String, Command>{
    val commandClasses = mapOf(
        "help" to HelpCommand(),
        "info" to InfoCommand(),
        "show" to ShowCommand(),
        "add" to AddCommand(),
        "update" to UpdateCommand(),
        "remove_by_id" to RemoveByIdCommand(),
        "clear" to ClearCommand(),
        "save" to SaveCommand(),
        "execute_script" to ExecuteScriptCommand(),
        "exit" to ExitCommand(),
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
 * Check if command exists
 *
 * @param commandFromUser : String - command for checking
 *
 * @return 'true' if command exists and 'false' if not
 */
fun checkIfCommandExists(commandFromUser: String) :Boolean{
    for(command in getParametersOfCommands().keys){
        if (command==commandFromUser){
            return true
        }
    }
    return false
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
        if (input.length == 0) convertedInput = null
    }

    val output = when(type){
        "String" -> convertedInput
        "Double" -> convertedInput?.toDouble()
        "Int" -> convertedInput?.toInt()
        "Mood" -> Mood.valueOf(convertedInput as String)
        "Long" -> convertedInput?.toLong()
        "Float" -> convertedInput?.toFloat()
        "Boolean" -> convertedInput.toBoolean()
        "Double?" -> convertedInput?.toDouble()
        "String?" -> convertedInput
        "Mood?" ->  convertedInput?.let { Mood.valueOf(it) }
        else -> throw IllegalArgumentException("Unsupported type: $type")
    }
    return output
}

/**
 * Check if input is in format of type
 *
 * @param type : String - type which we are waiting
 *
 * @param input : String - data from User
 *
 * @return 'true' if input is in format of type and 'false' if not
 */
fun checkType(type: String, input: String) : Boolean{
    val forInt = Regex("^-?\\d+$")
    val forDouble = Regex("^-?\\d+\\.\\d*$")
    val forDoubleNull = Regex("^(-?\\d+\\.\\d*$)?")
    val forMood= Regex("^(${Mood.values().joinToString("|")})$")
    val forMoodNull = Regex("^(${Mood.values().joinToString("|")})?$")
    val forFloat = Regex("^-?\\d+\\.\\d*F?$")
    val forString = Regex("^[a-zA-Z -]+[a-zA-Z \\-0-9]*\$")
    val forStringNull = Regex("^([a-zA-Z]+[a-zA-Z \\-0-9]*)?$")
    val forBoolean = Regex("^(true|false)$")

    val typeAndMask = hashMapOf<String, Regex>(
        "Int" to forInt,
        "Double" to forDouble,
        "Mood" to forMood,
        "Float" to forFloat,
        "String" to forString,
        "Boolean" to forBoolean,
        "Long" to forInt,
        "Double?" to forDoubleNull,
        "String?" to forStringNull,
        "Mood?" to forMoodNull
    )

    val minValues = getMinValues()
    val maxValues = getMaxValues()
    val descriptionOfType = getDescriptionOfType()


    if(typeAndMask[type]!!.matches(input)){
        if (checkMinAndMaxValue(input, type))
            return true
        else{
            val minValue = minValues[type]
            val maxValue = maxValues[type]
            printResults("Oh, there is something wrong in your input, it's too big or too small!\nMin value: $minValue\nMax value: $maxValue")
            return false
        }
    }
    else{
        printResults("Oh, there is something wrong in your input! You must enter the '$type'")
        printResults(descriptionOfType[type.substringBefore('?')]!!)
        if (type.endsWith('?')) printResults("*It also may be null! So you may just press the 'enter'*")
        return false
    }
}

/**
 * Check if input is not goes beyond the type
 *
 * @param input : String - count for checking
 *
 * @param type : String - type which we are waiting
 *
 * @return 'true' if it suits and 'false' if not
 */
fun checkMinAndMaxValue(input:String, type: String) : Boolean{
    if (input.isEmpty() and type.endsWith('?')){
        return true
    }
    else if (type in listOf("Double", "Double?", "Float", "Float?", "Long", "Long?", "Int", "Int?")){
        val inputAsBigDecimal = input.toBigDecimal()
        when(type){
            in listOf("Double", "Double?") -> {
                return !((inputAsBigDecimal.compareTo(Double.MAX_VALUE.toBigDecimal()) == 1 ) or (inputAsBigDecimal.compareTo((-1*Double.MAX_VALUE+1).toBigDecimal()) == -1))
            }
            in listOf("Float", "Float?") -> {
                return !((inputAsBigDecimal.compareTo(Float.MAX_VALUE.toBigDecimal()) == 1 ) or (inputAsBigDecimal.compareTo((-1*Float.MAX_VALUE+1).toBigDecimal()) == -1))
            }
            in listOf("Long", "Long?") -> {
                return !((inputAsBigDecimal.compareTo(Long.MAX_VALUE.toBigDecimal()) == 1 ) or (inputAsBigDecimal.compareTo((-1*Long.MAX_VALUE+1).toBigDecimal()) == -1))
            }
            in listOf("Int", "Int?") -> {
                return !((inputAsBigDecimal.compareTo(Int.MAX_VALUE.toBigDecimal()) == 1 ) or (inputAsBigDecimal.compareTo((-1*Int.MAX_VALUE+1).toBigDecimal()) == -1))
            }
        }
    }
    return true
}

/**
 * Give description of types
 *
 * @return HashMap<String, String> of pairs 'type' : 'its description / format for input'
 */
fun getDescriptionOfType() : HashMap<String, String>{
    val descriptionOfType = hashMapOf(
        "String" to "It may consist of english letters, numbers, spaces and '-':",
        "Double" to "It looks like numbers with dot, example: 12.34. You may add minus in the front:",
        "Int" to "It looks like numbers without dot, example: 1234. You may add minus in the front:",
        "Mood" to "It may be ${Mood.values().joinToString(", ")}:",
        "Long" to "It looks like numbers without dot, example: 1234. You may add minus in the front:",
        "Float" to "It looks like numbers with dot, example: 12.34. You may add minus in the front and 'F' in the end:",
        "Boolean" to "It may be 'true' or 'false':",
    )
    return descriptionOfType
}

/**
 * Give min values for Number-types
 *
 * @return HashMap<String, String> of pairs 'type' : 'its min value'
 */
fun getMinValues() : HashMap<String, Number>{
    val minValues = hashMapOf<String, Number>(
        "Int" to -Int.MAX_VALUE+1,
        "Long" to -Long.MAX_VALUE+1,
        "Float" to -Float.MAX_VALUE+1,
        "Double" to -Double.MAX_VALUE+1,
        "Int?" to -Int.MAX_VALUE+1,
        "Long?" to -Long.MAX_VALUE+1,
        "Float?" to -Float.MAX_VALUE+1,
        "Double?" to -Double.MAX_VALUE+1
    )

    return minValues
}

/**
 * Give max values for Number-types
 *
 * @return HashMap<String, String> of pairs 'type' : 'its max value'
 */
fun getMaxValues() : HashMap<String, Number>{
    val maxValues = hashMapOf<String, Number>(
        "Int" to Int.MAX_VALUE,
        "Long" to Long.MAX_VALUE,
        "Float" to Float.MAX_VALUE,
        "Double" to Double.MAX_VALUE,
        "Int?" to Int.MAX_VALUE,
        "Long?" to Long.MAX_VALUE,
        "Float?" to Float.MAX_VALUE,
        "Double?" to Double.MAX_VALUE
    )
    return maxValues
}