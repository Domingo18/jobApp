package com.example.project_2_cmp431.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**  Toast Message   This composable function displays a toast message.   @param message the message to display /
 *
 */
/**  Loading Spinner   This composable function displays a loading spinner.   It is used to show that the app is loading data. /
 *
 */
@Composable fun LoadingSpinner() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        CircularProgressIndicator(
            modifier =
                Modifier.size(48.dp),
            // Adjust size as needed
            strokeWidth = 4.dp
        )
        // Adjust stroke width as needed )
    }
}

@Composable fun ToastMessage(message: String) {

    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
