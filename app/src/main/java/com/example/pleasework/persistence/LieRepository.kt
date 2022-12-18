package com.example.pleasework.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pleasework.domain.Lie
import com.example.pleasework.domain.LieId
import com.example.pleasework.domain.LieWithLies

@Dao
interface LieRepository {
    @Query("SELECT * FROM lie")
    fun getAll(): LiveData<List<Lie>>

    @Insert
    fun insert(lie: Lie): Long

    @Insert
    fun insertRel(lid: LieId)

    @Transaction
    fun insertLieWithRelations(lie: LieWithLies) {
        val insertedId: Int = insert(lie.lie).toInt()
        lie.relatedTo.stream()
            .peek { it.parentLie = insertedId }
            .forEach {
                insertRel(it)
            }
    }

    @Delete()
    fun delete(lie: Lie)

    @Query("SELECT * FROM Lie as l left join LieId as lid on l.id=:id and lid.lieId=l.id")
    fun getLieWithRelationsById(id: Int): LiveData<LieWithLies>

    @Query("SELECT * FROM Lie as l where l.id=:id")
    fun getLieById(id: Int): LiveData<Lie>

    //    @Transaction
    @Query("SELECT * FROM Lie as l left join LieId as lid on lid.lieId=l.id")
    fun getAllLiesWithRelations(): LiveData<List<LieWithLies>>
}