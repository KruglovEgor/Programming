package client.interactive

import client.GUI.Localization.Localization
import client.helping_functions.convertJSONtoMapOfStringAndAny
import java.io.File
import client.helping_functions.readFromFile
import client.inputList
import javafx.geometry.Pos
import javafx.stage.Window
import javafx.util.Duration
import org.controlsfx.control.Notifications



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

fun checkPathForExecuteScript(path: String) : Boolean{
    val file = File(path.trim())
    if(!file.exists() or (file.extension != "txt")) return false
    else return true
}

fun executeScript(path : String, currentLanguage: String, currentWindow: Window?, login: String) {

    val txt = readFromFile(path)
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
                        else{
                            params["creator"] = login
                        }
                    }
                }
            } else validity = false

            if (validity){
                inputList.add(mapOf("command" to commandName, "params" to params))
            }
        }
        if (metExecuteScript) {
            Notifications.create()
                .title("Уведомление")
                .text(Localization.translations[currentLanguage]?.get("metExecuteScript") ?: "metExecuteScript")
                .owner(currentWindow)
                .hideAfter(Duration.seconds(10.0))
                .position(Pos.BOTTOM_RIGHT)
                .darkStyle()
                .graphic(null)
                .show()
        }
        }
    }

