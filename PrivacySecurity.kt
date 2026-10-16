package com.example.fireguide

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FGOrange = Color(0xFFFF4B18)
private val FGNavy = Color(0xFF10283A)
private val FGBackground = Color(0xFFF5F7FA)
private val FGGreen = Color(0xFF16834A)

class PrivacySecurityActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                PrivacySecurityScreen(
                    onBack = {
                        startActivity(
                            Intent(this, ProfileActivity::class.java)
                        )  },
                    onOpenAppSettings = {
                        openSystemSettings(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts(
                                    "package",
                                    packageName,
                                    null
                                )
                            )
                        )
                    },
                    onOpenNotificationSettings = {
                        openSystemSettings(
                            Intent(
                                Settings.ACTION_APP_NOTIFICATION_SETTINGS
                            ).putExtra(
                                Settings.EXTRA_APP_PACKAGE,
                                packageName
                            )
                        )
                    }
                )
            }
        }
    }

    private fun openSystemSettings(intent: Intent) {
        try {
            startActivity(intent)
        } catch (_: Exception) {
            startActivity(
                Intent(Settings.ACTION_SETTINGS)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySecurityScreen(
    onBack: () -> Unit,
    onOpenAppSettings: () -> Unit,
    onOpenNotificationSettings: () -> Unit
) {
    val checklist = remember {
        mutableStateListOf(
            false, false, false, false, false
        )
    }

    var privacyExpanded by remember { mutableStateOf(false) }
    var accountExpanded by remember { mutableStateOf(false) }
    var permissionsExpanded by remember { mutableStateOf(false) }

    val completedCount = checklist.count { it }

    Scaffold(
        containerColor = FGBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Privacy & Security",
                        fontWeight = FontWeight.Bold,
                        color = FGNavy
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back", color = FGOrange)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // SECURITY OVERVIEW
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FGNavy
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🛡️",
                            fontSize = 34.sp
                        )

                        Column(
                            modifier = Modifier.padding(start = 12.dp)
                        ) {
                            Text(
                                "Your security matters",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                "Privacy controls for FireGuide",
                                fontSize = 13.sp,
                                color = Color.LightGray
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "$completedCount of ${checklist.size} " +
                                "security practices completed",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        repeat(checklist.size) { index ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(7.dp)
                                    .background(
                                        if (checklist[index]) FGGreen
                                        else Color(0xFF536577),
                                        CircleShape
                                    )
                            ) {}
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "This checklist tracks your selections. " +
                                "It does not scan or verify your device.",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }

            // DEVICE PERMISSIONS
            FeatureCard(
                emoji = "🔐",
                title = "App Permissions",
                subtitle = "Review access granted to FireGuide",
                description = "Review permissions such as camera, " +
                        "microphone, location, and notifications if " +
                        "the app requests them. Revoke permissions " +
                        "you no longer want to grant.",
                expanded = permissionsExpanded,
                onExpand = {
                    permissionsExpanded = !permissionsExpanded
                },
                buttonText = "Manage App Permissions",
                onAction = onOpenAppSettings
            )

            // NOTIFICATION PRIVACY
            FeatureCard(
                emoji = "🔔",
                title = "Notification Privacy",
                subtitle = "Control alerts from FireGuide",
                description = "Turn notifications on or off and " +
                        "review available notification settings. " +
                        "Notification previews on your lock screen " +
                        "may reveal information to nearby people.",
                buttonText = "Notification Settings",
                onAction = onOpenNotificationSettings
            )

            // SECURITY CHECKLIST
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        "Security Checklist",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = FGNavy
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Mark each practice when you have completed it.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(8.dp))

                    val items = listOf(
                        "My device has a screen lock.",
                        "My operating system is up to date.",
                        "I use strong, unique account passwords.",
                        "I never share passwords or verification codes.",
                        "I review app permissions regularly."
                    )

                    items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checklist[index],
                                onCheckedChange = {
                                    checklist[index] = it
                                }
                            )

                            Text(
                                text = item,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp,
                                color = FGNavy
                            )
                        }
                    }

                    TextButton(
                        onClick = {
                            for (i in checklist.indices) {
                                checklist[i] = false
                            }
                        }
                    ) {
                        Text(
                            "Reset Checklist",
                            color = FGOrange
                        )
                    }
                }
            }

            // PRIVACY INFORMATION
            FeatureCard(
                emoji = "🗂️",
                title = "Privacy & Data",
                subtitle = "Understand how information is handled",
                description = "FireGuide should explain what personal " +
                        "information it collects, why it needs it, " +
                        "how it is stored, and whether it is shared. " +
                        "Check the published privacy policy for details " +
                        "that match the app's actual implementation.",
                expanded = privacyExpanded,
                onExpand = {
                    privacyExpanded = !privacyExpanded
                }
            )

            // ACCOUNT MANAGEMENT
            FeatureCard(
                emoji = "👤",
                title = "Account & Personal Data",
                subtitle = "Account access, data requests and deletion",
                description = "If FireGuide has user accounts, provide " +
                        "a verified way to request access to, correction " +
                        "of, or deletion of personal data. Account deletion " +
                        "must be connected to your authentication and " +
                        "database services before it can work.",
                expanded = accountExpanded,
                onExpand = {
                    accountExpanded = !accountExpanded
                }
            )

            // SAFE USE
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF0E7)
                )
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        "Stay Safe",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = FGOrange
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "• Install FireGuide from trusted sources.\n" +
                                "• Avoid sharing personal details unnecessarily.\n" +
                                "• Report suspicious activity through the " +
                                "official support channel.\n" +
                                "• Keep your account recovery information secure.",
                        fontSize = 14.sp,
                        lineHeight = 23.sp,
                        color = FGNavy
                    )
                }
            }

            Divider(color = Color.LightGray)

            Text(
                "FireGuide • Privacy & Security",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun FeatureCard(
    emoji: String,
    title: String,
    subtitle: String,
    description: String,
    expanded: Boolean = false,
    onExpand: () -> Unit = {},
    buttonText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    emoji,
                    fontSize = 27.sp
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = FGNavy
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        subtitle,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                Text(
                    description,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = FGNavy
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onExpand) {
                    Text(
                        if (expanded) "Show less" else "Learn more",
                        color = FGOrange
                    )
                }

                if (buttonText != null && onAction != null) {
                    Button(
                        onClick = onAction,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FGOrange
                        )
                    ) {
                        Text(buttonText, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "FireGuide Privacy & Security"
)
@Composable
fun PrivacySecurityScreenPreview() {
    MaterialTheme {
        PrivacySecurityScreen(
            onBack = {},
            onOpenAppSettings = {},
            onOpenNotificationSettings = {}
        )
    }
}