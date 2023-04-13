package Interactive

import BaseClasses.HumanBeing
import HelpingFunctions.*
import currentId
import dateOfInitialization
import listOfData
import listOfHumanBeing
import pathToCollection
import java.io.File
import java.time.LocalDateTime
import java.util.LinkedList



/**
 * Asks User for path to the json-file with collection
 *
 * @return path to collection
 */
fun getPathToCollection() : String{
    printResults("Please enter the absolute path to the file.json with collection: ")
    var usersPath = readln().trim()
    var file = File(usersPath)
    while (!file.exists() or (file.extension != "json")){
        printResults("It's a wrong file. Please enter another path")
        usersPath = readln().trim()
        file = File(usersPath)
    }
    return usersPath
}

/**
 * Download data from json-file with collection from User (keep asking if something goes wrong)
 */
fun downloadDataFromUsersCollection() {
    pathToCollection = getPathToCollection()
    val data = readFromFile(pathToCollection)
    if (data.isEmpty()){
        listOfData = LinkedList<MutableMap<String, Any?>>()
        listOfHumanBeing = LinkedList<HumanBeing>()
    }
    else {
        try {
            listOfData = convertJSONtoLinkedListOfMutableMapOfStringAndAny(data)
            makeListOfHumanBeing()
        } catch (e: Exception){
            printResults("Oh, there is something wrong with content in your file. Please enter path to other file.json:")
            downloadDataFromUsersCollection()
        }
    }
}


/**
 * Fill listOfHumanBeing with data from listOfData. Also remember the dateOfInitialization
 */
fun makeListOfHumanBeing() {
    dateOfInitialization = LocalDateTime.of(
        LocalDateTime.now().year,
        LocalDateTime.now().monthValue, LocalDateTime.now().dayOfMonth,
        LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second)

    val maxId = listOfData.maxOf { (it["id"] as Double?)?.toInt() ?: 0 }
    currentId = maxId+1

    /**
     * Count the id for creating new unit
     */
    fun getId() : Int{
        currentId += 1
        return currentId - 1
    }

    val groupedById = listOfData.groupBy { it["id"] ?: getId()
    }
    listOfData.clear()
    for(datum in groupedById){
        datum.value[0]["id"] = datum.key
        val unit = HumanBeing(datum.value[0])
        listOfData.add(unit.makeMapByUnit())
        listOfHumanBeing.add(unit)
    }
}