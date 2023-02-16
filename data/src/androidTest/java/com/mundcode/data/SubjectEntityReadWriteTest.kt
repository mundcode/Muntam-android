package com.mundcode.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mundcode.data.local.database.MuntamDatabase
import com.mundcode.data.local.database.dao.SubjectDao
import com.mundcode.data.local.database.model.createSubjectEntity
import java.io.IOException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubjectEntityReadWriteTest {
    private lateinit var subjectDao: SubjectDao
    private lateinit var db: MuntamDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            MuntamDatabase::class.java
        ).build()
        subjectDao = db.subjectDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeSubjectAndReadInList() = runBlocking {
        val subjectEntities = (1..10).map {
            createSubjectEntity(
                id = it,
                name = "과목 이름 : $it",
                totalQuestionNumber = it
            )
        }
        val answer = subjectEntities[1]

        subjectDao.insertAll(subjectEntities)

        val result = subjectDao.getSubjectById(answer.id).firstOrNull()

        assertEquals(answer.name, result?.name)
    }
}
