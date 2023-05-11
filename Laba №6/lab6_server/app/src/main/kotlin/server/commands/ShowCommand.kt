package commands


class ShowCommand() : Command {

    /**
     * Printing units in our collection using printResult()
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>): Result {
        val printAscending = PrintAscendingCommand()
        return printAscending.execute(mapOf("1" to 1))
    }
}