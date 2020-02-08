package system.debug

import core.GameState
import core.commands.Args
import core.commands.Command
import core.commands.parseTargets
import core.events.EventManager
import core.history.display
import status.stat.StatKind

class DebugCommand : Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("debug", "db")
    }

    override fun getDescription(): String {
        return "Debug: \n\tChange various settings for testing/cheating."
    }

    override fun getManual(): String {
        return "\n\tDebug - Toggle various debug settings all on or off at once." +
                "\n\tDebug list - View the gamestate's properties." +
                "\n\tDebug lvlreq <on/off> - Toggle the requirement for skills/spells to have a specific level." +
                "\n\tDebug statchanges <on/off> - Toggle whether stats (stamina, focus, health, etc) can be depleted." +
                "\n\tDebug random <on/off> - Toggle random chances always succeeding. " +
                "\n\tDebug displayupdates <on/off> - Toggle inline updating display messages (for things like progress bars). " +
                "\n\tDebug stat <stat name> <desired level> on *<target> - Set a stat to the desired level." +
                "\n\tDebug prop <prop name> <desired level> on *<target> - Set a property to the desired level." +
                "\n\tDebug tag *<remove> <tag name> on *<target> - Add (or remove) a tag." +
                "\n\tDebug weather <weather name> - Set weather in current location to the given weather, if it exists."
    }

    override fun getCategory(): List<String> {
        return listOf("Debugging")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("on"))

        if (args.isEmpty()) {
            sendDebugToggleEvent(DebugType.DEBUG_GROUP, arguments)
        } else {
            when (args.first()) {
                "list" -> EventManager.postEvent(DebugListEvent())
                "lvlreq" -> sendDebugToggleEvent(DebugType.LEVEL_REQ, arguments)
                "statchanges" -> sendDebugToggleEvent(DebugType.STAT_CHANGES, arguments)
                "random" -> sendDebugToggleEvent(DebugType.RANDOM, arguments)
                "displayupdates" -> sendDebugToggleEvent(DebugType.DISPLAY_UPDATES, arguments)
                "stat" -> sendDebugStatEvent(StatKind.LEVELED, arguments)
                "prop" -> sendDebugStatEvent(StatKind.PROP_VAL, arguments)
                "tag" -> sendDebugTagEvent(arguments)
                "weather" -> sendDebugWeatherEvent(arguments)
                else -> display("Did not understand debug command.")
            }
        }
    }

    private fun sendDebugToggleEvent(type: DebugType, args: Args) {
        val toggleWords = args.hasAny(listOf("on", "off", "true", "false"))
        val toggledOn = if (toggleWords.isNotEmpty()) {
            toggleWords.contains("on") || toggleWords.contains("true")
        } else {
            !GameState.properties.values.getBoolean(type.propertyName)
        }

        EventManager.postEvent(DebugToggleEvent(type, toggledOn))
    }

    private fun sendDebugStatEvent(type: StatKind, args: Args) {
        val target = parseTargets(args.getGroup("on")).firstOrNull()?.target ?: GameState.player
        val level = args.getNumber()

        if (level == null) {
            display("Could not find what number to set stat to: ${args.fullString}")
        } else {
            val statName = args.argsWithout(listOf("remove", args.args.first(), level.toString())).joinToString(" ")

            EventManager.postEvent(DebugStatEvent(target, type, statName, level))
        }
    }

    private fun sendDebugTagEvent(args: Args) {
        val target = parseTargets(args.getGroup("on")).firstOrNull()?.target ?: GameState.player
        val tagName = args.argsWithout(listOf("remove", args.args.first())).joinToString(" ")
        val isAdding = !args.contains("remove")

        EventManager.postEvent(DebugTagEvent(target, tagName, isAdding))
    }

    private fun sendDebugWeatherEvent(arguments: Args) {
        val weather = arguments.argsWithout(listOf("weather")).joinToString(" ")
        EventManager.postEvent(DebugWeatherEvent(weather))
    }

}