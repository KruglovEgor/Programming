package server.helping_functions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*




fun convertJSONtoMapOfStringAndAny(txt: String) :Map<String, Any?> {
    return Gson().fromJson(txt, Map::class.java) as Map<String, Any?>
}

/**
 * Write data to json-file
 *
 * @param pathToFile - path to file.json
 *
 * @param data - LinkedList<*> for writing in file
 */
fun writeInJSONFile(pathToFile: String, data: List<*>) {
    val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    val jsonString = gson.toJson(data)
    try  {
        val bos = BufferedOutputStream(FileOutputStream(pathToFile))
        bos.write(jsonString.toByteArray())
        bos.flush()
        bos.close()
    }
    catch (e: Exception){
        println("Error ${e.message}")
    }
}

fun convertMapToJSON (map : Map<String, Any?>?) : String{
    val gson = GsonBuilder().serializeNulls().create()
    return gson.toJson(map)
}

fun convertListOfMapsToJson(list : List<Map<String, Any?>>) : String{
    val gson = GsonBuilder().serializeNulls().create()
    return gson.toJson(list)
}