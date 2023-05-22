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
        var archiveLength = checkInput(archivesMap.size + 1)
        while (archiveLength == -1) {
            archiveLength = checkInput(archivesMap.size + 1)
        }
        when (archiveLength) {
            0 -> navigatorService.action(State.ARCHIVE_CREATE)
            in 1..archivesMap.size -> {
                archiveVariable = archiveLength
                navigatorService.action(State.ARCHIVE_OPEN)
            }
            else -> navigatorService.exit()
        }
    }

    fun openArchive() { // экран открытия архива
        println("0. Создать заметку")
        val notes: MutableMap<Int, Note> = mutableMapOf()
        if (this.archivesMap[archiveVariable]?.notesMap != null) {
            this.archivesMap[archiveVariable]?.notesMap?.forEach { (id, note) ->
                notes[id] = note
            }
            notes.forEach { (id, note) -> println("$id. Выбрать заметку ${note.name}") }
        }
        println("${notes.size + 1}. Выход")
        var noteLength = checkInput(notes.size + 1)
        while (noteLength == -1) {
            noteLength = checkInput(notes.size + 1)
        }
        when (noteLength) {
            0 -> navigatorService.action(State.NOTE_CREATE)
            in 1..notes.size -> {
                noteVariable = noteLength
                navigatorService.action(State.NOTE_OPEN)
            }
            else -> navigatorService.action(State.ARCHIVE_CHOOSE)
        }
    }

    fun createArchive() { // экран создания архива
        println("Введите название архива:")
        val name = scanner.nextLine()
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        idCounterArchive += 1
        val id = idCounterArchive
        archivesMap.put(id, Archive(name, mutableMapOf()))
        println("Создан архив: $id. ${archivesMap[id]?.name}")
        navigatorService.action(State.ARCHIVE_CHOOSE)
    }

    fun openNote() { // экран открытия выбранной заметки
        val name = archivesMap[this.archiveVariable]?.notesMap?.get(noteVariable)?.name
        val text = archivesMap[this.archiveVariable]?.notesMap?.get(noteVariable)?.text
        println("Заметка $name: $text")
        println("Введите цифру 1 для выхода")
        when (scanner.nextLine()) {
            "1" -> navigatorService.action(State.ARCHIVE_OPEN)
            else -> println("Такой команды нет")
        }
    }

    fun createNote() { // экран создания заметки
        println("Введите название заметки:")
        val name = scanner.nextLine()
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        println("Введите текст заметки:")
        val text = scanner.nextLine()
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        idCounterNote += 1
        val id = idCounterNote
        archivesMap[archiveVariable]?.notesMap?.put(id, Note(name, text, archiveVariable))
        navigatorService.action(State.ARCHIVE_OPEN)
    }

    private fun checkInput(length: Int): Int { // проверка ввода пользователя
        val input = Scanner(System.`in`).nextLine()
        if (input.toIntOrNull() == null) { // проверка числа на null
            println("Введенный идентификатор команды не является числом. Попробуйте снова.")
            return -1
        }
        if (input.isNullOrEmpty()) { // проверка на символы и null
            println(
                "Введенный идентификатор команды не является числом или не был указан." +
                        "Попробуйте снова."
            )
            return -1
        }
        if (input.toInt() > length) { // введено число за пределами идентификаторов команд
            println("Команды с таким идентификатором не существует. Попробуйте снова.")
            return -1
        }
        return input.toInt()
    }
}