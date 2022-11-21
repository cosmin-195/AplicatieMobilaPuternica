package com.example.pleasework

import java.io.Serializable

data class Lie(
    var id: String?,
    var title: String?,
    var text: String?,
    var severity: LieSeverity?,
    var liesRelatedTo: MutableList<Lie>,
    var peopleTold: MutableList<String>,
    var truth: String?
) : Serializable

enum class LieSeverity {
    MILD,
    MEDIUM,
    SEVERE
}
