package com.example.pleasework

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.pleasework.business.LieService
import com.example.pleasework.domain.Lie
import com.example.pleasework.domain.LieSeverity
import com.example.pleasework.persistence.AppDatabase
import com.example.pleasework.persistence.LieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@HiltAndroidApp
class App : Application() {

}

@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext appContext: Context):
            AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "liedb"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(db: AppDatabase): LieRepository {
        val executorService = Executors.newSingleThreadExecutor()
        executorService.submit {
            db.repository().insert(Lie(null, "a lie", "hjere", LieSeverity.SEVERE, "heeh"))
        }
//        db.repository().insert(Lie("1", "a lie", "hjere", LieSeverity.SEVERE, "heeh"))
        return db.repository()
    }

    @Provides
    @Singleton
    fun provideService(repo: LieRepository, executorService: ExecutorService): LieService {
        return LieService(repo, executorService)
    }

    @Provides
    @Singleton
    fun provideExecutorService(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }


}