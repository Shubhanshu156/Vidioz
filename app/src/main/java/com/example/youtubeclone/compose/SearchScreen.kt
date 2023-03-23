package com.example.youtubeclone.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.youtubeclone.R
import com.example.youtubeclone.ViewModels.SuggestionScreenViewModel

@Composable
fun SearchScreen(navHostController: NavHostController,q:String?) {
    var searchstring =remember{
        if (!q.isNullOrBlank()){
        mutableStateOf(q)}
        else{
            mutableStateOf("")
        }
    }
    ShowSuggestion(navHostController){it->
        searchstring.value=it
    }
    SearchBar(searchstring,navHostController)

}

@Composable
fun SearchBar(
    searString:MutableState<String>,
    navHostController: NavHostController,
    viewModel:SuggestionScreenViewModel = hiltViewModel(),

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {


        LaunchedEffect(key1 = searString.value ){
            viewModel.giveSuggestion(searString.value)
        }
        IconButton(onClick = { navHostController.navigate("home") }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = Modifier.size(30.dp)
            )
        }
        val textStyle = LocalTextStyle.current
        val textHeight = with(LocalDensity.current) { textStyle.fontSize }

        BasicTextField(
            value = searString.value,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (!searString.value.isNullOrEmpty()) navHostController.navigate("home?showchips=${false}&query=${searString.value}")
            }),
            maxLines = 1,
            onValueChange = {
                searString.value=it
            },
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                lineHeight = 20.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(androidx.compose.ui.graphics.Color.DarkGray)
                .weight(1f)
                .padding(horizontal = 10.dp)

                .height(30.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
        if (searString.value.isNotEmpty()) {
            IconButton(onClick = { searString.value = "" }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "")
            }
        }
    }


}


@Composable
fun ShowSuggestion(navHostController: NavHostController,viewModel:SuggestionScreenViewModel=hiltViewModel(),fill:(String) -> Unit) {

    val res by viewModel.responseresult.collectAsState()
    val isLoading by remember {
        viewModel.isLoading
    }
    LazyColumn(modifier = Modifier.padding(top = 60.dp)) {
        items(res.size) {
            SuggestionItem(s = res[it],navHostController,fill)
        }
    }
}

@Composable
fun SuggestionItem(s: String,navHostController: NavHostController, fill: (String) -> Unit) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 20.dp)
            .height(20.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = s,
            maxLines = 2,

            modifier = Modifier.weight(1f).clickable {
                navHostController.navigate("home?showchips=${false}&query=${s}")
            }
        )
        IconButton(onClick = {  fill(s)}) {
            Icon(
                painter = painterResource(id = R.drawable.upper_left_arrow),
                modifier = Modifier
                    .size(30.dp)
                    .padding(start = 10.dp),
                contentDescription = ""
            )
        }

    }
    Spacer(modifier = Modifier.height(30.dp))
}
