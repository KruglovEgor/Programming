package HelpingFunctions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.util.*

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


/**
 * Convert data from json-file to LinkedList<LinkedTreeMap<String, Any?>>
 *
 * @param txt - data from json file in format of String
 *
 * @return LinkedList<LinkedTreeMap<String, Any?>>
 */
fun convertJSONtoLinkedListOfMutableMapOfStringAndAny(txt: String) : LinkedList<MutableMap<String, Any?>> {
    return Gson().fromJson(txt, LinkedList::class.java) as LinkedList<MutableMap<String, Any?>>
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