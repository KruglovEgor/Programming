import base_classes.HumanBeing
import commands.Invoker
import commands.SaveCommand
import helping_functions.convertJSONtoLinkedListOfMutableMapOfStringAndAny
import helping_functions.convertJSONtoMapOfStringAndAny
import helping_functions.convertMapToJSON
import interactive.getClassesOfCommands
import interactive.getParametersOfCommands
import java.time.LocalDateTime
import java.util.*
import interactive.makeListOfHumanBeing
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess


var currentId : Int = 1
var dateOfInitialization = LocalDateTime.of(LocalDateTime.now().year,
    LocalDateTime.now().monthValue, LocalDateTime.now().dayOfMonth,
    LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second)
var listOfData = LinkedList<MutableMap<String, Any?>>()
var listOfHumanBeing = LinkedList<HumanBeing>()
var dataToSend = ""
var ongoing = true

fun main(){
    val inputStream = object {}.javaClass.getResourceAsStream("/Data.json")
    val dataFromFile = inputStream.bufferedReader().use { it.readText() }
    inputStream.close()
    listOfData = convertJSONtoLinkedListOfMutableMapOfStringAndAny(dataFromFile)

    makeListOfHumanBeing()
    startServer()
}


val invoker = Invoker()

fun startServer() {
    println("Server started")

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

    println("Received message from client $clientAddress: $message")

    if(message["type"] == "ping"){
        ping(clientAddress, serverSocket)
    }
    else if (message["type"] == "exec_command"){
        execCommand(clientAddress, serverSocket, message)
    }
    //println("Thread #${Thread.currentThread().id} is killed")
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
}

fun execCommand(clientAddress: InetSocketAddress, serverSocket: DatagramSocket, message: Map<String, Any?>){
    val map = mutableMapOf<String, Any?>()
    try {
        invoker.setCommand(getClassesOfCommands()[message["command"]]!!)
        val result = invoker.executeCommand(message["params"] as Map<String, Any?>)
        map["result"] = dataToSend
        map["success"] = result.success
        map["message"] = result.message
    } catch (e: Exception){
        map["success"] = false
        map["message"] = e.message
    }
    val responseData = convertMapToJSON(map).toByteArray()
    val responsePacket = DatagramPacket(responseData, responseData.size, clientAddress.address, clientAddress.port)
    serverSocket.send(responsePacket)
    dataToSend = ""
}


fun startReadingFromConsole(){
    val consoleInputThread = Thread {
        while (ongoing){
            val input = readln()
            if (input == "save"){
                val saveCommand = SaveCommand()
                val result = saveCommand.execute(mapOf("1" to 1))
                println("${result.success} ${result.message}")
            }
            else if(input == "exit"){
                ongoing = false
                val saveCommand = SaveCommand()
                saveCommand.execute(mapOf("1" to 1))
                exitProcess(1)
            }
            else{
                println("You can write 'save' or 'exit' only!")
            }
        }
    }
    consoleInputThread.start()
}