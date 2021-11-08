package traveling.location

import traveling.location.network.LocationNode
import traveling.position.Vector
import traveling.position.sum


class Route(val source: LocationNode, private val connections: MutableList<Connection> = mutableListOf()) {
    val distance by lazy { connections.asSequence().map { it.originPoint.getDistance() }.sum() }

    constructor(base: Route) : this(base.source) {
        base.getConnections().forEach {
            addLink(it)
        }
    }

    var destination: LocationNode = source
    val vector: Vector = Vector()

    override fun toString(): String {
        return "Route : ${source.name} - ${destination.name}; Steps: ${getConnections().size}"
    }

    fun addLink(connection: Connection) {
        if (connection.source.location != destination) {
            throw IllegalArgumentException("Route starting with '${source.name}' was passed a connection whose source '${connection.source.location.name}' did not match current destination '${destination.name}'.")
        }
        connections.add(connection)
        destination = connection.destination.location
    }

    fun getConnections(): List<Connection> {
        return connections.toList()
    }

    fun getVectorDistance(): Vector {
        return connections.asSequence().map { it.originPoint }.sum()
    }

    fun getDirectionString(): String {
        return connections.asSequence()
            .map { it.originPoint.direction.shortcut }
            .joinToString(", ")
            .uppercase()
    }

    fun isOnRoute(location: LocationNode): Boolean {
        return location == source || connections.any { location == it.destination.location }
    }

    fun getNextStep(location: LocationNode): Connection {
        return connections.first { it.source.location == location }
    }

    fun getRouteProgressString(currentLocation: LocationNode): String {
        val names = connections.asSequence()
            .map { it.source.location.name }
            .toMutableList()

        names.add(destination.name)
        return names.joinToString(", ") {
            if (it == currentLocation.name) {
                "*$it*"
            } else {
                it
            }
        }
    }

    fun trim(newStart: LocationNode): Route? {
        val connection = connections.firstOrNull { newStart == it.source.location }
        return if (connection == null) null
        else {
            val newConnections = connections.subList(connections.indexOf(connection), connections.size)
            Route(newStart).also { route ->
                newConnections.forEach {
                    route.addLink(it)
                }
            }
        }
    }

    fun getRouteString(): List<String> {
        return listOf(destination.name, distance.toString(), getDirectionString())
    }

}