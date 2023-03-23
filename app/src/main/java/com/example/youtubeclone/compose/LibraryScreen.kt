package com.example.youtubeclone.compose

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.youtubeclone.ViewModels.LibraryScreenViewModel
import com.example.youtubeclone.data.HistoryVideo

import com.example.youtubeclone.util.Constants.CLIENT_ID
import com.example.youtubeclone.util.Constants.poppins
import com.example.youtubeclone.util.rememberFirebaseAuthLauncher
import com.example.youtubeclone.util.shimmerEffect
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LibraryScreen(channelViewModel: ChannelViewModel = hiltViewModel()) {
    var user by remember { channelViewModel.currentuser }
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
                painter = painterResource(id = R.drawable.library),
                contentDescription = "",
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("Don’t miss new videos", fontFamily = poppins, fontWeight = FontWeight.ExtraBold)
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
            ShowHistory(true)
//            ShowHistory(false)
        }

    }

}

@Composable
fun ShowHistory(ishistory: Boolean, viewModel: LibraryScreenViewModel = hiltViewModel()) {
    val res by  viewModel.history.collectAsState()
    var isLoading by viewModel.isLoading

    Column(modifier = Modifier.padding(top = 30.dp, start = 20.dp)) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.baseline_watch_later_24),
                contentDescription = ""
            )
            Text(text = if (ishistory) "history" else "saved", fontFamily = poppins, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        LazyRow(modifier = Modifier.padding(top = 10.dp)) {
            if (isLoading) {
                items(3) {
                    HistoryCardLoad()
                    Spacer(modifier = Modifier.width(10.dp))
                }
            } else {
                items(res.size) {

                    HistoryCard(res[res.size-1-it])
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: HistoryVideo) {
    Column(modifier = Modifier.width(165.dp)) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.videothumbnail)
                .crossfade(true)
                .build(), contentDescription = "",
            modifier = Modifier
                .height(105.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        )
        Text(
            text = item.videotittle,
            fontSize = 12.sp,
            fontFamily = poppins,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
        Text(
            text = item.channelname,
            fontFamily = poppins,
            fontSize = 8.sp,
            fontWeight = FontWeight.ExtraLight,
            maxLines = 1
        )
    }
}

@Composable
fun HistoryCardLoad() {
    Column(modifier = Modifier.width(165.dp)) {
        Box(
            modifier = Modifier
                .height(105.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(
            modifier = Modifier
                .width(140.dp)
                .height(8.dp)
                .shimmerEffect()
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Spacer(
            modifier = Modifier
                .width(100.dp)
                .height(4.dp)
                .shimmerEffect()
        )
    }

}


