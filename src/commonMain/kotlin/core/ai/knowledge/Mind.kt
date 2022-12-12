package core.ai.knowledge

import core.ai.AI
import core.ai.DumbAI
import core.thing.Thing
import crafting.Recipe
import traveling.location.network.LocationNode

data class Mind(
    val ai: AI = DumbAI(),
    val memory: CreatureMemory = CreatureMemory()
){
    lateinit var creature: Thing; private set

    fun updateCreature(creature: Thing){
        this.creature = creature
        ai.creature = creature
    }

    fun knows(kind: String): ListFact {
        return memory.getListFact(kind) ?: UNKNOWN_LIST_FACT //TODO - ok to return this?
    }

    fun knows(source: Subject, kind: String): Fact {
        return memory.getFact(source, kind) ?: UNKNOWN_FACT //TODO
    }

    fun learn(fact: Fact) = learn(fact.source, fact.kind, fact.confidence, fact.amount)
    fun learn(source: SimpleSubject, kind: String, confidenceIncrease: Int, amountIncrease: Int) {
        val existing = memory.getFact(source, kind)
        val confidence = (existing?.confidence ?: 0) + confidenceIncrease
        val amount = (existing?.confidence ?: 0) + amountIncrease
        val fact = Fact(source, kind, confidence, amount)
        memory.remember(fact)
        memory.forget(fact)
    }

    fun learn(kind: String, addition: SimpleSubject) = learn(kind, listOf(addition))
    fun learn(kind: String, additions: List<SimpleSubject>) {
        val existing = memory.getListFact(kind)
        val fact = ListFact(kind, additions.toList() + (existing?.sources ?: listOf()))
        memory.remember(fact)
    }

    fun forgetLongTermMemory() {
        memory.forget()
    }

    fun knows(location: LocationNode): Boolean {
        val fact = knows("Location")
        return fact.sources.contains(SimpleSubject(location))
    }

    fun knows(recipe: Recipe): Boolean {
        val fact = knows("Recipe")
        return fact.sources.contains(SimpleSubject(recipe.name))
    }

    fun discover(location: LocationNode){
        learn("Location", SimpleSubject(location))
    }

    fun discover(recipe: Recipe){
        learn("Recipe", SimpleSubject(recipe.name))
    }

}