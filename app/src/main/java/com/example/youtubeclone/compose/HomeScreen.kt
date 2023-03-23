package com.example.youtubeclone.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import com.example.youtubeclone.data.responses.Data
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
//import androidx.compose.ui.focus.DefaultFocusProperties.start

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.youtubeclone.R
import com.example.youtubeclone.ViewModels.SearchViewModel
import com.example.youtubeclone.util.Constants.poppins
import com.example.youtubeclone.util.getFormattedCount
import com.example.youtubeclone.util.shimmerEffect
import kotlinx.coroutines.launch
import java.time.format.TextStyle


@Composable
fun HomeScreen(
    navcontroller: NavHostController,
    showchips: Boolean,
    query: String?,
    viewmodel: SearchViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (!showchips) viewmodel.getSearchResult(query!!)
        if (showchips) {
            Topbar() { navcontroller.navigate("search_screen") }
        }
        if (!showchips) {
            Searchbar(q = query!!, navcontroller = navcontroller)
        }
        if (showchips) {
            ChipSection(
                mutableListOf(
                    "AllVideos",
                    "figma",
                    "competitive programming",
                    "Game",
                    "Android Development",
                    "Anime",
                    "One Piece"
                )
            )


        }

        VideoCompose(navcontroller)

    }
}

@Composable
fun Searchbar(q: String, navcontroller: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(36.dp),
    ) {
        IconButton(
            onClick = { navcontroller.navigate("home") },

            ) {
            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = ""
            )

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(end = 40.dp)
                .height(30.dp)

                .background(Color.DarkGray)
                .fillMaxWidth()
        ) {
            Text(text = q,
                overflow = TextOverflow.Ellipsis,

                fontSize = 16.sp,
                fontFamily = poppins,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.ExtraLight,
                modifier = Modifier
                    .clickable { navcontroller.navigate("search_screen?q=${q}") }
                    .width(250.dp))
            IconButton(
                onClick = { navcontroller.navigate("search_screen") },

                ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
    Spacer(modifier = Modifier.height(10.dp))


}

@Composable
fun Topbar(onsearch: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {

        Image(
            painter = painterResource(id = R.drawable._375133_logo_youtube_icon),
            contentDescription = "YouTube Logo",
            modifier = Modifier
                .padding(start = 30.dp, end = 2.dp)
                .size(40.dp)
        )
        Text(
            text = "Vidioz", fontWeight = FontWeight.Bold, fontSize = 20.sp
        )
        IconButton(onClick = { onsearch() }) {
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
                modifier = Modifier
                    .padding(start = 80.dp)
                    .size(30.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = "Notifications",
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .size(30.dp)
        )
        RoundImage(
            modifier = Modifier.size(30.dp)
        )

    }

}

@Composable
fun RoundImage(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.baseline_person_24),
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 1.dp, color = Color.White, shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)
    )
}

@Composable
fun VideoCompose(navcontroller: NavHostController, viewModel: SearchViewModel = hiltViewModel()) {
    val res by viewModel.responseresult.collectAsState()
    val isLoading by remember {
        viewModel.isLoading
    }

    LazyColumn(modifier = Modifier.padding(bottom = 80.dp)) {
        if (isLoading) {
            items(5) {

                YoutubeVideoCardShimmer()
            }
        } else {
            items(res.size) {
                if (it >= res.size - 3) {
                    LaunchedEffect(key1 = true) {
                        viewModel.loadmoreresult()
                    }

                }
                YoutubeVideoCard(fields = res[it], navcontroller = navcontroller)

            }
        }
    }
}

@Composable
fun ChipSection(
    list: MutableList<String>, viewModel: SearchViewModel = hiltViewModel()
) {
    var selectedindex by rememberSaveable {
        mutableStateOf(0)
    }
    Column {
        LazyRow {
            items(list.size) {
                Card(
                    modifier = Modifier
                        .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            selectedindex = it
                            viewModel.getSearchResult(list[it])
                        },
                    backgroundColor = if (selectedindex == it) Color.White.copy(0.2f) else Color.Black,

                    ) {
                    Text(
                        text = list[it], fontSize = 15.sp, modifier = Modifier.padding(
                            horizontal = 16.dp, vertical = 8.dp
                        ), color = if (selectedindex == it) Color.Black else Color.White
                    )

                }
            }
        }
    }

}


@Composable
fun YoutubeVideoCard(fields: Data, navcontroller: NavHostController,viewModel: SearchViewModel= hiltViewModel()) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .clickable {
                viewModel.inserthistory(fields)

                navcontroller.navigate("video?videoid=${fields.videoId}")

            }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
    ) {
        Box {

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(fields.thumbnail.last().url)
                    .crossfade(true).build(),
                contentDescription = "",
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .height(182.dp)
                    .width(321.dp)
            )
            IconButton(
                {}, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(
                        Color.Black, CircleShape
                    )
                    .size(25.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Favorite icon",
                    tint = Color.Red
                )

            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        Row(horizontalArrangement = Arrangement.Start) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fields.channelThumbnail.last().url).crossfade(true).build(),
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .height(29.dp)
                    .width(29.dp)
            )
            Column(modifier = Modifier.padding(start = 15.dp)) {
                Text(
                    text = fields.title,
                    maxLines = 2,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = poppins,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
                Row {
                    Text(
                        text = fields.channelTitle,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = poppins,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp
                    )
                    Text(
                        text = getFormattedCount(fields.viewCount),
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = poppins,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = fields.publishedTimeText,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = poppins,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun YoutubeVideoCardShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()

            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .height(182.dp)
                .shimmerEffect()
                .width(321.dp)
        ) {

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(horizontalArrangement = Arrangement.Start) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(29.dp)
                    .width(29.dp)
                    .shimmerEffect()
            ) {


            }
            Box(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .shimmerEffect()
                    .height(30.dp)
                    .fillMaxWidth()
            ) {


            }
        }
        Spacer(
            modifier = Modifier.height(10.dp)

        )
    }
}

