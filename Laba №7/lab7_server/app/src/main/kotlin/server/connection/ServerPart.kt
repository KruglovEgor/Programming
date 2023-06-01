package server.connection

import server.helping_functions.convertJSONtoMapOfStringAndAny
import server.helping_functions.convertMapToJSON
import server.logger
import server.ongoing
import server.startReadingFromConsole
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.sql.Connection
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.logging.Level

fun startServer(connection: Connection) {
    logger.log(Level.INFO, "Server started")

    val serverSocket = DatagramSocket(8080)
    val buffer = ByteArray(1024)
    val fixedExecutor: ExecutorService = Executors.newFixedThreadPool(8)
    val cashedExecutor : ExecutorService = Executors.newCachedThreadPool()
    startReadingFromConsole(connection)
    val packets : Queue<DatagramPacket> = LinkedList()
    val mapsToSend :Queue<Pair<InetSocketAddress, MutableMap<String, Any?>>> = LinkedList()
    while (ongoing) {
        Thread{
            val packet = DatagramPacket(buffer, buffer.size)
            serverSocket.receive(packet)
            packets.add(packet)
            println(1)
        }.start()

        // Создаем новый поток для обработки запроса от клиента
        if (packets.isNotEmpty()){
            println(2)
            fixedExecutor.execute{
                val pair = handleClientRequest(packets.poll(), serverSocket, connection)
                mapsToSend.add(pair)
            }
        }

        if (mapsToSend.isNotEmpty()){
            println(3)
            cashedExecutor.execute{
                val pair = mapsToSend.poll()
                sendDataToClient(serverSocket, pair.first, pair.second)
            }
        }
    }

}

fun handleClientRequest(packet: DatagramPacket, serverSocket: DatagramSocket, connection: Connection) : Pair<InetSocketAddress, MutableMap<String, Any?>> {
    val clientAddress = packet.socketAddress as InetSocketAddress
    val message = convertJSONtoMapOfStringAndAny(String(packet.data, 0, packet.length))

    logger.log(Level.INFO, "Received message from client $clientAddress: $message")

    val mapToSend = when (message["type"]) {
        "ping" -> ping(clientAddress, serverSocket)
        "exec_command" -> execCommand(clientAddress, serverSocket, message, connection)
        "login" -> clientLogin(clientAddress, serverSocket, message, connection)
        "register" -> clientRegister(clientAddress, serverSocket, message, connection)
        else -> mutableMapOf<String, Any?>(
            "success" to false,
            "message" to "undefined type of command",
            "result" to "Error: undefined type of command"
        )
    }
    return  Pair(clientAddress, mapToSend)
}


fun sendDataToClient(serverSocket: DatagramSocket, clientAddress: InetSocketAddress, mapToSend:MutableMap<String, Any?>){
    val responseData = convertMapToJSON(mapToSend).toByteArray()
    val responsePacket = DatagramPacket(responseData, responseData.size, clientAddress.address, clientAddress.port)
    serverSocket.send(responsePacket)
    logger.log(Level.INFO, "Sent to $clientAddress:  ${convertMapToJSON(mapToSend)}")
}