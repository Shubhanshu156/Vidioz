package com.example.youtubeclone

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.youtubeclone.compose.DiscoverScreen
import com.example.youtubeclone.compose.LibraryScreen
import com.example.youtubeclone.compose.SearchScreen
import com.example.youtubeclone.compose.VideoScreen
import com.example.youtubeclone.data.navitem
import com.example.youtubeclone.ui.theme.YouTubeCloneTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YouTubeCloneTheme {
                var isvisible by remember {
                    mutableStateOf(true)
                }
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Scaffold(bottomBar = {
                    AnimatedVisibility(
                        visible = isvisible, enter = fadeIn(), exit = fadeOut()
                    ) {
                        BottomNavigationBar(items = listOf(
                            navitem(
                                name = "Home",
                                route = "home",
                                icon = painterResource(id = R.drawable.home)
                            ),
                            navitem(
                                name = "explore",
                                route = "explore",
                                icon = painterResource(id = R.drawable.discovery_2)

                            ),
                            navitem(
                                name = "channel",
                                route = "channel",
                                icon = painterResource(id = R.drawable.channels)

                            ),
                            navitem(
                                name = "library",
                                route = "library",
                                icon = painterResource(id = R.drawable.library)

                            ),
                        ), navController = navController, onItemClick = {

                            navController.navigate(it.route)
                        })
                    }
                })

                {

                    Navigation(navController = navController, padding = it) { newState ->
                        isvisible = newState
                    }
                }

            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    items: List<navitem>,
    modifer: Modifier = Modifier,
    navController: NavHostController,
    onItemClick: (navitem) -> Unit
) {
    BottomNavigation(
        modifer
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)),
        backgroundColor = Color.DarkGray,
        elevation = 5.dp,
    ) {
        val backStackEntry = navController.currentBackStackEntryAsState()
        items.forEach {
            BottomNavigationItem(selected = it.route == backStackEntry.value?.destination?.route,
                onClick = { onItemClick(it) },
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.White,
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(it.icon, contentDescription = "");
                        Text(text = it.name, textAlign = TextAlign.Center, fontSize = 13.sp);

                    }
                })
        }


    }
}


@Composable
fun Navigation(
    navController: NavHostController, padding: PaddingValues, isvisible: (Boolean) -> Unit
) {


    NavHost(
        navController = navController, startDestination = "home?showchips={showchips}&query={query}"
    ) {
        composable("home?showchips={showchips}&query={query}",
            arguments = listOf(navArgument("showchips") {
                type = NavType.BoolType
                defaultValue = true
            }, navArgument("query") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) { backStackEntry ->
            com.example.youtubeclone.compose.HomeScreen(
                navController,
                backStackEntry.arguments!!.getBoolean("showchips"),
                backStackEntry.arguments!!.getString("query"),
            )
            isvisible(true)
        }

        composable("explore") {
            isvisible(true)
            DiscoverScreen()
        }
        composable("library") {
            isvisible(true)
            LibraryScreen()
        }
        composable("channel") {
            isvisible(true)
            com.example.youtubeclone.compose.ChannelScreen()
        }
        composable(
            "search_screen?q={q}", arguments = listOf(navArgument("q") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            var q = it.arguments!!.getString("q")
            isvisible(false)
            SearchScreen(navController, q)
        }
        composable(route = "video?videoid={videoid}",
            deepLinks= listOf(
                navDeepLink {
                    uriPattern="https://youtubeclone.com/{videoid}"
                    action= Intent.ACTION_VIEW
                }
            ),
            arguments = listOf(navArgument("videoid") {
                type = NavType.StringType
                defaultValue=null
                nullable=true
            },

            )
        ) {
            var videoid = it.arguments!!.getString("videoid")
            isvisible(false)
            VideoScreen(navController, videoid)
        }

    }


}

