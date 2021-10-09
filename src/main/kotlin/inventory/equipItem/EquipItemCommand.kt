package inventory.equipItem

import core.body.Body
import core.body.Slot
import core.commands.*
import core.events.EventManager
import core.history.display
import core.history.displayYou
import core.target.Target

class EquipItemCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Equip")
    }

    override fun getDescription(): String {
        return "Equip an item from your inventory"
    }

    override fun getManual(): String {
        return """
	Equip <item> - Equip an item
	Equip <item> to <body part> - Equip an item to a specific body part (ex: left hand). X
	Equip <item> to <body part> f - Equip an item even if that means unequipping what's already equipped there. X"""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "on")))
        val arguments = Args(args, delimiters, listOf("f"))

        if (arguments.isEmpty()) {
            suggestEquippableItems(source)
        } else {
            val item = getItem(source, arguments)
            val attachPointGuess = getAttachPoint(arguments)
            val body = source.body
            val force = arguments.has("f")

            if (item == null) {
                source.displayYou("Could not find ${arguments.getBaseString()}. (Did you mean 'equip <item> to <body part>?")
            } else {
                if (!item.canEquipTo(body)) {
                    source.displayYou("You can't equip ${item.name}.")
                } else {
                    val slot = findSlot(attachPointGuess, body, item)
                    if (slot == null) {
                        suggestAttachPoints(attachPointGuess, item)
                    } else {
                        val equippedItems = slot.getEquippedItems(body)
                        if (equippedItems.isNotEmpty() && !force) {
                            confirmEquip(item, equippedItems, attachPointGuess)
                        } else {
                            EventManager.postEvent(EquipItemEvent(source, item, slot))
                        }
                    }
                }
            }
        }
    }

    private fun getItem(source: Target, args: Args): Target? {
        val itemName = args.getBaseString()
        return source.inventory.getItem(itemName)
    }

    private fun getAttachPoint(args: Args): String? {
        return if (args.hasGroup("to")) {
            args.getString("to")
        } else {
            null
        }
    }

    private fun findSlot(attachPointGuess: String?, body: Body, item: Target): Slot? {
        return if (attachPointGuess == null) {
            body.getDefaultSlot(item)
        } else {
            item.findSlot(body, attachPointGuess)
        }
    }

    private fun suggestEquippableItems(source: Target) {
        val equippableItems = getEquipableItems(source)
        val message = "What do you want to equip?\n\t${equippableItems.joinToString(", ")}"
        val response = ResponseRequest(message, equippableItems.associate { it.name to "equip ${it.name}" })
         CommandParser.setResponseRequest(response)
    }

    private fun getEquipableItems(source: Target): List<Target> {
        val body = source.body
        val equippedItems = body.getEquippedItems()
        return source.inventory.getAllItems().filter { it.canEquipTo(body) && !equippedItems.contains(it) }
    }

    private fun suggestAttachPoints(attachPointGuess: String?, item: Target) {
        val message = "Could not find attach point $attachPointGuess. Where would you like to equip $item?\n\t${item.equipSlots.joinToString("\n\t")}"
        val response = ResponseRequest(message,
            item.equipSlots.flatMap { it.attachPoints }.associateWith { "equip $item to $it" })
         CommandParser.setResponseRequest(response)
    }

    private fun confirmEquip(newEquip: Target, equippedItems: List<Target>, attachPoint: String?) {
        val message = "Replace ${equippedItems.joinToString(", ")} with ${newEquip.name}?"

        val toPart = if (attachPoint.isNullOrBlank()) {
            ""
        } else {
            " to $attachPoint"
        }

        val response = ResponseRequest(message, mapOf("y" to "equip $newEquip$toPart f", "n" to ""))
         CommandParser.setResponseRequest(response)
    }
}