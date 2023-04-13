package Commands

class Invoker {

    /**
     * Instance of class realizing Command interface
     */
    private var command: Command? = null

    /**
     * Set command to invoker
     *
     * @param command - Instance of class realizing Command interface
     */
    fun setCommand(command: Command){
        this.command = command
    }

    /**
     * Execute command which was set
     *
     * @param map : Map<String, Any?> - parameters for Command.execute()
     *
     * @return Result
     */
    fun executeCommand(map: Map<String, Any?>) : Result{
        return command!!.execute(map)
    }
}