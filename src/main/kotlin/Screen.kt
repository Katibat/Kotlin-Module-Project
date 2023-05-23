import java.util.*

class Screen(private val navigatorService: NavigatorService) {
    private var scanner = Scanner(System.`in`)
    private var archivesMap: MutableMap<Int, Archive> = mutableMapOf()
    private var archiveVariable: Int = 0 // переменная для фиксации выбора пользователя id архива
    private var noteVariable: Int = 0 // переменная для фиксации выбора пользователя id заметки
    private var idCounterArchive: Int = 0
    private var idCounterNote: Int = 0

    fun chooseArchive() { // экран выбора архива
        println("0. Создать архив")
        archivesMap.forEach { (id, archive) ->
            println(
                "$id. Выбрать архив ${archive.name}" +
                        " (количество заметок: ${archive.notesMap?.size})"
            )
        }
        println("${archivesMap.size + 1}. Выход")
        when (val archiveIds = checkInput(archivesMap.size + 1)) {
            0 -> navigatorService.action(State.ARCHIVE_CREATE)
            in 1..archivesMap.size -> {
                archiveVariable = archiveIds
                navigatorService.action(State.ARCHIVE_OPEN)
            }
            else -> navigatorService.exit()
        }
    }

    fun openArchive() { // экран открытия архива
        println("0. Создать заметку")
        val notes: MutableMap<Int, Note> = archivesMap[archiveVariable]?.notesMap ?: mutableMapOf()
        notes.forEach { (id, note) -> println("$id. Выбрать заметку ${note.name}") }
        println("${notes.size + 1}. Выход")
        when (val noteIds = checkInput(notes.size + 1)) {
            0 -> navigatorService.action(State.NOTE_CREATE)
            in 1..notes.size -> {
                noteVariable = noteIds
                navigatorService.action(State.NOTE_OPEN)
            }
            else -> navigatorService.action(State.ARCHIVE_CHOOSE)
        }
    }

    fun createArchive() { // экран создания архива
        println("Введите название архива:")
        val name = readInput()
        idCounterArchive += 1
        val id = idCounterArchive
        archivesMap.put(id, Archive(name, mutableMapOf()))
        println("Создан архив: $id. ${archivesMap[id]?.name}")
        navigatorService.action(State.ARCHIVE_CHOOSE)
    }

    fun openNote() { // экран открытия выбранной заметки
        println("Заметка ${archivesMap[archiveVariable]?.notesMap?.get(noteVariable)?.name}: " +
                "${archivesMap[archiveVariable]?.notesMap?.get(noteVariable)?.text}")
        println("Введите цифру 1 для выхода")
        when (scanner.nextLine()) {
            "1" -> navigatorService.action(State.ARCHIVE_OPEN)
            else -> println("Такой команды нет")
        }
    }

    fun createNote() { // экран создания заметки
        println("Введите название заметки:")
        val name = readInput()
        println("Введите текст заметки:")
        val text = readInput()
        idCounterNote += 1
        val id = idCounterNote
        archivesMap[archiveVariable]?.notesMap?.put(id, Note(name, text, archiveVariable))
        navigatorService.action(State.ARCHIVE_OPEN)
    }

    private fun checkInput(length: Int) : Int {
        var idCommand = checkingForCommand(length)
        while (idCommand == -1) {
            idCommand = checkingForCommand(length)
        }
        return idCommand
    }

    private fun checkingForCommand(length: Int) : Int { // проверка ввода пользователя
        val input = scanner.nextLine()
        if (input.toIntOrNull() == null) { // проверка числа на null
            println("Введенный идентификатор команды не является числом или не был указан." +
                        "Попробуйте снова.")
            return -1
        }
        if (input.toInt() > length) { // введено число за пределами идентификаторов команд
            println("Команды с таким идентификатором не существует. Попробуйте снова.")
            return -1
        }
        return input.toInt()
    }

    private fun readInput() : String {
        return scanner.nextLine().replaceFirstChar { it.titlecase(Locale.getDefault()) }
    }
}