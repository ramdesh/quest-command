package core.ai.knowledge.dsl

import core.ai.knowledge.Fact
import core.ai.knowledge.FactFinder
import core.ai.knowledge.Mind
import core.ai.knowledge.Subject

class RelationshipFinderBuilder(private val kind: String) {
    private var relevantSource: (Subject) -> Boolean = { true }
    private var findFact: ((mind: Mind, source: Subject, kind: String) -> Fact) = {_, source, kind -> Fact(source, kind, 0, 0) }

//    fun build(): Relat {
//        return FactFinder(kind, relevantSource, findFact)
//    }

    fun relevantSource(){

    }

    fun find(){

    }

}

//fun relationship(kind: String, initializer: RelationshipFinderBuilder.() -> Unit): FactFinder {
//    return RelationshipFinderBuilder(kind).apply(initializer).build()
//}