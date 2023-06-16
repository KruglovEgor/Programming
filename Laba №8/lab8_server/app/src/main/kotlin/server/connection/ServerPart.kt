package server.connection

import server.helping_functions.convertJSONtoMapOfStringAndAny
import server.helping_functions.convertMapToJSON
import server.logger
import server.ongoing
import server.startReadingFromConsole
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.sql.Connection
import java.util.Queue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.logging.Level

fun startServer(connection: Connection) {
    logger.log(Level.INFO, "Server started")


    val datagramChannel = DatagramChannel.open()
    datagramChannel.configureBlocking(false)
    val serverSocket = datagramChannel.socket()
    serverSocket.bind(InetSocketAddress(8080))
    val selector: Selector = Selector.open()
    datagramChannel.register(selector, SelectionKey.OP_READ)


    val fixedExecutor: ExecutorService = Executors.newFixedThreadPool(4)
    val cashedExecutor : ExecutorService = Executors.newCachedThreadPool()
    startReadingFromConsole(connection)
    val packets : Queue<DatagramPacket> = LinkedBlockingQueue()
    val mapsToSend :Queue<Pair<InetSocketAddress, MutableMap<String, Any?>>> = LinkedBlockingQueue()

    while (ongoing) {
        selector.select(10)

        val selectedKeys = selector.selectedKeys()
        val iterator = selectedKeys.iterator()

        while (iterator.hasNext()){
            val key = iterator.next()
            iterator.remove()
                if (key.isReadable){

                        val buffer = ByteBuffer.allocate(1024)
                        val senderAddress = datagramChannel.receive(buffer)
                        if (senderAddress != null){
                            Thread{
                                buffer.flip()
                                val packetData = ByteArray(buffer.limit())
                                buffer.get(packetData)
                                val packet = DatagramPacket(packetData, packetData.size, senderAddress)
                                packets.add(packet)
                            }.start()
                        }

            }
        }
        if (packets.isNotEmpty()){
            println(2)
            fixedExecutor.execute{
                val pair = handleClientRequest(packets.poll(), connection)
                    mapsToSend.add(pair)
            }
        }

        if (mapsToSend.isNotEmpty()){
            println(3)
            cashedExecutor.execute{
                val pair = mapsToSend.poll()
                sendDataToClient(datagramChannel, pair.first, pair.second)
            }
        }
    }

}

fun handleClientRequest(packet: DatagramPacket, connection: Connection) : Pair<InetSocketAddress, MutableMap<String, Any?>> {
    val clientAddress = packet.socketAddress as InetSocketAddress
    val message = convertJSONtoMapOfStringAndAny(String(packet.data, 0, packet.length))

    logger.log(Level.INFO, "Received message from client $clientAddress: $message")

    val mapToSend = when (message["type"]) {
        "ping" -> ping()
        "exec_command" -> execCommand(message, connection)
        "login" -> clientLogin(message, connection)
        "register" -> clientRegister(message, connection)
        else -> mutableMapOf<String, Any?>(
            "success" to false,
            "message" to "undefined type of command",
            "result" to "Error: undefined type of command"
        )
    }
    return  Pair(clientAddress, mapToSend)
}


fun sendDataToClient(datagramChannel: DatagramChannel, clientAddress: InetSocketAddress, mapToSend:MutableMap<String, Any?>){
    val responseData = convertMapToJSON(mapToSend).toByteArray()
    val wrappedData = ByteBuffer.wrap(responseData)
    datagramChannel.send(wrappedData, clientAddress)
    logger.log(Level.INFO, "Sent to $clientAddress:  ${convertMapToJSON(mapToSend)}")
}