package system.location

import core.gameState.GameState
import core.gameState.location.*
import core.utility.NameSearchableList
import system.DependencyInjector

object LocationManager {
    private val locationHelper = LocationHelper()
    private var networks = loadNetworks()

    fun reset() {
        networks = loadNetworks()
    }

    private fun loadNetworks(): NameSearchableList<Network> {
        val parser = DependencyInjector.getImplementation(LocationParser::class.java)
        val locations = parser.loadLocations()
        val nodes: List<LocationNode> = parser.loadLocationNodes()

        val nodeMap = locationHelper.buildInitialMap(nodes)
        locationHelper.createNeighborsAndNeighborLinks(nodeMap)
        createLocationIfNeeded(nodeMap, locations)

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }.distinct()
            val network = Network(entry.key, entry.value, networkLocations)
            entry.value.forEach { it.network = network }
            network
        }

        return NameSearchableList(networks)
    }

    private fun createLocationIfNeeded(nodeMap: Map<String, List<LocationNode>>, locations: NameSearchableList<Location>) {
        nodeMap.values.forEach { network ->
            network.forEach { node ->
                if (!locations.exists(node.locationName)) {
                    locations.add(Location(node.locationName))
                }
            }
        }
    }

    fun networkExists(name: String = GameState.player.location.parent): Boolean {
        return networks.getOrNull(name) != null
    }

    fun getNetwork(name: String = GameState.player.location.parent): Network {
        return networks.getOrNull(name) ?: throw IllegalArgumentException("Network $name does not exist!")
    }

    fun getNetworks(): List<Network> {
        return networks.toList()
    }




}