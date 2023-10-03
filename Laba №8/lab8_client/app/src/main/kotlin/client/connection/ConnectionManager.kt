package client.connection

import client.*
import client.helping_functions.convertJSONtoMapOfStringAndAny
import client.helping_functions.convertMapToJSON
import client.helping_functions.writeInJSONFile
import client.interactive.getParametersOfCommands
import org.apache.commons.codec.digest.DigestUtils
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector


//fun getConnection(){
//    val channel = DatagramChannel.open()
//    channel.configureBlocking(false)
//    val selector = Selector.open()
//    channel.register(selector, SelectionKey.OP_WRITE)
//
//    val serverAddress = InetSocketAddress("localhost", 8080)
////    val serverAddress = InetSocketAddress("77.234.214.82", 28538)
//
//    val buffer = ByteBuffer.allocate(100*1024)
//    try {
//        signUp(serverAddress, buffer, channel, selector)
//        while (ongoing) communicationProcess(serverAddress, buffer, channel, selector)
//    } catch (e: Exception){
//        println("Ops, there is problem with connection. Try one more time or return later!")
//        println(e.message)
//    }
//}


//fun communicationProcess(serverAddress: InetSocketAddress, buffer: ByteBuffer, channel: DatagramChannel, selector: Selector){
//    ping(serverAddress, buffer, channel, selector)
//    val mapToSend: MutableMap<String, Any?>
//    if (inputList.isEmpty()){
//        println("Enter a command to send to the server: ")
//        val userInput = listenToUser(readln().trim())
//        mapToSend = userInput
//    }
//    else{
//        mapToSend = inputList.first().toMutableMap()
//        mapToSend["isNull"] = mapToSend.isEmpty()
//        inputList.removeFirst()
//    }
//    mapToSend["type"] = "exec_command"
//    (mapToSend["params"] as MutableMap<String, Any?>)["creator"] = login
//    if (!(mapToSend["isNull"] as Boolean)){
//        if (mapToSend["command"] == "exit") {
//            ongoing = false
//        }
//        else {
//            mapToSend.remove("isNull")
//            val receivedMap = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, mapToSend, selector)
//            if (success){
//                println(receivedMap["result"])
//            }
//        }
//    }
//}


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
    while (receivedData.isEmpty()){
        sendData(serverAddress, buffer, channel, dataToSend)
        channel.register(selector, SelectionKey.OP_READ)
        selector.select(delay)
        val selectedKeys = selector.selectedKeys()
        val iterator = selectedKeys.iterator()
        while (iterator.hasNext()){
            val key = iterator.next()
            iterator.remove()
            if (key.isReadable) {
                receivedData = receiveData(buffer, channel)
                channel.register(selector, SelectionKey.OP_WRITE)
            }
        }
        if (receivedData.isEmpty()){
            println("Server is unavailable! Reconnecting...")
            Thread.sleep(timeToReconnect)
        }
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


//fun signUp(serverAddress: InetSocketAddress, buffer: ByteBuffer, channel: DatagramChannel, selector: Selector){
//    var answer = ""
//    while ((answer != "login") and (answer != "register")){
//        println("Choose: 'login' or 'register'")
//        answer = readln().trim()
//    }
//
//    println("Please, enter your username:")
//    login = readln().trim()
//    while (login.isEmpty()){
//        println("Login can't be empty!")
//        println("Please, enter your username:")
//        login = readln().trim()
//    }
//    println("Please, enter your password")
//    var password = readln().trim()
//    while (password.isEmpty()){
//        println("Password can't be empty!")
//        println("Please, enter your password:")
//        password = readln().trim()
//    }
//    HashedPassword = DigestUtils.sha512Hex(password)
//    val dataToSend = mapOf(
//        "type" to answer,
//        "login" to login,
//        "password" to HashedPassword
//    )
//    val response = sendAndReceiveDataWithCheckingValidity(serverAddress, buffer, channel, dataToSend, selector)
//    if (!success){
//        signUp(serverAddress, buffer, channel, selector)
//    }
//    else {
//        println(response["result"])
//    }
//}



