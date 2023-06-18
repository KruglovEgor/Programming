package client.GUI.SignIn

import client.*
import client.GUI.Localization.Localization
import client.GUI.Localization.getTranslation
import client.GUI.MainScene.MainView
import client.connection.sendAndReceiveDataWithCheckingValidity
import javafx.animation.TranslateTransition
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.animation.Interpolator
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.stage.Stage
import javafx.util.Duration
import org.apache.commons.codec.digest.DigestUtils
import org.controlsfx.control.Notifications
import tornadofx.*
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.Selector
import javax.swing.text.StyledEditorKit.BoldAction


class AuthApp : App(AuthView::class){
    override fun start(stage: Stage) {
        //val authView = AuthView(serverAddress, buffer, channel, selector)

        super.start(stage)
        stage.width = 900.0
        stage.height = 900.0
    }

}

class AuthView(language : String = "ru") : View() {
    override val root = BorderPane()
    //default language
    private var currentLanguage = language

    private val label: Label
    private val languageLabel: Label
    private val loginField = TextField()
    private val passwordField = PasswordField()
    private var loginPromptText: String
    private var passwordPromptText: String
    private var authorizationButton: Button
    private val registrationLabel: Label
    private val registrationLink : Button


    init {
        title = "App"
        label = Label(Localization.translations[currentLanguage]?.get("auth") ?: "auth").apply {
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
            center = label
            top = languageBox
        }

        loginPromptText = Localization.translations[currentLanguage]?.get("loginPrompt") ?: "loginPrompt"
        passwordPromptText = Localization.translations[currentLanguage]?.get("passwordPrompt") ?: "passwordPrompt"

        loginField.promptText = loginPromptText
        passwordField.promptText = passwordPromptText

        loginField.style = "-fx-font-size: 18px;"
        passwordField.style = "-fx-font-size: 18px;"

        val loginBox = HBox(10.0, loginField).apply {
            alignment = Pos.CENTER
        }
        val passwordBox = HBox(10.0, passwordField).apply {
            alignment = Pos.CENTER
        }

        val inputPane = VBox(10.0).apply {
            alignment = Pos.CENTER
            children.addAll(loginBox, passwordBox)
        }


        authorizationButton = Button( Localization.translations[currentLanguage]?.get("authorizationButton") ?: "authorizationButton").apply {
            style {
                fontSize = 18.px
                backgroundColor += c("#00FF00")
                textFill = Color.WHITE
            }
        }

        registrationLabel = Label(Localization.translations[currentLanguage]?.get("registrationLabel") ?: "registrationLabel")
        registrationLink = Button(Localization.translations[currentLanguage]?.get("registrationLink") ?: "registrationLink")

        registrationLink.setOnAction {
            registration(currentLanguage)
        }

        val registrationBox = HBox(10.0, registrationLabel, registrationLink).apply {
            alignment = Pos.CENTER
        }

        val centerPane = VBox(20.0, inputPane, authorizationButton, registrationBox).apply {
            alignment = Pos.CENTER
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

        authorizationButton.action {
            if (loginField.text.isNotEmpty() && passwordField.text.isNotEmpty()){
                sendAuthorization(loginField.text, passwordField.text)
            } else{
                Notifications.create()
                    .title("Уведомление")
                    .text(Localization.translations[currentLanguage]?.get("emptyFieldsException") ?: "emptyFieldsException")
                    .owner(currentWindow)
                    .hideAfter(Duration.seconds(2.0))
                    .position(Pos.BOTTOM_RIGHT)
                    .darkStyle()
                    .graphic(null)
                    .show()
            }

        }

    }


    private fun updateTranslations() {
        label.text = getTranslation(currentLanguage, "auth")
        languageLabel.text = getTranslation(currentLanguage,"languageLabel")
        loginPromptText = getTranslation(currentLanguage,"loginPrompt")
        passwordPromptText = getTranslation(currentLanguage,"passwordPrompt")
        loginField.promptText = loginPromptText
        passwordField.promptText = passwordPromptText
        authorizationButton.text = getTranslation(currentLanguage,"authorizationButton")
        registrationLabel.text = getTranslation(currentLanguage,"registrationLabel")
        registrationLink.text = getTranslation(currentLanguage,"registrationLink")
    }



    private fun registration(currentLanguage: String) {
        val transition = TranslateTransition(Duration.seconds(1.0), primaryStage.scene.root)
        transition.interpolator = Interpolator.EASE_BOTH
        transition.fromY = 0.0
        transition.toY = -primaryStage.scene.height

        transition.setOnFinished {
            replaceWith(RegView(currentLanguage))
        }

        transition.play()
    }


    private fun goToMain(currentLanguage: String, login: String){
        val transition = TranslateTransition(Duration.seconds(1.0), primaryStage.scene.root)
        transition.interpolator = Interpolator.EASE_BOTH
        transition.fromY = 0.0
        transition.toY = -primaryStage.scene.height

        transition.setOnFinished {
            replaceWith(MainView(currentLanguage, login))
        }

        transition.play()
    }


    private fun sendAuthorization(login: String, password: String){
        HashedPassword = DigestUtils.sha512Hex(password)
        val dataToSend = mapOf(
            "type" to "login",
            "login" to login,
            "password" to HashedPassword
        )
        val response = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, dataToSend, selector)
        if(response["success"] as Boolean){
            goToMain(currentLanguage, login)
        }
        else{
            Notifications.create()
                .title("Уведомление")
                .text(response["message"].toString())
                .owner(currentWindow)
                .hideAfter(Duration.seconds(2.0))
                .position(Pos.BOTTOM_RIGHT)
                .darkStyle()
                .graphic(null)
                .show()
        }

    }
}
