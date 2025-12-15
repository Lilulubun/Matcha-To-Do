package com.example.matchatodo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchatodo.data.Goal
import com.example.matchatodo.data.MatchaDatabase
import com.example.matchatodo.data.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MatchaViewModel(application: Application) : AndroidViewModel(application) {
    // Access the DAO
    private val dao = MatchaDatabase.getDatabase(application).goalDao()

    // 1. Real-time data stream (Exposes the list to the UI)
    val goals = dao.getAllGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. Add Goal (Renamed from 'createGoal' to match MatchaApp.kt)
    fun addGoal(name: String, taskList: List<String>) {
        if (name.isBlank() || taskList.isEmpty()) return

        viewModelScope.launch {
            // Create Task objects from the list of strings
            val tasks = taskList.map { Task(text = it) }
            val newGoal = Goal(name = name, tasks = tasks)
            dao.insertGoal(newGoal)
        }
    }

    // 3. Toggle Task Completion
    fun toggleTask(goalId: String, taskId: String) {
        viewModelScope.launch {
            // Find the current goal in our list
            val currentGoal = goals.value.find { it.id == goalId } ?: return@launch

            // Create a new list of tasks with the specific one toggled
            val updatedTasks = currentGoal.tasks.map {
                if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
            }

            // Check if all tasks are done
            val isAllDone = updatedTasks.isNotEmpty() && updatedTasks.all { it.isCompleted }

            // Update the Database
            dao.updateGoal(currentGoal.copy(tasks = updatedTasks, isCompleted = isAllDone))
        }
    }

    // 4. Delete Goal
    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            // SAFE DELETE: Find the object first, then delete it.
            // (Standard Room @Delete requires an object, not just an ID string)
            val goalToDelete = goals.value.find { it.id == goalId }
            if (goalToDelete != null) {
                dao.deleteGoal(goalToDelete)
            }
        }
    }
}