package server.base_classes

import java.time.LocalDateTime

/**
 * Make LocalDateTime by params
 *
 * @param list : List<Int> consisted of 6 Int numbers in order [year, month, day, hour, minute, second]
 *
 * @return time : LocalDateTime made with parameters
 */
fun makeLocalDateTime(data: String) : LocalDateTime {
    val dateTimeParts = data.split('-', ':', ' ').map { it.toInt() }
    return LocalDateTime.of(dateTimeParts[0], dateTimeParts[1], dateTimeParts[2], dateTimeParts[3], dateTimeParts[4], dateTimeParts[5])
}
