package helping_functions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.net.FileNameMap
import java.util.*


fun convertMapToJSON (map : Map<String, Any?>?) : String{
    val gson = GsonBuilder().serializeNulls().create()
    return gson.toJson(map)
}

/**
 * Read data from file to String. Keep asking if we can't find such file.
 *
 * @param pathToFile - path to the file
 *
 * @return String with all data from file
 */
fun readFromFile(pathToFile: String): String {
    var txt = ""
    try {
        if (Scanner( File(pathToFile)).useDelimiter("\\Z").hasNext()){
            txt = Scanner( File(pathToFile)).useDelimiter("\\Z").next()}
    } catch (e: FileNotFoundException) {
        throw FileNotFoundException("It seems like you don't have such file")
    }
    return txt
}

fun convertJSONtoMapOfStringAndListOfAny(txt: String) :Map<String, List<Any>> {
    return Gson().fromJson(txt, Map::class.java) as Map<String, List<Any>>
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

fun convertJSONtoMapOfStringAndAny(txt: String) :Map<String, Any?> {
    return Gson().fromJson(txt, Map::class.java) as Map<String, Any?>
}

