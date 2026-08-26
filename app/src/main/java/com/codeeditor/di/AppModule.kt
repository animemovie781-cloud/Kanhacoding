package com.codeeditor.di

import android.content.Context
import androidx.room.Room
import com.codeeditor.data.local.AppDatabase
import com.codeeditor.data.local.ChatDao
import com.codeeditor.data.local.FileDao
import com.codeeditor.data.local.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "code_editor_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideFileDao(db: AppDatabase): FileDao = db.fileDao()

    @Provides
    fun provideChatDao(db: AppDatabase): ChatDao = db.chatDao()

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context)
    }
}
