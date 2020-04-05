package inventory

import core.body.Body
import core.properties.Properties
import core.target.Target
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import traveling.location.location.Location


class Inventory(private val body: Body = createInventoryBody()) {
    constructor(items: List<Target>) : this(Body().also { it.layout.rootNode.getLocation().addTargets(items) })

    fun exists(item: Target): Boolean {
        return getItemsNameSearchable().exists(item) || NameSearchableList(getAllItems()).exists(item)
    }

    fun getItem(name: String): Target? {
        return getAllItems().toNameSearchableList().getOrNull(name) ?: NameSearchableList(getAllItems()).getOrNull(name)
    }

    fun getItems(name: String): List<Target> {
        return getAllItems().toNameSearchableList().getAll(name)
    }

    fun addAll(items: List<Target>) {
        items.forEach { add(it) }
    }


    private fun findLocationWith(item: Target): Location? {
        return body.getParts().firstOrNull { it.getItems().contains(item) }
                ?: body.getParts().flatMap { it.getItems() }.mapNotNull { it.inventory.findLocationWith(item) }.firstOrNull()
    }

    private fun findLocationThatCanTake(item: Target): Location? {
        return body.getParts().firstOrNull { it.canHold(item) }
                ?: body.getParts().flatMap { it.getItems() }.mapNotNull { it.inventory.findLocationThatCanTake(item) }.firstOrNull()
    }

    //Eventually add count
    fun attemptToAdd(item: Target): Boolean {
        val location = findLocationThatCanTake(item) ?: return false
        val match = location.getItems(item.name).firstOrNull()

        if (match != null && item.isStackable(match)) {
            match.properties.incCount(item.properties.getCount())
        } else {
            location.addTarget(item)
        }

        return true
    }

    fun add(item: Target) {
        if (!attemptToAdd(item)) {
            body.getRootPart().addTarget(item)
        }
    }

    fun remove(item: Target, count: Int = 1) {
        val location = findLocationWith(item)
        if (location != null) {
            if (item.properties.getCount() > count) {
                item.properties.incCount(-count)
            } else {
                location.removeTarget(item)
            }
        }
    }

    /**
     * Return all items of this inventory and any sub-inventory
     */
    fun getAllItems(): List<Target> {
        val items = getItems()
        return (items + items.flatMap { it.inventory.getAllItems() })
    }

    fun getItems(): List<Target> {
        return body.getParts().flatMap { it.getItems() }
    }

    private fun getItemsNameSearchable(): NameSearchableList<Target> {
        return getItems().toNameSearchableList()
    }

    fun findItemsByProperties(properties: Properties): List<Target> {
        return getAllItems().filter { it.properties.hasAll(properties) }
    }

    fun getWeight(): Int {
        return getItems().sumBy { it.getWeight() }
    }

}

fun createInventoryBody(capacity: Int? = null): Body {
    return Body("Inventory").also {
        with(it.getRootPart().properties.tags) {
            add("Container")
            add("Open")

        }
        if (capacity != null){
            it.getRootPart().properties.values.put("Capacity", capacity)
        }
    }
}