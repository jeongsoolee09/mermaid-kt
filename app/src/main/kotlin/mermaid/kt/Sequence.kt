package mermaid.kt.Sequence

class Unreachable() : Exception()

/* ============ Components ============ */

interface Component {
    fun render(acc: StringBuilder) : StringBuilder
}

/* =================================== */
/* ============ Base Case ============ */
/* =================================== */

abstract class Base : Component

// ============ Sub-Component Constructs ============

abstract class SubComponent : Base()

class Actor(val name: String) : SubComponent() {
    override fun toString() = this.name

    override fun render(acc: StringBuilder) : StringBuilder =
        acc.append(this.toString())
}

fun actor(name: String) = Actor(name)

class Color(val r: Int, val g: Int, val b: Int, val a: Float) : SubComponent() {
    companion object {
        fun fromString(colorString: String): Color =
            when (colorString) {
                "red" -> Color(255, 0, 0, 0.2.toFloat())
                "green" -> Color(0, 255, 0, 0.1.toFloat())
                "blue" -> Color(0, 0, 255, 0.1.toFloat())
                "yellow" -> Color(255, 255, 0, 0.5.toFloat())
                "gray" -> Color(0, 0, 0, 0.1.toFloat())
                else -> Color(0, 0, 0, 0.toFloat()) // fall back to black.
            }
        fun toString(color: Color): String =
            "rgb(${color.r}, ${color.g}, ${color.b}, ${color.a})"
    }
    override fun render(acc: StringBuilder) : StringBuilder =
        acc.append(this.toString())
}

// ============ Labels ============

abstract class Label() : Base()

class Autonumber() : Label() {
    override fun render(acc: StringBuilder): StringBuilder =
        acc.append("autonumber")
}

class Participants(var participants: List<Actor>?) : Label() {
    override fun render(acc: StringBuilder): StringBuilder =
        acc.append(participants!!.joinToString(prefix="participants ", separator=", "))
}

class Activate(var actor: Actor?) : Label() {
    override fun render(acc: StringBuilder): StringBuilder =
        acc.append("activate " + actor.toString())
}

class Deactivate(var actor: Actor?) : Label() {
    override fun render(acc: StringBuilder): StringBuilder =
        acc.append("deactivate " + actor.toString())
}

// ============ Arrows ============

abstract class Arrow() : Base()

abstract class Solid() : Arrow()

class SolidLine(var from: Actor?, var to: Actor?, var message: String?,
                val activate: Boolean = false, val deactivate: Boolean = false): Solid() {
    override fun render(acc: StringBuilder) : StringBuilder =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> acc.append("${this.from}->${this.to}: ${this.message}")
            Pair(true, false)  -> acc.append("${this.from}->+${this.to}: ${this.message}")
            Pair(false, true)  -> acc.append("${this.from}->-${this.to}: ${this.message}")
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
            }
}


class SolidArrow(var from: Actor?, var to: Actor?, var message: String?,
                 val activate: Boolean = false, val deactivate: Boolean = false): Solid() {
    override fun render(acc: StringBuilder) : StringBuilder =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> acc.append("${this.from}->>${this.to}: ${this.message}")
            Pair(true, false)  -> acc.append("${this.from}->>+${this.to}: ${this.message}")
            Pair(false, true)  -> acc.append("${this.from}->>-${this.to}: ${this.message}")
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
            }
}

class SolidCross(var from: Actor?, var to: Actor?, var message: String?,
                 val activate: Boolean = false, val deactivate: Boolean = false): Solid() {
    override fun render(acc: StringBuilder) : StringBuilder =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> acc.append("${this.from}-x${this.to}: ${this.message}")
            Pair(true, false)  -> acc.append("${this.from}-x+${this.to}: ${this.message}")
            Pair(false, true)  -> acc.append("${this.from}-x-${this.to}: ${this.message}")
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
            }
}

class SolidOpen(var from: Actor?, var to: Actor?, var message: String?,
                val activate: Boolean = false, val deactivate: Boolean = false): Solid() {
    override fun render(acc: StringBuilder) : StringBuilder =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> acc.append("${this.from}-)${this.to}: ${this.message}")
            Pair(true, false)  -> acc.append("${this.from}-)+${this.to}: ${this.message}")
            Pair(false, true)  -> acc.append("${this.from}-)-${this.to}: ${this.message}")
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
            }
}

abstract class Dotted() : Arrow()

class DottedLine(var from: Actor?, var to: Actor?, var message: String?,
                 val activate: Boolean = false, val deactivate: Boolean = false): Dotted() {
    override fun render(acc: StringBuilder) : StringBuilder =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> acc.append("${this.from}-->${this.to}: ${this.message}")
            Pair(true, false)  -> acc.append("${this.from}-->+${this.to}: ${this.message}")
            Pair(false, true)  -> acc.append("${this.from}-->-${this.to}: ${this.message}")
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
        }
}


class DottedArrow(var from: Actor?, var to: Actor?, var message: String?,
                  val activate: Boolean = false, val deactivate: Boolean = false): Dotted() {
    override fun render(acc: StringBuilder) : StringBuilder =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> acc.append("${this.from}-->>${this.to}: ${this.message}")
            Pair(true, false)  -> acc.append("${this.from}-->>+${this.to}: ${this.message}")
            Pair(false, true)  -> acc.append("${this.from}-->>-${this.to}: ${this.message}")
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
        }
}


class DottedCross(var from: Actor?, var to: Actor?, var message: String?,
                  val activate: Boolean = false, val deactivate: Boolean = false): Dotted() {
    override fun render(acc: StringBuilder) : StringBuilder =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> acc.append("${this.from}--x${this.to}: ${this.message}")
            Pair(true, false)  -> acc.append("${this.from}--x+${this.to}: ${this.message}")
            Pair(false, true)  -> acc.append("${this.from}--x-${this.to}: ${this.message}")
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
        }
}


class DottedOpen(var from: Actor?, var to: Actor?, var message: String?,
                 val activate: Boolean = false, val deactivate: Boolean = false): Dotted() {
    override fun render(acc: StringBuilder) : StringBuilder =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> acc.append("${this.from}--)${this.to}: ${this.message}")
            Pair(true, false)  -> acc.append("${this.from}--)+${this.to}: ${this.message}")
            Pair(false, true)  -> acc.append("${this.from}--)-${this.to}: ${this.message}")
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
        }
}

abstract class Note() : Base()

class NoteLeft(var actor: Actor?, var note: String?) : Block() {
    override fun render(acc: StringBuilder) : StringBuilder =
        acc.append("Note left of ${this.actor}: ${this.note}")
}

class NoteRight(var actor: Actor?, var note: String?) : Block() {
    override fun render(acc: StringBuilder) : StringBuilder =
        acc.append("Note right of ${this.actor}: ${this.note}")
}

class NoteOver(var actor1: Actor?, var actor2: Actor?, var note: String?) : Block() {
    override fun render(acc: StringBuilder) : StringBuilder =
        acc.append(if (this.actor2 != null) "Note over ${this.actor1},${this.actor2}: $note"
                   else "Note over ${this.actor1}: ${this.note}")
}

/* ======================================== */
/* ============ Inductive Case ============ */
/* ======================================== */

abstract class Inductive : Component {
    val children = arrayListOf<Component>()

    // For Base Components
    fun <T : Component> initComponent(component: T): T {
        children.add(component)
        return component
    }

    // For Inductive Components
    fun <T : Component> initComponent(component: T, thenWhat: T.() -> Unit): T {
        component.thenWhat()
        children.add(component)
        return component
    }

    /* Builder Methods */

    // Labels ============

    fun autonumber() =
        initComponent(Autonumber())

    fun participants(vararg participants: Actor) =
        initComponent(Participants(participants.toList()))

    fun activate(actor: Actor) =
        initComponent(Activate(actor))

    fun deactivate(actor: Actor) =
        initComponent(Deactivate(actor))

    // Solid Arrows ============

    fun solidLine(from: Actor, to: Actor, message: String,
                 activate: Boolean = false, deactivate: Boolean = false) =
        initComponent(SolidLine(from, to, message, activate, deactivate))

    fun solidArrow(from: Actor, to: Actor, message: String,
              activate: Boolean = false, deactivate: Boolean = false) =
        initComponent(SolidArrow(from, to, message, activate, deactivate))

    fun solidCross(from: Actor, to: Actor, message: String,
              activate: Boolean = false, deactivate: Boolean = false) =
        initComponent(SolidCross(from, to, message, activate, deactivate))

    fun solidOpen(from: Actor, to: Actor, message: String,
                  activate: Boolean = false, deactivate: Boolean = false) =
        initComponent(SolidOpen(from, to, message, activate, deactivate))

    // Dotted Arrows ============

    fun dottedLine(from: Actor, to: Actor, message: String,
                 activate: Boolean = false, deactivate: Boolean = false) =
        initComponent(DottedLine(from, to, message, activate, deactivate))

    fun dottedArrow(from: Actor, to: Actor, message: String,
              activate: Boolean = false, deactivate: Boolean = false) =
        initComponent(DottedArrow(from, to, message, activate, deactivate))

    fun dottedCross(from: Actor, to: Actor, message: String,
              activate: Boolean = false, deactivate: Boolean = false) =
        initComponent(DottedCross(from, to, message, activate, deactivate))

    fun dottedOpen(from: Actor, to: Actor, message: String,
                  activate: Boolean = false, deactivate: Boolean = false) =
        initComponent(DottedOpen(from, to, message, activate, deactivate))

    // Notes ============

    fun noteLeft(actor: Actor, note: String) =
        initComponent(NoteLeft(actor, note))

    fun noteRight(actor: Actor, note: String) =
        initComponent(NoteRight(actor, note))

    fun noteOver(actor1: Actor, actor2: Actor, note: String) =
        initComponent(NoteOver(actor1, actor2, note))

    fun noteOver(actor: Actor, note: String) =
        initComponent(NoteOver(actor, null, note))

    // Blocks ============

    fun loop(label: String, thenWhat: Loop.() -> Unit) =
        initComponent(Loop(label), thenWhat)

    fun highlight(colorString: String, thenWhat: Highlight.() -> Unit) =
        initComponent(Highlight(Color.fromString(colorString)), thenWhat)

    fun alternative(condition: String, thenWhat: Alternative.() -> Unit) =
        initComponent(Alternative(condition), thenWhat)

    fun parallel(description: String, thenWhat: Parallel.() -> Unit) =
        initComponent(Parallel(description), thenWhat)

    fun optional(description: String, thenWhat: Optional.() -> Unit) =
        initComponent(Optional(description), thenWhat)
}

// ============ Blocks ============

abstract class Block() : Inductive()

class Loop(var label: String?) : Block() {
    override fun render(acc: StringBuilder) : StringBuilder {
        acc.append("loop ")
        acc.append(this.label)
        acc.append("\n")
        for (child in this.children) {
            acc.append("    ")
            child.render(acc)
        }
        acc.append("end")
        return acc
    }
}

class Highlight(var color: Color?) : Block() {
    override fun render(acc: StringBuilder) : StringBuilder {
        acc.append("rect ")
        acc.append(this.color.toString())
        acc.append("\n")
        for (child in this.children) {
            acc.append("    ")
            child.render(acc)
        }
        acc.append("end")
        return acc
    }
}

class Alternative(var condition: String?) : Block() {
    fun elseClause(condition: String?, thenWhat: ElseClause.() -> Unit) =
        initComponent(ElseClause(condition), thenWhat)

    override fun render(acc: StringBuilder) : StringBuilder {
        acc.append("alt ")
        acc.append(this.condition)
        acc.append("\n")
        for (child in this.children) {
            acc.append("    ")
            child.render(acc)
        }
        acc.append("end")
        return acc
    }
}

class Parallel(var description: String?) : Block() {
    fun andClause(condition: String?, thenWhat: AndClause.() -> Unit) =
        initComponent(AndClause(condition), thenWhat)

    override fun render(acc: StringBuilder) : StringBuilder {
        acc.append("parallel ")
        acc.append(this.description)
        acc.append("\n")
        for (child in this.children) {
            acc.append("    ")
            child.render(acc)
        }
        acc.append("end")
        return acc
    }
}

class Optional(var description: String?) : Block() {
    override fun render(acc: StringBuilder) : StringBuilder {
        acc.append("opt ")
        acc.append(this.description)
        acc.append("\n")
        for (child in this.children) {
            acc.append("    ")
            child.render(acc)
        }
        acc.append("end")
        return acc
    }
}

// ============ Clauses ============

abstract class Clause() : Inductive()

class ElseClause(var condition: String?) : Clause() {
    override fun render(acc: StringBuilder) : StringBuilder {
        acc.append("else ")
        acc.append(this.condition)
        acc.append("\n")
        for (child in this.children) {
            acc.append("    ")
            child.render(acc)
        }
        acc.append("end")
        return acc
    }
}

class AndClause(var condition: String?) : Clause() {
    override fun render(acc: StringBuilder) : StringBuilder {
        acc.append("and ")
        acc.append(this.condition)
        acc.append("\n")
        for (child in this.children) {
            acc.append("    ")
            child.render(acc)
        }
        acc.append("end")
        return acc
    }
}

/* ==================================== */
/* ============ Main Class ============ */
/* ==================================== */

class SequenceDiagram() : Inductive() {
    override fun render(acc: StringBuilder) : StringBuilder {
        acc.append("sequenceDiagram")
        acc.append("\n")
        for (child in this.children) {
            acc.append("    ")
            child.render(acc)
        }
        return acc
    }

    fun initSequenceDiagram(thenWhat: SequenceDiagram.() -> Unit) =
        initComponent(SequenceDiagram(), thenWhat)
}

fun sequenceDiagram(thenWhat: SequenceDiagram.() -> Unit) : String {
    val builder = StringBuilder()
    val diagram = SequenceDiagram().initSequenceDiagram(thenWhat)
    println(diagram.children)
    diagram.render(builder)
    return builder.toString()
}
