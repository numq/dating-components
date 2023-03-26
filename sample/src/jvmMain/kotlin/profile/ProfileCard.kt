package profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.skia.Image

@Composable
fun ProfileCard(profile: Profile) {
    Image.makeFromEncoded(profile.bytes).runCatching {
        Image(bitmap = toComposeImageBitmap(),
            contentDescription = "profile",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)
    }.getOrNull() ?: CircularProgressIndicator()
}