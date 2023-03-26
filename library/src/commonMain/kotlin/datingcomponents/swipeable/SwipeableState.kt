package datingcomponents.swipeable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun rememberSwipeableState(
    swipeThreshold: Float = .4f,
    swipeAngle: Float = 25f,
    onLeft: () -> Unit,
    onRight: () -> Unit,
) = remember { SwipeableState(swipeThreshold, swipeAngle, onLeft, onRight) }

class SwipeableState constructor(
    private val swipeThreshold: Float = .4f,
    private val swipeAngle: Float = 25f,
    private val onLeft: () -> Unit,
    private val onRight: () -> Unit,
) {

    internal var rect: Rect by mutableStateOf(Rect.Zero)

    internal var offset: Offset? by mutableStateOf(null)

    internal val alpha: Float by derivedStateOf {
        lerp(abs(offsetPercentage), 0f, swipeThreshold, 0f, 1f).coerceIn(0f, 1f)
    }

    internal val shift: Float by derivedStateOf {
        offset?.run { x.coerceIn(-rect.width, rect.width) } ?: 0f
    }

    internal val scale: Float by derivedStateOf {
        lerp(abs(offsetPercentage), 0f, swipeThreshold, .9f, 1f).coerceIn(.9f, 1f)
    }

    internal val rotation: Float by derivedStateOf {
        offset?.run { (offsetPercentage * swipeAngle).coerceIn(-swipeAngle, swipeAngle) } ?: 0f
    }

    internal val direction by derivedStateOf {
        offset?.run {
            when {
                x < 0 -> SwipeDirection.LEFT
                x > 0 -> SwipeDirection.RIGHT
                else -> SwipeDirection.NONE
            }
        } ?: SwipeDirection.NONE
    }

    internal var isAnimationRunning by mutableStateOf(false)

    internal var isSwipingToLeft by mutableStateOf(false)

    internal var isSwipingToRight by mutableStateOf(false)

    internal fun handleSwipeAction(
        coroutineScope: CoroutineScope,
        direction: SwipeDirection,
        swipeOffset: Offset?,
    ) {
        coroutineScope.launch {

            isAnimationRunning = true

            val swiped = swipeOffset?.run { swipeAvailable(x) } ?: true

            isSwipingToDefault = !swiped
            isSwipingToLeft = swiped && direction == SwipeDirection.LEFT
            isSwipingToRight = swiped && direction == SwipeDirection.RIGHT

            joinAll(
                launch {
                    animatedAlpha.animateTo(if (isSwipingToDefault) 1f else 0f, tween(animationDuration))
                },
                launch {
                    animatedScale.animateTo(if (isSwipingToDefault) .9f else 1f, tween(animationDuration))
                },
                launch {
                    animatedShift.animateTo(
                        if (swiped) when (direction) {
                            SwipeDirection.LEFT -> rect.width * -1.5f
                            SwipeDirection.RIGHT -> rect.width * 1.5f
                            else -> 0f
                        } else 0f, tween(animationDuration)
                    )
                },
                launch {
                    animatedRotation.animateTo(
                        if (swiped) when (direction) {
                            SwipeDirection.LEFT -> swipeAngle * -1.5f
                            SwipeDirection.RIGHT -> swipeAngle * 1.5f
                            else -> 0f
                        } else 0f, tween(animationDuration))
                }
            )

            if (swiped) when (direction) {
                SwipeDirection.LEFT -> onLeft()
                SwipeDirection.RIGHT -> onRight()
                else -> Unit
            }

            isAnimationRunning = false

            isSwipingToDefault = false
            isSwipingToLeft = false
            isSwipingToRight = false
        }
    }

    internal fun handleControlsAction(coroutineScope: CoroutineScope, direction: SwipeDirection) =
        handleSwipeAction(coroutineScope, direction, null)

    internal val animatedAlpha = Animatable(0f)

    internal val animatedShift = Animatable(0f)

    internal val animatedScale = Animatable(0f)

    internal val animatedRotation = Animatable(0f)

    private val animationDuration = 500

    private var isSwipingToDefault by mutableStateOf(false)

    private val offsetPercentage by derivedStateOf { offset?.run { x / rect.width } ?: 0f }

    private fun swipeAvailable(value: Float) = abs(value) / rect.width > swipeThreshold

    private fun lerp(
        value: Float,
        minValue: Float,
        maxValue: Float,
        targetMinValue: Float,
        targetMaxValue: Float,
    ) = (value - minValue) / (maxValue - minValue) * (targetMaxValue - targetMinValue) + targetMinValue
}