package com.appcoding.social.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens

@Composable
fun AuthButton(
    text : String,
    isLoading : Boolean,
    onClick : () -> Unit
){
    Button(
        modifier = Modifier
            .height(Dimens.signup_button_height)
            .fillMaxWidth(),
        onClick = { onClick },
        shape = RoundedCornerShape(Dimens.textfield_corner),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Colors.appcolor

        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}