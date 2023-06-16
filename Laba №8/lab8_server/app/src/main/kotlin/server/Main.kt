package server

import server.base_classes.HumanBeing
import server.commands.Invoker
import server.commands.SaveCommand
import server.connection.*
import java.time.LocalDateTime
import java.sql.DriverManager
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess
import server.interactive.createCollection
import java.sql.Connection
import java.util.concurrent.ConcurrentLinkedDeque


var dateOfInitialization = LocalDateTime.of(LocalDateTime.now().year,
    LocalDateTime.now().monthValue, LocalDateTime.now().dayOfMonth,
    LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second)
var listOfHumanBeing = ConcurrentLinkedDeque<HumanBeing>()
var ongoing = true
val logger: Logger = Logger.getLogger("ServerLogger")
val invoker = Invoker()

//todo add reading stuff for connecting to db from file (like json or yaml)
//todo add more information for update
const val url = "jdbc:postgresql://localhost:5432/Lab7"
const val username = "postgres"
const val password = "postgres16"


fun main(){
    val connection = DriverManager.getConnection(url, username, password)
    logger.log(Level.INFO, "Connected to DB")

    listOfHumanBeing = createCollection(connection)
    startServer(connection)
}


fun startReadingFromConsole(connection: Connection){
    val consoleInputThread = Thread {
        while (ongoing){
            val input = readln()
            if (input == "save"){
                val saveCommand = SaveCommand()
                val result = saveCommand.execute(mapOf("1" to 1))
                logger.log(Level.INFO, "Saved: ${result.result}")
            }
            else if(input == "exit"){
                ongoing = false
                val saveCommand = SaveCommand()
                val result = saveCommand.execute(mapOf("1" to 1))
                connection.close()
                logger.log(Level.INFO, "Exited and saved: ${result.result}")
                exitProcess(1)
            }
            else{
                logger.log(Level.WARNING, "You can write 'save' or 'exit' only!")
            }
        }
    }
    consoleInputThread.start()
}



