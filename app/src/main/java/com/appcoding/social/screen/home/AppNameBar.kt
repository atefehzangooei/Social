package com.appcoding.social.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.appcoding.social.R
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens

@Composable
fun AppNameBar() {

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(Dimens.normal_padding)
        .background(Colors.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {
        Text(text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(painter = painterResource(R.drawable.like),
                contentDescription = "heart",
                modifier = Modifier
                    .size(20.dp))

            Spacer(modifier = Modifier.size(10.dp))
        }

    }
}
