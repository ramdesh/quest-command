package explore.look

import core.commands.CommandParser
import core.history.display
import core.history.displayUpdate
import core.history.displayUpdateEnd
import core.history.displayToMe
import core.target.Target
import status.stat.HEALTH

fun describeBattle(source: Target) {
    val creatures = source.location.getLocation().getCreatures(source)
    creatures.filter { it != source }.forEach {
        it.display("${it.getDisplayName()} is ${source.position.getDistance(it.position)} away from ${source.getDisplayName()}.")
    }

    if (!CommandParser.isPlayersTurn()) {
        source.display("It is ${CommandParser.commandSource}'s turn.")
    }

    creatures.forEach {
        it.display("\t${status(it)}")
    }
    printTurnStatus(source, creatures)
}

private fun status(target: Target): String {
    return "${target.name}: ${target.soul.getCurrent(HEALTH)}/${target.soul.getTotal(HEALTH)} HP, ${target.ai.getActionPoints()}/100 AP, ${target.ai.action?.javaClass?.simpleName ?: "None"}."
}

private fun printTurnStatus(source: Target, creatures: List<Target>) {
    val combatantString = creatures.map {
        when {
            it.ai.isActionReady() -> ""
            it.ai.getActionPoints() == 0 -> ""
            it.ai.canChooseAction() -> "$it is making a choice"
            it.ai.action != null -> "$it is preforming an action with ${it.ai.action!!.timeLeft} time left"
            else -> "$it is getting ready to make a choice: ${it.ai.getActionPoints()}/100"
        }
    }.filter { it.isNotBlank() }.joinToString("\n")

    if (combatantString.isNotBlank()) {
        source.displayToMe(combatantString)
    }
}

fun printUpdatingStatus(creatures: List<Target>) {
    val combatantString = creatures.joinToString("\t\t") {
        val points = it.ai.getActionPoints()
        val timeLeft = it.ai.action?.timeLeft ?: 0
        "${it.getDisplayName()}: $points AP, $timeLeft action time left"
    }
    displayUpdate(combatantString)
}

fun printUpdatingStatusEnd(creatures: List<Target>) {
    val combatantString = creatures.joinToString("\t\t") {
        val points = it.ai.getActionPoints()
        val timeLeft = it.ai.action?.timeLeft ?: 0
        "${it.getDisplayName()}: $points AP, $timeLeft action time left"
    }
    displayUpdateEnd(combatantString)
}