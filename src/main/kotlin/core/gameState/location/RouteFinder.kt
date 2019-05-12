package core.gameState.location

class RouteFinder(private val source: LocationNode, private val destination: LocationNode, private val depth: Int) {
    private val potentials: MutableList<Route> = mutableListOf()
    private val examined: MutableList<LocationNode> = mutableListOf()
    private var solution: Route? = null

    init {
        findRoute()
    }

    fun hasRoute(): Boolean {
        return solution != null
    }

    fun getRoute(): Route {
        return solution!!
    }

    private fun findRoute() {
        if (source.getNeighborLinks().any { it.destination.location == destination }) {
            solution = Route(source)
            solution?.addLink(source.getNeighborLinks().first { it.destination.location == destination })
        } else {
            source.getNeighborLinks().forEach {
                val route = Route(source)
                route.addLink(it)
                potentials.add(route)
                if (route.destination == destination){
                    solution = route
                }
            }
            examined.add(source)

            buildNeighbors()
        }
    }


    private fun buildNeighbors() {
        if (solution == null && potentials.isNotEmpty() && depth > potentials.first().getLinks().size) {
            val current = potentials.toList()
            potentials.clear()

            current.forEach { route ->
                if (!examined.contains(route.destination)) {
                    examined.add(route.destination)
                    route.destination.getNeighborLinks().forEach { connection ->
                        if (!examined.contains(connection.destination.location) && solution == null) {
                            val newRoute = Route(route)
                            newRoute.addLink(connection)
                            potentials.add(newRoute)
                            if (newRoute.destination == destination){
                                solution = newRoute
                            }
                        }
                    }
                }
            }
            buildNeighbors()
        }
    }


}