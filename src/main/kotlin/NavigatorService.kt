/**
 * интерфейс для экранов
 */
interface NavigatorService {
    fun action(state: State)
    fun exit()
}