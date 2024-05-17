package com.nhatvm.toptop.data.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nhatvm.toptop.data.Data.repositories.User

@Composable
fun FunFactsNavigationGraph(){
    val user = User(userName = "admin", password = "1234")
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN){
        composable(Routes.LOGIN_SCREEN){
            LoginScreen(
                navController = navController,
                onLogin = { username, password ->
                    if (username.trim() == "" && password.trim() == "") {
                        navController.navigate(Routes.HOME_SCREEN)
                    } else {

                    }
                }
            )
        }
        composable(Routes.HOME_SCREEN){
        }
        composable(Routes.USER_SCREEN){
        }

    }
}