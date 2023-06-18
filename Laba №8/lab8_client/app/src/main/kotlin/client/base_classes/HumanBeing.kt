package server.base_classes

import client.base_classes.Mood
import javafx.beans.property.*
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
class HumanBeing(map: Map<String, Any?>) : Comparable<HumanBeing>  {
    val id = SimpleIntegerProperty((map["id"] as? Number ?: 0).toInt())
    val name = SimpleStringProperty(map["name"] as? String ?: "DefaultName")
    val coordinate_x = SimpleDoubleProperty((map["coordinate_x"] as? Number)?.toDouble() ?: 0.0)
    val coordinate_y = SimpleFloatProperty((map["coordinate_y"] as? Number)?.toFloat() ?: 0.0F)
    val creationDate = SimpleStringProperty(map["creationDate"] as? String? ?: ("${LocalDateTime.now().year}-${LocalDateTime.now().monthValue}" +
            "-${LocalDateTime.now().dayOfMonth} ${LocalDateTime.now().hour}:${LocalDateTime.now().minute}:${LocalDateTime.now().second}"))

    val realHero = SimpleBooleanProperty(map["realHero"] as? Boolean ?: false)
    val hasToothPick = SimpleBooleanProperty(map["hasToothpick"] as? Boolean ?: false)
    val impactSpeed = SimpleLongProperty((map["impactSpeed"] as? Number)?.toLong() ?: 0)
    val soundtrackName = SimpleStringProperty( map["soundtrackName"] as? String ?: "DefaultSoundtrackName")
    val minutesOfWaiting = SimpleObjectProperty<Double?>(map["minutesOfWaiting"] as? Double?)
    val mood = SimpleObjectProperty<String?>(map["mood"] as? String?)
    val car = SimpleObjectProperty<String?>(map["car"] as? String?)
    val creator = SimpleStringProperty(map["creator"] as? String ?: "undefined")

    override fun compareTo(other: HumanBeing): Int {
        TODO("Not yet implemented")
    }

    fun makeMap() : Map<String, Any?>{
        return mapOf(
            "id" to id.value,
            "name" to name.value,
            "coordinate_x" to coordinate_x.value,
            "coordinate_y" to coordinate_y.value,
            "creationDate" to creationDate.value,
            "realHero" to realHero.value,
            "hasToothPick" to hasToothPick.value,
            "impactSpeed" to impactSpeed.value,
            "soundtrackName" to soundtrackName.value,
            "minutesOfWaiting" to minutesOfWaiting.value,
            "mood" to mood.value,
            "car" to car.value,
            "creator" to creator.value
        )
    }

}




