package client.GUI.Map


import client.GUI.Localization.Localization
import client.GUI.Localization.getTranslation
import client.GUI.MainScene.MainView
import client.buffer
import client.channel
import client.connection.sendAndReceiveDataWithCheckingValidity
import client.helping_functions.convertJSONtoListOfMapAndString
import client.interactive.*
import client.selector
import client.serverAddress
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.util.Duration
import org.controlsfx.control.Notifications
import server.base_classes.HumanBeing
import tornadofx.*
import java.security.MessageDigest
import java.util.concurrent.CompletableFuture


class MapView(language: String = "ru", val login: String = "test") : View() {
    private var humans = mutableListOf<HumanBeing>()
    private val humanImage = Image("color_human_small.png")
    private val mapImage = Image("map.png")
    private var currentLanguage = language
    private val backButton = Button(Localization.translations[currentLanguage]?.get("backButton") ?: "backButton")
    private val languageLabel = Label(Localization.translations[currentLanguage]?.get("languageLabel") ?: "languageLabel")
    private val offsetY = 20.0
    override val root = stackpane {
        prefWidth = 1598.0
        prefHeight = 863.0
        title = "Map view"

    }

    init {
        setMap()
        root.alignment = Pos.TOP_LEFT
        val timeline = Timeline(
            KeyFrame(Duration.seconds(3.0), { setMap()
            })
        )
        timeline.cycleCount = Timeline.INDEFINITE
        timeline.play()
    }

    private fun getToolbar(): ToolBar {
        val toolBar = ToolBar()
        backButton.action {
            replaceWith(MainView(currentLanguage, login))
        }
        languageLabel.apply {
            style {
                textFill = Color.BLACK
                fontSize = 18.px
            }
        }
        val languageComboBox = ComboBox(FXCollections.observableArrayList(Localization.translations.keys))
        // Установка текущего языка в выпадающем списке
        languageComboBox.value = currentLanguage

        val languageBox = HBox(10.0, languageLabel, languageComboBox).apply {
            alignment = Pos.CENTER_RIGHT
        }

        languageComboBox.valueProperty().addListener { _, _, newLanguage ->
            if (Localization.translations.containsKey(newLanguage)) {
                currentLanguage = newLanguage
                updateTranslations()
            }
        }
        val leftBox = HBox(10.0, backButton).apply {
            alignment = Pos.CENTER_LEFT
        }

        val rightBox = HBox(10.0, languageBox).apply {
            alignment = Pos.CENTER_RIGHT
            HBox.setHgrow(this, Priority.ALWAYS)
        }

        toolBar.apply {
            style {
                alignment = Pos.TOP_LEFT
            }
        }
        toolBar.items.addAll(leftBox, rightBox)

        return toolBar
    }

    private fun updateTranslations(){
        backButton.text = getTranslation(currentLanguage, "backButton")
        languageLabel.text = getTranslation(currentLanguage, "languageLabel")
    }

    private fun setMap(){
        root.clear()
        val vBox = VBox(0.0)
        vBox.children.add(getToolbar())
        val group = Group()
        val map = imageview(mapImage)
        group.children.add(map)
        humans=getCollectionOfHumans()
        for (human in humans){
            val imageview = imageview(humanImage)

            imageview.setOnMouseClicked {
                val map = mutableMapOf<String, Any?>()
                map["type"] = "exec_command"
                map["command"] = "update"
                map["params"] = mutableMapOf<String, Any?>()
                val parametersFromClient = dialogAboutParameters(human, login)
                if (!(parametersFromClient.isNullOrEmpty())) {
                    parametersFromClient.forEach { (key, value) -> (map["params"] as MutableMap<String, Any?>)[key] = value }
                    (map["params"]as MutableMap<String, Any?>)["creator"] = login
                    (map["params"]as MutableMap<String, Any?>)["id"] = human.id.value
                    val response = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, map, selector)
                    Notifications.create()
                        .title("Уведомление")
                        .text(response["result"].toString())
                        .owner(currentWindow)
                        .hideAfter(Duration.seconds(5.0))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle()
                        .graphic(null)
                        .show()
                }
            }

            imageview.xProperty().bind(human.coordinate_x)
            imageview.yProperty().bind(human.coordinate_y)
            imageview.tooltip { "${human.id.value}\n${human.name.value}\n(${human.coordinate_x.value},${human.coordinate_y.value})\n${human.creator.value}" }
            val bgRectangle = Rectangle().apply{
                width = imageview.boundsInParent.width
                height = imageview.boundsInParent.height
                x=imageview.x
                y=imageview.y
                fill = Color.TRANSPARENT
                fill=getColorFromHash(getHash(human.creator.value))
            }
            group.children.add(bgRectangle)
            group.children.add(imageview)
        }
        vBox.children.add(group)
        root.add(vBox)
    }

    private fun dialogAboutParameters(unit: HumanBeing, login: String) : MutableMap<String, Any?>?{
        val futureResult = CompletableFuture<MutableMap<String, Any?>?>()
        if (unit.creator.value == login){
            val dialog = Dialog<List<String>>()
            dialog.title = Localization.translations[currentLanguage]?.get("dialogTitle") ?: "dialogTitle"
            dialog.headerText = Localization.translations[currentLanguage]?.get("dialogHeaderText") ?: "dialogHeaderText"

            val params = mutableMapOf<String, Any?>()
            val parametersAndAnswers = mutableMapOf<String, TextField>()
            val answersAndTypes = mutableMapOf<TextField, String>()
            val vbox = VBox()
            val mapByUnit = unit.makeMap()
            println(mapByUnit["hasToothpick"])
            for ((param, type) in getQuestionsForUnit()){
                val question = "$param ($type)"
                var ans = mapByUnit[param].toString()
                if (mapByUnit[param] == null){
                    if (getDescriptionOfHumanBeingFields()[param] == "Boolean") ans = "false"
                    else ans = ""
                }

                val answerField = TextField(ans)
                parametersAndAnswers[param] = answerField
                answersAndTypes[answerField] = type
                vbox.add(HBox(10.0, label(question), answerField))
            }

            val sendButton = Button("Send")
            sendButton.action {
                if(checkAnswers(answersAndTypes)){
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

        }
        else{
            futureResult.complete(null)
            val dialog = Dialog<List<String>>()
            dialog.title = Localization.translations[currentLanguage]?.get("dialogTitle") ?: "dialogTitle"
            dialog.headerText = Localization.translations[currentLanguage]?.get("dialogHeaderText") ?: "dialogHeaderText"

            val vbox = VBox()
            val mapByUnit = unit.makeMap()
            for ((param, type) in getQuestionsForUnit()){
                val question = "$param ($type)"
                val paramField = Text(mapByUnit[param].toString())
                vbox.add(HBox(10.0, label(question), paramField))
            }

            dialog.dialogPane.content = vbox
            val cancelButton = ButtonType.CANCEL
            dialog.dialogPane.buttonTypes.addAll(cancelButton)

            dialog.dialogPane.lookupButton(cancelButton).addEventFilter(ActionEvent.ACTION) {
                dialog.close()
            }
            dialog.setOnCloseRequest {
                dialog.close()
            }

            dialog.showAndWait()
        }
        return futureResult.join()
    }


    private fun getCollectionOfHumans() : MutableList<HumanBeing>{
        val updatedHumans = mutableListOf<HumanBeing>()
        val mapToSend = mapOf(
            "type" to "exec_command",
            "command" to "show",
            "params" to mapOf("1" to 1)
        )
        val response = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, mapToSend, selector)
        if (response["success"] as Boolean){
            val result = convertJSONtoListOfMapAndString(response["result"].toString())
            for(i in result){
                updatedHumans.add(HumanBeing(i))
            }
            return updatedHumans
        }
        else return humans
    }


    private fun getHash(login: String) : ByteArray {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(login.toByteArray())
    }


    private fun getColorFromHash(hash: ByteArray): Color {
        val red = hash[0].toInt() and 0xFF
        val green = hash[1].toInt() and 0xFF
        val blue = hash[2].toInt() and 0xFF

        return Color.rgb(red, green, blue)
    }


    override fun onDock() {
        // Ограничь максимальный размер окна
        primaryStage.apply {
            maxWidth = 1598.0
            maxHeight = 863.0+offsetY
        }
    }
}





class MapApp : App(MapView::class){
    override fun start(stage: Stage) {
        val mapView = MapView()
        val scene = Scene(mapView.root)
        stage.scene = scene
        stage.show()
    }
}