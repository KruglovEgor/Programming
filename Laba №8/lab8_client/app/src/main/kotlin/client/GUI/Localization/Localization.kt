package client.GUI.Localization

import java.time.format.DateTimeFormatter

class Localization {
    companion object {
        val translations = mapOf(
            "ru" to mapOf(
                "auth" to "Авторизация",
                "languageLabel" to "Язык:",
                "loginPrompt" to "Логин",
                "passwordPrompt" to "Пароль",
                "authorizationButton" to "Войти",
                "registrationLabel" to "Нет аккаунта?",
                "registrationLink" to "Регистрация",
                "registrationButton" to "Зарегестрироваться",
                "reg" to "Регистрация",
                "authorizationLabel" to "Уже есть аккаунт?",
                "authorizationLink" to "Авторизация",
                "commandLabel" to "Введите комманду ('help' чтобы ознакомиться со всеми командами):",
                "sendButton" to "Отправить",
                "resultLabel" to "Результат:",
                "dialogTitle" to "Параметры",
                "dialogHeaderText" to "Пожалуйста, введите параметры!",
                "noSuchCommand" to "Нет такой команды!",
                "emptyFieldsException" to "Поля не должны быть пустыми!",
                "mapButton" to "Перейти к карте",
                "backButton" to "Назад",
                "cantReadFileException" to "Не можем считать данные из файла",
                "metExecuteScript" to "В файле есть 'execute_script', он может привести к бесконечному зацикливанию!",
                "dialogTitleCanChange" to "Вы можете изменить параметры!",
                "dialogHeaderCanChange" to "Параметры юнита:",
                "dialogTitleCantChange" to "Вы не можете изменить параметры!",
                "dialogHeaderCantChange" to "Но вы можете на них посмотреть!"
                ),
            "en" to mapOf(
                "auth" to "Authorization",
                "languageLabel" to "Language:",
                "loginPrompt" to "Login",
                "passwordPrompt" to "Password",
                "authorizationButton" to "Sign in",
                "registrationLabel" to "Don't have account?",
                "registrationLink" to "Registration",
                "registrationButton" to "Create account",
                "reg" to "Registration",
                "authorizationLabel" to "Already have account?",
                "authorizationLink" to "Authorization",
                "commandLabel" to "Enter your command ('help' for the list of commands):",
                "sendButton" to "Send",
                "resultLabel" to "Result:",
                "dialogTitle" to "Parameters",
                "dialogHeaderText" to "Please, enter the parameters!",
                "noSuchCommand" to "There is no such command!",
                "emptyFieldsException" to "Fields can't be empty!",
                "mapButton" to "Go to map",
                "backButton" to "Back",
                "cantReadFileException" to "Can't read data from file!",
                "metExecuteScript" to "There is 'execute_script' in file. It may cause the infinity loop!",
                "dialogTitleCanChange" to "You can change parameters of unit!",
                "dialogHeaderCanChange" to "Here the parameters:",
                "dialogTitleCantChange" to "You can't change parameters!",
                "dialogHeaderCantChange" to "But you can see them:"
            )
        )

        val deltaTime = mapOf<String, Double>(
            "ru" to 0.0,
            "en" to 12.0
        )

        val dataFormatter = mapOf<String, DateTimeFormatter>(
            "ru" to DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
            "en" to DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
    }
}


fun getTranslation(currentLanguage: String, key: String): String {
    return Localization.translations[currentLanguage]?.get(key) ?: key
}