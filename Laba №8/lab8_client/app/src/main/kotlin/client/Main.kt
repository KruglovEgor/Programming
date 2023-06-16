package client

import client.GUI.SignIn.AuthApp
import tornadofx.launch
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.util.*


const val timeToReconnect = 5000.toLong()
const val delay = 3000.toLong()
var success = true
var ongoing = true
var inputList = LinkedList<Map<String, Any?>>()
var login = ""
var HashedPassword = ""


val channel = DatagramChannel.open()
val selector = Selector.open()
val serverAddress = InetSocketAddress("localhost", 8080)
val buffer = ByteBuffer.allocate(100*1024)


//todo clear base_classes
//todo dont send execute_script
//todo check if login or password is empty before sending
//todo maybe make update as unique command and types like "type1"->"type1?"

fun main(){
    channel.configureBlocking(false)
    channel.register(selector, SelectionKey.OP_WRITE)
    launch<AuthApp>()
}
