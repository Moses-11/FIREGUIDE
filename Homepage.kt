package com.example.fireguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme

// ================================================================
// FIRE GUIDE COLOURS
// ================================================================

private val FireOrange = Color(0xFFFF4B16)
private val FireDark = Color(0xFF10233D)
private val FireGray = Color(0xFF718096)
private val SoftOrange = Color(0xFFFFF0E5)
private val SoftOrange2 = Color(0xFFFFE2CF)
private val CardBorder = Color(0xFFF0F0F0)

// ================================================================
// HOMEPAGE ACTIVITY
// ================================================================

class HomepageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FIREGUIDETheme {
                FireGuideHomeScreen(
                    onFeedbackClick = {
                        startActivity(Intent(this, FeedbackActivity::class.java))
                    },
                    onAnimationClick = {
                        startActivity(Intent(this, AnimationActivity::class.java))
                    },
                    onEmergencyGuideClick = {
                        startActivity(Intent(this, EmergencyGuideActivity::class.java))
                    },
                    onSettingsClick = {
                        startActivity(Intent(this, SettingsActivity::class.java))
                    }
                )
            }
        }
    }
}

// ================================================================
// HOMEPAGE
// ================================================================

@Composable
fun FireGuideHomeScreen(
    onFeedbackClick: () -> Unit = {},
    onAnimationClick: () -> Unit = {},
    onEmergencyGuideClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            FireGuideBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab

                    when (tab) {
                        1 -> onFeedbackClick()
                        2 -> onAnimationClick()
                        3 -> onSettingsClick()
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ====================================================
            // DECORATIVE ORANGE ARC BACKGROUND
            // ====================================================

            HomepageArcBackground()

            // ====================================================
            // HOMEPAGE CONTENT
            // ====================================================

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())

            ) {

                // =================================================
                // HEADER
                // =================================================

                HomeHeader()

                Spacer(modifier = Modifier.height(4.dp))

                // =================================================
                // HERO SECTION
                // =================================================

                HeroSection(
                    onWatchAnimation = onAnimationClick
                )

                // =================================================
                // PAGE INDICATORS
                // =================================================

                PageIndicators()

                // =================================================
                // FEATURE CARDS
                // =================================================

                SectionTitle(
                    title = "Explore Fire Safety",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "3D Animation",
                        description = "Watch interactive fire safety scenarios.",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        },
                        onClick = onAnimationClick
                    )

                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "Emergency Guide",
                        description = "What to do before, during and after a fire.",
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        },
                        onClick = onEmergencyGuideClick
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // =================================================
                // SAFETY TIP
                // =================================================

                SafetyTipCard(
                    onClick = {
                        // Safety-tip details can be connected later.
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ================================================================
// DECORATIVE HOMEPAGE ARC BACKGROUND
// ================================================================

@Composable
private fun HomepageArcBackground() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .size(270.dp)
                .offset(
                    x = (-85).dp,
                    y = (-60).dp
                )
                .clip(CircleShape)
                .background(
                    FireOrange.copy(alpha = 0.08f)
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(270.dp)
                .offset(
                    x = 80.dp,
                    y = 70.dp
                )
                .clip(CircleShape)
                .background(
                    FireOrange.copy(alpha = 0.10f)
                )
        )

    }
}

// ================================================================
// HEADER
// ================================================================

@Composable
private fun HomeHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 18.dp,
                end = 18.dp,
                top = 14.dp,
                bottom = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(R.drawable.fire_logo),
            contentDescription = "Fire Guide Logo",
            modifier = Modifier.size(56.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = "FIRE",
                    color = FireDark,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = " GUIDE",
                    color = FireOrange,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Text(
                text = "LEARN • PREPARE • STAY SAFE",
                color = FireGray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.7.sp
            )
        }

        // Notifications


        // Profile
        
    }
}

// ================================================================
// HERO SECTION
// ================================================================

@Composable
private fun HeroSection(
    onWatchAnimation: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(390.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(FireOrange)
    ) {

        // Decorative orange panel behind the firefighter.
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.52f)
                .align(Alignment.CenterEnd)
                .clip(
                    RoundedCornerShape(
                        topStart = 150.dp,
                        bottomStart = 150.dp
                    )
                )


        )

        ImageWithFirefighter()

        Column(
            modifier = Modifier
                .fillMaxWidth(0.60f)
                .align(Alignment.TopStart)
                .padding(
                    start = 22.dp,
                    top = 48.dp,
                    end = 4.dp
                )
        ) {

            Text(
                text = "WELCOME TO",
                color = FireGray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row {

                Text(
                    text = "FIRE",
                    color = FireDark,
                    fontSize = 31.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = " GUIDE",
                    color = FireGray,
                    fontSize = 31.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Learn Fire Safety.\nBuild a Safer Tomorrow.",
                color = FireDark,
                fontSize = 18.sp,
                lineHeight = 25.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))


        }
    }
}

// ================================================================
// FIREFIGHTER IMAGE
// ================================================================

@Composable
private fun BoxScope.ImageWithFirefighter() {

    Image(
        painter = painterResource(R.drawable.firefighter),
        contentDescription = "Firefighter",
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.58f)
            .align(Alignment.CenterEnd)
            .clip(
                RoundedCornerShape(
                    topStart = 150.dp,
                    bottomStart = 150.dp
                )
            ),
        contentScale = ContentScale.Crop
    )
}

// ================================================================
// PAGE INDICATORS
// ================================================================

@Composable
private fun PageIndicators() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 12.dp,
                bottom = 16.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(5) { index ->

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(
                        width = if (index == 0) 22.dp else 10.dp,
                        height = 10.dp
                    )
                    .clip(CircleShape)
                    .background(
                        if (index == 0) {
                            FireOrange
                        } else {
                            Color(0xFFDCE1E6)
                        }
                    )
            )
        }
    }
}

// ================================================================
// SECTION TITLE
// ================================================================

@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {

    Text(
        text = title,
        color = FireDark,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

// ================================================================
// FEATURE CARD
// ================================================================

@Composable
private fun FeatureCard(
    modifier: Modifier,
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(210.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            1.dp,
            CardBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(FireOrange),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = title,
                    color = FireDark,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = FireDark,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = description,
                color = FireGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

// ================================================================
// SAFETY TIP
// ================================================================

@Composable
private fun SafetyTipCard(
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftOrange
        ),
        border = BorderStroke(
            1.dp,
            SoftOrange2
        )
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 18.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE0B8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = "Safety Tip",
                    tint = FireOrange,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Safety Tip of the Day",
                    color = FireOrange,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Always know your nearest exit and keep corridors clear.",
                    color = FireDark,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = FireDark,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

// ================================================================
// BOTTOM NAVIGATION
// ================================================================

@Composable
private fun FireGuideBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {

        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            colors = navigationColors()
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Feedback"
                )
            },
            label = { Text("Feedback") },
            colors = navigationColors()
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = "Animation"
                )
            },
            label = { Text("Animation") },
            colors = navigationColors()
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            },
            label = { Text("Settings") },
            colors = navigationColors()
        )
    }
}

// ================================================================
// NAVIGATION COLORS
// ================================================================

@Composable
private fun navigationColors() =
    NavigationBarItemDefaults.colors(
        selectedIconColor = FireOrange,
        selectedTextColor = FireOrange,
        indicatorColor = Color.Transparent,
        unselectedIconColor = FireGray,
        unselectedTextColor = FireGray
    )

// ================================================================
// PREVIEW
// ================================================================

@Preview(
    name = "Fire Guide Homepage",
    device = "spec:width=393dp,height=852dp,dpi=440",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun HomepagePreview() {
    FIREGUIDETheme {
        FireGuideHomeScreen()
    }
}