package mermaid.kt

import kotlin.test.*

import mermaid.kt.Sequence.*

private val sample = sequenceDiagram {
    autonumber()
    participants(listOf(Actor("alice"), Actor("bob"), Actor("john")))

    alternative("x=0") {
        solidArrow(Actor("alice"), Actor("bob"), "hihi")
        dottedArrow(Actor("bob"), Actor("alice"), "hihi")
        elseClause("x=1") {
            solidArrow(Actor("alice"), Actor("bob"), "hihi")
            dottedArrow(Actor("bob"), Actor("alice"), "hihi")
        }
        elseClause("x=2") {
            solidArrow(Actor("alice"), Actor("bob"), "hihi")
            dottedArrow(Actor("bob"), Actor("alice"), "hihi")
        }
        elseClause("x=3") {
            solidArrow(Actor("alice"), Actor("bob"), "hihi")
            dottedArrow(Actor("bob"), Actor("alice"), "hihi")
        }
    }

    solidArrow(Actor("alice"), Actor("bob"), "hihi")
    noteOver(Actor("alice"), "alice says hihi")
    solidArrow(Actor("bob"), Actor("john"), "hoho")
    noteOver(Actor("bob"), Actor("john"), "bob says hoho")

    parallel("Alice to Bob") {
        solidArrow(Actor("alice"), Actor("bob"), "hihi")
        andClause("Bob to John") {
            solidArrow(Actor("bob"), Actor("john"), "hihi")
        }
        andClause("Alice to John") {
            solidArrow(Actor("alice"), Actor("john"), "hihi")
        }
    }

    highlight("red") {
        optional("this is red") {
            solidArrow(Actor("alice"), Actor("bob"), "hihi")
            dottedArrow(Actor("bob"), Actor("alice"), "hihi")
        }
    }
}

class BaseTest {
    @Test
    fun testNote() {
        val leftExpected = """
        sequenceDiagram
            Note left of John: Text in note
        """.trimIndent()
        val leftReal = sequenceDiagram {
            noteLeft(Actor("John"), "Text in note")
        }
        assertEquals(leftReal, leftExpected)

        val rightExpected = """
        sequenceDiagram
            Note right of John: Text in note
        """.trimIndent()
        val rightReal = sequenceDiagram {
            noteRight(Actor("John"), "Text in note")
        }
        assertEquals(rightReal, rightExpected)

        val overExpected = """
        sequenceDiagram
            Note over John: Text in note
        """.trimIndent()
        val overReal = sequenceDiagram {
            noteOver(Actor("John"), "Text in note")
        }
        assertEquals(overReal, overExpected)

        val over2Expected = """
        sequenceDiagram
            Note over Alice,Bob: Text in note
        """.trimIndent()
        val over2Real = sequenceDiagram {
            noteOver(Actor("Alice"), Actor("Bob"), "Text in note")
        }
        assertEquals(over2Real, over2Expected)
    }
}

class InductiveTest {

}

class CombinedTest {

}
