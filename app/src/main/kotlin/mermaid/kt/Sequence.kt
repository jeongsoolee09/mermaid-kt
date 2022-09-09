package mermaid.kt.Sequence

/* ============ Components ============ */

abstract class Component {
    val children = arrayListOf<Component>()
    protected fun <T : Component> initComponent(component: T, trailingContent: T.() -> Unit): T {
        component.trailingContent()
        children.add(component)
        return component
    }

    // abstract fun render(acc: StringBuilder)

    // abstract fun toString(acc: StringBuilder)
}

/* ============ Labels ============ */

abstract class Label() : Component() {
    fun autonumber(trailingContent: Autonumber.() -> Unit) =
            initComponent(Autonumber(), trailingContent)

    fun participants(participants: List<Actor>, trailingContent: Participants.() -> Unit) =
            initComponent(Participants(participants), trailingContent)
}

class Autonumber() : Label()

data class Participants(var participants: List<Actor>?) : Label()

/* ============ Blocks ============ */
abstract class Block() : Component() {
    fun loop(label: String, trailingContent: Loop.() -> Unit) =
            initComponent(Loop(label), trailingContent)

    fun noteLeft(actor: Actor, note: String, trailingContent: NoteLeft.() -> Unit) =
            initComponent(NoteLeft(actor, note), trailingContent)

    fun noteRight(actor: Actor, note: String, trailingContent: NoteRight.() -> Unit) =
            initComponent(NoteRight(actor, note), trailingContent)

    fun noteOver(actor1: Actor, actor2: Actor, note: String, trailingContent: NoteOver.() -> Unit) =
            initComponent(NoteOver(actor1, actor2, note), trailingContent)

    fun noteOver(actor: Actor, note: String, trailingContent: NoteOver.() -> Unit) =
            initComponent(NoteOver(actor, null, note), trailingContent)

    fun highlight(colorString: String, trailingContent: Highlight.() -> Unit) =
            initComponent(Highlight(Color.fromString(colorString)), trailingContent)

    fun alternative(clauses: List<Clause>, trailingContent: Alternative.() -> Unit) =
            initComponent(Alternative(clauses), trailingContent)

    fun parallel(description: String, trailingContent: Parallel.() -> Unit) =
            initComponent(Parallel(description), trailingContent)

    fun optional(description: String, trailingContent: Optional.() -> Unit) =
            initComponent(Optional(description), trailingContent)
}

data class Loop(var label: String?) : Block()

data class NoteLeft(var actor: Actor?, var note: String?) : Block()

data class NoteRight(var actor: Actor?, var not: String?) : Block()

data class NoteOver(var actor1: Actor?, var actor2: Actor?, var note: String?) : Block()

data class Highlight(var color: Color?) : Block()

data class Alternative(var clauses: List<Clause>?) : Block()

data class Parallel(var description: String?) : Block()

data class Optional(var description: String?) : Block()

/* ============ Calls ============ */

abstract class Call() : Component() {
    fun callSync(from: Actor, to: Actor, message: String, trailingContent: CallSync.() -> Unit) =
            initComponent(CallSync(from, to, message), trailingContent)

    fun callActivate(
            from: Actor,
            to: Actor,
            message: String,
            trailingContent: CallActivate.() -> Unit
    ) = initComponent(CallActivate(from, to, message), trailingContent)
    fun callCross(from: Actor, to: Actor, message: String, trailingContent: CallCross.() -> Unit) =
            initComponent(CallCross(from, to, message), trailingContent)

    fun callAsync(from: Actor, to: Actor, message: String, trailingContent: CallAsync.() -> Unit) =
            initComponent(CallAsync(from, to, message), trailingContent)
}

class CallSync(var from: Actor?, var to: Actor?, var message: String?) : Call()

class CallActivate(var from: Actor?, var to: Actor?, var message: String?) : Call()

class CallCross(var from: Actor?, var to: Actor?, var message: String?) : Call()

class CallAsync(var from: Actor?, var to: Actor?, var message: String?) : Call()

/* ============ Replys ============ */

abstract class Reply() : Component() {
    fun replySync(from: Actor, to: Actor, message: String, trailingContent: ReplySync.() -> Unit) =
            initComponent(ReplySync(from, to, message), trailingContent)

    fun replyActivate(
            from: Actor,
            to: Actor,
            message: String,
            trailingContent: ReplyActivate.() -> Unit
    ) = initComponent(ReplyActivate(from, to, message), trailingContent)

    fun replyCross(
            from: Actor,
            to: Actor,
            message: String,
            trailingContent: ReplyCross.() -> Unit
    ) = initComponent(ReplyCross(from, to, message), trailingContent)

    fun replyAsync(
            from: Actor,
            to: Actor,
            message: String,
            trailingContent: ReplyAsync.() -> Unit
    ) = initComponent(ReplyAsync(from, to, message), trailingContent)
}

class ReplySync(var from: Actor?, var to: Actor?, var message: String?) : Reply()

class ReplyActivate(var from: Actor?, var to: Actor?, var message: String?) : Reply()

class ReplyCross(var from: Actor?, var to: Actor?, var message: String?) : Reply()

class ReplyAsync(var from: Actor?, var to: Actor?, var message: String?) : Reply()

/* ============ Back and Forths ============ */

abstract class BackAndForth() : Component() {
    fun withSync(
            from: Actor,
            to: Actor,
            messageFrom: String,
            messageTo: String,
            trailingContent: WithSync.() -> Unit
    ) = initComponent(WithSync(from, to, messageFrom, messageTo), trailingContent)

    fun withSync(from: Actor, to: Actor, trailingContent: WithSync.() -> Unit) =
            initComponent(WithSync(from, to, "calls", "replies"), trailingContent)

    fun withAsync(
            from: Actor,
            to: Actor,
            messageFrom: String,
            messageTo: String,
            trailingContent: WithAsync.() -> Unit
    ) = initComponent(WithAsync(from, to, messageFrom, messageTo), trailingContent)

    fun withAsync(from: Actor, to: Actor, trailingContent: WithAsync.() -> Unit) =
            initComponent(WithAsync(from, to, "calls", "replies"), trailingContent)
}

class WithSync(var from: Actor?, var to: Actor?, var messageFrom: String?, var messageTo: String?) :
        BackAndForth()

class WithAsync(
        var from: Actor?,
        var to: Actor?,
        var messageFrom: String?,
        var messageTo: String?
) : BackAndForth()

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

// val sample = sequenceDiagram {
//     autonumber()
//     participants("alice", "bob")
//     alternative {
//         ifClause("x=1") {
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
//     highlight("red") {
//         optional("this is optional") {
//             callSync("alice", "bob", "hihi")
//             replySync("bob", "alice", "hihi")
//         }
//     }
// }
