package com.example.mytaskmanager.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.mytaskmanager.database.TaskManagerDao
import com.example.mytaskmanager.database.TaskManagerDatabase
import com.example.mytaskmanager.home.TasksLocalDataSource
import com.example.mytaskmanager.home.TasksLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "POCH_FILENAME")

@Module
@InstallIn(SingletonComponent::class)
class TaskManagerAppModule {
    @Singleton
    @Provides
    fun provideTaskManagerDao(taskManagerDatabase: TaskManagerDatabase): TaskManagerDao
    = taskManagerDatabase.taskManagerDao()

    @Singleton
    @Provides
    fun provideTaskManagerDatabase(
        @ApplicationContext context: Context
    ): TaskManagerDatabase = Room.databaseBuilder(
        context,
        TaskManagerDatabase::class.java,
        "task_manager_db"
    )
        .fallbackToDestructiveMigration()
        .build()
}

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {
    @Singleton
    @Provides
    fun bindDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class ApplicationSettingModule {
//    @Binds
//    abstract fun bindApplicationSetting(applicationSettingImpl: ApplicationSettingImpl): ApplicationSetting
//}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class CreateTaskModule {
//    @Binds
//    abstract fun bindLoginNetworkLocalDataSource(loginNetworkLocalResponseImpl: LoginNetworkLocalDataSourceImpl): LoginNetworkLocalDataSource
//
// }

@Module
@InstallIn(SingletonComponent::class)
abstract class TasksModule {
    @Binds
    abstract fun bindTaskLocalDataSource(tasksLocalDataSourceImpl: TasksLocalDataSourceImpl): TasksLocalDataSource
 }

@Module
@InstallIn(SingletonComponent::class) // Assuming you're using Hilt
object AppModule {
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
