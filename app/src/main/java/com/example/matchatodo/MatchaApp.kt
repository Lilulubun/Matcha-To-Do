package com.example.matchatodo

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.matchatodo.ui.screens.*
import com.example.matchatodo.viewmodel.MatchaViewModel

@Composable
fun MatchaApp() {
    val navController = rememberNavController()
    val viewModel: MatchaViewModel = viewModel()

    // 🔹 source of truth
    val goals by viewModel.goals.collectAsState()

    // 🔹 temporary states for multi-step creation
    var tempGoalName by remember { mutableStateOf("") }
    var tempTasks by remember { mutableStateOf<List<String>>(emptyList()) }

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {

        /* ----------------------------------
           DASHBOARD
        ---------------------------------- */
        composable("dashboard") {
            DashboardScreen(
                goals = goals,
                onNavigateToCreate = {
                    navController.navigate("create")
                },
                onNavigateToActive = { goalId ->
                    navController.navigate("active/$goalId")
                },
                onDelete = { goalId ->
                    viewModel.deleteGoal(goalId)
                }
            )
        }

        /* ----------------------------------
           CREATE GOAL
        ---------------------------------- */
        composable("create") {
            GoalCreationScreen(
                onBack = {
                    navController.popBackStack()
                },
                onContinue = { goalName ->
                    tempGoalName = goalName
                    navController.navigate("addTask")
                }
            )
        }

        /* ----------------------------------
           ADD TASKS
        ---------------------------------- */
        composable("addTask") {
            AddTaskScreen(
                goalName = tempGoalName,
                onBack = {
                    navController.popBackStack()
                },
                onStartBrewing = { name, tasks ->
                    tempGoalName = name
                    tempTasks = tasks
                    navController.navigate("review")
                }
            )
        }

        /* ----------------------------------
           REVIEW
        ---------------------------------- */
        composable("review") {
            ReviewGoalScreen(
                goalName = tempGoalName,
                tasks = tempTasks,
                onBack = {
                    navController.popBackStack()
                },
                onStartGoal = {
                    viewModel.addGoal(tempGoalName, tempTasks)

                    // reset temp state (important)
                    tempGoalName = ""
                    tempTasks = emptyList()

                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        /* ----------------------------------
           ACTIVE PROGRESS
        ---------------------------------- */
        composable("active/{goalId}") { backStackEntry ->
            val goalId = backStackEntry.arguments?.getString("goalId")
            val selectedGoal = goals.find { it.id == goalId }

            ActiveProgressScreen(
                goal = selectedGoal,
                onBack = {
                    navController.popBackStack()
                },
                onToggleTask = { taskId ->
                    if (goalId != null) {
                        viewModel.toggleTask(goalId, taskId)
                    }
                },
                onGoalCompleted = { completedGoal ->
                    // 🔥 pass goal name SAFELY
                    val encodedName = Uri.encode(completedGoal.name)

                    navController.navigate("reward/$encodedName") {
                        // do NOT remove dashboard from backstack
                        popUpTo("dashboard") { inclusive = false }
                    }
                }
            )
        }

        /* ----------------------------------
           REWARD
        ---------------------------------- */
        composable(
            route = "reward/{goalName}",
            arguments = listOf(
                navArgument("goalName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val goalName =
                backStackEntry.arguments?.getString("goalName") ?: ""

            RewardScreen(
                goalName = goalName,
                onBackToHome = {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }
    }
}
