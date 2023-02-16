package com.mundcode.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mundcode.data.local.database.MuntamDatabase
import com.mundcode.data.local.database.dao.ExamDao
import com.mundcode.data.local.database.dao.QuestionDao
import com.mundcode.data.local.database.dao.SubjectDao
import com.mundcode.data.local.database.model.ExamEntity
import com.mundcode.data.local.database.model.QuestionEntity
import com.mundcode.data.local.database.model.SubjectEntity
import com.mundcode.data.local.database.model.createExamEntity
import com.mundcode.data.local.database.model.createQuestionEntities
import com.mundcode.data.local.database.model.createSubjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
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
    private lateinit var subjectEntity: SubjectEntity
    private lateinit var examEntity: ExamEntity
    private lateinit var questionsEntity: List<QuestionEntity>

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            MuntamDatabase::class.java
        ).build()
        questionDao = db.questionDao()
        subjectDao = db.subjectDao()
        examDao = db.examDao()

        val subjectId = SUBJECT_ID
        val examId = EXAM_ID
        subjectEntity = createSubjectEntity(subjectId)
        examEntity = createExamEntity(id = examId, subjectId = subjectId)
        questionsEntity = createQuestionEntities(
            size = 10,
            subjectId = SUBJECT_ID,
            examId = EXAM_ID
        )
        subjectDao.insert(subjectEntity)
        examDao.insert(examEntity)

        questionDao.insertAll(questionsEntity)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testWriteAndReadExam() = runBlocking(Dispatchers.IO) {
        // subjectId 과 examId 모두 0 으로 하면 Foreign Key Constraint Failed  에러
        val result = examDao.getExam(EXAM_ID)
        assertEquals(examEntity.id, result.id)
        // 디비에 들어갔다가 나오면 InstantConverters 에 따라서 값이 바뀌어 있음.
        assertFalse(examEntity.createdAt == result.createdAt)
    }

    @Test
    fun testGetQuestionsByExamId() = runBlocking {

        val result = questionDao.getQuestionsByExamId(examId = EXAM_ID).firstOrNull()
        assertEquals(result?.map { it.id }, questionsEntity.map { it.id })
    }

    companion object {
        const val SUBJECT_ID = 1
        const val EXAM_ID = 2
        const val QUESTION_LIST_SIZE = 10
    }
}