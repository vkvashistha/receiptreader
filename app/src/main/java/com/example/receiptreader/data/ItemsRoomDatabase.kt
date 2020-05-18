package com.example.receiptreader.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)
abstract class ItemsRoomDatabase : RoomDatabase() {
    abstract fun itemDao() : ItemDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ItemsRoomDatabase? = null

        fun getDatabase(context: Context): ItemsRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemsRoomDatabase::class.java,
                    "items_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}