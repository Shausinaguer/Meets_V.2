package com.example.meetsv2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class, Post::class, ArchivedPost::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                 .addCallback(object : RoomDatabase.Callback() {
                     override fun onCreate(db: SupportSQLiteDatabase) {
                         super.onCreate(db)
                         db.execSQL(
                             """
                             CREATE TRIGGER archive_post_trigger
                             AFTER DELETE ON posts
                             FOR EACH ROW
                             BEGIN
                                 INSERT INTO archived_posts (id, content, timestamp, userId)
                                 VALUES (OLD.id, OLD.content, OLD.timestamp, OLD.userId);
                             END;
                             """
                         )
                     }
                 })
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
