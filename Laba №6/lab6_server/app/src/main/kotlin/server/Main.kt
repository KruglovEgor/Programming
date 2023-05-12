package server

import server.base_classes.HumanBeing
import server.commands.Invoker
import server.commands.SaveCommand
import server.helping_functions.convertJSONtoLinkedListOfMutableMapOfStringAndAny
import server.helping_functions.convertJSONtoMapOfStringAndAny
import server.helping_functions.convertMapToJSON
import server.helping_functions.readFromFileOrCreateFile
import server.interactive.getClassesOfCommands
import server.interactive.getParametersOfCommands
import java.time.LocalDateTime
import java.util.*
import server.interactive.makeListOfHumanBeing
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess


var currentId : Int = 1
var dateOfInitialization = LocalDateTime.of(LocalDateTime.now().year,
    LocalDateTime.now().monthValue, LocalDateTime.now().dayOfMonth,
    LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second)
var listOfData = LinkedList<MutableMap<String, Any?>>()
var listOfHumanBeing = LinkedList<HumanBeing>()
var ongoing = true
val logger: Logger = Logger.getLogger("ServerLogger")

fun main(){
    val data = readFromFileOrCreateFile("Data.json")
    if (data.isEmpty()){
        listOfData = LinkedList<MutableMap<String, Any?>>()
        listOfHumanBeing = LinkedList<HumanBeing>()
    }
    else{
        try {
            listOfData = convertJSONtoLinkedListOfMutableMapOfStringAndAny(data)
            makeListOfHumanBeing()
        } catch (e : Exception){
            println(e.message)
            listOfData = LinkedList<MutableMap<String, Any?>>()
            listOfHumanBeing = LinkedList<HumanBeing>()
        }
    }
    startServer()
}


val invoker = Invoker()

fun startServer() {
    logger.log(Level.INFO, "Server started")

   val serverSocket = DatagramSocket(8080)
//    val serverSocket = DatagramSocket(28538, InetAddress.getByName("77.234.214.82"))
    val buffer = ByteArray(1024)
    val executor: ExecutorService = Executors.newFixedThreadPool(8)

    startReadingFromConsole()

    while (ongoing) {
        val packet = DatagramPacket(buffer, buffer.size)
        serverSocket.receive(packet)

        // Создаем новый поток для обработки запроса от клиента
        executor.execute{
            handleClientRequest(packet, serverSocket)
        }
    }

}

fun handleClientRequest(packet: DatagramPacket, serverSocket: DatagramSocket) {
    val clientAddress = packet.socketAddress as InetSocketAddress
    val message = convertJSONtoMapOfStringAndAny(String(packet.data, 0, packet.length))

    logger.log(Level.INFO, "Received message from client $clientAddress: $message")

    if(message["type"] == "ping"){
        ping(clientAddress, serverSocket)
    }
    else if (message["type"] == "exec_command"){
        execCommand(clientAddress, serverSocket, message)
    }
}

fun ping(clientAddress: InetSocketAddress, serverSocket: DatagramSocket){
    val map = mutableMapOf<String, Any?>()
    try {
        map["result"] = getParametersOfCommands()
        map["success"] = true
        map["message"] = ""
    } catch (e: Exception){
        map["success"] = false
        map["message"] = e.message
    }
    val responseData = convertMapToJSON(map).toByteArray()
    val responsePacket = DatagramPacket(responseData, responseData.size, clientAddress.address, clientAddress.port)
    serverSocket.send(responsePacket)
    logger.log(Level.INFO, "Sent to $clientAddress:  ${convertMapToJSON(map)}")
}

fun execCommand(clientAddress: InetSocketAddress, serverSocket: DatagramSocket, message: Map<String, Any?>){
    val map = mutableMapOf<String, Any?>()
    try {
        invoker.setCommand(getClassesOfCommands()[message["command"]]!!)
        val result = invoker.executeCommand(message["params"] as Map<String, Any?>)
        map["success"] = result.success
        map["message"] = result.message
        map["result"] = result.result
    } catch (e: Exception){
        map["success"] = false
        map["message"] = e.message
        map["result"] = "Error $message"
    }
    val responseData = convertMapToJSON(map).toByteArray()
    val responsePacket = DatagramPacket(responseData, responseData.size, clientAddress.address, clientAddress.port)
    serverSocket.send(responsePacket)
    logger.log(Level.INFO, "Sent to $clientAddress:  ${convertMapToJSON(map)}")
}


fun startReadingFromConsole(){
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