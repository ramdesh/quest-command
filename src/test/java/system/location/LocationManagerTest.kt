package system.location

import core.gameState.location.LocationLink
import core.gameState.location.LocationNode
import core.utility.NameSearchableList
import org.junit.Assert
import org.junit.Test
import system.DependencyInjector

class LocationManagerTest {

    @Test
    fun linksCreateNeighborsIfTheyDoNotExist() {
        val neighborLink = LocationLink("neighbor")
        val source = LocationNode("source", locations = mutableListOf(neighborLink))
        val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(source))

        DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
        LocationManager.reset()

        Assert.assertEquals(source, LocationManager.getLocationNode(source.name))
        Assert.assertEquals(neighborLink.name, LocationManager.findLocation(neighborLink.name).name)
    }

    @Test
    fun noDuplicateNodesOrLinks() {
        val neighborExistsName = "neighbor exists"
        val neighborDoesNotExistsName = "neighbor doesn't exists"

        val source = LocationNode("source", locations = mutableListOf(LocationLink(neighborExistsName), LocationLink(neighborDoesNotExistsName)))
        val neighborExists = LocationNode(neighborExistsName)
        val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(listOf(source, neighborExists)))

        DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
        LocationManager.reset()

        val neighborDoesNotExists = LocationManager.findLocation(neighborDoesNotExistsName)

        //No Duplicate Nodes
        Assert.assertEquals(source, LocationManager.getLocationNode(source.name))
        Assert.assertEquals(neighborExists, LocationManager.findLocation(neighborExists.name))
        Assert.assertEquals(neighborDoesNotExistsName, neighborDoesNotExists.name)
        Assert.assertEquals(3, LocationManager.countLocationNodes())

        //No Duplicate Links
        Assert.assertEquals(2, source.getNeighborLinks().size)
        Assert.assertEquals(1, neighborExists.getNeighborLinks().size)
        Assert.assertEquals(1, neighborDoesNotExists.getNeighborLinks().size)
    }

    @Test
    fun oneWayLinksDontLinkBack() {

        val neighbor = LocationNode("neighbor")
        val source = LocationNode("source", locations = mutableListOf(LocationLink(neighbor.name, oneWay = true)))
        val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(listOf(source, neighbor)))

        DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
        LocationManager.reset()

        Assert.assertEquals(source, LocationManager.getLocationNode(source.name))
        Assert.assertEquals(neighbor, LocationManager.getLocationNode(neighbor.name))
        Assert.assertEquals(1, source.getNeighborLinks().size)
        Assert.assertEquals(0, neighbor.getNeighborLinks().size)

    }

    @Test
    fun locationNodesCreateLocationsThatDontExist() {
        val source = LocationNode("source")
        val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(source))

        DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
        LocationManager.reset()

        Assert.assertEquals(source.name, source.getLocation().name)
    }


}