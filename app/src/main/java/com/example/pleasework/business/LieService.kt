package com.example.pleasework.business

import androidx.lifecycle.LiveData
import com.example.pleasework.domain.Lie
import com.example.pleasework.domain.LieWithLies
import com.example.pleasework.persistence.LieRepository
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class LieService @Inject constructor(
    val repository: LieRepository,
    private val executorService: ExecutorService
) {

    fun getAll(): LiveData<List<Lie>> {
        return this.repository.getAll();
    }

    fun remove(id: Int) {
        executorService.submit {
            this.repository.delete(Lie(id, null, null, null, null))
        }
    }

    fun add(lie: Lie) {
        executorService.submit {
            repository.insert(lie)
        }
    }

    fun getLieWithRelationsById(id: Int): LiveData<LieWithLies> {
        return repository.getLieWithRelationsById(id)
    }

    fun insertLieWithRelations(lie: LieWithLies) {
        executorService.submit {
            repository.insertLieWithRelations(lie)
        }
    }
}