import BaseClasses.HumanBeing
import Commands.Invoker
import HelpingFunctions.printResults
import Interactive.*
import java.time.LocalDateTime
import java.util.*

var pathToCollection = ""
var currentId : Int = 1
var dateOfInitialization = LocalDateTime.of(LocalDateTime.now().year,
    LocalDateTime.now().monthValue, LocalDateTime.now().dayOfMonth,
    LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second)
var listOfData = LinkedList<MutableMap<String, Any?>>()
var listOfHumanBeing = LinkedList<HumanBeing>()
var ongoing = true



fun main(){
    downloadDataFromUsersCollection()

    while (ongoing){
        printResults("\nWrite your command:")
        listenToUser(readln())
    }
}

/**
 * Execute user's commands
 *
 * @param input : String - input from User
 */
fun listenToUser(input: String){
    val invoker = Invoker()
    try {
        val list = commandHandler(input)
        invoker.setCommand(getClassesOfCommands()[list[0]]!!)
        printResults(" ")
        val result = invoker.executeCommand(list[1] as Map<String, Any?>)
        if (!result.success){
            printResults(result.message)
        }
    } catch (e:Exception){
        printResults(e.message!!)
    }
}
