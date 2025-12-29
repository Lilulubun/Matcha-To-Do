package com.example.matchatodo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.matchatodo.viewmodel.MatchaViewModel
import com.example.matchatodo.ui.screens.*

@Composable
fun MatchaApp() {
    val navController = rememberNavController()
    val viewModel: MatchaViewModel = viewModel()

    // 1. Observe the list of goals from the ViewModel
    // Note: This assumes your viewModel has a 'goals' StateFlow or MutableState
    val goals by viewModel.goals.collectAsState()
    var tempGoalName by remember { mutableStateOf("") }
    var tempTasks by remember { mutableStateOf<List<String>>(emptyList()) }


    NavHost(navController = navController, startDestination = "dashboard") {

        // --- DASHBOARD ---
        composable("dashboard") {
            DashboardScreen(
                goals = goals, // Pass the DATA, not the ViewModel
                onNavigateToCreate = { navController.navigate("create") },
                onNavigateToActive = { goalId -> navController.navigate("active/$goalId") },
                onNavigateToReward = { navController.navigate("reward") },
                onDelete = { goalId ->
                    viewModel.deleteGoal(goalId)
                }
            )
        }

        // --- CREATE GOAL ---
        composable("create") {
            GoalCreationScreen(
                onBack = { navController.popBackStack() },
                onContinue = { goalName ->
                    navController.navigate("add_tasks/$goalName")
                }
            )
        }

        // --- ADD TASKS ---
        composable("addTask") {
            AddTaskScreen(
                goalName = tempGoalName,
                onBack = { navController.popBackStack() },
                onStartBrewing = { name, tasks ->
                    tempGoalName = name
                    tempTasks = tasks
                    navController.navigate("review")
                }
            )
        }
        composable("review") {
            ReviewGoalScreen(
                goalName = tempGoalName,
                tasks = tempTasks,
                onBack = { navController.popBackStack() },
                onStartGoal = {
                    viewModel.addGoal(tempGoalName, tempTasks)
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        // --- ACTIVE PROGRESS ---
        composable("active/{goalId}") { backStackEntry ->
            val goalId = backStackEntry.arguments?.getString("goalId")

            // Find the specific goal object from our list
            val selectedGoal = goals.find { it.id == goalId }

            ActiveProgressScreen(
                goal = selectedGoal, // Pass the specific GOAL object
                onBack = { navController.popBackStack() },
                onToggleTask = { taskId ->
                    if (goalId != null) {
                        viewModel.toggleTask(goalId, taskId)
                    }
                },
                onGoalCompleted = {
                    navController.navigate("reward") {
                        popUpTo("dashboard")
                    }
                }
            )
        }

        // --- REWARD ---
        composable("reward") {
            RewardScreen(
                onBackToHome = {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }
    }
}