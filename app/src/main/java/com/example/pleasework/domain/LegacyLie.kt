package com.example.pleasework.domain
import java.io.Serializable

data class LegacyLie(
    var id: String?,
    var title: String?,
    var text: String?,
    var severity: LieSeverity?,
    var liesRelatedTo: MutableList<LegacyLie>,
    var peopleTold: MutableList<String>,
    var truth: String?
) : Serializable{
    override fun toString(): String {
        return "Lie(id=$id, title=$title)"
    }
}

enum class LieSeverity {
    MILD,
    MEDIUM,
    SEVERE
}
