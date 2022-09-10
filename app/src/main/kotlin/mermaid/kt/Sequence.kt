package mermaid.kt.Sequence

/* ============ Components ============ */

interface Component

/* =================================== */
/* ============ Base Case ============ */
/* =================================== */

abstract class Base : Component

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

abstract class Call() : Arrow() {
    fun callSync(from: Actor, to: Actor, message: String) = CallSync(from, to, message)

    fun callActivate(
            from: Actor,
            to: Actor,
            message: String,
    ) = CallActivate(from, to, message)

    fun callCross(from: Actor, to: Actor, message: String) = CallCross(from, to, message)

    fun callAsync(from: Actor, to: Actor, message: String) = CallAsync(from, to, message)
}

class CallSync(var from: Actor?, var to: Actor?, var message: String?) : Call()

class CallActivate(var from: Actor?, var to: Actor?, var message: String?) : Call()

class CallCross(var from: Actor?, var to: Actor?, var message: String?) : Call()

class CallAsync(var from: Actor?, var to: Actor?, var message: String?) : Call()

// ============ Replys ============

abstract class Reply() : Base() {
    fun replySync(from: Actor, to: Actor, message: String) = ReplySync(from, to, message)

    fun replyActivate(from: Actor, to: Actor, message: String) = ReplyActivate(from, to, message)

    fun replyCross(from: Actor, to: Actor, message: String) = ReplyCross(from, to, message)

    fun replyAsync(from: Actor, to: Actor, message: String) = ReplyAsync(from, to, message)
}

class ReplySync(var from: Actor?, var to: Actor?, var message: String?) : Reply()

class ReplyActivate(var from: Actor?, var to: Actor?, var message: String?) : Reply()

class ReplyCross(var from: Actor?, var to: Actor?, var message: String?) : Reply()

class ReplyAsync(var from: Actor?, var to: Actor?, var message: String?) : Reply()

/* ======================================== */
/* ============ Inductive Case ============ */
/* ======================================== */

abstract class Inductive : Component {
    val children = arrayListOf<Inductive>()
    protected fun <T : Inductive> initComponent(component: T, thenWhat: T.() -> Unit): T {
        component.thenWhat()
        children.add(component)
        return component
    }

    // abstract fun render(acc: StringBuilder)

    // abstract fun toString(acc: StringBuilder)
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

data class NoteRight(var actor: Actor?, var not: String?) : Block()

data class NoteOver(var actor1: Actor?, var actor2: Actor?, var note: String?) : Block()

data class Highlight(var color: Color?) : Block()

class Alternative(var condition: String?) : Block() {
    fun andClause(condition: String?, thenWhat: AndClause.() -> Unit) =
            initComponent(AndClause(condition), thenWhat)
}

data class Parallel(var description: String?) : Block()

data class Optional(var description: String?) : Block()

// ============ Back and Forths ============

abstract class BackAndForth() : Inductive() {
    fun withSync(
            from: Actor,
            to: Actor,
            messageFrom: String,
            messageTo: String,
            thenWhat: WithSync.() -> Unit
    ) = initComponent(WithSync(from, to, messageFrom, messageTo), thenWhat)

    fun withSync(from: Actor, to: Actor, thenWhat: WithSync.() -> Unit) =
            initComponent(WithSync(from, to, "calls", "replies"), thenWhat)

    fun withAsync(
            from: Actor,
            to: Actor,
            messageFrom: String,
            messageTo: String,
            thenWhat: WithAsync.() -> Unit
    ) = initComponent(WithAsync(from, to, messageFrom, messageTo), thenWhat)

    fun withAsync(from: Actor, to: Actor, thenWhat: WithAsync.() -> Unit) =
            initComponent(WithAsync(from, to, "calls", "replies"), thenWhat)
}

class WithSync(var from: Actor?, var to: Actor?, var messageFrom: String?, var messageTo: String?) :
        BackAndForth()

class WithAsync(
        var from: Actor?,
        var to: Actor?,
        var messageFrom: String?,
        var messageTo: String?
) : BackAndForth()

// ============ Clauses ============

abstract class Clause() : Inductive()

data class ElseClause(var condition: String?) : Clause()

data class AndClause(var condition: String?) : Clause()

// val sample = sequenceDiagram {
//     autonumber()
//     participants("alice", "bob", "john")
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

//     callSync("alice", "bob", "hihi")
//     noteOver("alice", "alice says hihi")
//     callSync("bob", "john", "hoho")
//     noteOver("bob", "john", "bob says hoho")

//     parallel("Alice to Bob") {
//         andClause("Alice to Bob") {
//             callSync("alice", "bob", "hihi")
//         }
//         andClause("Alice to John") {
//             callSync("alice", "john", "hihi")
//         }
//     }

//     highlight("red") {
//         optional("this is optional") {
//             callSync("alice", "bob", "hihi")
//             replySync("bob", "alice", "hihi")
//         }
//     }
// }
