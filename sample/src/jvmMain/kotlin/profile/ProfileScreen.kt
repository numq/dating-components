package profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import datingcomponents.stack.Stack
import datingcomponents.swipeable.rememberSwipeableState
import upload.UploadDialog
import java.util.*

@Composable
fun ProfileScreen() {

    val (uploading, setUploading) = remember { mutableStateOf(false) }

    val profiles = remember { mutableStateListOf<Profile>() }

    if (uploading) {
        UploadDialog(onClose = { setUploading(false) }) { files ->
            profiles.addAll(files.filter { it.extension in arrayOf("png", "jpg", "jpeg") }.map {
                Profile(UUID.randomUUID().toString(), it.bytes)
            })
        }
    }

    val onSwipe: () -> Unit = { profiles.removeFirstOrNull() }

    val swipeableState = rememberSwipeableState(
        onLeft = onSwipe,
        onRight = onSwipe
    )

    Scaffold(Modifier.fillMaxSize()) { paddingValues ->
        Box(
            Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (profiles.isNotEmpty()) Stack(
                Modifier.aspectRatio(9 / 16f).fillMaxHeight(.5f),
                profiles,
                swipeableState
            ) {
                ProfileCard(it)
            }
            else IconButton(onClick = { setUploading(true) }) {
                Icon(Icons.Rounded.Add, "add images")
            }
        }
    }
}
