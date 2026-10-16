package com.example.fireguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme

// ================================================================
// FIRE GUIDE COLOURS
// ================================================================

private val FireOrange = Color(0xFFFF4B16)
private val FireRed = Color(0xFFFF3217)
private val FireDark = Color(0xFF10233D)
private val FireGray = Color(0xFF718096)
private val SoftOrange = Color(0xFFFFF0E5)
private val SoftPeach = Color(0xFFFFE8D8)
private val FieldBorder = Color(0xFFE1E5EA)
private val LightSurface = Color(0xFFF9FAFC)

// ================================================================
// FEEDBACK ACTIVITY
// ================================================================

class FeedbackActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FIREGUIDETheme {
                FeedbackScreen(
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
// FEEDBACK SCREEN
// ================================================================

@Composable fun FeedbackScreen( onBackToHome: () -> Unit = {} ) {

    var selectedType by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showMessageError by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }

    val feedbackTypes = listOf(
        FeedbackType(
            title = "Suggestion",
            icon = Icons.Outlined.Feedback
        ),
        FeedbackType(
            title = "Report Issue",
            icon = Icons.Outlined.ReportProblem
        ),
        FeedbackType(
            title = "App Review",
            icon = Icons.Outlined.ThumbUp
        ),
        FeedbackType(
            title = "General Question",
            icon = Icons.AutoMirrored.Outlined.HelpOutline
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.White,
                        Color(0xFFFFFBF8)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 28.dp
                )
        ) {

            // ========================================================
            // HEADER
            // ========================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterEnd as Alignment.Vertical
            ) {

                Image(
                    painter = painterResource(R.drawable.fire_logo),
                    contentDescription = "Fire Guide Logo",
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "FIRE",
                            color = FireDark,
                            fontSize = 29.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = " GUIDE",
                            color = FireOrange,
                            fontSize = 29.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Text(
                        text = "LEARN • PREPARE • STAY SAFE",
                        color = FireGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.7.sp
                    )
                }

                // Notification icon
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {

                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = FireDark,
                            modifier = Modifier.size(29.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(FireOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "3",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(3.dp))

                // Profile icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(FireOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))


            // ========================================================
            // HERO / INTRODUCTION
            // ========================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(205.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                SoftPeach,
                                Color(0xFFFFDCC7)
                            )
                        )
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.62f)
                        .padding(
                            start = 24.dp,
                            top = 25.dp
                        )
                ) {

                    Text(
                        text = "We Value",
                        color = FireDark,
                        fontSize = 31.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Your Feedback",
                        color = FireRed,
                        fontSize = 31.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(9.dp))

                    Text(
                        text = "Your feedback helps us improve Fire Guide and make it safer and more useful for everyone.",
                        color = FireGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                Image(
                    painter = painterResource(R.drawable.fighter2),
                    contentDescription = "Friendly firefighter",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.52f)
                        .align(Alignment.BottomEnd),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            // ========================================================
            // FEEDBACK TYPE
            // ========================================================

            Text(
                text = "Feedback Type",
                color = FireDark,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 2.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                feedbackTypes.forEachIndexed { index, type ->

                    FeedbackTypeCard(
                        type = type,
                        selected = selectedType == index,
                        onClick = {
                            selectedType = index
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            // ========================================================
            // MESSAGE
            // ========================================================

            Text(
                text = "Your Message",
                color = FireDark,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        if (it.length <= 500) {
                            message = it
                            showMessageError = false
                            submitted = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp),
                    placeholder = {
                        Text(
                            text = "Write your feedback, suggestion or issue here...",
                            color = FireGray.copy(alpha = 0.75f),
                            fontSize = 16.sp
                        )
                    },
                    isError = showMessageError,
                    shape = RoundedCornerShape(18.dp),
                    colors = feedbackTextFieldColors(),
                    maxLines = 7
                )

                Text(
                    text = "${message.length}/500",
                    color = FireGray,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 16.dp,
                            bottom = 11.dp
                        )
                )
            }

            if (showMessageError) {
                Text(
                    text = "Please enter your feedback before sending.",
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        top = 5.dp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            // ========================================================
            // EMAIL
            // ========================================================

            Text(
                text = "Email (Optional)",
                color = FireDark,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(9.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    submitted = false
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email",
                        tint = FireGray
                    )
                },
                placeholder = {
                    Text(
                        text = "your@email.com",
                        color = FireGray.copy(alpha = 0.75f)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(18.dp),
                colors = feedbackTextFieldColors()
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "We'll contact you if we need more details.",
                color = FireGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(22.dp))


            // ========================================================
            // SUCCESS MESSAGE
            // ========================================================

            if (submitted) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFEAF8EE))
                        .padding(14.dp)
                ) {

                    Text(
                        text = "Thank you! Your feedback has been recorded.",
                        color = Color(0xFF237A3B),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }


            // ========================================================
            // SEND FEEDBACK
            // ========================================================

            Button(
                onClick = {
                    if (message.isBlank()) {
                        showMessageError = true
                        submitted = false
                    } else {
                        showMessageError = false
                        submitted = true

                        // Later this can be connected to Firebase/Firestore.
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                shape = RoundedCornerShape(31.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FireOrange,
                    contentColor = Color.White
                )
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(27.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Send Feedback",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            // ========================================================
            // BACK TO HOME
            // ========================================================

            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                shape = RoundedCornerShape(31.dp),
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
                    contentDescription = "Back to Home",
                    modifier = Modifier.size(27.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Back to Home",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}

// ================================================================
// FEEDBACK TYPE MODEL
// ================================================================

private data class
FeedbackType( val title: String,
              val icon: androidx.compose.ui.graphics.vector.ImageVector
)

// ================================================================
// FEEDBACK TYPE CARD
// ================================================================

@Composable private fun FeedbackTypeCard( type: FeedbackType, selected:
Boolean, onClick: () -> Unit, modifier: Modifier = Modifier ) {

    val backgroundColor =
        if (selected) SoftOrange else LightSurface

    val borderColor =
        if (selected) FireOrange else FieldBorder

    val iconColor =
        if (selected) FireOrange else FireDark

    Column(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor)
            .border(
                width = if (selected) 1.8.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(
                horizontal = 5.dp,
                vertical = 13.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(
                    if (selected)
                        FireOrange
                    else
                        Color.White
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = type.icon,
                contentDescription = type.title,
                tint = if (selected) Color.White else iconColor,
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = type.title,
            color = if (selected) FireOrange else FireDark,
            fontSize = 13.sp,
            fontWeight = if (selected)
                FontWeight.Bold
            else
                FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 17.sp
        )
    }

}

// ================================================================
// TEXT FIELD COLOURS
// ================================================================

@Composable private fun feedbackTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = FireOrange,
        unfocusedBorderColor = FieldBorder,
        errorBorderColor = Color.Red,
        focusedTextColor = FireDark,
        unfocusedTextColor = FireDark,
        cursorColor = FireOrange,
        focusedPlaceholderColor = FireGray.copy(alpha = 0.65f),
        unfocusedPlaceholderColor = FireGray.copy(alpha = 0.65f),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

// ================================================================
// PREVIEW
// ================================================================

@Preview(
    name = "Fire Guide Feedback",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440"
)
@Composable
private fun FeedbackPreview()
{ FIREGUIDETheme {
    FeedbackScreen()
   }
}