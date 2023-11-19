package com.swmpire.delifyit.domain.uitl

import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

object DateFilter {
    fun getStartOfDay(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return Timestamp(Date(calendar.timeInMillis))
    }

    fun getStartOfNextDay(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return Timestamp(Date(calendar.timeInMillis))
    }

    fun getStartOfMonth(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return Timestamp(Date(calendar.timeInMillis))
    }

    fun getEndOfMonth(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, 1)
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.MILLISECOND, -1)
        }
        return Timestamp(Date(calendar.timeInMillis))
    }

    fun getStartOfYear(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return Timestamp(Date(calendar.timeInMillis))
    }

    fun getEndOfYear(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, get(Calendar.YEAR) + 1)
            set(Calendar.DAY_OF_YEAR, 1)
            add(Calendar.MILLISECOND, -1)
        }
        return Timestamp(Date(calendar.timeInMillis))
    }
}