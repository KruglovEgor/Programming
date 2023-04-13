package BaseClasses

import currentId
import java.time.LocalDateTime

/**
 * Human being
 *
 * @property id
 * @property name
 * @property coordinates
 * @property creationDate
 * @property realHero
 * @property hasToothpick
 * @property impactSpeed
 * @property soundtrackName
 * @property minutesOfWaiting
 * @property mood
 * @property car
 * @constructor Create empty Human being
 */
class HumanBeing(val id: Int, val name: String="DefaultName", val coordinates: Coordinates = Coordinates(),
                 val creationDate: LocalDateTime = LocalDateTime.of(LocalDateTime.now().year,
                         LocalDateTime.now().monthValue, LocalDateTime.now().dayOfMonth,
                         LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second),
                 val realHero: Boolean = false, val hasToothpick: Boolean = false, val impactSpeed: Long = 0,
                 val soundtrackName: String = "DefaultSoundtrackName", val minutesOfWaiting: Double? = null,
                 val mood: Mood? = null, val car: Car? = null) : Comparable<HumanBeing>  {



    constructor(map: Map<String, Any?>) :this(
        (map["id"] as? Number ?: currentId).toInt(), map["name"] as? String ?: "DefaultName",
        Coordinates(map["coordinates"] as? List<Number> ?: listOf(0.0, 0.0F)),
        makeLocalDateTime(map["creationDate"] as? List<Int> ?:
        listOf(LocalDateTime.now().year, LocalDateTime.now().monthValue,
            LocalDateTime.now().dayOfMonth, LocalDateTime.now().hour,
            LocalDateTime.now().minute, LocalDateTime.now().second)),
        map["realHero"] as? Boolean ?: false,
        map["hasToothpick"] as? Boolean ?: false,
        (map["impactSpeed"] as? Number ?: 0.0).toLong(),
        map["soundtrackName"] as? String ?: "DefaultSoundtrackName",
        map["minutesOfWaiting"] as? Double,
        (map["mood"] as? String)?.let { Mood.valueOf(it) },
        Car(map["car"] as? String))

    override fun compareTo(other: HumanBeing): Int {
        return id.compareTo(other.id)
    }


    override fun toString(): String {
        return """
            
            id: $id
            name: $name
            coordinates: $coordinates
            creation date: $creationDate
            real hero: $realHero
            has toothpick: $hasToothpick
            impact speed: $impactSpeed
            soundtrack name: $soundtrackName
            minutes of waiting: $minutesOfWaiting
            mood: $mood
            car: $car
            
            """.trimIndent()
    }

    /**
     * Make linked tree map
     *
     * @return
     */
    fun makeMapByUnit():MutableMap<String, Any?>{
        val list = mutableMapOf<String, Any?>()
        list["id"] = id
        list["name"] = name
        list["coordinates"] = coordinates.getCoordinates()
        list["creationDate"] = listOf(creationDate.year, creationDate.monthValue, creationDate.dayOfMonth, creationDate.hour, creationDate.minute, creationDate.second)
        list["realHero"] = realHero
        list["hasToothpick"] = hasToothpick
        list["impactSpeed"] = impactSpeed
        list["soundtrackName"] = soundtrackName
        list["minutesOfWaiting"] = minutesOfWaiting
        list["mood"] = mood
        list["car"] = car

        return list
    }

}

/**
 * Give description of HumanBeing's fields
 *
 * @return Map<String, String> with pairs 'name of field which User must enter' : 'type of field'
 */
fun getDescriptionOfHumanBeingFields(): Map<String, String>{
    val fields = mapOf(
        "name" to "String",
        "coordinate_x" to "Double",
        "coordinate_y" to "Float",
        "realHero" to "Boolean",
        "hasToothpick" to "Boolean",
        "impactSpeed" to "Long",
        "soundtrackName" to "String",
        "minutesOfWaiting" to "Double?",
        "mood" to "Mood?",
        "car" to "String?"
    )

    return fields
}