package datingcomponents.swipeable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun SwipeControls(width: Int, state: SwipeableState) = with(state) {

    val coroutineScope = rememberCoroutineScope()

    Row(
        Modifier.width(width.dp).padding(32.dp).graphicsLayer {
            alpha = if (isAnimationRunning || shift != 0f) 0f else 1f
        },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(modifier = Modifier.clip(CircleShape).clickable {
            handleControlsAction(coroutineScope, SwipeDirection.LEFT)
        }) {
            Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Clear, "dislike", Modifier.fillMaxSize(.5f))
            }
        }
        Card(modifier = Modifier.clip(CircleShape).clickable {
            handleControlsAction(coroutineScope, SwipeDirection.RIGHT)
        }) {
            Box(Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Favorite, "like", Modifier.fillMaxSize(.5f))
            }
        }
    }
}