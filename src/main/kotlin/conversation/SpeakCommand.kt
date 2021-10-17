package conversation

import conversation.parsing.QuestionType
import conversation.parsing.Verb
import conversation.start.StartConversationEvent
import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import core.utility.capitalize2

class SpeakCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Speak", "spk", "talk")
    }

    override fun getDescription(): String {
        return "Start a conversation with someone."
    }

    override fun getManual(): String {
        return """  
	Speak with <thing> - Start a conversation with someone.
	To stop talking, type 'goodbye' or 'exit'.
	To ask a question, end your statement with '?'.
	Ex: 'Where you are?'.
	Available question types:  ${QuestionType.values().map { it.name.lowercase().capitalize2() }}
	Available verbs:  ${Verb.values().map { it.name.lowercase().capitalize2() }}"""
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("to", "with"))
        when {
            arguments.hasGroup("with") -> speakTo(source, arguments.getString("with"))
            arguments.hasGroup("to") -> speakTo(source, arguments.getString("to"))
            else -> source.displayToMe("Couldn't find someone to speak with.")
        }
    }

    private fun speakTo(speaker: Thing, thingName: String) {
        val things = speaker.location.getLocation().getThings(thingName)
        if (things.size == 1) {
            EventManager.postEvent(StartConversationEvent(speaker, things.first()))
        } else {
            val creatures = speaker.location.getLocation().getCreatures(speaker)
            val message = "Speak to who?\n\t${creatures.joinToString(", ")}"
            val response = ResponseRequest(message, creatures.associate { it.name to "speak to ${it.name}." })
            CommandParser.setResponseRequest(response)
        }
    }
}
