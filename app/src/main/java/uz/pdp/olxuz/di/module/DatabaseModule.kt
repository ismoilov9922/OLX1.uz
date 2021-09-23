package uz.pdp.olxuz.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import uz.pdp.olxuz.database.dao.ProductDao
import uz.pdp.olxuz.database.database.AppDatabase
import javax.inject.Singleton

class DatabaseModule(private val context: Context) {


//    @Singleton
//    @Provides
//    fun provideContext(): Context = context
//
//    @Singleton
//    @Provides
//    fun provideDatabase(context: Context): AppDatabase {
//        return Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
//            .fallbackToDestructiveMigration()
//            .allowMainThreadQueries()
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideGithubDao(appDatabase: AppDatabase): ProductDao = appDatabase.productDao()


}