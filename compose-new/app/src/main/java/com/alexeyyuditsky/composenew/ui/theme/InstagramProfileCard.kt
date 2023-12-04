package com.alexeyyuditsky.composenew.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexeyyuditsky.composenew.MainViewModel
import com.alexeyyuditsky.composenew.R

@Composable
fun InstagramProfileCard(viewModel: MainViewModel) {
    val isFollowed: State<Boolean> = viewModel.isFollowing.observeAsState(false)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(8.dp),
                    painter = painterResource(id = R.drawable.ic_instagram),
                    contentDescription = null
                )
                UserState("6,950", "Posts")
                UserState("436M", "Followers")
                UserState("76", "Following")
            }
            SimpleBigCursiveText("Instagram")
            SimpleSmallText("#YoursToMake")
            SimpleSmallText("www.facebook.com/emotional_health")
            Button(isFollowed.value) { viewModel.changeFollowingStatus() }
        }
    }
}

@Composable
private fun Button(
    isFollowed: Boolean,
    clickListener: () -> Unit
) {
    Button(
        onClick = { clickListener.invoke() },
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFollowed) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    ) {
        val text = if (isFollowed) {
            stringResource(R.string.unfollow)
        } else {
            stringResource(R.string.follow)
        }
        Text(text = text)
    }
}

@Composable
fun UserState(title: String, value: String) {
    Column(
        modifier = Modifier.height(60.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Cursive,
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SimpleSmallText(title: String) {
    Text(
        text = title,
        fontSize = 14.sp
    )
}

@Composable
fun SimpleBigCursiveText(title: String) {
    Text(
        text = title,
        fontSize = 32.sp,
        fontFamily = FontFamily.Cursive
    )
}