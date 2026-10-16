package com.example.fireguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FireExtinguisher
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme

private val FireOrange = Color(0xFFFF4B16)
private val FireDark = Color(0xFF10233D)
private val FireGray = Color(0xFF718096)
private val SoftOrange = Color(0xFFFFF1EB)
private val SoftRed = Color(0xFFFFE8E3)
private val Background = Color(0xFFF8FAFC)

class EmergencyGuideActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FIREGUIDETheme {
                EmergencyGuideScreen(
                    onBackHome = {
                        startActivity(
                            Intent(this, HomepageActivity::class.java)
                        )
                        finish()
                    }
                )
            }
        }
    }
}

private data class EmergencyStep(
    val number: Int,
    val title: String,
    val description: String
)

@Composable
fun EmergencyGuideScreen(
    onBackHome: () -> Unit
) {
    var selectedStep by remember { mutableStateOf<Int?>(null) }

    val steps = listOf(
        EmergencyStep(
            1,
            "Stay Calm",
            "Do not panic. Stay alert and prepare to leave the building immediately."
        ),
        EmergencyStep(
            2,
            "Raise the Alarm",
            "Activate the fire alarm or alert people nearby if you discover a fire."
        ),
        EmergencyStep(
            3,
            "Leave Immediately",
            "Use the nearest safe exit. Do not delay to collect personal belongings."
        ),
        EmergencyStep(
            4,
            "Stay Low in Smoke",
            "If smoke is present, stay as low as possible and move toward a safe exit."
        ),
        EmergencyStep(
            5,
            "Never Use the Lift",
            "Use stairs during a fire emergency. Lifts may stop working or open onto a dangerous floor."
        ),
        EmergencyStep(
            6,
            "Go to the Assembly Point",
            "Move to the designated assembly area and remain there so everyone can be accounted for."
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            EmergencyGuideHeader(
                onBackHome = onBackHome
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Emergency Guide",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FireDark
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Know what to do when a fire emergency occurs.",
                        fontSize = 15.sp,
                        color = FireGray,
                        lineHeight = 21.sp
                    )
                }

                item {
                    EmergencyAlertCard()
                }

                item {
                    Text(
                        text = "What To Do",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = FireDark
                    )
                }

                items(steps) { step ->
                    EmergencyStepCard(
                        step = step,
                        expanded = selectedStep == step.number,
                        onClick = {
                            selectedStep =
                                if (selectedStep == step.number) {
                                    null
                                } else {
                                    step.number
                                }
                        }
                    )
                }

                item {
                    DoNotCard()
                }

                item {
                    EmergencyCallCard()
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "FIRE GUIDE • Stay Alert • Stay Safe",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = FireGray
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }
}

@Composable
private fun EmergencyGuideHeader(
    onBackHome: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackHome) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back to Home",
                tint = FireDark
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Fire Guide",
                tint = FireOrange,
                modifier = Modifier.size(27.dp)
            )

            Spacer(modifier = Modifier.size(7.dp))

            Text(
                text = "FIRE GUIDE",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FireDark
            )
        }
    }
}

@Composable
private fun EmergencyAlertCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = FireOrange
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column {
                Text(
                    text = "FIRE EMERGENCY",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Alert others and evacuate immediately.",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.95f),
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
private fun EmergencyStepCard(
    step: EmergencyStep,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(SoftOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = step.number.toString(),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FireOrange
                    )
                }

                Spacer(modifier = Modifier.size(13.dp))

                Text(
                    text = step.title,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = FireDark
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open ${step.title}",
                    tint = FireGray
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = step.description,
                    fontSize = 14.sp,
                    color = FireGray,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(
                        start = 55.dp,
                        end = 8.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun DoNotCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftRed
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FireExtinguisher,
                    contentDescription = "Fire extinguisher",
                    tint = FireOrange,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = "Things You Must NOT Do",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = FireDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val warnings = listOf(
                "Do not panic or push others.",
                "Do not go back inside a burning building.",
                "Do not use lifts during evacuation.",
                "Do not hide in rooms, toilets or cupboards.",
                "Do not return for personal belongings."
            )

            warnings.forEach { warning ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = FireOrange
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = warning,
                        fontSize = 14.sp,
                        color = FireDark,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun EmergencyCallCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = FireDark
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Sos,
                contentDescription = "Emergency",
                tint = FireOrange,
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.size(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Need Emergency Help?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Contact the appropriate emergency service immediately.",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    lineHeight = 18.sp
                )
            }

            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call emergency services",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Preview(
    name = "Emergency Guide Preview",
    showBackground = true,
    backgroundColor = 0xFFF8FAFC,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun EmergencyGuidePreview() {
    FIREGUIDETheme {
        EmergencyGuideScreen(
            onBackHome = {}
        )
    }
}