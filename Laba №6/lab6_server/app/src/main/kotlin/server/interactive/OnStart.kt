package server.interactive

import server.currentId
import server.dateOfInitialization
import server.listOfData
import server.listOfHumanBeing
import server.base_classes.HumanBeing
import java.time.LocalDateTime

/**
 * Fill server.getListOfHumanBeing with data from server.getListOfData. Also remember the server.getDateOfInitialization
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