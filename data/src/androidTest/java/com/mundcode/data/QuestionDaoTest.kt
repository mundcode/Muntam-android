package com.mundcode.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mundcode.data.local.database.MuntamDatabase
import com.mundcode.data.local.database.dao.ExamDao
import com.mundcode.data.local.database.dao.QuestionDao
import com.mundcode.data.local.database.dao.SubjectDao
import com.mundcode.data.local.database.model.createExamEntity
import com.mundcode.data.local.database.model.createSubjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class QuestionDaoTest {
    private lateinit var questionDao: QuestionDao
    private lateinit var subjectDao: SubjectDao
    private lateinit var examDao: ExamDao
    private lateinit var db: MuntamDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            MuntamDatabase::class.java
        ).build()
        questionDao = db.questionDao()
        subjectDao = db.subjectDao()
        examDao = db.examDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testWriteAndReadExam() = runBlocking(Dispatchers.IO) {
        // subjectId 과 examId 모두 0 으로 하면 Foreign Key Constraint Failed  에러
        val subjectId = 1
        val examId = 2
        val subjectEntity = createSubjectEntity(subjectId)
        val examEntity = createExamEntity(id = examId, subjectId = subjectId)

        subjectDao.insert(subjectEntity)
        examDao.insert(examEntity)
        val result = examDao.getExam(examId)

        assertEquals(examEntity.id, result.id)
        // 디비에 들어갔다가 나오면 InstantConverters 에 따라서 값이 바뀌어 있음.
        assertFalse(examEntity.createdAt == result.createdAt)
    }
}