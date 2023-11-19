package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.FirestoreRepository
import com.swmpire.delifyit.domain.uitl.DateFilter
import com.swmpire.delifyit.domain.uitl.TimeIntervals
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalOrdersUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(interval: TimeIntervals) : Flow<Long> {
        return when (interval) {
            TimeIntervals.DAY -> {
                val startOfDay = DateFilter.getStartOfDay()
                val endOfDay = DateFilter.getStartOfNextDay()
                firestoreRepository.getTotalOrdersInRange(startOfDay, endOfDay)
            }
            TimeIntervals.MONTH -> {
                val startOfMonth = DateFilter.getStartOfMonth()
                val endOfMonth = DateFilter.getEndOfMonth()
                firestoreRepository.getTotalOrdersInRange(startOfMonth, endOfMonth)
            }
            TimeIntervals.YEAR -> {
                val startOfYear = DateFilter.getStartOfYear()
                val endOfYear = DateFilter.getEndOfYear()
                firestoreRepository.getTotalOrdersInRange(startOfYear, endOfYear)
            }
        }
    }
}