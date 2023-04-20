package commands

import helping_functions.printResults

class HelpCommand() : Command {

    /**
     * Get commands description
     *
     * @return String with all available commands with their description
     */
    private fun getHelp():String{
        val txt = """
            help : вывести справку по доступным командам
            info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
            show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
            add {element} : добавить новый элемент в коллекцию
            update id {element} : обновить значение элемента коллекции, id которого равен заданному
            remove_by_id id : удалить элемент из коллекции по его id
            clear : очистить коллекцию
            save : сохранить коллекцию в файл
            execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
            exit : завершить программу (без сохранения в файл)
            remove_first : удалить первый элемент из коллекции
            remove_head : вывести первый элемент коллекции и удалить его
            add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
            count_by_minutes_of_waiting minutesOfWaiting : вывести количество элементов, значение поля minutesOfWaiting которых равно заданному
            count_less_than_mood mood : вывести количество элементов, значение поля mood которых меньше заданного
            print_ascending : вывести элементы коллекции в порядке возрастания
            """.trimIndent();
        return txt
    }

    /**
     * Printing all available commands with their description using printResult()
     *
     * @param map - not used
     *
     * @return Result
     */
    override fun execute(map: Map<String, Any?>): Result {
        var success = true
        var message = ""
        try {
            printResults(getHelp())
        } catch (e: Exception){
            success = false
            message = e.message.toString()
        }
        return Result(success, message)
    }
}