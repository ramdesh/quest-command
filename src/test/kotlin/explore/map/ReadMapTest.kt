package explore.map

import traveling.position.Vector
import traveling.location.Connection
import traveling.location.network.LocationNode
import traveling.location.location.LocationPoint
import core.history.ChatHistory
import core.target.Target
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
        val player = Target("Bob")
        val event = ReadMapEvent(player, target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It has no known neighbors.", actual)
    }

    @Test
    fun aSingleNeighborIsProperlyDisplayedWithDirection(){
        val target = LocationNode("My Place")
        target.addConnection(Connection(LocationPoint(target), LocationPoint(LocationNode("Destination", discovered = true)), Vector(0, 10, 0)))
        val player = Target("Bob")
        val event = ReadMapEvent(player, target)

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
        target.addConnection(Connection(targetPoint, LocationPoint(LocationNode("north", discovered = true)), Vector(0, 10, 0)))
        target.addConnection(Connection(targetPoint, LocationPoint(LocationNode("south", discovered = true)), Vector(0, -10, 0)))
        target.addConnection(Connection(targetPoint, LocationPoint(LocationNode("east", discovered = true)), Vector(10, 0, 0)))
        target.addConnection(Connection(targetPoint, LocationPoint(LocationNode("west", discovered = true)), Vector(-10, 0, 0)))
        val player = Target("Bob")
        val event = ReadMapEvent(player, target)

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