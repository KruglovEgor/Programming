package client.GUI.Map


import client.GUI.Localization.Localization
import client.buffer
import client.channel
import client.connection.sendAndReceiveDataWithCheckingValidity
import client.helping_functions.convertJSONtoListOfMapAndString
import client.interactive.*
import client.selector
import client.serverAddress
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
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


class MapView(var currentLanguage: String = "ru", val login: String = "11") : View() {
    private var humans = mutableListOf<HumanBeing>()
    private val humanImage = Image("C:\\Users\\tentu\\OneDrive\\Рабочий стол\\ITMO\\2 семестр\\Прога\\lab8\\lab8_client\\app\\src\\main\\kotlin\\client\\color_human_small.png")
    private val mapImage = Image("C:\\Users\\tentu\\OneDrive\\Рабочий стол\\ITMO\\2 семестр\\Прога\\lab8\\lab8_client\\app\\src\\main\\kotlin\\client\\map.png")
    override val root = stackpane {
        prefWidth = 1598.0
        prefHeight = 863.0
        title = "Map view"

    }
    init {
        setMap()
    }



    private fun setMap(){
        root.clear()
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
        root.add(group)
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
            for ((param, type) in getQuestionsForUnit()){
                val question = "$param ($type)"
                val answerField = TextField(mapByUnit[param].toString())
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
            maxHeight = 863.0
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