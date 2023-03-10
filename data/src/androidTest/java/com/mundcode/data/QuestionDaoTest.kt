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
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
            size = QUESTION_LIST_SIZE,
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

    // QuestionDao 를 테스트 하기 위해 필요한 Subject, Exam 이 잘 들어갔는지 테스트
    @Test
    fun testWriteAndReadExam() = runBlocking(Dispatchers.IO) {
        // subjectId 과 examId 모두 0 으로 하면 Foreign Key Constraint Failed  에러
        val result = examDao.getExamById(EXAM_ID)
        assertEquals(examEntity.id, result.id)
        // 디비에 들어갔다가 나오면 InstantConverters 에 따라서 값이 바뀌어 있음.
        assertFalse(examEntity.createdAt == result.createdAt)
    }

    @Test
    fun testGetQuestionsByExamId() = runBlocking {
        val result = questionDao.getQuestionsByExamIdFlow(examId = EXAM_ID).firstOrNull()
        assertEquals(result?.map { it.id }, questionsEntity.map { it.id })
    }

    @Test
    fun testGetQuestionByQuestionId() = runBlocking {
        val questionNumber = 2
        val result = questionDao.getQuestionByQuestionNumber(
            examId = EXAM_ID,
            questionNumber = questionNumber
        ).firstOrNull()
        assertEquals(
            questionsEntity.find { it.questionNumber == 2 }?.id,
            result?.id
        )
    }

    @Test
    fun testGetQuestionsById() = runBlocking {
        val id = 2
        val result = questionDao.getQuestionById(id).firstOrNull()
        assertEquals(
            questionsEntity.find { it.id == 2 }?.id,
            result?.id
        )
    }

    @Test
    fun testUpdateQuestion() = runBlocking {
        val id = 2
        val question = questionsEntity.find { it.id == id }
        question?.copy(isCorrect = question.isCorrect.not())?.let {
            questionDao.updateQuestion(it)
        }

        val result = questionDao.getQuestionById(id).firstOrNull()

        assertNotEquals(question?.isCorrect, result?.isCorrect)
        assertEquals(question?.isFavorite, result?.isFavorite)
    }

    companion object {
        const val SUBJECT_ID = 1
        const val EXAM_ID = 2
        const val QUESTION_LIST_SIZE = 10
    }
}
