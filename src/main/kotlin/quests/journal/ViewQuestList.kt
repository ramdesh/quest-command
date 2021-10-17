package quests.journal

import core.events.EventListener
import core.history.displayToMe
import core.thing.Thing
import core.utility.NameSearchableList
import quests.Quest
import quests.QuestManager

class ViewQuestList : EventListener<ViewQuestListEvent>() {

    override fun execute(event: ViewQuestListEvent) {
        val quests = getQuests(event)

        val message = quests.joinToString("\n") {
            "${it.name}\n\t${it.getLatestJournalEntry()}"
        }

        displayQuestMessage(event.source, message, event)
    }

    private fun getQuests(event: ViewQuestListEvent): NameSearchableList<Quest> {
        return if (event.justActive) {
            QuestManager.getActiveQuests()
        } else {
            QuestManager.getAllPlayerQuests()
        }
    }

    private fun displayQuestMessage(source: Thing, message: String, event: ViewQuestListEvent) {
        if (message.isBlank()) {
            if (event.justActive) {
                source.displayToMe("I don't have any active quests.")
            } else {
                source.displayToMe("I don't have any quests.")
            }
        } else {
            source.displayToMe(message)
        }
    }

}