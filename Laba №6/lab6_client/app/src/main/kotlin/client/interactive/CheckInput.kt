package client.interactive

import client.base_classes.Mood
import client.helping_functions.convertJSONtoMapOfStringAndListOfAny
import client.helping_functions.readFromFileOrCreateFile


/**
 * Give description of HumanBeing's fields
 *
 * @return Map<String, String> with pairs 'name of field which User must enter' : 'type of field'
 */
fun getDescriptionOfHumanBeingFields(): Map<String, String>{
    val fields = mapOf(
        "name" to "String",
        "coordinate_x" to "Double",
        "coordinate_y" to "Float",
        "realHero" to "Boolean",
        "hasToothpick" to "Boolean",
        "impactSpeed" to "Long",
        "soundtrackName" to "String",
        "minutesOfWaiting" to "Double?",
        "mood" to "Mood?",
        "car" to "String?"
    )

    return fields
}


/**
 * Get parameters of commands
 *
 * @return LinkedTreeMap<String, List<Any>> of pairs 'command' : '['count_of_params', 'type_of_1st_param', 'type_of_2nd_param', ...]'
 */
fun getParametersOfCommands() : Map<String, List<Any>> {
    val txt = readFromFileOrCreateFile("CommandsParameters.json")
//    val inputStream = object {}.javaClass.getResourceAsStream("/CommandsParameters.json")
//    val dataFromFile = inputStream.bufferedReader().use { it.readText() }
    if (txt.isEmpty()){
        return  mapOf<String, List<Any>>()
    }
    else{
        try {
            return convertJSONtoMapOfStringAndListOfAny(txt)
        } catch (e: Exception){
            println("Error ${e.message}")
            return mapOf<String, List<Any>>()
        }
    }
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
 * Check if input is in format of type
 *
 * @param type : String - type which we are waiting
 *
 * @param input : String - data from User
 *
 * @return 'true' if input is in format of type and 'false' if not
 */
fun checkType(type: String, input: String, printErrors: Boolean) : Boolean{
    val forInt = Regex("^-?\\d+\\.?0*$")
    val forDouble = Regex("^-?\\d+\\.\\d*$")
    val forDoubleNull = Regex("^(-?\\d+\\.\\d*$)?")
    val forMood= Regex("^(${Mood.values().joinToString("|")})$")
    val forMoodNull = Regex("^(${Mood.values().joinToString("|")})?$")
    val forFloat = Regex("^-?\\d+\\.\\d*$")
    val forString = Regex("^[a-zA-Z -]+[a-zA-Z \\-0-9]*\$")
    val forStringNull = Regex("^([a-zA-Z]+[a-zA-Z \\-0-9]*)?$")
    val forBoolean = Regex("^(true|false)$")

    val typeAndMask = hashMapOf(
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
            if (printErrors) println("Oh, there is something wrong in your input, it's too big or too small!\nMin value: $minValue\nMax value: $maxValue")
            return false
        }
    }
    else{
        if (printErrors){
            println("Oh, there is something wrong in your input! You must enter the '$type'")
            println(descriptionOfType[type.substringBefore('?')]!!)
            if (type.endsWith('?')) println("*It also may be null! So you may just press the 'enter'*")
        }
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
        "Float" to "It looks like numbers with dot, example: 12.34. You may add minus in the front:",
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
        "Boolean" -> convertedInput.toBoolean()
        "Double?" -> convertedInput?.toDouble()
        "String?" -> convertedInput
        "Mood?" ->  convertedInput?.let { Mood.valueOf(it) }
        else -> throw IllegalArgumentException("Unsupported type: $type")
    }
    return output
}