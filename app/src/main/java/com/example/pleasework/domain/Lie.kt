package com.example.pleasework.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity
data class Lie(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var title: String?,
    var text: String?,
    var severity: LieSeverity?,
    var truth: String?
) : Serializable {
    override fun toString(): String {
        return "Lie(id=$id, title=$title)"
    }
}

@Entity
data class LieId(
    @PrimaryKey var lieId: Int,
    var parentLie: Int
)

data class LieWithLies(
    @Embedded val lie: Lie,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentLie",
    )
    val relatedTo: MutableList<LieId>
)