package client.GUI.MainScene

import client.*
import client.GUI.Localization.Localization
import client.GUI.Localization.getTranslation
import client.GUI.Map.MapView
import client.base_classes.Result
import client.connection.sendAndReceiveDataWithCheckingValidity
import client.helping_functions.convertJSONtoListOfMapAndString
import client.interactive.*
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.util.Duration
import org.controlsfx.control.Notifications
import server.base_classes.HumanBeing
import server.base_classes.makeLocalDateTime
import tornadofx.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

class MainApp : App(MainView::class){
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 900.0
        stage.height = 900.0
    }
}


class MainView(language: String = "ru", val login: String = "test") : View() {
    override val root = BorderPane()
    private var currentLanguage = language
    private val languageLabel: Label
    private val commandLabel: Label
    private val sendButton: Button
    private val humans = FXCollections.observableArrayList<HumanBeing>()
    private val resultLabel: Label
    private val resultField: Text
    private val mapButton: Button
    private var tableView = TableView<HumanBeing>()
    var table = vbox {
        tableView = tableview(humans){
        column("id", HumanBeing::id)
        column("name", HumanBeing::name)
        column("coordinate_x", HumanBeing::coordinate_x)
        column("coordinate_y", HumanBeing::coordinate_y)
        column("creationDate", HumanBeing::creationDate) {
            cellFormat {
                val data = makeLocalDateTime(it)
                val deltaTime = Localization.deltaTime[currentLanguage] ?: 0.0
                val newData = data.plusHours(deltaTime.toLong())
                text = newData.format(Localization.dataFormatter[currentLanguage])
            }
        }
        column("realHero", HumanBeing::realHero)
        column("hasToothPick", HumanBeing::hasToothpick)
        column("impactSpeed", HumanBeing::impactSpeed)
        column("soundtrackName", HumanBeing::soundtrackName)
        column("minutesOfWaiting", HumanBeing::minutesOfWaiting)
        column("mood", HumanBeing::mood)
        column("car", HumanBeing::car)
        column("creator", HumanBeing::creator)
        }
    }


    init {
        getCollection()
        title = "App"
        val loginLabel = Label(login).apply {
            style {
                textFill = Color.WHITE
                fontSize = 24.px
            }
        }

        languageLabel = Label(Localization.translations[currentLanguage]?.get("languageLabel") ?: "languageLabel").apply {
            style {
                textFill = Color.WHITE
                fontSize = 24.px
            }
        }
        val languageComboBox = ComboBox(FXCollections.observableArrayList(Localization.translations.keys))
        // Установка текущего языка в выпадающем списке
        languageComboBox.value = currentLanguage

        val languageBox = HBox(10.0, languageLabel, languageComboBox).apply {
            alignment = Pos.CENTER_RIGHT
        }

        val topPane = BorderPane().apply {
            style {
                backgroundColor += c("#0000FF")
            }
            center = loginLabel
            top = languageBox
        }

        commandLabel = Label(Localization.translations[currentLanguage]?.get("commandLabel") ?: "commandLabel").apply {
            style{
                fontSize = 18.px
            }
        }
        val commandField = TextField()
        sendButton = Button(Localization.translations[currentLanguage]?.get("sendButton") ?: "sendButton").apply {
            style{
                fontSize = 18.px
                backgroundColor += c("#00FF00")
                textFill = Color.WHITE
            }
        }
        resultLabel = Label(Localization.translations[currentLanguage]?.get("resultLabel") ?: "resultLabel").apply {
            style{
                fontSize = 18.px
            }
        }
        resultField = text("").apply {
            style{
                fontSize = 18.px
            }
        }

        val commandBox = VBox(10.0, commandLabel, commandField, sendButton, resultLabel, resultField).apply {
            alignment = Pos.CENTER
        }

        mapButton = Button(Localization.translations[currentLanguage]?.get("mapButton") ?: "mapButton")
        mapButton.action {
            replaceWith(MapView(currentLanguage, login))
        }
        mapButton.apply { style{fontSize=18.px} }


        val centerPane = BorderPane().apply {
            top = commandBox
            center = table
            bottom = mapButton
        }

        with(root) {
            style {
                backgroundColor += c("#FFFFFF")
            }
            top = topPane
            center = centerPane
        }

        languageComboBox.valueProperty().addListener { _, _, newLanguage ->
            if (Localization.translations.containsKey(newLanguage)) {
                currentLanguage = newLanguage
                updateTranslations()
            }
        }


        sendButton.action {
            try {
                val commandFromUser = commandField.text.trim()
                if ((commandFromUser == "show") or (commandFromUser == "printAscending")) {
                    getCollection()
                    resultField.text = "Success"
                }
                else if(commandFromUser.isNotEmpty()){
                    var cancelled = false
                    val map = mutableMapOf<String, Any?>()
                    if (checkIfCommandExists(commandFromUser)){
                        map["type"] = "exec_command"
                        map["command"] = commandFromUser
                        val neededParams = getParameters(getParametersOfCommands()[commandFromUser]!!)
                        map["params"] = mutableMapOf<String, Any?>("creator" to login)
                        if (neededParams.isNotEmpty()){
                            val parametersFromClient = dialogAboutParameters(neededParams)
                            if (((map["params"] as MutableMap<String, Any?>?).isNullOrEmpty())) cancelled = true
                            else parametersFromClient!!.forEach { (key, value) -> (map["params"] as MutableMap<String, Any?>)[key] = value }
                        }
                        if (!cancelled){
                            val response = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, map, selector)
                            resultField.text = response["result"].toString()
                        }
                    }
                    else{
                        resultField.text = Localization.translations[currentLanguage]?.get("noSuchCommand") ?: "noSuchCommand"
                    }
                }
            } catch (e: Exception){
                println(e.message)
                println(e.stackTrace)
                Notifications.create()
                    .title("Уведомление")
                    .text(e.message.toString())
                    .owner(currentWindow)
                    .hideAfter(Duration.seconds(10.0))
                    .position(Pos.BOTTOM_RIGHT)
                    .darkStyle()
                    .graphic(null)
                    .show()
            }
        }

    }

    private fun dialogAboutParameters(neededParams: List<Pair<String, String>>) : MutableMap<String, Any?>? {
        val futureResult = CompletableFuture<MutableMap<String, Any?>?>()
        val dialog = Dialog<List<String>>()
        dialog.title = Localization.translations[currentLanguage]?.get("dialogTitle") ?: "dialogTitle"
        dialog.headerText = Localization.translations[currentLanguage]?.get("dialogHeaderText") ?: "dialogHeaderText"

        val params = mutableMapOf<String, Any?>()
        val parametersAndAnswers = mutableMapOf<String, TextField>()
        val answersAndTypes = mutableMapOf<TextField, String>()
        val vbox = VBox()
        for (i in neededParams){
            if(i.first == "unit"){
                for ((param, type) in getQuestionsForUnit()){
                    val question = "$param ($type)"
                    val answerField = TextField()
                    parametersAndAnswers[param] = answerField
                    answersAndTypes[answerField] = type
                    vbox.add(HBox(10.0, label(question), answerField))
                }
            }
            else{
                val question = "${i.first} (${i.second})"
                val answerField = TextField()
                parametersAndAnswers[i.first] = answerField
                answersAndTypes[answerField] = i.second
                vbox.add(HBox(10.0, label(question), answerField))
            }
        }

        val sendButton = Button("Send")
        sendButton.action {
            if(answersAndTypes.size == 1 && answersAndTypes.values.first() == "Path"){
                if (checkPathForExecuteScript(answersAndTypes.keys.first().text)){
                    executeScript(answersAndTypes.keys.first().text, currentLanguage, currentWindow, login)
                    println(inputList)
                    for (map in inputList){

                        val response = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, map+ mapOf("type" to "exec_command"), selector)
                        resultField.text = response["result"].toString()
                    }
                    dialog.close()
                    futureResult.complete(null)
                }
                else{
                    Notifications.create()
                        .title("Уведомление")
                        .text(Localization.translations[currentLanguage]?.get("cantReadFileException") ?: "cantReadFileException")
                        .owner(currentWindow)
                        .hideAfter(Duration.seconds(10.0))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle()
                        .graphic(null)
                        .show()
                }
            }

            else if(checkAnswers(answersAndTypes)){
                for ((param, ans) in parametersAndAnswers){
                    params[param] = convertToNeededType(ans.text, answersAndTypes[ans])
                }
                dialog.close()
                futureResult.complete(params)
            }

        }
        vbox.add(sendButton)
        dialog.dialogPane.content = vbox
        val cancelButton = ButtonType.CANCEL
        dialog.dialogPane.buttonTypes.addAll(cancelButton)

        dialog.dialogPane.lookupButton(cancelButton).addEventFilter(ActionEvent.ACTION) {
            dialog.close()
            futureResult.complete(null)
        }
        dialog.setOnCloseRequest {
            dialog.close()
            if (params.isNullOrEmpty()) futureResult.complete(null)
        }

        dialog.showAndWait()


        return futureResult.join()
    }


    private fun getCollection() : Result{
        val mapToSend = mapOf(
            "type" to "exec_command",
            "command" to "show",
            "params" to mapOf("1" to 1)
        )
        val response = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, mapToSend, selector)
        if (response["success"] as Boolean){
            humans.clear()
            val result = convertJSONtoListOfMapAndString(response["result"].toString())
            for(i in result){
                humans.add(HumanBeing(i))
            }
        }
        else{
            resultField.text = response["result"].toString()
        }
        return Result(response["success"] as Boolean, response["message"].toString(), response["result"].toString())
    }


    private fun updateTranslations() {
        languageLabel.text = getTranslation(currentLanguage,"languageLabel")
        commandLabel.text = getTranslation(currentLanguage, "commandLabel")
        sendButton.text = getTranslation(currentLanguage, "sendButton")
        mapButton.text = getTranslation(currentLanguage, "mapButton")
        updateCreationDateColumn()
        tableView.refresh()
    }

    private fun updateCreationDateColumn(){
        val creationDateColumn = tableView.columns.firstOrNull {  it.text == "creationDate"} as? TableColumn<HumanBeing, String>
        creationDateColumn?.cellFormat {
            val data = makeLocalDateTime(it)
            val deltaTime = Localization.deltaTime[currentLanguage] ?: 0.0
            val newData = data.plusHours(deltaTime.toLong())
            text = newData.format(Localization.dataFormatter[currentLanguage])
        }
    }



}