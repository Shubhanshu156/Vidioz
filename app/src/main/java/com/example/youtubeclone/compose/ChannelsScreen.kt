package com.example.youtubeclone.compose

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.youtubeclone.R
import com.example.youtubeclone.ViewModels.ChannelViewModel
import com.example.youtubeclone.data.responses.SubscribedChannels

import com.example.youtubeclone.util.Constants.CLIENT_ID
import com.example.youtubeclone.util.Constants.poppins
import com.example.youtubeclone.util.rememberFirebaseAuthLauncher
import com.example.youtubeclone.util.shimmerEffect
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ChannelScreen(channelViewModel: ChannelViewModel = hiltViewModel()) {
    var user by remember {
        channelViewModel.currentuser
    }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = {
            user = null
        }
    )
    val token = CLIENT_ID
    val context = LocalContext.current

        if (user == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.channels),
                    contentDescription = "",
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Don’t miss new videos",
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "Sign in to see updates from your favorite YouTube channels",
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraLight,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 30.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                IconButton(onClick = {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(
                            painterResource(id = R.drawable.google),
                            contentDescription = "",

                            )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Signin with google",
                            fontSize = 10.sp,
                            fontFamily = poppins,
                            fontWeight = FontWeight.ExtraLight
                        )

                    }
                }
            }

        } else {
            Column() {
                ChannelsTopSection()
                SearchFilter("Search Channels")
                ShowChannelList()
            }

        }
    }



@Composable
private fun ChannelsTopSection() {
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
            text = "Channels",
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
                    .padding(start = 30.dp, top = 20.dp)
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
                        .padding(start = 50.dp, top = 40.dp),
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
                .padding(top = 20.dp, end = 30.dp)
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
fun ShowChannelList(viewModel: ChannelViewModel = hiltViewModel()) {

    val res by viewModel.subscribedlist.collectAsState()
    val isLoading by remember {
        viewModel.isLoading
    }
    LazyColumn(modifier = Modifier.padding(top=20.dp)) {

        if (isLoading) {
            items(10) {
                LoadSubscribeList()
                Spacer(modifier = Modifier.height(20.dp))
            }

        } else {
            items(res.size) {
                SubscribeListitem(item = res[it])
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

    }
}

@Composable
fun LoadSubscribeList() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .shimmerEffect()
        ) {}
        Column(modifier=Modifier.padding(10.dp)) {
            Spacer(
                modifier = Modifier
                    .width(120.dp)
                    .height(8.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row() {
                Text(
                    text = "",
                    modifier = Modifier
                        .width(30.dp)
                        .height(10.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "",
                    modifier = Modifier
                        .width(30.dp)
                        .height(10.dp)
                        .shimmerEffect()
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        var buttonstate by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .width(84.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Black)
        ) {
            Button(
                onClick = { buttonstate = !buttonstate },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentPadding = PaddingValues(0.dp) // <-- set contentPadding to 0.dp
            ) {
                Text("Subscribe", color = Color.White)
            }
        }
    }
}

@Composable
fun SubscribeListitem(item: SubscribedChannels,viewModel: ChannelViewModel= hiltViewModel()) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(item.thumbnailurl)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
        )
        Column(modifier=Modifier.padding(start=10.dp)) {
            Text(
                fontSize = 14.sp,
                text = item.channelname.substring(1, item.channelname.length),
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins
            )
            Row() {
                Text(
                    fontSize = 12.sp,
                    text = item.subscribers + " Subscribers ",
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraLight,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    fontSize = 12.sp,
                    text = item.videocount.toString() + " videos",
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraLight,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        var buttonstate by remember { mutableStateOf(true) }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))

                .width(87.dp)
                .height(37.dp)

                .background(Color.Black)

        )

        {
            Button(
                onClick = {
                    buttonstate = !buttonstate
                    if (buttonstate) {

                        viewModel.insertitem(item)
                    } else {
                        viewModel.deleteitem(item)
                    }
                },
                colors = if (buttonstate) {

                    ButtonDefaults.buttonColors(Color.Black)
                } else {
                    ButtonDefaults.buttonColors(Color.Red)
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize() ,
                contentPadding = PaddingValues(0.dp)
            ) {
                if (buttonstate) {
                    Text(fontSize=12.sp,text = "Subscribed", color = Color.White)
                } else {
                    Text(fontSize=12.sp,text = "Subscribe", color = Color.White)
                }
            }
        }
    }
}






