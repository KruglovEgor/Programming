package server.connection

import server.helping_functions.convertMapToJSON
import server.interactive.getClassesOfCommands
import server.interactive.getParametersOfCommands
import server.invoker
import server.logger
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.sql.Connection
import java.util.logging.Level


fun ping() : MutableMap<String, Any?>{
    val map = mutableMapOf<String, Any?>()
    try {
        map["result"] = getParametersOfCommands()
        map["success"] = true
        map["message"] = ""
    } catch (e: Exception){
        map["success"] = false
        map["message"] = e.message
    }
    return map

}

fun execCommand(message: Map<String, Any?>, connection: Connection): MutableMap<String, Any?>{
    val map = mutableMapOf<String, Any?>()
    try {
        invoker.setCommand(getClassesOfCommands(connection)[message["command"]]!!)
        val result = invoker.executeCommand(message["params"] as Map<String, Any?>)
        map["success"] = result.success
        map["message"] = result.message
        map["result"] = result.result
    } catch (e: Exception){
        map["success"] = false
        map["message"] = e.message
        map["result"] = "Error $message"
    }
    return map

}

fun clientLogin(message: Map<String, Any?>, connection: Connection): MutableMap<String, Any?>{
    val login = message["login"].toString()
    val password = message["password"].toString()
    val map = mutableMapOf<String, Any?>()
    val statement = connection.prepareStatement("SELECT * FROM users WHERE login = ?")
    statement.setString(1, login)

    try {
        val resultSet = statement.executeQuery()
        if (resultSet.next()){
            if(password == resultSet.getString("password")){
                map["success"] = true
                map["message"] = ""
                map["result"] = "Success"
            } else{
                map["success"] = false
                map["message"] = "Wrong password"
                map["result"] = "Error: $message"
            }

        }
        else{
            map["success"] = false
            map["message"] = "There is no such login registered"
            map["result"] = "Error: $message"
        }


    } catch (e: Exception){
        map["success"] = false
        map["message"] = e.message
        map["result"] = "Error: $message"
    }
    return map
}


fun clientRegister(message: Map<String, Any?>, connection: Connection): MutableMap<String, Any?>{
    val login = message["login"].toString()
    val password = message["password"].toString()
    val map = mutableMapOf<String, Any?>()
    val statement = connection.prepareStatement("SELECT * FROM users WHERE login = ?")
    statement.setString(1, login)
    try {
        val resultSet = statement.executeQuery()

        if (resultSet.next()){
            map["success"] = false
            map["message"] = "Current login is occupied!"
            map["result"] = "Error $message"
        }
        else{
            val insertStatement = connection.prepareStatement("INSERT INTO users (login, password) VALUES (?, ?)")
            insertStatement.setString(1, login)
            insertStatement.setString(2, password)
            insertStatement.executeUpdate()
            logger.log(Level.INFO, "Registered new user $login")
            map["success"] = true
            map["message"] = ""
            map["result"] = "Success"
        }

    } catch (e: Exception){
        map["success"] = false
        map["message"] = e.message
        map["result"] = "Error: $message"
    }
    return map
}