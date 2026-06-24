package com.example.nepsis.core.di

import android.content.Context
import androidx.room.Room
import com.example.nepsis.core.network.RetrofitClient
import com.example.nepsis.core.utils.SessionManager
import com.example.nepsis.data.local.NepsisDatabase
import com.example.nepsis.data.repository.AuthRepositoryImpl
import com.example.nepsis.data.repository.NepsisRepositoryImpl
import com.example.nepsis.data.repository.ProfileRepositoryImpl
import com.example.nepsis.domain.repository.AuthRepository
import com.example.nepsis.domain.repository.NepsisRepository
import com.example.nepsis.domain.repository.ProfileRepository

object ServiceLocator {
    private var database: NepsisDatabase? = null
    private var sessionManager: SessionManager? = null

    fun provideSessionManager(context: Context): SessionManager {
        return sessionManager ?: synchronized(this) {
            val instance = SessionManager(context.applicationContext)
            sessionManager = instance
            instance
        }
    }

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

    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl(api = RetrofitClient.supabaseService)
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        return ProfileRepositoryImpl(
            api = RetrofitClient.supabaseService,
            dao = provideDatabase(context).nepsisDao()
        )
    }
}