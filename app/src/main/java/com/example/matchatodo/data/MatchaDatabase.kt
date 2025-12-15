package com.example.matchatodo.data

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID
import kotlinx.coroutines.flow.Flow

// --- MODELS ---
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val tasks: List<Task>,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

// --- CONVERTERS (To save List<Task> as text) ---
class Converters {
    @TypeConverter
    fun fromString(value: String): List<Task> {
        val listType = object : TypeToken<List<Task>>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromList(list: List<Task>): String = Gson().toJson(list)
}

// --- DAO (Commands) ---
@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)
}

// --- DATABASE SETUP ---
@Database(entities = [Goal::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MatchaDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile private var INSTANCE: MatchaDatabase? = null
        fun getDatabase(context: Context): MatchaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, MatchaDatabase::class.java, "matcha_db")
                    .build().also { INSTANCE = it }
            }
        }
    }
}