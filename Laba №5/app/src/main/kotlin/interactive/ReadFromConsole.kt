package interactive

import base_classes.getDescriptionOfHumanBeingFields
import exceptions.NoSuchCommandException
import java.io.File
import helping_functions.printResults


/**
 * Start asking User parameters for his command
 *
 * @param commandFromUser : String - command what User entered
 *
 * @return List<Any> = ['command', 'map with parameters']
 */
fun commandHandler(commandFromUser: String) : List<Any>{
    val listOfParametersOfCommands = getParametersOfCommands()
    val listOfCommandAndParametersFromUser = mutableListOf<Any>()
    if (checkIfCommandExists(commandFromUser)){
        listOfCommandAndParametersFromUser.add(commandFromUser)

        val params = getParameters(listOfParametersOfCommands[commandFromUser] as List<Any>)
        listOfCommandAndParametersFromUser.add(params)
        return listOfCommandAndParametersFromUser
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
    for (i in 0 until listOfDescriptionAndType[0] as Int){
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
            printResults("Please enter the $type:")
            var input = readln()
            while (!checkType(type, input)){
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
    val coordinates = mutableListOf<Number>()
    for (field in fields.keys){
        printResults("Please, enter the $field. It must be ${fields[field]}!")
        var input = readln()
        while (! checkType(fields[field].toString(), input)){
            input = readln()
        }
        if(field.startsWith("coordinate")){
            coordinates.add(convertToNeededType(input, fields[field]) as Number)
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
    printResults("Please enter the absolute path to the file.txt with command for execution: ")
    var usersPath = readln().trim()
    var file = File(usersPath)
    while (!file.exists() or (file.extension != "txt")){
        printResults("It's a wrong file. Please enter another path:")
        usersPath = readln().trim()
        file = File(usersPath)
    }
    return usersPath
}
