package explore.map

import core.gameState.Position
import core.gameState.location.Connection
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint
import core.history.ChatHistory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ReadMapTest {

    @Before
    fun setup(){
        ChatHistory.reset()
    }

    @Test
    fun noNeighbors(){
        val target = LocationNode("My Place")
        val event = ReadMapEvent(target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It has no known neighbors.", actual)
    }

    @Test
    fun aSingleNeighborIsProperlyDisplayedWithDirection(){
        val target = LocationNode("My Place")
        target.addLink(Connection(LocationPoint(target), LocationPoint(LocationNode("Destination")), Position(0,10,0)))
        val event = ReadMapEvent(target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It is neighbored by:\n" +
                "  Name         Distance  Direction Path  \n" +
                "  Destination  10        N               \n", actual)
    }

    @Test
    fun neighborsAreProperlyDisplayedWithDirection(){
        val target = LocationNode("My Place")
        val targetPoint = LocationPoint(target)
        target.addLink(Connection(targetPoint, LocationPoint(LocationNode("north")), Position(0,10,0)))
        target.addLink(Connection(targetPoint, LocationPoint(LocationNode("south")), Position(0,-10,0)))
        target.addLink(Connection(targetPoint, LocationPoint(LocationNode("east")), Position(10,0,0)))
        target.addLink(Connection(targetPoint, LocationPoint(LocationNode("west")), Position(-10,0,0)))
        val event = ReadMapEvent(target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It is neighbored by:\n" +
                "  Name   Distance  Direction Path  \n" +
                "  north  10        N               \n" +
                "  south  10        S               \n" +
                "  east   10        E               \n" +
                "  west   10        W               \n", actual)
    }
}