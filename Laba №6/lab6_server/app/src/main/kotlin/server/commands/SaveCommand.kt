package commands

import listOfHumanBeing
import java.util.*
import listOfData
import java.io.File
import java.io.FileOutputStream

class SaveCommand() : Command {

    /**
     * Save listOfHumanBeing in file.json (path to it is pathToCollection)
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>) : Result {
        var success = true
        var message = ""
        try {
            val list = LinkedList<MutableMap<String, Any?>>()
            for (unit in listOfHumanBeing){ list.add(unit.makeMapByUnit())}
            val resourceUrl = object {}.javaClass.getResource("/resources/Data.json")
            println(resourceUrl)
            println(list)
            val file = File(resourceUrl.toURI())
            val outputStream = FileOutputStream(file)
            outputStream.write(list.toString().toByteArray())
            outputStream.close()
            listOfData = list
        } catch (e : Exception){
            success = false
            message = e.message.toString()
        }

        return Result(success, message)
    }
}