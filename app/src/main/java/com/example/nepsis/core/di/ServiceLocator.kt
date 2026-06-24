package com.example.nepsis.core.di

import android.content.Context
import androidx.room.Room
import com.example.nepsis.core.network.RetrofitClient
import com.example.nepsis.data.local.NepsisDatabase
import com.example.nepsis.data.repository.NepsisRepositoryImpl
import com.example.nepsis.domain.repository.NepsisRepository

object ServiceLocator {
    private var database: NepsisDatabase? = null

    fun provideDatabase(context: Context): NepsisDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NepsisDatabase::class.java,
                "nepsis_db"
            ).build()
            database = instance
            instance
        }
    }

    fun provideNepsisRepository(context: Context): NepsisRepository {
        return NepsisRepositoryImpl(
            api = RetrofitClient.supabaseService,
            dao = provideDatabase(context).nepsisDao()
        )
    }
}