package core.utility

import crafting.Recipe
import crafting.RecipeManager

class NameSearchableList<N : Named>() : ArrayList<N>() {
    private val proxies = HashMap<String, N>()

    constructor(item: N) : this() {
        add(item)
    }

    constructor(items: List<N>) : this() {
        addAll(items)
    }

    override fun clear() {
        proxies.clear()
        super.clear()
    }

    fun addProxy(item: N, names: List<String>){
        names.forEach { addProxy(item, it) }
    }

    private fun addProxy(item: N, name: String){
        if (!contains(item)){
            add(item)
        }
        proxies[name] = item
    }

    fun exists(target: N): Boolean {
        return contains(target)
    }

    fun exists(name: String): Boolean {
        if (name.isBlank()) {
            return false
        }
        return getOrNull(name) != null
    }

    fun exists(name: List<String>): Boolean {
        if (name.isEmpty()) return false
        val fullName = name.joinToString(" ").toLowerCase()
        return exists(fullName)
    }

    fun get(name: String): N {
        return getOrNull(name)!!
    }

    fun getAll(name: String): List<N> {
        return filter { it.name.toLowerCase().contains(name.toLowerCase()) }
    }

    fun getAll(names: List<String>): List<N> {
        return names.asSequence().map { getOrNull(it) }.filterNotNull().toList()
    }

    fun get(name: List<String>): N {
        val fullName = name.joinToString(" ").toLowerCase()
        return get(fullName)
    }

    fun getOrNull(name: String): N? {
        if (proxies.containsKey(name)){
            return proxies[name]
        }
        val matches = getAll(name)
        return when {
            matches.isEmpty() -> null
            matches.size == 1 -> matches[0]
            else -> bestMatch(matches, name)
        }
    }

    private fun bestMatch(matches: List<N>, name: String): N {
        return firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
                ?: matches[0]
    }

}