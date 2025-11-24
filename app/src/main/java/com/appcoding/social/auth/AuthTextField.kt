package com.appcoding.social.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens

@Composable
fun AuthTextField(
    value : String,
    onValueChanged : (String) -> Unit,
    placeHolder : String,
    type : String){

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChanged ,
        shape = RoundedCornerShape(Dimens.textfield_corner),
        placeholder = { Text(text = placeHolder) },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodySmall,
        colors = TextFieldDefaults.colors(
            focusedPlaceholderColor = Colors.outline_textfield_border,
            unfocusedPlaceholderColor = Colors.outline_textfield_border,
            focusedIndicatorColor = Colors.outline_textfield_border,
            unfocusedIndicatorColor = Colors.outline_textfield_border
        ),
        visualTransformation =
        if(type == "password")
            PasswordVisualTransformation()
        else
        VisualTransformation.None

    )

}