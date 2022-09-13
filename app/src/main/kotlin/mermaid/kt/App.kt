package mermaid.kt

import mermaid.kt.Sequence.*

fun main() {
    val sd = sequenceDiagram {
        noteLeft(Actor("john"), "Text in note")
    }
}
