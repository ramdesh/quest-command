package core.gameState

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.behavior.BehaviorRecipe
import core.gameState.inhertiable.InheritRecipe
import core.utility.max
import system.BehaviorManager
import system.InheritableManager
import system.ItemManager

class Item(
        override val name: String,
        override val description: String = "",
        override val locationDescription: String? = null,
        private val weight: Int = 0,
        var count: Int = 1,
        equipSlots: List<List<String>> = listOf(),
        @JsonProperty("behaviors") private val behaviorRecipes: MutableList<BehaviorRecipe> = mutableListOf(),
        override val properties: Properties = Properties(),
        inherits: List<InheritRecipe> = listOf()
) : Target {
    init {
        applyInherits(inherits)
    }

    constructor(base: Item, locationDescription: String? = null) : this(base.name, base.description, locationDescription
            ?: base.locationDescription, base.weight, base.count, base.equipSlots.map { it.bodyParts }, base.behaviorRecipes, Properties(base.properties))

    val equipSlots = equipSlots.map { Slot(it) }
    val soul = Soul(this)
    private val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)

    init {
        soul.addStats(properties.stats)
    }

    override fun toString(): String {
        return name
    }

    private fun applyInherits(inherits: List<InheritRecipe>) {
        inherits.forEach { it ->
            val inherit = InheritableManager.getInheritable(it)
            val itemBehaviorRecipeNames = behaviorRecipes.map { it.name }
            behaviorRecipes.addAll(inherit.behaviorRecipes.filter { !itemBehaviorRecipeNames.contains(it.name) })
            properties.inherit(inherit.properties)
        }
    }

    fun evaluate(event: Event): Boolean {
        return behaviors.any { it.evaluate(event) }
    }

    fun evaluateAndExecute(event: Event) {
        behaviors.filter { it.evaluate(event) }
                .forEach { it.execute(this) }
    }

    fun canEquipTo(body: Body): Boolean {
        equipSlots.forEach { slot ->
            if (body.canEquip(slot)) {
                return true
            }
        }
        return false
    }

    fun getEquippedSlot(body: Body): Slot {
        return equipSlots.first { it.itemIsEquipped(this, body) }
    }

    fun findSlot(body: Body, bodyPart: String): Slot? {
        return equipSlots.firstOrNull { it.contains(bodyPart) && body.canEquip(it) }
    }

    fun getDamage(): Int {
        val chop = properties.values.getInt("chopDamage", 0)
        val stab = properties.values.getInt("stabDamage", 0)
        val slash = properties.values.getInt("slashDamage", 0)
        return max(chop, stab, slash)
    }

    fun isStackable(other: Item): Boolean {
        return name == other.name && properties.matches(other.properties)
    }

    fun getTaggedItemName(): String {
        val orig = ItemManager.getItem(name)
        val newTags = properties.tags.getAll() - orig.properties.tags.getAll()
        return newTags.joinToString(" ") + " " + name
    }

}