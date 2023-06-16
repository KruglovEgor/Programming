package client.interactive

import javafx.scene.control.TextField

fun getQuestionsForUnit() : List<Pair<String, String>>{
    return listOf(
        Pair("name", "String"),
        Pair("coordinate_x", "Double"),
        Pair("coordinate_y", "Float"),
        Pair("realHero", "Boolean"),
        Pair("hasToothpick", "Boolean"),
        Pair("impactSpeed", "Long"),
        Pair("soundtrackName", "String"),
        Pair("minutesOfWaiting", "Double?"),
        Pair("mood", "Mood?"),
        Pair("car", "String?")
    )
}



fun checkAnswers(answersAndTypes: Map<TextField, String>) : Boolean{
    var isOk = true
    for ((answer, type) in answersAndTypes){
        if (!checkType(type, answer.text, false)){
            isOk = false
            answer.style = "-fx-text-box-border: red; -fx-focus-color: red;"
        }
        else answer.apply {
           answer.style = "-fx-text-box-border: green; -fx-focus-color: red;"
        }

    }

    return isOk
}