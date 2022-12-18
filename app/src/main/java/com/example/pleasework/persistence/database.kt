package com.example.pleasework.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pleasework.domain.Lie
import com.example.pleasework.domain.LieId

@Database(entities = [Lie::class, LieId::class], version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repository(): LieRepository
}
