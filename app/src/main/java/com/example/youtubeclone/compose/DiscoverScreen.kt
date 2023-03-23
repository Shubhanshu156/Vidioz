package com.example.youtubeclone.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.youtubeclone.R
import com.example.youtubeclone.ViewModels.TrendingViewModel
import com.example.youtubeclone.data.TextImage
import com.example.youtubeclone.data.responses.Data
import com.example.youtubeclone.util.Constants.poppins
import com.example.youtubeclone.util.getFormattedCount
import com.example.youtubeclone.util.shimmerEffect


@Composable
fun DiscoverScreen(viewModel: TrendingViewModel = hiltViewModel()) {
    var selectedIndex by remember {
        mutableStateOf(0);

    }

    val isLoading by remember {
        viewModel.isLoading
    }
    DiscoverTopSection()
    Spacer(modifier = Modifier.height(30.dp))
    SearchFilter()
    Spacer(modifier = Modifier.height(25.dp))
    PostTabView(imageWithTexts = listOf(
        TextImage(painterResource(id = R.drawable.trend), "trending"),
        TextImage(painterResource(id = R.drawable.game), "gaming"),
        TextImage(painterResource(id = R.drawable.music), "music"),
        TextImage(painterResource(id = R.drawable.learn), text = "movies"),
    ), onTabSelected = { selectedIndex = it })
    when (selectedIndex) {
        0 -> viewModel.getTrendingResults("All", "IN")
        1 -> viewModel.getTrendingResults("games", "IN")
        2 -> viewModel.getTrendingResults("music", "IN")
        3 -> viewModel.getTrendingResults("movies", "IN")
    }
    ShowResults()
}

@Composable
fun DiscoverTopSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = Modifier.padding(start = 30.dp)
            )
        }
        Text(
            text = "Explore",
            fontFamily = poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 100.dp)
        )
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "",
                modifier = Modifier.padding(start = 100.dp, end = 30.dp)
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchFilter(hint: String = "") {
    var text by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    var textValue by remember { mutableStateOf("") }

    var isHintDisplayed by remember { mutableStateOf(true) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = textValue,
                onValueChange = {
                    textValue = it.take(20) // limit the text to 20 characters
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done // set the IME action to "Done"
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide() // hide the keyboard when "Done" is pressed
                    }
                ),
                modifier = Modifier
                    .padding(start = 30.dp, top = 90.dp)
                    .height(58.dp)
                    .width(230.dp)
                    .border(BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(13.dp)),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontFamily = poppins,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Justify
                )
            )

            if (textValue.isEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 50.dp, top = 110.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = "Search Channels",
                        fontFamily = poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
            }
        }
        IconButton(
            onClick = {},
            modifier = Modifier
                .padding(top = 90.dp, end = 30.dp)
                .height(56.dp)
                .aspectRatio(1f)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.setting_4),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(2.dp)
            )
        }
    }
}

@Composable
fun PostTabView(
    modifier: Modifier = Modifier,
    imageWithTexts: List<TextImage>,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val inactiveColor = Color(0xFF777777)

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        contentColor = Color.Black,
        edgePadding = 10.dp,

        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Color.Red,
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
            )
        },
        modifier = modifier.padding(top = 190.dp, start = 0.dp),

        ) {
        imageWithTexts.forEachIndexed { index, item ->
            Tab(
                selected = selectedTabIndex == index,
                selectedContentColor = Color.White,

                unselectedContentColor = Color.DarkGray,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(index)
                }
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = item.Image,
                        contentDescription = item.text,
                        tint = if (selectedTabIndex == index) Color.White else Color.LightGray,
                        modifier = Modifier
                            .padding(10.dp)

                            .size(20.dp)
                    )
                    Text(
                        text = item.text,
                        fontSize = 10.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

            }
        }
    }
}


@Composable
fun TrendItemLoad() {
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
fun ShowResults(viewModel: TrendingViewModel = hiltViewModel()) {
    val res by viewModel.responseresult.collectAsState()
    val isLoading by remember {
        viewModel.isLoading
    }
    LazyColumn(modifier = Modifier.padding(top = 250.dp, bottom = 80.dp)) {
        if (isLoading) {
            items(5) {
                TrendItemLoad()
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else {
            items(res.size) {
                TrendItem(res[it])
                Spacer(modifier = Modifier.height(10.dp))
            }
        }


    }
}


@Composable
fun TrendItem(fields: Data) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 10.dp),
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
                    text = getFormattedCount(fields.viewCount) + " views",
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


