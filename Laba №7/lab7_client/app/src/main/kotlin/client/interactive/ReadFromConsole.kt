package client.interactive

import client.exceptions.NoSuchCommandException
import client.helping_functions.convertJSONtoMapOfStringAndAny
import java.io.File
import client.helping_functions.readFromFile
import client.inputList


/**
 * Start asking User parameters for his command
 *
 * @param commandFromUser : String - command what User entered
 *
 * @return List<Any> = ['command', 'map with parameters']
 */
fun commandHandler(commandFromUser: String) : MutableMap<String, Any?>{
    val listOfParametersOfCommands = getParametersOfCommands()
    val mapOfCommandAndParametersFromUser = mutableMapOf<String, Any?>()
    if (checkIfCommandExists(commandFromUser)){
        mapOfCommandAndParametersFromUser["command"] = commandFromUser
        val params = getParameters(listOfParametersOfCommands[commandFromUser] as List<Any>)
        mapOfCommandAndParametersFromUser["params"] = params
        return mapOfCommandAndParametersFromUser
    }
    else throw NoSuchCommandException()
}

/**
 * Asks User to enter the value of needed type
 *
 * @param listOfDescriptionAndType : list of ['number of params', 'description and type of 1st param', 'description and type of 2nd param', ...]
 *
 * @return MutableMap<String, Any?> with parameters
 */
fun getParameters(listOfDescriptionAndType: List<Any>) :MutableMap<String, Any?>{
    var description = ""
    var type = ""
    val map = mutableMapOf<String, Any?>()
    for (i in 0 until (listOfDescriptionAndType[0] as Number).toInt()){
        description = (listOfDescriptionAndType[i+1] as String).split(" ")[0]
        type = (listOfDescriptionAndType[i+1] as String).split(" ")[1]
        if((description == "unit") and (type == "Map")){
            val parametersOfHumanBeing = getParametersOfHumanBeing()
            for(key in parametersOfHumanBeing.keys){
                map[key] = parametersOfHumanBeing[key]
            }
        }
        else if(type == "Path"){
            map[description] = getPathToScript()
        }
        else{
            println("Please enter the $type:")
            var input = readln()
            while (!checkType(type, input, true)){
                input = readln()
            }
            map[description] = convertToNeededType(input, type)
        }
    }

    return map
}

/**
 * Ask User to enter values of HumanBeing's fields
 *
 * @return Map<String, Any?> with pairs of 'field' : 'value'
 */
fun getParametersOfHumanBeing() : Map<String, Any?>{
    val fields = getDescriptionOfHumanBeingFields()
    val map = mutableMapOf<String, Any?>()
    val coordinates = mutableListOf<Any?>()
    for (field in fields.keys){
        println("Please, enter the $field. It must be ${fields[field]}!")
        var input = readln()
        while (! checkType(fields[field].toString(), input, true)){
            input = readln()
        }
        if(field.startsWith("coordinate")){
            coordinates.add(convertToNeededType(input, fields[field]))
        } else{
            map[field] = convertToNeededType(input, fields[field])
        }
    }
    map["coordinates"] = coordinates
    return map
}

/**
 * Ask User to enter path to txt file with script
 *
 * @return String with data from this file
 */
fun getPathToScript() : String{
    println("Please enter the absolute path to the file.txt with command for execution: ")
    var usersPath = readln().trim()
    var file = File(usersPath)
    while (!file.exists() or (file.extension != "txt")){
        println("It's a wrong file. Please enter another path:")
        usersPath = readln().trim()
        file = File(usersPath)
    }

    val txt = readFromFile(usersPath)
    val listOfLines = txt.trim().split("\r\n")
    for(i in listOfLines.indices){
        var metExecuteScript = false
        if(listOfLines[i].isEmpty()) break
        val splitedLine = listOfLines[i].split(" ")
        val commandName =splitedLine[0]
        var params = mutableMapOf<String, Any?>()

        if (splitedLine.size >= 2){
            params = convertJSONtoMapOfStringAndAny(splitedLine.slice(1 until splitedLine.size).joinToString(separator = " ")).toMutableMap()
        }
        if(commandName == "execute_script"){
            metExecuteScript = true
        }
        else{
            var validity = true
            if (checkIfCommandExists(commandName)){
                val neededParams = getParametersOfCommands()[commandName]
                if(neededParams!![0] != 0){
                    for(j in 1 until neededParams.size){
                        if (neededParams[j].toString() != "unit Map"){
                            val nameInMap = (neededParams[j] as String).split(" ")[0]
                            val neededType = (neededParams[j] as String).split(" ")[1]
                            if (!checkType(neededType, params[nameInMap].toString(), false)) validity = false
                        }
                    }
                }
            } else validity = false

            if (validity){
                inputList.add(mapOf("command" to commandName, "params" to params))
            }
        }
        if (metExecuteScript) println("In file was command execute_script. It may cause the infinity loop, so please enter it by yourself if you want to execute it.")
    }
    return ""
}
