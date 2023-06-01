package client

import client.helping_functions.convertJSONtoMapOfStringAndAny
import client.helping_functions.convertMapToJSON
import client.helping_functions.writeInJSONFile
import client.interactive.commandHandler
import client.interactive.getParametersOfCommands
import org.apache.commons.codec.digest.DigestUtils
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.util.*


const val timeToReconnect = 5000.toLong()
const val delay = 1000.toLong()
var success = true
var ongoing = true
var inputList = LinkedList<Map<String, Any?>>()
var login = ""
var HashedPassword = ""


//todo dont send execute_script
fun main(){
    getConnection()
}

/**
 * Execute user's commands
 *
 * @param input : String - input from User
 */
fun listenToUser(input: String) :  MutableMap<String, Any?> {
    try {
        val map = commandHandler(input)
        map["isNull"] = false
        return map
    } catch (e: Exception){
        e.message?.let { println(it) }
        return mutableMapOf("isNull" to true)
    }
}

fun getConnection(){
    val channel = DatagramChannel.open()
    channel.configureBlocking(false)
    val selector = Selector.open()
    channel.register(selector, SelectionKey.OP_READ)

    val serverAddress = InetSocketAddress("localhost", 8080)
//    val serverAddress = InetSocketAddress("77.234.214.82", 28538)

    val buffer = ByteBuffer.allocate(100*1024)
    try {
        signUp(serverAddress, buffer, channel, selector)
    } catch (e: Exception){
        println("Ops, there is problem with connection. Try one more time or return later!")
    }

    while (ongoing) {
        try {
            communicationProcess(serverAddress, buffer, channel, selector)
        } catch (e: Exception){
            println("Ops, there is problem with connection. Try one more time or return later!")
        }
    }
}


fun communicationProcess(serverAddress: InetSocketAddress, buffer: ByteBuffer, channel: DatagramChannel, selector: Selector){
    ping(serverAddress, buffer, channel, selector)
    val mapToSend: MutableMap<String, Any?>
    if (inputList.isEmpty()){
        println("Enter a command to send to the server: ")
        val userInput = listenToUser(readln().trim())
        mapToSend = userInput
    }
    else{
        mapToSend = inputList.first().toMutableMap()
        mapToSend["isNull"] = mapToSend.isEmpty()
        inputList.removeFirst()
    }
    mapToSend["type"] = "exec_command"
    (mapToSend["params"] as MutableMap<String, Any?>)["creator"] = login
    if (!(mapToSend["isNull"] as Boolean)){
        if (mapToSend["command"] == "exit") {
            ongoing = false
        }
        else {
            mapToSend.remove("isNull")
            val receivedMap = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, mapToSend, selector)
            if (success){
                println(receivedMap["result"])
            }
        }
    }
}


fun sendData(serverAddress: InetSocketAddress, buffer: ByteBuffer, channel: DatagramChannel, dataToSend : Map<String, Any?>){
    buffer.put(convertMapToJSON(dataToSend).toByteArray())
    buffer.flip()
    channel.send(buffer, serverAddress)
    buffer.clear()
}


fun receiveData(buffer: ByteBuffer, channel: DatagramChannel) : ByteArray{
    channel.receive(buffer)
    buffer.flip()

    val receivedData = ByteArray(buffer.remaining())
    buffer.get(receivedData)
    buffer.clear()
    return receivedData
}

fun sendAndReceiveDataWithCheckingAvailabilityOfServer(serverAddress: InetSocketAddress, buffer: ByteBuffer, channel: DatagramChannel, dataToSend: Map<String, Any?>, selector: Selector) : ByteArray{
    var receivedData = ByteArray(0)
    while (true){
        sendData(serverAddress, buffer, channel, dataToSend)

        if(selector.select(delay) > 0){
            val selectedKeys = selector.selectedKeys()
            val iterator = selectedKeys.iterator()
            while(iterator.hasNext()) {
                val key = iterator.next()
                if(key.isReadable) {
                    receivedData = receiveData(buffer, channel)
                }
                iterator.remove()
            }
        }

        if (receivedData.isNotEmpty()) {
            break
        }
        println("Server is unavailable. Time to reconnect - $timeToReconnect milliseconds")
        Thread.sleep(timeToReconnect)
    }
    return receivedData
}

fun sendAndReceiveDataWithCheckingValidity(serverAddress: InetSocketAddress, buffer: ByteBuffer, channel: DatagramChannel, dataToSend: Map<String, Any?>, selector: Selector) : Map<String, Any?>{
    val receivedData = sendAndReceiveDataWithCheckingAvailabilityOfServer(serverAddress, buffer, channel,dataToSend, selector)
    val receivedMessage = String(receivedData)
    val map = convertJSONtoMapOfStringAndAny(receivedMessage)
    if(!(map["success"] as Boolean)){
        println("Something went wrong. Message: ${map["message"]}.")
        success = false
    }
    else success = true
    return map
}

fun ping(serverAddress: InetSocketAddress, buffer: ByteBuffer, channel: DatagramChannel, selector: Selector){
    val map = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, mapOf("type" to "ping"), selector)
    if(success){
        if (map["result"] as Map<String, List<Any>> != getParametersOfCommands()){
            println("There is a change in commands on the server! Print 'help' to get more information.")
            map["result"]?.let { writeInJSONFile("CommandsParameters.json", it) }
        }
    }
}


fun signUp(serverAddress: InetSocketAddress, buffer: ByteBuffer, channel: DatagramChannel, selector: Selector){
    var answer = ""
    while ((answer != "login") and (answer != "register")){
        println("Choose: 'login' or 'register'")
        answer = readln().trim()
    }

    println("Please, enter your username:")
    login = readln().trim()
    println("Please, enter your password")
    HashedPassword = DigestUtils.sha512Hex(readln().trim())
    val dataToSend = mapOf(
        "type" to answer,
        "login" to login,
        "password" to HashedPassword
    )
    val response = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, dataToSend, selector)
    if (!success){
        signUp(serverAddress, buffer, channel, selector)
    }
    else {
        println(response["result"])
    }
}
