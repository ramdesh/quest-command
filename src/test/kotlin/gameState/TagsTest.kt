package gameState

import core.gameState.Tags
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class TagsTest {

    @Test
    fun hasTag() {
        val tag = Tags(listOf("Apple"))
        assertTrue(tag.has("Apple"))
    }

    @Test
    fun doesNotHaveTag() {
        val tag = Tags(listOf("Apple"))
        assertFalse(tag.has("Pear"))
    }

    @Test
    fun hasTagRegardlessOfCapitalization() {
        val tag = Tags(listOf("Apple"))
        assertTrue(tag.has("aPpLE"))
    }

    @Test
    fun hasAll() {
        val tag = Tags(listOf("Apple", "Pear", "Orange"))
        val desired = Tags(listOf("Apple", "Pear"))
        assertTrue(tag.hasAll(desired))
    }

    @Test
    fun doesNotHaveAll() {
        val tag = Tags(listOf("Apple", "Pear"))
        val desired = Tags(listOf("Apple", "Pear", "Orange"))
        assertFalse(tag.hasAll(desired))
    }

    @Test
    fun hasNone() {
        val tag = Tags(listOf("Apple", "Pear"))
        val desired = Tags(listOf("Orange", "Banana"))
        assertTrue(tag.hasNone(desired))
    }

    @Test
    fun doesNotHaveNone() {
        val tag = Tags(listOf("Apple", "Pear"))
        val desired = Tags(listOf("Orange", "Pear"))
        assertFalse(tag.hasNone(desired))
    }

    @Test
    fun matches() {
        val tagA = Tags(listOf("Apple", "Pear"))
        val tagB = Tags(listOf("Pear", "ApPLe"))
        assertTrue(tagA.matches(tagB))
        assertTrue(tagB.matches(tagA))
    }

    @Test
    fun noMatchIfALarger() {
        val tagA = Tags(listOf("Apple", "Pear", "Orange"))
        val tagB = Tags(listOf("Pear", "ApPLe"))
        assertFalse(tagA.matches(tagB))
        assertFalse(tagB.matches(tagA))
    }

    @Test
    fun noMatchIfBLarger() {
        val tagA = Tags(listOf("Pear", "ApPLe"))
        val tagB = Tags(listOf("Apple", "Pear", "Orange"))
        assertFalse(tagA.matches(tagB))
        assertFalse(tagB.matches(tagA))
    }

    @Test
    fun doNotAddDuplicateTags() {
        val tag = Tags(listOf("Apple"))
        tag.add(("aPPlE"))
        assertEquals(1, tag.getAll().size)
        assertEquals("Apple", tag.getAll()[0])
    }


}