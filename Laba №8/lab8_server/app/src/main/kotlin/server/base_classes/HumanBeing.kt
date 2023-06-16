package server.base_classes

import java.time.LocalDateTime

/**
 * Human being
 *
 * @property id
 * @property name
 * @property coordinate_x
 * @property coordinate_x
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
class HumanBeing(val id: Int, var name: String="DefaultName", var coordinate_x: Double = 0.0, var coordinate_y : Float = 0.0F,
                 val creationDate: LocalDateTime = LocalDateTime.of(LocalDateTime.now().year,
                         LocalDateTime.now().monthValue, LocalDateTime.now().dayOfMonth,
                         LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second),
                 var realHero: Boolean = false, var hasToothpick: Boolean = false, var impactSpeed: Long = 0,
                 var soundtrackName: String = "DefaultSoundtrackName", var minutesOfWaiting: Double? = null,
                 var mood: Mood? = null, var car: Car? = null, var creator: String = "undefined") : Comparable<HumanBeing>  {



    constructor(map: Map<String, Any?>) :this(
        (map["id"] as? Number ?: 0).toInt(), map["name"] as? String ?: "DefaultName",
        (map["coordinate_x"] as? Number ?: 0.0).toDouble(),
        (map["coordinate_y"] as? Number ?: 0.0).toFloat(),
        makeLocalDateTime(
            map["creationDate"] as? String? ?: ("${LocalDateTime.now().year}-${LocalDateTime.now().monthValue}" +
                    "-${LocalDateTime.now().dayOfMonth} ${LocalDateTime.now().hour}:${LocalDateTime.now().minute}:${LocalDateTime.now().second}")
        ),
        map["realHero"] as? Boolean ?: false,
        map["hasToothpick"] as? Boolean ?: false,
        (map["impactSpeed"] as? Number)?.toLong() ?: 0,
        map["soundtrackName"] as? String ?: "DefaultSoundtrackName",
        map["minutesOfWaiting"] as? Double?,
        (map["mood"] as? String?)?.let { Mood.valueOf(it) },
        Car(map["car"] as? String?),
        map["creator"] as? String ?: "undefined"
    )

    override fun compareTo(other: HumanBeing): Int {
        return name.compareTo(other.name)
    }


    override fun toString(): String {
        return """
            
            id: $id
            name: $name
            coordinate_x: $coordinate_x
            coordinate_y: $coordinate_y
            creation date: $creationDate
            real hero: $realHero
            has toothpick: $hasToothpick
            impact speed: $impactSpeed
            soundtrack name: $soundtrackName
            minutes of waiting: $minutesOfWaiting
            mood: $mood
            car: $car
            creator: $creator
            
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
        list["coordinate_x"] = coordinate_x
        list["coordinate_y"] = coordinate_y
        list["creationDate"] = "${creationDate.year}-${creationDate.monthValue}-${creationDate.dayOfMonth} ${creationDate.hour}:${creationDate.minute}:${creationDate.second}"
        list["realHero"] = realHero
        list["hasToothpick"] = hasToothpick
        list["impactSpeed"] = impactSpeed
        list["soundtrackName"] = soundtrackName
        list["minutesOfWaiting"] = minutesOfWaiting
        list["mood"] = mood
        list["car"] = car
        list["creator"] = creator
        return list
    }

    fun getParametersForDB():Map<String, String>{
        val map = mapOf(
            "id" to "$id",
            "name" to "'$name'",
            "creationDate" to "'${creationDate.year}-${creationDate.monthValue}-${creationDate.dayOfMonth} ${creationDate.hour}:${creationDate.minute}:${creationDate.second}'",
            "coordinate_x" to "$coordinate_x",
            "coordinate_y" to "$coordinate_y",
            "realhero" to "$realHero",
            "hastoothpick" to "$hasToothpick",
            "impactspeed" to "$impactSpeed",
            "soundtrackname" to "'$soundtrackName'",
            "minutesofwaiting" to "$minutesOfWaiting",
            "mood" to when(mood){
                        null -> "null"
                        else -> "'$mood'"
                    },
            "car" to when(car?.name){
                null -> "null"
                else -> "'$car'"
            },
            "creator" to "'$creator'"
        )
        return  map
    }


    fun setNewParameters(map: Map<String, Any?>) {
        name = map["name"] as? String ?: name
        coordinate_x = (map["coordinate_x"] as? Number)?.toDouble() ?: coordinate_x
        coordinate_y = (map["coordinate_y"] as? Number)?.toFloat() ?: coordinate_y
        realHero = map["realHero"] as? Boolean ?: realHero
        hasToothpick = map["hasToothPick"] as? Boolean ?: hasToothpick
        impactSpeed = (map["impactSpeed"] as? Number ?: impactSpeed).toLong()
        soundtrackName = map["SoundtrackName"] as? String ?: soundtrackName
        minutesOfWaiting = map["minutesOfWaiting"] as? Double? ?: minutesOfWaiting
        mood =  (map["mood"] as? String? ?: mood?.name)?.let{Mood.valueOf(it)}
        car = Car(map["car"] as? String? ?: car?.name)
    }

}



/**
 * Give description of HumanBeing's fields
 *
 * @return Map<String, String> with pairs 'name of field which User must enter' : 'type of field'
 */
fun getDescriptionOfHumanBeingFields(): Map<String, String>{
    val fields = mapOf(
        "id" to "Int",
        "name" to "String",
        "coordinate_x" to "Double",
        "coordinate_y" to "Float",
        "creationDate" to "String",
        "realHero" to "Boolean",
        "hasToothpick" to "Boolean",
        "impactSpeed" to "Long",
        "soundtrackName" to "String",
        "minutesOfWaiting" to "Double?",
        "mood" to "Mood?",
        "car" to "String?",
        "creator" to "String"
    )
    return fields
}
