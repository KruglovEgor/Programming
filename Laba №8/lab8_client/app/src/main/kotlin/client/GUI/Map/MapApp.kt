package client.GUI.Map


import client.buffer
import client.channel
import client.connection.sendAndReceiveDataWithCheckingValidity
import client.helping_functions.convertJSONtoListOfMapAndString
import client.selector
import client.serverAddress
import javafx.animation.*
import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.paint.Color

import javafx.util.Duration
import server.base_classes.HumanBeing
import tornadofx.*
import javafx.scene.shape.Rectangle
import java.security.MessageDigest


class MapView(val login: String = "11") : View() {
    private var humans = mutableListOf<HumanBeing>()
    private val updateInterval:Long = 3

    override val root = scrollpane {
        val canvasWidth = 1000.0
        val canvasHeight = 1000.0
        val image = Image("C:\\Users\\tentu\\OneDrive\\Рабочий стол\\ITMO\\2 семестр\\Прога\\lab8\\lab8_client\\app\\src\\main\\kotlin\\client\\human_small.png")
        val canvas = canvas(canvasWidth, canvasHeight)
        val sizeOfHuman = 32.0

        // Обнови оси и засечки при изменении размеров окна
        fun updateAxes() {
            val scaleX = canvas.width / canvasWidth
            val scaleY = canvas.height / canvasHeight

            val gc = canvas.graphicsContext2D

            // Очисти Canvas
            gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

            // Нарисуй оси
            gc.stroke = Color.BLACK
            gc.lineWidth = 2.0
            gc.strokeLine(0.0, canvas.height / 2, canvas.width, canvas.height / 2) // Ось x
            gc.strokeLine(canvas.width / 2, 0.0, canvas.width / 2, canvas.height) // Ось y

            // Нарисуй засечки и их значения
            gc.stroke = Color.GRAY
            gc.lineWidth = 1.0
            val stepSize = 25
            val numStepsX = (canvasWidth / scaleX).toInt() / stepSize
            val numStepsY = (canvasHeight / scaleY).toInt() / stepSize

            for (i in 1 until numStepsX) {
                val x = i * stepSize * scaleX
                gc.strokeLine(x, (canvas.height / 2) - 5, x, (canvas.height / 2) + 5)
                gc.fillText((x - (canvas.width / 2)).toInt().toString(), x, (canvas.height / 2) + 20)
            }

            for (i in 1 until numStepsY) {
                val y = i * stepSize * scaleY
                gc.strokeLine((canvas.width / 2) - 5, y, (canvas.width / 2) + 5, y)
                gc.fillText((y - (canvas.height / 2)).toInt().toString(), (canvas.width / 2) + 10, y)
            }
        }

        fun drawImageOnCanvas(image: Image, unit: HumanBeing, width_: Double, height_:Double, login: String) {
            val gc = canvas.graphicsContext2D

//            val imageView = imageview(image)
//            imageView.setOnMouseClicked {
//                println(1)
//            }

            // Определение координат относительно центра Canvas
            val centerX = canvas.width / 2
            val centerY = canvas.height / 2
            val scaleX = canvas.width / canvasWidth
            val scaleY = canvas.height / canvasHeight

            val x_ = centerX + unit.coordinate_x.value * scaleX - width_ / 2
            val y_ = centerY - unit.coordinate_y.value.toDouble() * scaleY - height_ / 2

            // Отрисовка изображения
            gc.apply {
                fill = Color.TRANSPARENT // Установка прозрачного цвета фона
                fill = getColorFromHash(getHash(unit.creator.value))
                fillRect(x_, y_, width_, height_) // Заполнение фона прямоугольником
                drawImage(image, x_, y_, width_, height_)
            }
//            imageView.xProperty().bind(x_.toProperty())
//            imageView.yProperty().bind(y_.toProperty())
//            imageView.tooltip {
//                text=unit.name.value
//            }
            //canvas.add(imageView)
        }


        fun playAnimation(added: List<HumanBeing>, removed: List<HumanBeing>) {
            val currentSize = SimpleDoubleProperty(0.0)
            val timeline = Timeline(
                KeyFrame(Duration.ZERO, KeyValue(currentSize, 1)),
                KeyFrame(1.seconds, KeyValue(currentSize, sizeOfHuman))
            )
            //updateAxes()

            timeline.currentTimeProperty().addListener { _, _, _ ->
                updateAxes()
                added.forEach { human -> drawImageOnCanvas(image, human, currentSize.value, currentSize.value, human.creator.value) }
                removed.forEach { human -> drawImageOnCanvas(image, human, sizeOfHuman - currentSize.value, sizeOfHuman - currentSize.value, human.creator.value) }
            }
            timeline.play()
            add(canvas)
        }


        // Установи ползунки прокрутки
        hbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
        vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS

        // Обнови оси при изменении размеров окна
        canvas.widthProperty().addListener { _, _, _ -> updateAxes() }
        canvas.heightProperty().addListener { _, _, _ -> updateAxes() }

        val timeline = Timeline()
        val keyFrame = KeyFrame(
            Duration.seconds(updateInterval.toDouble()),
            {
                val updatedHumans = getCollectionOfHumans()
                val added = updatedHumans.filter { newHuman ->
                    humans.none { it.id.value == newHuman.id.value }
                }
                val removed = humans.filter { oldHuman ->
                    updatedHumans.none { it.id.value == oldHuman.id.value }
                }

                if (added.isNotEmpty() || removed.isNotEmpty()){
                    playAnimation(added, removed)
                }
                humans = updatedHumans
            }
        )
        timeline.cycleCount = Timeline.INDEFINITE
        timeline.keyFrames.add(keyFrame)
        timeline.play()

        // Обнови оси после открытия окна
        Platform.runLater {
            updateAxes()
        }
        content = canvas

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

    private fun showTooltip(unit: HumanBeing, node: Node) {
    val tooltip = Tooltip(unit.name.value)
    tooltip.show(node, node.scene.window.x + node.localToScene(0.0, 0.0).x + node.scene.window.width, node.scene.window.y + node.localToScene(0.0, 0.0).y + node.scene.window.height)
}


    fun showUnitDetails(unit: HumanBeing) {
        val alert = Alert(Alert.AlertType.INFORMATION).apply {
            title = "Unit Details"
            headerText = "Full Information"
            contentText = "Name: ${unit.name}\n"}

        val changeButtonType = ButtonType("Change")
        alert.buttonTypes.add(changeButtonType)

        val result = alert.showAndWait()
        if (result.orElse(null) == changeButtonType) {
            // Действия при нажатии кнопки "Change"
            // Например, открытие окна или выполнение других действий
        }
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
            maxWidth = 1050.0
            maxHeight = 1050.0
        }
    }
}





class MapApp : App(MapView::class)