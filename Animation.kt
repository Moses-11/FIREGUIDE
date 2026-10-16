package com.example.fireguide

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi

// ================================================================
// FIRE GUIDE COLOURS
// ================================================================


private val FireOrange = Color(0xFFFF4B16)
private val FireRed = Color(0xFFFF3D1F)
private val FireDark = Color(0xFF10233D)
private val FireGray = Color(0xFF718096)
private val SoftOrange = Color(0xFFFFF0E5)
private val VerySoftOrange = Color(0xFFFFF8F4)
private val CardBorder = Color(0xFFECEFF2)
private val SoftBlue = Color(0xFFEFF5FA)

// ================================================================
// ACTIVITY
// ================================================================

class AnimationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FIREGUIDETheme {
                AnimationScreen(
                    onBackToHome = {
                        startActivity(
                            Intent(this, HomepageActivity::class.java)
                        )
                    }
                )
            }
        }
    }
}

// ================================================================
// DATA
// ================================================================

enum class AnimationCategory {
    ALL,
    HOME,
    SCHOOL,
    PUBLIC,
    EMERGENCY
}

data class AnimationLesson(
    val title: String,
    val description: String,
    val duration: String,
    val imageRes: Int,
    val category: AnimationCategory
)

private val animationLessons = listOf(
    AnimationLesson(
        title = "Using a Fire Extinguisher",
        description = "Learn the correct PASS method for using a fire extinguisher.",
        duration = "00:45",
        imageRes = R.drawable.fighter3,
        category = AnimationCategory.ALL
    ),
    AnimationLesson(
        title = "Emergency Evacuation",
        description = "Learn how to evacuate safely during a fire emergency.",
        duration = "01:09",
        imageRes = R.drawable.fighter3,
        category = AnimationCategory.ALL
    ),
    AnimationLesson(
        title = "Kitchen Fire Safety",
        description = "Learn how to prevent and respond to common kitchen fires.",
        duration = "01:50",
        imageRes = R.drawable.fighter3,
        category = AnimationCategory.HOME
    ),
    AnimationLesson(
        title = "Electrical Fire Safety",
        description = "Understand electrical fire risks and safer responses.",
        duration = "02:10",
        imageRes = R.drawable.fighter3,
        category = AnimationCategory.HOME
    ),
    AnimationLesson(
        title = "Fire Safety at School",
        description = "Learn important evacuation and classroom safety procedures.",
        duration = "02:55",
        imageRes = R.drawable.fighter3,
        category = AnimationCategory.SCHOOL
    ),
    AnimationLesson(
        title = "Fire Safety in Public Places",
        description = "Learn what to look for when entering public buildings.",
        duration = "02:35",
        imageRes = R.drawable.fighter3,
        category = AnimationCategory.PUBLIC
    ),
    AnimationLesson(
        title = "First Aid for Burns",
        description = "Learn basic first-aid steps for minor burns.",
        duration = "03:15",
        imageRes = R.drawable.fighter3,
        category = AnimationCategory.EMERGENCY
    )
)

// ================================================================
// MAIN SCREEN
// ================================================================

@Composable
fun AnimationScreen(
    onBackToHome: () -> Unit = {},
    onAnimationClick: (AnimationLesson) -> Unit = {}
) {

    var selectedCategory by remember {
        mutableStateOf(AnimationCategory.ALL)
    }

    var selectedVideo by remember {
        mutableStateOf<AnimationLesson?>(null)
    }

    val filteredLessons = remember(selectedCategory) {
        if (selectedCategory == AnimationCategory.ALL) {
            animationLessons
        } else {
            animationLessons.filter {
                it.category == selectedCategory
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ------------------------------------------------------------
        // TOP HEADER
        // ------------------------------------------------------------

        AnimationHeader(
            onBackToHome = onBackToHome
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // --------------------------------------------------------
            // HERO
            // --------------------------------------------------------

            AnimationHero(
                onStartWatching = {
                    onAnimationClick(animationLessons.first())
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --------------------------------------------------------
            // CATEGORY TITLE
            // --------------------------------------------------------

            SectionTitle(
                title = "Animation Categories",
                actionText = "See All",
                onActionClick = {
                    selectedCategory = AnimationCategory.ALL
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --------------------------------------------------------
            // CATEGORIES
            // --------------------------------------------------------

            LazyRow(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 20.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    listOf(
                        Triple(
                            AnimationCategory.ALL,
                            "All",
                            Icons.Outlined.LocalFireDepartment
                        ),
                        Triple(
                            AnimationCategory.HOME,
                            "At Home",
                            Icons.Outlined.Home
                        ),
                        Triple(
                            AnimationCategory.SCHOOL,
                            "At School",
                            Icons.Outlined.School
                        ),
                        Triple(
                            AnimationCategory.PUBLIC,
                            "Public",
                            Icons.Outlined.Business
                        ),
                        Triple(
                            AnimationCategory.EMERGENCY,
                            "Emergency",
                            Icons.Outlined.Warning
                        )
                    )
                ) { item ->

                    CategoryCard(
                        title = item.second,
                        icon = item.third,
                        selected = selectedCategory == item.first,
                        onClick = {
                            selectedCategory = item.first
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // --------------------------------------------------------
            // FEATURED
            // --------------------------------------------------------

            SectionTitle(
                title = "Featured Animations",
                actionText = "See All",
                onActionClick = {
                    selectedCategory = AnimationCategory.ALL
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredLessons.isNotEmpty()) {

                val featuredLessons = filteredLessons.take(2)

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    featuredLessons.forEach { lesson ->

                        LargeAnimationCard(
                            lesson = lesson,
                            onClick = {
                                if (
                                    lesson.title == "Emergency Evacuation" ||
                                    lesson.title == "Using a Fire Extinguisher" ||
                                    lesson.title == "Fire Safety at School"
                                ) {
                                    selectedVideo = lesson
                                } else {
                                    onAnimationClick(lesson)
                                }
                            },
                            onPlayClick = {
                                if (
                                    lesson.title == "Emergency Evacuation" ||
                                    lesson.title == "Using a Fire Extinguisher" ||
                                    lesson.title == "Fire Safety at School"
                                ) {
                                    selectedVideo = lesson
                                } else {
                                    onAnimationClick(lesson)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // --------------------------------------------------------
            // MORE ANIMATIONS
            // --------------------------------------------------------

            SectionTitle(
                title = "More Animations",
                actionText = "See All",
                onActionClick = {
                    selectedCategory = AnimationCategory.ALL
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 20.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(filteredLessons.drop(2)) { lesson ->

                    SmallAnimationCard(
                        lesson = lesson,
                        onClick = {
                            if (
                                lesson.title == "Emergency Evacuation" ||
                                lesson.title == "Using a Fire Extinguisher" ||
                                lesson.title == "Fire Safety at School"
                            ) {
                                selectedVideo = lesson
                            } else {
                                onAnimationClick(lesson)
                            }
                        },
                        onPlayClick = {
                            if (
                                lesson.title == "Emergency Evacuation" ||
                                lesson.title == "Using a Fire Extinguisher" ||
                                lesson.title == "Fire Safety at School"
                            ) {
                                selectedVideo = lesson
                            } else {
                                onAnimationClick(lesson)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // --------------------------------------------------------
            // LEARNING TIP
            // --------------------------------------------------------

            LearningTipCard()

            Spacer(modifier = Modifier.height(25.dp))

            // --------------------------------------------------------
            // BACK TO HOME
            // --------------------------------------------------------

            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(58.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = FireOrange
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.5.dp,
                    color = FireOrange
                )
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back to Home"
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Back to Home",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    selectedVideo?.let { lesson ->
        AnimationVideoDialog(
            lesson = lesson,
            onDismiss = { selectedVideo = null }
        )
    }
}

// ================================================================
// HEADER
// ================================================================

@Composable
private fun AnimationHeader(
    onBackToHome: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onBackToHome,
            modifier = Modifier.size(46.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = FireDark,
                modifier = Modifier.size(27.dp)
            )
        }

        Image(
            painter = painterResource(R.drawable.fire_logo),
            contentDescription = "Fire Guide",
            modifier = Modifier.size(55.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row {

                Text(
                    text = "FIRE",
                    color = FireDark,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = " GUIDE",
                    color = FireOrange,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "LEARN • PREPARE • STAY SAFE",
                color = FireGray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.6.sp
            )
        }

        Box(
            modifier = Modifier.size(45.dp),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.NotificationsNone,
                contentDescription = "Notifications",
                tint = FireDark,
                modifier = Modifier.size(28.dp)
            )


        }
    }
}

// ================================================================
// HERO
// ================================================================

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun AnimationHero(
    onStartWatching: () -> Unit
) {

    val infiniteTransition = rememberInfiniteTransition(
        label = "hero_animation"
    )

    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating_firefighter"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(285.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFFF1E9),
                        Color(0xFFFFD7C0),
                        Color(0xFFFFA66F)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .align(Alignment.TopCenter)
                .padding(
                    start = 1.dp,
                    top = 1.dp,

                    )
        ) {

            Text(
                text = "3D Animated",
                color = FireDark,
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Fire Safety",
                color = FireOrange,
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Watch and learn through interactive 3D animations and real-life scenarios.",
                color = FireGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(15.dp))


        }

        Image(
            painter = painterResource(R.drawable.fighter3),
            contentDescription = "Firefighter",
            modifier = Modifier
                .fillMaxHeight(0.40f)
                .fillMaxWidth(0.70f)
                .align(Alignment.BottomCenter)
                .offset(y = floatingOffset.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

// ================================================================
// ANIMATED PLAY ICON
// ================================================================



// ================================================================
// CATEGORY CARD
// ================================================================

@Composable
private fun CategoryCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {

    val animatedScale by animateFloatAsState(
        targetValue = if (selected) 1.03f else 1f,
        animationSpec = tween(180),
        label = "category_scale"
    )

    Column(
        modifier = Modifier
            .width(112.dp)
            .height(130.dp)
            .scale(animatedScale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) VerySoftOrange else Color.White
            )
            .border(
                width = if (selected) 1.8.dp else 1.dp,
                color = if (selected) FireOrange else CardBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(
                    if (selected) FireOrange else SoftBlue
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (selected) Color.White else FireDark,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = title,
            color = if (selected) FireOrange else FireDark,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ================================================================
// SECTION TITLE
// ================================================================

@Composable
private fun SectionTitle(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            color = FireDark,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = actionText,
            color = FireOrange,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onActionClick() }
                .padding(5.dp)
        )

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = FireOrange,
            modifier = Modifier.size(21.dp)
        )
    }
}

// ================================================================
// LARGE ANIMATION CARD
// ================================================================

@Composable
private fun LargeAnimationCard(
    lesson: AnimationLesson,
    onClick: () -> Unit,
    onPlayClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() },
        color = Color.White,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            CardBorder
        )
    ) {

        Column {

            AnimationThumbnail(
                lesson = lesson,
                height = 205.dp,
                onPlayClick = onPlayClick
            )

            Column(
                modifier = Modifier.padding(15.dp)
            ) {

                Text(
                    text = lesson.title,
                    color = FireDark,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = lesson.description,
                    color = FireGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ================================================================
// SMALL ANIMATION CARD
// ================================================================

@Composable
private fun SmallAnimationCard(
    lesson: AnimationLesson,
    onClick: () -> Unit,
    onPlayClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .width(190.dp)
            .clickable { onClick() }
    ) {

        AnimationThumbnail(
            lesson = lesson,
            height = 120.dp,
            onPlayClick = onPlayClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = lesson.title,
            color = FireDark,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = lesson.description,
            color = FireGray,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ================================================================
// THUMBNAIL
// ================================================================

@Composable
private fun AnimationThumbnail(
    lesson: AnimationLesson,
    height: androidx.compose.ui.unit.Dp,
    onPlayClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(20.dp))
            .background(SoftOrange)
    ) {

        Image(
            painter = painterResource(lesson.imageRes),
            contentDescription = lesson.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay behind controls
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .size(52.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.68f))
                .clickable { onPlayClick() },
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.PlayArrow,
                contentDescription = "Play ${lesson.title}",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        // Duration badge
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color.Black.copy(alpha = 0.72f))
                .padding(
                    horizontal = 9.dp,
                    vertical = 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = "Duration",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = lesson.duration,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ================================================================
// IN-APP VIDEO PLAYER
// ================================================================

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
private fun AnimationVideoDialog(
    lesson: AnimationLesson,
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val exoPlayer = remember(lesson.title) {
        ExoPlayer.Builder(context).build().apply {

            val videoResource = when (lesson.title) {
                "Using a Fire Extinguisher" ->
                    R.raw.using_fireextinguisher

                "Emergency Evacuation" ->
                    R.raw.emergency_evacuation

                "Fire Safety at School" ->
                    R.raw.school_firesafety

                else ->
                    R.raw.emergency_evacuation
            }

            val videoUri = "android.resource://${context.packageName}/$videoResource".toUri()

            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }
    }

    androidx.compose.runtime.DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(lesson.title, color = FireDark, fontWeight = FontWeight.Bold) },
        text = {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                        setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = FireOrange)
            ) { Text("Close") }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}

// ================================================================
// LEARNING TIP
// ================================================================

@Composable
private fun LearningTipCard() {

    val infiniteTransition = rememberInfiniteTransition(
        label = "tip_icon"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tip_rotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(SoftOrange)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .graphicsLayer {
                    rotationZ = rotation
                }
                .clip(CircleShape)
                .background(Color(0xFFFFE0B8)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = "Safety learning tip",
                tint = FireOrange,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Learn Before an Emergency",
                color = FireOrange,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Watch the animations and practise the steps so you know what to do when it matters.",
                color = FireDark,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}

// ================================================================
// PREVIEW
// ================================================================

@Preview(
    name = "Fire Guide Animation Page",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440"
)
@Composable
private fun AnimationScreenPreview() {

    FIREGUIDETheme {
        AnimationScreen()
    }
}