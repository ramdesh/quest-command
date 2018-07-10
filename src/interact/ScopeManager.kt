package interact

import travel.ArriveEvent
import core.events.EventListener
import core.gameState.GameState
import core.gameState.Target
import core.utility.NameSearchableList
import inventory.PickupItemEvent
import system.*

object ScopeManager {
    private val targets = NameSearchableList<Target>()

    init {
        resetTargets()
    }

    class ArrivalHandler() : EventListener<ArriveEvent>() {
        override fun execute(event: ArriveEvent) {
            resetTargets()
            addTargets(ItemManager.getItems(event.destination.items))
            addTargets(ActivatorManager.getActivators(event.destination.activators))
        }
    }

    class ItemSpawner : EventListener<ItemSpawnedEvent>() {
        override fun execute(event: ItemSpawnedEvent) {
            if (event.target == null) {
                println("${event.item.name} appeared.")
                addTarget(event.item)
            } else {
                EventManager.postEvent(PickupItemEvent(event.target, event.item))
            }
        }
    }

    class ActivatorSpawner : EventListener<SpawnActivatorEvent>() {
        override fun execute(event: SpawnActivatorEvent) {
            println("${event.activator.name} appeared.")
            addTarget(event.activator)
        }
    }

    class ScopeRemover : EventListener<RemoveScopeEvent>() {
        override fun execute(event: RemoveScopeEvent) {
            removeTarget(event.target)
        }
    }

    fun addTarget(target: Target) {
        if (!targets.contains(target)) {
            targets.add(target)
        }
    }

    fun addTargets(targets: List<Target>) {
        ScopeManager.targets.addAll(targets)
    }

    fun removeTarget(target: Target) {
        targets.remove(target)
    }

    fun getTargets(): List<Target> {
        return targets.toList()
    }

    private fun resetTargets() {
        targets.clear()
        addTarget(GameState.player)
    }

    fun targetExists(name: String): Boolean {
        return targets.exists(name)
    }

    fun targetExists(name: List<String>): Boolean {
        return targets.exists(name)
    }

    fun getTarget(name: String): Target {
        return targets.get(name)
    }

    fun getTarget(name: List<String>): Target {
        return targets.get(name)
    }

}