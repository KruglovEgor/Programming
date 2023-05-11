package helping_functions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.util.*


/**
 * Convert data from json-file to Map<String, Any?>
 *
 * @param txt - data from json file in format of String
 *
 * @return LinkedList<LinkedTreeMap<String, Any?>>
 */
fun convertJSONtoLinkedListOfMutableMapOfStringAndAny(txt: String) : LinkedList<MutableMap<String, Any?>> {
    return Gson().fromJson(txt, LinkedList::class.java) as LinkedList<MutableMap<String, Any?>>
}

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
fun writeInJSONFile(pathToFile: String, data: LinkedList<*>) {
    val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    val jsonString = gson.toJson(data)
    try  {
        val bos = BufferedOutputStream(FileOutputStream(pathToFile))
        bos.write(jsonString.toByteArray())
        bos.flush()
        bos.close()
    }
    catch (e: IOException){
        throw IOException(e)
    }
    catch (e: RuntimeException){
        throw RuntimeException(e)
    }
}

fun convertMapToJSON (map : Map<String, Any?>?) : String{
    val gson = GsonBuilder().serializeNulls().create()
    return gson.toJson(map)
}