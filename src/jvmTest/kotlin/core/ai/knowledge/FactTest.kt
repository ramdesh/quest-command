package core.ai.knowledge

import kotlin.test.Test
import kotlin.test.assertEquals


class FactTest {
    //Examples of Facts and Relationships
    private val bob = SimpleSubject("Bob")
    private val loc = SimpleSubject(locationName = "Kanbara Home")

    //In the player's mind
    val location = Fact(loc, "Exists", 100, 1) //0 would mean confident it does NOT exist

    @Test
    fun singleEntryAverage() {
        val actual = average(6 to 10)
        assertEquals(6, actual.confidence)
        assertEquals(10, actual.amount)
    }

    @Test
    fun confidenceIsAveraged() {
        val actual = average(0 to 10, 10 to 1, 5 to 1)
        assertEquals(5, actual.confidence)
    }

    @Test
    fun amountIsWeightedAveraged() {
        val actual = average(2 to 10, 5 to 50, 3 to 40)
        assertEquals(39, actual.amount)
    }

    @Test
    fun zeroConfidence() {
        val actual = average(0 to 10)
        assertEquals(0, actual.amount)
    }

    private fun average(vararg facts: Pair<Int, Int>): Fact {
        return facts.map { (confidence, amount) -> fact(confidence, amount) }.average()
    }

    private fun fact(confidence: Int = 0, amount: Int = 0) = Fact(bob, "test", confidence, amount)
}