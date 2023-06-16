package client.GUI.SignIn

import client.*
import client.GUI.Localization.Localization
import client.GUI.Localization.getTranslation
import client.GUI.MainScene.MainView
import client.connection.sendAndReceiveDataWithCheckingValidity
import javafx.animation.Interpolator
import javafx.animation.TranslateTransition
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage

import javafx.util.Duration
import org.apache.commons.codec.digest.DigestUtils
import org.controlsfx.control.Notifications
import tornadofx.*

class RegApp : App(RegView::class){
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 900.0
        stage.height = 900.0
    }
}

class RegView(language: String = "ru") : View() {
    override val root = BorderPane()

    private var currentLanguage = language

    private val label : Label
    private val languageLabel : Label
    private val loginField = TextField()
    private val passwordField = PasswordField()
    private var loginPromptText : String
    private var passwordPromptText : String
    private val registrationButton : Button
    private val authorizationLabel : Label
    private val authorizationLink: Button

    init {
        title = "App"
        label = Label(Localization.translations[currentLanguage]?.get("reg") ?: "reg").apply {
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

        val registrationBox = VBox(10.0).apply {
            alignment = Pos.CENTER
            children.addAll(loginBox, passwordBox)
        }

        registrationButton = Button(Localization.translations[currentLanguage]?.get("registrationButton") ?: "registrationButton").apply {
            style{
                fontSize = 18.px
                backgroundColor += c("#00FF00")
                textFill = Color.WHITE
            }
        }

        authorizationLabel = Label(Localization.translations[currentLanguage]?.get("authorizationLabel") ?: "authorizationLabel")
        authorizationLink = Button(Localization.translations[currentLanguage]?.get("authorizationLink") ?: "authorizationLink")

        authorizationLink.setOnAction {
            login(currentLanguage)
        }

        val authorizationBox = HBox(10.0, authorizationLabel, authorizationLink).apply {
            alignment = Pos.CENTER
        }

        val centerPane = VBox(20.0, registrationBox, registrationButton, authorizationBox).apply {
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

        registrationButton.action {
            sendRegistration(loginField.text, passwordField.text)
        }

    }

    private fun updateTranslations() {
        label.text = getTranslation(currentLanguage, "reg")
        languageLabel.text = getTranslation(currentLanguage,"languageLabel")
        loginPromptText = getTranslation(currentLanguage,"loginPrompt")
        passwordPromptText = getTranslation(currentLanguage,"passwordPrompt")
        loginField.promptText = loginPromptText
        passwordField.promptText = passwordPromptText
        registrationButton.text = getTranslation(currentLanguage,"registrationButton")
        authorizationLabel.text = getTranslation(currentLanguage,"authorizationLabel")
        authorizationLink.text = getTranslation(currentLanguage,"authorizationLink")
    }

    private fun login(currentLanguage: String) {
        val transition = TranslateTransition(Duration.seconds(1.0), primaryStage.scene.root)
        transition.interpolator = Interpolator.EASE_BOTH
        transition.fromY = 0.0
        transition.toY = primaryStage.scene.height

        transition.setOnFinished {
            replaceWith(AuthView(currentLanguage))
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


    private fun sendRegistration(login: String, password: String){
        HashedPassword = DigestUtils.sha512Hex(password)
        val dataToSend = mapOf(
            "type" to "register",
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