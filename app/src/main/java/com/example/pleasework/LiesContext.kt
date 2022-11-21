package com.example.pleasework

object LiesContext {
    val lies = mutableListOf(
        Lie(
            "1",
            "lie1",
            "the lie 1 fsav",
            LieSeverity.MILD,
            ArrayList(),
            ArrayList(),
            "the real thing"
        ),
        Lie(
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