package com.example.backend.service

fun String?.normalize(): String? {
    return this?.replace("\\s+".toRegex(), " ")
}

fun String?.normalizeNotNull(): String {
    return this?.replace("\\s+".toRegex(), " ") ?: return ""
}

fun String.wrapPaging(pageNum: Int, fetch: Int): String {
    return """
    SELECT * FROM (
        SELECT a.*, rownum r__
            FROM (
                    $this
            ) a
            WHERE rownum < (($pageNum * $fetch) + 1 )
         )
    WHERE r__ >= ((($pageNum-1) * $fetch) + 1)
""".trimIndent()
}

