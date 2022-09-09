package mermaid.kt.Sequence

/* ============ Components ============ */

abstract class Component {
    val children = arrayListOf<Component>()
    protected fun <T : Component> initComponent(component: T, init: T.() -> Unit): T {
        component.init()
        children.add(component)
        return component
    }
    protected fun toString(builder: StringBuilder) {}
}

/* ============ Labels ============ */

abstract class Label() : Component() {
    fun autonumber(init: Autonumber.() -> Unit) {}
    fun participants(participants: List<Actor>, init: Participants.() -> Unit) {}
}

class Autonumber() : Label()

data class Participants(var participants: List<Actor>?) : Label()

/* ============ Blocks ============ */
abstract class Block() : Component() {
    fun loop(label: String, followingContent: Loop.() -> Unit) =
            initComponent(Loop(label), followingContent)
    fun noteLeft(actor: Actor, note: String, followingContent: NoteLeft.() -> Unit) =
            initComponent(NoteLeft(actor, note), followingContent)
    fun noteRight(actor: Actor, note: String, followingContent: NoteRight.() -> Unit) =
            initComponent(NoteRight(actor, note), followingContent)
    fun noteOver(
            actor1: Actor,
            actor2: Actor,
            note: String,
            followingContent: NoteOver.() -> Unit
    ) = initComponent(NoteOver(actor1, actor2, note), followingContent)
    fun noteOver(actor: Actor, note: String, followingContent: NoteOver.() -> Unit) =
            initComponent(NoteOver(actor, null, note), followingContent)
    fun highlight(colorString: String, followingContent: Highlight.() -> Unit) =
            initComponent(Highlight(Color.fromString(colorString)), followingContent)
    fun alternative(clauses: List<Clause>, followingContent: Alternative.() -> Unit) =
            initComponent(Alternative(clauses), followingContent)
    fun parallel(description: String, followingContent: Parallel.() -> Unit) =
            initComponent(Parallel(description), followingContent)
    fun optional(description: String, followingContent: Optional.() -> Unit) =
            initComponent(Optional(description), followingContent)
}

data class Loop(var label: String?) : Block()

data class NoteLeft(var actor: Actor?, var note: String? = null) : Block()

data class NoteRight(var actor: Actor?, var not: String?) : Block()

data class NoteOver(var actor1: Actor?, var actor2: Actor?, var note: String?) : Block()

data class Highlight(var color: Color?) : Block()

data class Alternative(var clauses: List<Clause>?) : Block()

data class Parallel(var description: String?) : Block()

data class Optional(var description: String?) : Block()

/* ============ Calls ============ */

abstract class Call() : Component() {
    fun sync(init: CallSync.() -> Unit) {}
    fun activate(init: CallActivate.() -> Unit) {}
    fun cross(init: CallCross.() -> Unit) {}
    fun async(init: CallAsync.() -> Unit) {}
}

class CallSync() : Call()

class CallActivate() : Call()

class CallCross() : Call()

class CallAsync() : Call()

/* ============ Replys ============ */

abstract class Reply() : Component() {
    fun sync(init: ReplySync.() -> Unit) {}
    fun activate(init: ReplyActivate.() -> Unit) {}
    fun cross(init: ReplyCross.() -> Unit) {}
    fun async(init: ReplyAsync.() -> Unit) {}
}

class ReplySync() : Reply()

class ReplyActivate() : Reply()

class ReplyCross() : Reply()

class ReplyAsync() : Reply()

/* ============ Back and Forths ============ */

abstract class BackAndForth() : Component() {
    fun sync(init: WithSync.() -> Unit) {}
    fun async(init: WithAsync.() -> Unit) {}
}

class WithSync() : BackAndForth()

class WithAsync() : BackAndForth()

/* ============ Sub-Component Constructs ============ */

abstract class SubComponent

data class Clause(val condition: String, val thenWhat: List<Component>) : SubComponent()

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
