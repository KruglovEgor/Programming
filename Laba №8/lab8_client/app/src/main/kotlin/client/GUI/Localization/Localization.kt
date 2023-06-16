package client.GUI.Localization

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
                "noSuchCommand" to "Нет такой команды!"
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
                "noSuchCommand" to "There is no such command!"
            )
        )
    }
}


fun getTranslation(currentLanguage: String, key: String): String {
    return Localization.translations[currentLanguage]?.get(key) ?: key
}