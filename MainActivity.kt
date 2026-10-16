package com.example.fireguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme

// ================================================================
// FIRE GUIDE COLOURS
// ================================================================

private val FireOrange = Color(0xFFFF4B16)
private val FireDarkOrange = Color(0xFFF4511E)
private val FireNavy = Color(0xFF10283A)
private val FireGray = Color(0xFF607080)
private val SoftOrange = Color(0xFFFFE6D8)


// ================================================================
// MAIN ACTIVITY
// ================================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FIREGUIDETheme {

                FireGuideWelcomeScreen(
                    onGetStartedClick = {

                        // Open the SignUp page
                        startActivity(
                            Intent(
                                this@MainActivity,
                                SignUp::class.java
                            )
                        )
                    }
                )
            }
        }
    }
}


// ================================================================
// WELCOME SCREEN
// ================================================================

@Composable
fun FireGuideWelcomeScreen(
    onGetStartedClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ========================================================
        // DECORATIVE EDGE GLOW
        // ========================================================

        Box(
            modifier = Modifier
                .size(190.dp)
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
                .size(210.dp)
                .offset(
                    x = 80.dp,
                    y = 70.dp
                )
                .clip(CircleShape)
                .background(
                    FireOrange.copy(alpha = 0.10f)
                )
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(30.dp)
            )


            // ====================================================
            // FIRE LOGO
            // ====================================================

            Image(
                painter = painterResource(
                    id = R.drawable.fire_logo
                ),
                contentDescription = "FireGuide Logo",
                modifier = Modifier.size(82.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )


            // ====================================================
            // FIRE GUIDE NAME
            // ====================================================

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "FIRE",
                    color = FireNavy,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "GUIDE",
                    color = FireDarkOrange,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }


            Text(
                text = "LEARN  •  PREPARE  •  STAY SAFE",
                color = FireGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            // ====================================================
            // MAIN MESSAGE
            // ====================================================

            Text(
                text = "Learn Fire Safety.",
                color = FireNavy,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Build a Safer Tomorrow.",
                color = FireNavy,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            Text(
                text = "Explore interactive 3D animations\n" +
                        "and practical emergency guides\n" +
                        "to stay informed and prepared.",
                color = FireGray,
                fontSize = 17.sp,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // ====================================================
            // FIREFIGHTER HERO AREA
            // ====================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 180.dp,
                            topEnd = 180.dp,
                            bottomStart = 70.dp,
                            bottomEnd = 70.dp
                        )
                    )
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                SoftOrange,
                                FireOrange
                            )
                        )
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.firefighter
                    ),
                    contentDescription = "Firefighter",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(
                modifier = Modifier.height(15.dp)
            )


            // ====================================================
            // PAGE INDICATORS
            // ====================================================

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                PageIndicator(active = true)
                PageIndicator(active = false)
                PageIndicator(active = false)
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // ====================================================
            // GET STARTED BUTTON
            // ====================================================

            Button(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(34.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FireOrange,
                    contentColor = Color.White
                )
            ) {

                Text(
                    text = "Get Started",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = "›",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Light
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // ====================================================
            // FOOTER
            // ====================================================

            Text(
                text = "Together for a Safer Community",
                color = FireGray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}


// ================================================================
// PAGE INDICATOR
// ================================================================

@Composable
private fun PageIndicator(
    active: Boolean
) {

    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .size(
                width = if (active) 24.dp else 10.dp,
                height = 10.dp
            )
            .clip(CircleShape)
            .background(
                if (active) {
                    FireOrange
                } else {
                    Color.LightGray
                }
            )
    )
}


// ================================================================
// ANDROID STUDIO PREVIEW
// ================================================================

@Preview(
    name = "Fire Guide Smartphone",
    device = "spec:width=393dp,height=852dp,dpi=440",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun FireGuideWelcomePreview() {

    FIREGUIDETheme {

        FireGuideWelcomeScreen(
            onGetStartedClick = {}
        )
    }
}