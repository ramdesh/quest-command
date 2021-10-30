package explore.look

import core.Player
import core.history.displayToMe
import core.thing.Thing

fun describeThing(source: Player, thing: Thing) {
    var message = thing.getDisplayName()
    message += "\n\t${thing.description}"
    source.displayToMe(message)
}

fun describeThingDetailed(source: Player, thing: Thing) {
    var message = thing.getDisplayName()
    message += "\n\t${thing.description}"
    message += describeStatusEffects(thing)
    message += describeProperties(thing)
    source.displayToMe(message)
}

private fun describeStatusEffects(thing: Thing): String {
    if (thing.soul.getConditions().isNotEmpty()) {
        val effects = thing.soul.getConditions().joinToString(", ") { it.name }
        return "\n\t${thing.name} is $effects"
    }
    return ""
}

private fun describeProperties(thing: Thing): String {
    if (!thing.properties.isEmpty()) {
        return "\n\tTags: ${thing.properties.tags}" +
                "\n\tValues: ${thing.properties.values}"
    }
    return ""
}