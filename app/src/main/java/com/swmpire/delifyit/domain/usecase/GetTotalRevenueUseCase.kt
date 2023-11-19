package com.swmpire.delifyit.domain.usecase

import com.swmpire.delifyit.domain.repository.FirestoreRepository
import com.swmpire.delifyit.domain.uitl.DateFilter
import com.swmpire.delifyit.domain.uitl.TimeIntervals
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalRevenueUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(interval: TimeIntervals) : Flow<Int> {
        return when (interval) {
            TimeIntervals.DAY -> {
                val startOfDay = DateFilter.getStartOfDay()
                val endOfDay = DateFilter.getStartOfNextDay()
                firestoreRepository.getTotalRevenueInRange(startOfDay, endOfDay)
            }
            TimeIntervals.MONTH -> {
                val startOfMonth = DateFilter.getStartOfMonth()
                val endOfMonth = DateFilter.getEndOfMonth()
                firestoreRepository.getTotalRevenueInRange(startOfMonth, endOfMonth)
            }
            TimeIntervals.YEAR -> {
                val startOfYear = DateFilter.getStartOfYear()
                val endOfYear = DateFilter.getEndOfYear()
                firestoreRepository.getTotalRevenueInRange(startOfYear, endOfYear)
            }
        }
    }
}