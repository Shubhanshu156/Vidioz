package com.example.youtubeclone.compose

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.youtubeclone.ViewModels.VideoScreenViewModel
import com.example.youtubeclone.util.Constants.poppins
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.youtubeclone.R
import com.example.youtubeclone.data.responses.Data
import com.example.youtubeclone.services.downloader.AndroidDownloader
import com.example.youtubeclone.util.shimmerEffect
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VideoScreen(
    navHostController: NavHostController,
    videoid: String?,

    viewModel: VideoScreenViewModel = hiltViewModel()
) {
    var isloading = remember {
        mutableStateOf(true)
    }
    var candownload = remember { mutableStateOf(false) }
    viewModel.VideoId.value = videoid!!
    LaunchedEffect(key1 = true) {
        viewModel.getvideolinks("144p", videoid!!)
        viewModel.getVideoDetails(videoid)
        viewModel.getRelatedVideos(videoid!!)
    }
    val res by viewModel.videoDetails.collectAsState()


    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    val downloader = AndroidDownloader(LocalContext.current)
    BottomSheetScaffold(
        scaffoldState = scaffoldState,

        sheetContent = {
            if (candownload.value) {

                val options = viewModel.qualityurls.keys.toList().sorted()
                var selectedOption by remember { mutableStateOf(options.first()) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .height(260.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                        .background(Color.Gray)


                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Text(
                            text = "  Select an option:",
                            modifier = Modifier.padding(start = 30.dp)
                        )
                        RadioButtonGroup(
                            selectedOption = selectedOption,
                            options = options
                        ) { option ->
                            selectedOption = option
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(35.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.Gray)
                            ) {
                                Button(
                                    onClick = { scope.launch { sheetState.collapse() } },
                                    colors = ButtonDefaults.buttonColors(Color.Black),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentPadding = PaddingValues(0.dp) // <-- set contentPadding to 0.dp
                                ) {
                                    Text("Cancel", color = Color(0xff0096ff))
                                }
                            }


                            Box(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(35.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.Black)
                            ) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            sheetState.collapse()

                                        }
                                        if (viewModel.qualityurls[selectedOption] != null) {
                                            downloader.downloadFile(
                                                viewModel.qualityurls[selectedOption]!!,
                                                res.VideoTittle
                                            )
                                        }

                                    },
                                    colors = ButtonDefaults.buttonColors(Color(0xff0096ff)),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentPadding = PaddingValues(0.dp) // <-- set contentPadding to 0.dp
                                ) {
                                    Text("Download", color = Color.White)
                                }
                            }

                        }
                    }
                }


            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 150.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .width(60.dp)
                            .height(6.dp)
                            .background(Color.DarkGray)


                    )
                    Text(
                        text = "Description",
                        fontSize = 16.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = res.VideoTittle,
                        fontSize = 14.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = res.description,
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraLight,
                        fontSize = 12.sp
                    )
                }
            }
        },
        sheetPeekHeight = 0.dp
    )
    {
        Column() {
            Box(
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .height(221.dp)
                    .fillMaxWidth()
            ) {

                var urlmp = viewModel.qualityurls["144p"]
                if (isloading.value) {
                    ShowLoadingVideo()
                }
                if (!urlmp.isNullOrEmpty()) {
                    VideoPlayer(urlmp!!, isloading)
                }

            }

            AboutSection(
                sheetState = sheetState,
                scaffoldState = scaffoldState,
                scope = scope,
                videoid = videoid!!,
                candownload
            )
            Spacer(modifier = Modifier.height(20.dp))
            RelatedVideo(videoid, navHostController)
        }

    }


}

@Composable
fun ShowLoadingVideo() {

    Box(
        modifier = androidx.compose.ui.Modifier

            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black)
    ) {
        Indicator(modifier = androidx.compose.ui.Modifier.align(Alignment.Center))
    }

}


@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(url: String, isloading: MutableState<Boolean>) {

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val context = LocalContext.current
    val player = ExoPlayer.Builder(context).build()


    val playerView = PlayerView(context)
    playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    player.addListener(object : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY && player.playWhenReady) {
                isloading.value = false

            } else if (playbackState == Player.STATE_ENDED) {
                // Video playback has ended
                // Do something, like hide the player UI
            }
        }
    })
    val playWhenReady by rememberSaveable {
        mutableStateOf(true)
    }

    player.setMediaItem(MediaItem.fromUri(url))

    playerView.player = player
//
    LaunchedEffect(player) {
        player.prepare()
        player.playWhenReady = playWhenReady

    }

    Column(
        modifier = androidx.compose.ui.Modifier

            .fillMaxHeight()
            .fillMaxWidth()


    ) {
        DisposableEffect(
            AndroidView(factory = {
                playerView
            })
        ) {
            val observer = LifecycleEventObserver { owner, event ->
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> {
                        player.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        player.play()
                    }
                    else -> {}
                }
            }
            val lifecycle = lifecycleOwner.value.lifecycle
            lifecycle.addObserver(observer)

            onDispose {
                player.release()
                lifecycle.removeObserver(observer)
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AboutSection(
    sheetState: BottomSheetState,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    videoid: String,
    candownload: MutableState<Boolean>,
    videoScreenViewModel: VideoScreenViewModel = hiltViewModel(),

    ) {
    val res by videoScreenViewModel.videoDetails.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .wrapContentHeight()
            .clip(RoundedCornerShape(bottomStart = 70.dp, bottomEnd = 70.dp))
            .background(Color.DarkGray)
            .padding(horizontal = 20.dp)
    ) {
        if (!res.ChannelThumbnail.isNullOrEmpty()) {
            TittleSection(
                VideoURL = res.ChannelThumbnail.last().url,
                VideoTittle = res.VideoTittle,
                ChannelName = res.ChannelName,
                Channelviews = res.views,
                time = res.time,
                sheetState = sheetState,
                ScaffoldState = scaffoldState,
                scope = scope,
                candownload

            )
        } else {
            LoadTittleSection()
        }
        ButtonSection(videoid = videoid, sheetState, scaffoldState, candownload, scope)
        NotificationSection(videoid)


    }


}

@Composable
fun LoadTittleSection() {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Box(

            modifier = androidx.compose.ui.Modifier
                .size(33.dp)
                .clip(CircleShape)
                .shimmerEffect()

        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                "", fontFamily = poppins, fontWeight = FontWeight.ExtraBold, modifier = Modifier
                    .shimmerEffect()
                    .height(5.dp)
                    .width(220.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .shimmerEffect()
                    .height(5.dp)
                    .width(220.dp)
            ) {

            }
        }
        IconButton(onClick = {})
        {
            Icon(painterResource(id = R.drawable.arrow_down), contentDescription = "")
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TittleSection(
    VideoURL: String,
    VideoTittle: String,
    ChannelName: String,
    Channelviews: String,
    time: String,
    sheetState: BottomSheetState,
    ScaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    candownload: MutableState<Boolean>

) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(VideoURL).crossfade(true)
                .build(),
            contentDescription = "",
            modifier = androidx.compose.ui.Modifier
                .size(33.dp)
                .clip(CircleShape)

        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Text(VideoTittle, fontFamily = poppins, fontWeight = FontWeight.ExtraBold)
            Row(
                verticalAlignment = Alignment.CenterVertically,

                ) {
                val text1 =
                    ChannelName.substring(0, Integer.min(ChannelName.length, 13)) + ".".repeat(
                        ChannelName.length - Integer.min(ChannelName.length, 13)
                    )
                Text(
                    text1,

                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 14.sp, maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(

                    " " + com.example.youtubeclone.util.getFormattedCount(Channelviews) + " views.",
                    fontFamily = poppins,

                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.ExtraLight
                )
                Text(
                    " $time.",

                    fontFamily = poppins,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraLight
                )
            }
        }
        var isArrowUp by remember { mutableStateOf(true) } // state to track the current icon
        IconButton(onClick = {
            candownload.value = false
            isArrowUp = !isArrowUp
            scope.launch {
                if (sheetState.isCollapsed) {
                    sheetState.expand()
                } else {
                    sheetState.collapse()
                }
            }
        })
        {
            Icon(
                if (isArrowUp) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = ""
            )
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ButtonSection(
    videoid: String,
    sheetState: BottomSheetState,
    scaffoldState: BottomSheetScaffoldState,
    candownload: MutableState<Boolean>,
    scope: CoroutineScope,
    viewModel: VideoScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val res = viewModel.videoDetails.collectAsState()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()


    ) {
        ButtonItem(painter = painterResource(id = R.drawable.group_20), text = "like") {

        }
        ButtonItem(painter = painterResource(id = R.drawable.group_19), text = "dislike") {}
        ButtonItem(painter = painterResource(id = R.drawable.group_18), text = "share") {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Link")
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://youtubeclone.com/${videoid}")
            context.startActivity(Intent.createChooser(shareIntent, "Share link via"))
        }
        ButtonItem(painter = painterResource(id = R.drawable.group_17), text = "download") {
            candownload.value = true
            scope.launch {
                sheetState.expand()
            }


        }
        var issaved by remember {
            mutableStateOf(false)
        }
        ButtonItem(painter = painterResource(id = R.drawable.group_16), text = "save") {
            issaved=!issaved
            if (issaved){

            }
            else{

            }
        }

    }
}


@Composable

fun ButtonItem(painter: Painter, text: String, itemclick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { itemclick() }) {
            Icon(painter = painter, contentDescription = "")
        }
        Text(
            text,
            fontFamily = poppins,
            fontWeight = FontWeight.ExtraLight,
            fontSize = 12.sp
        )
    }
}

@Composable
fun NotificationSection(videoid: String, viewModel: VideoScreenViewModel = hiltViewModel()) {
    val res by viewModel.subscribedvideo.collectAsState()
    var buttonstate by viewModel.issubscribed
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, CircleShape)
                .padding(3.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.notification), contentDescription = "")
        }
        var context = LocalContext.current
        Button(
            onClick = {
                if (viewModel.user != null) {
                    buttonstate = !buttonstate
                    if (buttonstate) {

                        viewModel.insertitem(res)
                    } else {
                        viewModel.deleteitem(res)
                    }
                } else {
                    Toast.makeText(context, "Signin First", Toast.LENGTH_SHORT)
                }
            },
            colors = if (buttonstate) {

                ButtonDefaults.buttonColors(Color.Gray)
            } else {
                ButtonDefaults.buttonColors(Color.Red)
            },
            modifier = Modifier
                .padding(16.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(20.dp))

        ) {
            if (buttonstate) {
                Text(text = "Subscribed", color = Color.White)
            } else {
                Text(text = "Subscribe", color = Color.White)
            }

        }
    }
}


@Composable
fun RelatedVideo(
    videoid: String?,
    navHostController: NavHostController,
    viewModel: VideoScreenViewModel = hiltViewModel()
) {
    Text(
        "Maybe You like that",
        fontFamily = poppins,
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(start = 20.dp)
    )
    val res by viewModel.responseresult.collectAsState()


    val isLoading by remember {
        viewModel.isLoading
    }
    LazyColumn(modifier = Modifier.padding()) {
        if (isLoading) {
            items(5) {
                RelatedItemLoad()
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else {
            items(res.size) {
                if (it >= res.size - 4) {
                    LaunchedEffect(key1 = true) {
                        viewModel.loadmorevideo(videoid!!)
                    }
                }
                RelatedItem(res[it], navHostController = navHostController)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }


    }

}


@Composable
fun Indicator(
    size: Dp = 32.dp, // indicator size
    sweepAngle: Float = 90f, // angle (lenght) of indicator arc
    color: Color = MaterialTheme.colors.primary, // color of indicator arc line
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {

    val transition = rememberInfiniteTransition()
    val currentArcStartAngle by transition.animateValue(
        0, 360, Int.VectorConverter, infiniteRepeatable(
            animation = tween(
                durationMillis = 1100, easing = LinearEasing
            )
        )
    )

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }


    androidx.compose.foundation.Canvas(
        modifier = modifier
            .size(size)
            .progressSemantics()
            .padding(strokeWidth / 2)
    ) {

        drawCircle(Color.LightGray, style = stroke)

        drawArc(
            color,
            // arc start angle
            // -90 shifts the start position towards the y-axis
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}

@Composable
fun RelatedItem(fields: Data, navHostController: NavHostController) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 10.dp)
            .clickable {
                navHostController.navigate("video?videoid=${fields.videoId}&channelurl=${fields.channelThumbnail.last().url}&time=${fields.publishedTimeText}")
                {
                    popUpTo("home") { inclusive = false }
                }

            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(fields.thumbnail.last().url)
                .crossfade(true)
                .build(), contentDescription = "",
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .height(105.dp)
                .width(165.dp)
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                text = fields.title, fontWeight = FontWeight.ExtraBold,
                fontFamily = poppins,
                maxLines = 2,

                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
            )
            Row() {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(fields.channelThumbnail.last().url)
                        .crossfade(true)
                        .build(), contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(17.dp)
                        .width(17.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = fields.channelTitle, fontWeight = FontWeight.ExtraLight,
                    fontFamily = poppins,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                )
            }
            Row() {
                Text(
                    text = com.example.youtubeclone.util.getFormattedCount(fields.viewCount) + " views",
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = poppins,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = fields.publishedTimeText, fontWeight = FontWeight.ExtraLight,
                    fontFamily = poppins,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                )
            }

        }


    }

}

@Composable
fun RelatedItemLoad() {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(

            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .height(105.dp)
                .width(165.dp)
                .shimmerEffect()
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Spacer(
                modifier = Modifier
                    .shimmerEffect()
                    .fillMaxWidth()
                    .height(20.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 20.dp, start = 10.dp)
            ) {
                Box(

                    modifier = Modifier
                        .clip(CircleShape)
                        .height(17.dp)
                        .width(17.dp)
                        .shimmerEffect()
                ) {}
                Spacer(modifier = Modifier.width(10.dp))
                Spacer(
                    modifier = Modifier
                        .shimmerEffect()
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }
            Row(modifier = Modifier.padding(top = 10.dp, start = 5.dp)) {
                Spacer(
                    modifier = Modifier
                        .shimmerEffect()
                        .width(90.dp)
                        .height(5.dp)
                )
                Spacer(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .shimmerEffect()
                        .width(90.dp)
                        .height(5.dp)
                )
            }


        }

    }


}

@Composable
fun RadioButtonGroup(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    options.forEach { option ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            RadioButton(
                selected = (selectedOption == option),
                onClick = { onOptionSelected(option) },
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(text = option)
        }
    }
}
