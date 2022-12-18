package com.example.pleasework.domain

import androidx.room.*
import java.io.Serializable

@Entity
data class Lie(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var title: String?,
    var text: String?,
    var severity: LieSeverity?,
//    var liesRelatedTo: MutableList<Lie>,
//    var peopleTold: MutableList<String>,
    var truth: String?
) : Serializable {
    override fun toString(): String {
        return "Lie(id=$id, title=$title)"
    }
}

@Entity
data class LieId(
    @PrimaryKey var lieId: Int,
    var parentLie:Int
)


//@Entity(primaryKeys = ["id", "lieId"])
//data class LieCrossRef(
//    val lieId: String,
//    val id: String
//)

data class LieWithLies(
    @Embedded val lie: Lie,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentLie",
//        associateBy = Junction(LieCrossRef::class)
    )
    val relatedTo: MutableList<LieId>
)