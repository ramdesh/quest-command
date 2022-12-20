package conversation.dsl

import conversation.Conversation
import conversation.dialogue.DialogueEvent
import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.events.Event
import core.thing.Thing
import core.utility.Named
import traveling.location.network.LocationNode

class DialogueBuilder {
    var priority: Int? = null
    private val depthScale: Int = 2
    private val conditions: MutableList<(Conversation) -> Boolean> = mutableListOf()
    private val children: MutableList<DialogueBuilder> = mutableListOf()
    private var results: MutableList<((Conversation) -> List<Event>)> = mutableListOf()

    fun cond(condition: (Conversation) -> Boolean) {
        conditions.add(condition)
    }

    fun question(questionType: QuestionType) {
        cond { it.question() == questionType }
    }

    fun verb(verb: Verb) {
        cond { it.verb() == verb }
    }

    fun subject(condition: (Conversation) -> Named) {
        cond { it.subject() == condition(it) }
    }

    fun branch(initializer: DialogueBuilder.() -> Unit) {
        children.add(DialogueBuilder().apply(initializer))
    }

    fun line(line: ((Conversation) -> String)) {
        this.results.add { listOf(DialogueEvent(it.getLatestListener(), it, line(it))) }
    }

    fun event(result: ((Conversation) -> Event)) {
        this.results.add { listOf(result(it)) }
    }

    fun events(results: ((Conversation) -> List<Event>)) {
        this.results.add(results)
    }

    internal fun build(depth: Int = 0): DialogueTree {
        val usedPriority = priority ?: (10 + depthScale * depth)
        val condition: (Conversation) -> Boolean = { convo -> conditions.all { it(convo) } }
        val dialogues = results.map { Dialogue(it, usedPriority) }
        val trees = children.map { it.build(depth + 1) }

        return DialogueTree(condition, dialogues, trees)
    }
}

fun conversations(
    initializer: DialogueBuilder.() -> Unit
): List<DialogueTree> {
    return listOf(DialogueBuilder().apply(initializer).build())
}

fun Conversation.question(): QuestionType? {
    return history.last().parsed?.questionType
}

fun Conversation.verb(): Verb? {
    return history.last().parsed?.verb
}

fun Conversation.subject(): Named? {
    return history.last().parsed?.subject
}

fun Conversation.subjects(): List<Named>? {
    return history.last().parsed?.subjects
}

fun Named?.hasTag(tag: String): Boolean {
    return if (this != null) {
        (this is LocationNode && getLocation().properties.tags.has(tag)) ||
                (this is Thing && properties.tags.has(tag))
    } else {
        false
    }
}