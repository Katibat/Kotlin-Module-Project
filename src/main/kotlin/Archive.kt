data class Archive(
    val name: String, // название архива
    val notesMap: MutableMap<Int, Note>? = mutableMapOf() // список заметок в архиве
)