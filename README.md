# Mermaid.kt

## Usage

As described in the tests, a possible usage would be:

```kotlin
val alice = Actor("alice")
val bob = Actor("bob")
val john = Actor("john")

val example1 = sequenceDiagram {
    loop("until dead") {
	solidArrow(alice, bob, "hihi")
	solidArrow(bob, alice, "hoho")
	optional("hoho") {
	    solidArrow(alice, bob, "hihi")
	    alternative("x = 1") {
		solidArrow(alice, bob, "hihi")
		elseClause("x = 2") {
		    solidArrow(bob, john, "hihi")
		}
		elseClause("x = 3") {
		    solidArrow(john, alice, "hihi")
		}
	    }
	}
    }
    parallel("alice to bob") {
	solidArrow(alice, bob, "hihi")
	andClause("bob to alice") {
	    solidArrow(bob, alice, "hihi")
	}
    }
}
```

The above translates to:

```mermaid
sequenceDiagram
    loop until dead
	alice->>bob: hihi
	bob->>alice: hoho
	opt hoho
	    alice->>bob: hihi
	    alt x = 1
		alice->>bob: hihi
	    else x = 2
		bob->>john: hihi
	    else x = 3
		john->>alice: hihi
	    end
	end
    end
    par alice to bob
	alice->>bob: hihi
    and bob to alice
	bob->>alice: hihi
    end
```