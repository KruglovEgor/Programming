package client

import client.GUI.Map.MapApp
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
var inputList = LinkedList<Map<String, Any?>>()
var login = ""
var HashedPassword = ""


val channel = DatagramChannel.open()
val selector = Selector.open()
val serverAddress = InetSocketAddress("localhost", 8080)
val buffer = ByteBuffer.allocate(100*1024)

// beauty
//todo clean base_classes
//todo fix dialog textFields (make more beautiful)
//todo clean code
//todo maybe make map to scroll (can ruin everything)


// important
//todo make execute_script
//todo translate help (send version in different languages)


//will be nice
//todo make animation for map
//todo dont send execute_script
//todo maybe in case of update put the values of existing unit in dialog
//todo make special phrases for map while checking parameters
//todo show advices in dialog (about types)
//todo add showButton on MainView


fun main(){
    channel.configureBlocking(false)
    channel.register(selector, SelectionKey.OP_WRITE)
    launch<AuthApp>()
}
