package core.gameState.location

import com.fasterxml.jackson.annotation.JsonProperty
import core.gameState.Direction
import core.utility.Named

val NOWHERE_NODE = LocationNode("Nowhere")
const val DEFAULT_NETWORK = "Wilderness"

class LocationNode(
        override val name: String,
        val locationName: String = name,
        val parent: String = DEFAULT_NETWORK,
        @JsonProperty("locations") val protoConnections: List<ProtoConnection> = listOf(),
        private val connections: MutableList<Connection> = mutableListOf()
) : Named {

    lateinit var network: Network

    override fun toString(): String {
        return name
    }

    fun getDescription(): String {
        return name
    }

    fun addConnection(connection: Connection) {
        if (!hasConnection(connection)) {
            connections.add(connection)
        }
    }

    private fun hasConnection(connection: Connection): Boolean {
        return connections.any {
            it.source == connection.source && it.destination == connection.destination
        }
    }

    fun getNeighborConnections(): List<Connection> {
        return connections.toList()
    }

    fun getNeighbors(): List<LocationNode> {
        return connections.map { it.destination.location }
    }

    fun getNeighbors(direction: Direction): List<LocationNode> {
        return connections.asSequence()
                .filter { it.vector.direction == direction }
                .map { it.destination.location }
                .toList()
    }

    fun getLocation(): Location {
        return network.getLocation(locationName)
    }

    fun nameMatches(args: List<String>): Boolean {
        return name.toLowerCase().split(" ").contains(args[0])
    }

    fun getConnection(destination: LocationNode): Connection? {
        return connections.firstOrNull { it.destination.location == destination }
    }

    fun getSiblings(): String {
        val locations = getNeighborConnections()
        return if (locations.isNotEmpty()) {
            val siblings = locations.joinToString(", ") { getLocationWithDirection(it, false) }
            "is neighbored by $siblings"
        } else {
            "has no known neighbors"
        }
    }

    fun isMovingToRestricted(destination: LocationNode): Boolean {
        val link = getConnection(destination)
        return link == null || link.restricted
    }

    private fun getLocationWithDirection(neighbor: Connection, far: Boolean): String {
        val direction = neighbor.vector.direction
        return if (direction == Direction.NONE) {
            neighbor.destination.location.name
        } else {
            val farString = if (far) {
                "Far "
            } else ""
            "${neighbor.destination.location.name} ($farString$direction)"
        }
    }

    fun getDistanceToLowestNodeInNetwork(): Int {
        val lowestNodes = network.getFurthestLocations(Direction.BELOW)
        return if (lowestNodes.isEmpty()) {
            0
        } else {
            val lowestNode = lowestNodes.first()
            val route = RouteFinder(this, lowestNode)
            if (route.hasRoute()) {
                route.getRoute().getDistance()
            } else {
                0
            }
        }
    }

    fun isAnOuterNode(direction: Direction): Boolean {
        val furthestNodes = network.getFurthestLocations(direction)
        return direction != Direction.NONE && (furthestNodes.isEmpty() || furthestNodes.contains(this))
    }


}