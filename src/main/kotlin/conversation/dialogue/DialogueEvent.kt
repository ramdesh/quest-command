package conversation.dialogue

import conversation.Conversation
import conversation.parsing.SentenceParser
import core.events.Event
import core.target.Target

class DialogueEvent(val speaker: Target, val conversation: Conversation, val line: String) : Event {
    val parsed: ParsedDialogue? by lazy { ((SentenceParser(speaker, conversation.getLatestListener(), conversation, line))).parsedDialogue }
}