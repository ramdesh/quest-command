package interact.scope.spawn

import core.events.EventListener
import core.gameState.NO_VECTOR
import core.history.display
import interact.scope.ScopeManager

class ActivatorSpawner : EventListener<SpawnActivatorEvent>() {
    override fun execute(event: SpawnActivatorEvent) {
        if (!event.silent) {
            display("${event.activator.name} appeared.")
        }
        if (event.position != NO_VECTOR){
            event.activator.position = event.position
        }
        ScopeManager.getScope(event.targetLocation).addTarget(event.activator)
    }
}