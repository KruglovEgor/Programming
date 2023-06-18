package client.interactive

import client.helping_functions.convertJSONtoMapOfStringAndAny
import java.io.File
import client.helping_functions.readFromFile
import client.inputList





/**
 * Asks User to enter the value of needed type
 *
 * @param listOfDescriptionAndType : list of ['number of params', 'description and type of 1st param', 'description and type of 2nd param', ...]
 *
 * @return MutableMap<String, Any?> with parameters
 */
fun getParameters(listOfDescriptionAndType: List<Any>) : List<Pair<String, String>>{
    var description = ""
    var type = ""
    val map = mutableMapOf<String, Any?>()
    val listOfParamPairs = mutableListOf<Pair<String, String>>()
    for (i in 0 until (listOfDescriptionAndType[0] as Number).toInt()) {
        description = (listOfDescriptionAndType[i + 1] as String).split(" ")[0]
        type = (listOfDescriptionAndType[i + 1] as String).split(" ")[1]
        listOfParamPairs.add(Pair(description, type))
    }
    return  listOfParamPairs }


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
