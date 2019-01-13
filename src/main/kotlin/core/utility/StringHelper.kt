package core.utility

fun String.wrapNonEmpty(prefix: String, suffix: String): String {
    return if (isBlank()) {
        this
    } else {
        prefix + this + suffix
    }
}

fun String.apply(params: Map<String, String>): String {
    var modified = this
    params.keys.sortedBy { -it.length }
            .forEach {
                modified = modified.replace("$$it", params[it]!!, true)
            }
    return modified
}

fun String.repeat(times: Int) : String {
    var result = ""
    for (i in 0 until times){
        result += this
    }
    return result
}