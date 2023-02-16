package com.mundcode.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mundcode.data.local.database.MuntamDatabase
import com.mundcode.data.local.database.dao.QuestionDao
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class QuestionDaoTest {
    private lateinit var questionDao: QuestionDao
    private lateinit var db: MuntamDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            MuntamDatabase::class.java
        ).build()
        questionDao = db.questionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}