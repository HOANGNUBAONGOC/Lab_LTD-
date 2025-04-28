package com.example.businesscardapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

@Composable
fun Businesscard() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.nen),
            contentDescription = null
        )
        Column (modifier = Modifier.fillMaxSize()
            .padding(top = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            BasicInfo()
            Spacer(modifier = Modifier.height(32.dp))
            ContactInfo()
        }
    }
}

@Composable
fun BasicInfo(){
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){

        Image(
            modifier = Modifier
                .size(210.dp)
                .clip(CircleShape)
                .border(3.dp, Color.White, CircleShape),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.anh),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "HOANG NU BAO NGOC",
            color = Color(0xFF6650a4),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            fontFamily = FontFamily.Monospace,
            text = "Future Engineer",
            color = Color.White,
            fontSize = 22.sp
        )
    }
}

@Composable
fun ContactInfo(){
    Column {
        ContactItem(icon = Icons.Default.Call, text = "+84 775457834")
        ContactItem(icon = Icons.Default.Email, text = "ngochnb.23ns@vku.udn.vn")
        ContactItem(icon = Icons.Default.LocationOn, text = "VietNam, Asia, Earth")

    }
}

@Composable
fun ContactItem(icon: ImageVector, text: String){
    Row{
        Icon(
            modifier = Modifier
                .size(48.dp)
                .padding(top = 16.dp),
            imageVector = icon,
            tint = Color.White,
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(
                start = 12.dp,
                top = 16.dp),
            text = text,
            color = Color.White,
            fontSize = 24.sp
            )
    }
}

@Preview(showBackground = true)
@Composable
fun BusinesscardPreview() {
    Businesscard()
}
