package explore.look

import core.Player
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.history.display

class LookCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Look", "ls")
    }

    override fun getDescription(): String {
        return "Observe your surroundings."
    }

    override fun getManual(): String {
        return """
	Look all - View the objects you can interact with.
	Look <thing> - Look at a specific thing."""
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "look" && args.isEmpty() -> clarifyThing(source)
            args.isEmpty() -> EventManager.postEvent(LookEvent(source))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(LookEvent(source))
            source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing, argString).isNotEmpty() -> EventManager.postEvent(LookEvent(source, source.thing.currentLocation().getThingsIncludingPlayerInventory(
                source.thing,
                argString
            ).first()))
            else -> source.display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyThing(source: Player) {
        source.respond {
            message("Look at what?")
            options(listOf("all") + source.thing.currentLocation().getThings().map { it.name })
            command { "look $it" }
        }
    }

}