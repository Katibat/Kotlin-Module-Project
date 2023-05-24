class NoteApp: NavigatorService {
    private var state = State.ARCHIVE_CHOOSE

    fun start() {
        println("Приложение 'Заметки' приветствует Вас!")
        val screen = Screen(this)
        while (true) {
            when (state) {
                State.ARCHIVE_CHOOSE -> screen.chooseArchive()
                State.ARCHIVE_OPEN -> screen.openArchive()
                State.ARCHIVE_CREATE -> screen.createArchive()
                State.NOTE_OPEN -> screen.openNote()
                State.NOTE_CREATE -> screen.createNote()
                State.EXIT -> break
            }
        }
    }

    override fun action(state: State) {
        this.state = state
    }

    override fun exit() {
        this.state = State.EXIT
    }
}