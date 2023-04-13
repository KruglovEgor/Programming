package BaseClasses

import java.time.LocalDateTime

/**
 * Make LocalDateTime by params
 *
 * @param list : List<Int> consisted of 6 Int numbers in order [year, month, day, hour, minute, second]
 *
 * @return time : LocalDateTime made with parameters
 */
fun makeLocalDateTime(list: List<Int>) : LocalDateTime {
    var listOfDate = mutableListOf(0, 0, 0, 0, 0, 0)
    for (i in 0 until Integer.min(6, list.size)){
        listOfDate[i] = list[i]
    }
    return LocalDateTime.of(listOfDate[0], listOfDate[1], listOfDate[2], listOfDate[3],
        listOfDate[4], listOfDate[5])
}
