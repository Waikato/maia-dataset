package maia.ml.dataset.util

import maia.ml.dataset.error.MissingValue
import maia.ml.dataset.type.standard.Nominal
import maia.ml.dataset.type.standard.Numeric
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertIs


/**
 * Tests the builders.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
internal class BuildTest {

    @Test
    fun testBuildHeaders() {
        val testHeaders = buildHeaders {
            "num-f"     feature     Numeric(false)
            "num-t"     target      Numeric(true)
            "nom-f"     feature     Nominal(false, "a", "b")
            "nom-t"     target      Nominal(false, "a", "b")
        }

        assertEquals(4, testHeaders.size, "headers wrong size: ${testHeaders.size}")
        assert(!testHeaders["num-f"]!!.isTarget) { "num-f is target" }
        assert(testHeaders["num-t"]!!.isTarget) { "num-t is feature" }
        assert(!testHeaders["nom-f"]!!.isTarget) { "nom-f is target" }
        assert(testHeaders["nom-t"]!!.isTarget) { "nom-t is feature" }
        assertIs<Numeric>(testHeaders["num-f"]!!.type, "num-f is not Numeric")
        assertIs<Numeric>(testHeaders["num-t"]!!.type, "num-t is not Numeric")
        assertIs<Nominal>(testHeaders["nom-f"]!!.type, "nom-f is not Nominal")
        assertIs<Nominal>(testHeaders["nom-t"]!!.type, "nom-t is not Nominal")
    }

    @Test
    fun testBuildRow() {
        val testRow = buildRow {
            "present"     feature     Numeric(true).canonicalRepresentation *= 1.2
            "missing"     target      Numeric(true) // No value
        }

        val presentValue = assertDoesNotThrow {
            testRow.getValue(testRow.headers["present"]!!.type.canonicalRepresentation)
        }
        assertEquals(1.2, presentValue)
        assertThrows<MissingValue> {
            testRow.getValue(testRow.headers["missing"]!!.type.canonicalRepresentation)
        }
    }

}
