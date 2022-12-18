package com.example.pleasework.domain

object LiesContext {
    val legacyLies = mutableListOf(
        LegacyLie(
            "1",
            "lie1",
            "the lie 1 fsav",
            LieSeverity.MILD,
            ArrayList(),
            ArrayList(),
            "the real thing"
        ),
        LegacyLie(
            "2",
            "lie2",
            "the second lie",
            LieSeverity.SEVERE,
            ArrayList(),
            ArrayList(),
            "the real thing"
        )
    )
}