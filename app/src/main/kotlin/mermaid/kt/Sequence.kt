package mermaid.kt.Sequence

class Unreachable() : Exception()

/* ============ Components ============ */

interface Component {
    fun render(builder: StringBuilder, indent: String)
    fun <T : Component> initComponent(component: T, thenWhat: T.() -> Unit): T
}

/* =================================== */
/* ============ Base Case ============ */
/* =================================== */

abstract class Base : Component {
    override fun render(builder: StringBuilder, indent: String): Unit {
    }
    override fun <T : Component> initComponent(component: T, thenWhat: T.() -> Unit) = component
}

// ============ Sub-Component Constructs ============

    abstract class SubComponent : Base()

data class Actor(val name: String) : SubComponent()

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
        fun toString(color: Color): String = "rgb(${color.r}, ${color.g}, ${color.b}, ${color.a})"
    }
}

// ============ Labels ============

    abstract class Label() : Base() {
        fun autonumber() = Autonumber()

        fun participants(participants: List<Actor>) = Participants(participants)
    }

class Autonumber() : Label()

data class Participants(var participants: List<Actor>?) : Label()

// ============ Calls ============

abstract class Arrow() : Base()

abstract class Solid() : Arrow() {
    fun line(from: Actor, to: Actor, message: String,
             activate: Boolean = false, deactivate: Boolean = false) = SolidLine(from, to, message, activate, deactivate)

    fun arrow(from: Actor, to: Actor, message: String,
              activate: Boolean = false, deactivate: Boolean = false) = SolidArrow(from, to, message, activate, deactivate)

    fun cross(from: Actor, to: Actor, message: String,
              activate: Boolean = false, deactivate: Boolean = false) = SolidCross(from, to, message, activate, deactivate)

    fun openArrow(from: Actor, to: Actor, message: String,
                  activate: Boolean = false, deactivate: Boolean = false) = SolidOpen(from, to, message, activate, deactivate)
}

class SolidLine(var from: Actor?, var to: Actor?, var message: String?,
                val activate: Boolean = false, val deactivate: Boolean = false): Solid() {
    override fun toString() : String =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> "${this.from}->${this.to}: ${this.message}"
            Pair(true, false)  -> "${this.from}->+${this.to}: ${this.message}"
            Pair(false, true)  -> "${this.from}->-${this.to}: ${this.message}"
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
            }
}

class SolidArrow(var from: Actor?, var to: Actor?, var message: String?,
                 val activate: Boolean = false, val deactivate: Boolean = false): Solid() {
    override fun toString() : String =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> "${this.from}-->${this.to}: ${this.message}"
            Pair(true, false)  -> "${this.from}-->+${this.to}: ${this.message}"
            Pair(false, true)  -> "${this.from}-->-${this.to}: ${this.message}"
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
            }
}

class SolidCross(var from: Actor?, var to: Actor?, var message: String?,
                 val activate: Boolean = false, val deactivate: Boolean = false): Solid() {
    override fun toString() : String =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> "${this.from}-x${this.to}: ${this.message}"
            Pair(true, false)  -> "${this.from}-x+${this.to}: ${this.message}"
            Pair(false, true)  -> "${this.from}-x-${this.to}: ${this.message}"
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
            }
}

class SolidOpen(var from: Actor?, var to: Actor?, var message: String?,
                val activate: Boolean = false, val deactivate: Boolean = false): Solid() {
    override fun toString() : String =
        when(Pair(this.activate, this.deactivate)) {
            Pair(false, false) -> "${this.from}-)${this.to}: ${this.message}"
            Pair(true, false)  -> "${this.from}-)+${this.to}: ${this.message}"
            Pair(false, true)  -> "${this.from}-)-${this.to}: ${this.message}"
            Pair(true, true)   ->
                throw IllegalArgumentException("Cannot be both activate and deactivate receiver at the same time")
            else -> throw Unreachable()
            }
}

abstract class Dotted() : Arrow() {
    fun line(from: Actor, to: Actor, message: String,
             activate: Boolean = false, deactivate: Boolean = false) =
        DottedLine(from, to, message, activate, deactivate)

    fun arrow(from: Actor, to: Actor, message: String,
              activate: Boolean = false, deactivate: Boolean = false) =
        DottedArrow(from, to, message, activate, deactivate)

    fun cross(from: Actor, to: Actor, message: String,
              activate: Boolean = false, deactivate: Boolean = false) =
        DottedCross(from, to, message, activate, deactivate)

    fun openArrow(from: Actor, to: Actor, message: String,
                  activate: Boolean = false, deactivate: Boolean = false) =
        DottedOpen(from, to, message, activate, deactivate)
}

class DottedLine(var from: Actor?, var to: Actor?, var message: String?,
                 val activate: Boolean = false, val deactivate: Boolean = false): Dotted()

class DottedArrow(var from: Actor?, var to: Actor?, var message: String?,
                  val activate: Boolean = false, val deactivate: Boolean = false): Dotted()

class DottedCross(var from: Actor?, var to: Actor?, var message: String?,
                  val activate: Boolean = false, val deactivate: Boolean = false): Dotted()

class DottedOpen(var from: Actor?, var to: Actor?, var message: String?,
                 val activate: Boolean = false, val deactivate: Boolean = false): Dotted()

/* ======================================== */
/* ============ Inductive Case ============ */
/* ======================================== */

abstract class Inductive : Component {
    val children = arrayListOf<Inductive>()
    override fun render(builder: StringBuilder, indent: String) {
        when (this) {}
    }
    override fun <T : Component> initComponent(component: T, thenWhat: T.() -> Unit): T {
        component.thenWhat()
        if (component is Inductive) {
            children.add(component)
        } else {
            throw IllegalArgumentException("WTF")
        }
        return component
    }
}

// ============ Blocks ============

abstract class Block() : Inductive() {
    fun loop(label: String, thenWhat: Loop.() -> Unit) = initComponent(Loop(label), thenWhat)

    fun noteLeft(actor: Actor, note: String, thenWhat: NoteLeft.() -> Unit) =
        initComponent(NoteLeft(actor, note), thenWhat)

    fun noteRight(actor: Actor, note: String, thenWhat: NoteRight.() -> Unit) =
        initComponent(NoteRight(actor, note), thenWhat)

    fun noteOver(actor1: Actor, actor2: Actor, note: String, thenWhat: NoteOver.() -> Unit) =
        initComponent(NoteOver(actor1, actor2, note), thenWhat)

    fun noteOver(actor: Actor, note: String, thenWhat: NoteOver.() -> Unit) =
        initComponent(NoteOver(actor, null, note), thenWhat)

    fun highlight(colorString: String, thenWhat: Highlight.() -> Unit) =
        initComponent(Highlight(Color.fromString(colorString)), thenWhat)

    fun alternative(condition: String, thenWhat: Alternative.() -> Unit) =
        initComponent(Alternative(condition), thenWhat)

    fun parallel(description: String, thenWhat: Parallel.() -> Unit) =
        initComponent(Parallel(description), thenWhat)

    fun optional(description: String, thenWhat: Optional.() -> Unit) =
        initComponent(Optional(description), thenWhat)
}

data class Loop(var label: String?) : Block()

data class NoteLeft(var actor: Actor?, var note: String?) : Block()

data class NoteRight(var actor: Actor?, var note: String?) : Block()

data class NoteOver(var actor1: Actor?, var actor2: Actor?, var note: String?) : Block()

data class Highlight(var color: Color?) : Block()

class Alternative(var condition: String?) : Block() {
    fun elseClause(condition: String?, thenWhat: ElseClause.() -> Unit) =
        initComponent(ElseClause(condition), thenWhat)
}

class Parallel(var description: String?) : Block() {
    fun andClause(condition: String?, thenWhat: AndClause.() -> Unit) =
        initComponent(AndClause(condition), thenWhat)
}

data class Optional(var description: String?) : Block()

// ============ Clauses ============

    abstract class Clause() : Inductive()

data class ElseClause(var condition: String?) : Clause()

data class AndClause(var condition: String?) : Clause()

// val sample = sequenceDiagram {
    //     autonumber()
    //     participants("alice", "bob", "john")
    //
    //     alternative("x=0") {
        //         callSync("alice", "bob", "hihi")
        //         replySync("bob", "alice", "hihi")
        //         elseClause("x=1") {
            //             callSync("alice", "bob", "hihi")
            //             replySync("bob", "alice", "hihi")
            //         }
            //         elseClause("x=2") {
                //             callSync("alice", "bob", "hihi")
                //             replySync("bob", "alice", "hihi")
                //         }
                //         elseClause("x=3") {
                    //             callSync("alice", "bob", "hihi")
                    //             replySync("bob", "alice", "hihi")
                    //         }
                    //     }
                    //
                    //     callSync("alice", "bob", "hihi")
                    //     noteOver("alice", "alice says hihi")
                    //     callSync("bob", "john", "hoho")
                    //     noteOver("bob", "john", "bob says hoho")
                    //
                    //     parallel("Alice to Bob") {
                        //         callSync("alice", "bob", "hihi")
                        //         andClause("Bob to John") {
                            //             callSync("bob", "john", "hihi")
                            //         }
                            //         andClause("Alice to John") {
                                //             callSync("alice", "john", "hihi")
                                //         }
                                //     }
                                //
                                //     highlight("red") {
                                    //         optional("this is optional") {
                                        //             callSync("alice", "bob", "hihi")
                                        //             replySync("bob", "alice", "hihi")
                                        //         }
                                        //     }
                                        // }
