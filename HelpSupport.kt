
package com.example.fireguide

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val HelpOrange = Color(0xFFFF4B18)
private val HelpNavy = Color(0xFF10283A)
private val HelpBackground = Color(0xFFF5F7FA)
private val HelpGreen = Color(0xFF16834A)

// Replace this placeholder with your real support email.
private const val SUPPORT_EMAIL = "kyalomosesk001@gmail.com"

class HelpSupportActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                HelpSupportScreen(
                    onBack = {
                        startActivity(
                            Intent(this, ProfileActivity::class.java)
                        )
                    },
                    onContactSupport = { openSupportEmail() }
                )
            }
        }
    }

    private fun openSupportEmail() {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse(
                "mailto:$SUPPORT_EMAIL" +
                        "?subject=FireGuide%20Support%20Request"
            )
        )

        try {
            startActivity(
                Intent.createChooser(
                    emailIntent,
                    "Contact FireGuide Support"
                )
            )
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "No email application is available on this device.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

private data class HelpFaq(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
    onBack: () -> Unit,
    onContactSupport: () -> Unit
) {
    var expandedFaq by remember { mutableStateOf(-1) }

    val faqs = listOf(
        HelpFaq(
            "How do I use FireGuide?",
            "Open the relevant section from the Home screen, " +
                    "select the topic you need, and follow the " +
                    "instructions provided. Available features depend " +
                    "on the version of the app you have installed."
        ),
        HelpFaq(
            "What should I do during a fire emergency?",
            "Prioritize human safety. Alert people nearby, activate " +
                    "the fire alarm if safe, and evacuate using a safe " +
                    "exit. Stay low if smoke is present, never use lifts " +
                    "during a fire, and do not re-enter a burning building. " +
                    "Contact your local emergency services."
        ),
        HelpFaq(
            "A video or animation will not play. What should I do?",
            "Check that the app is up to date, close and reopen it, " +
                    "and try again. If the problem continues, note the " +
                    "name of the affected video and contact support."
        ),
        HelpFaq(
            "What if a page does not open?",
            "Return to the Home screen and try again. If the issue " +
                    "persists, restart FireGuide and check for an app " +
                    "update. Contact support if the problem continues."
        ),
        HelpFaq(
            "How can I protect my privacy?",
            "Open Privacy & Security from your Profile screen to " +
                    "review privacy guidance, check your security " +
                    "practices, and access your app permission settings."
        ),
        HelpFaq(
            "How do I report a problem?",
            "Use the Contact Support button below. Describe what " +
                    "you were doing, what happened, and any error message " +
                    "you saw. Avoid including passwords or sensitive " +
                    "personal information."
        )
    )

    Scaffold(
        containerColor = HelpBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Help & Support",
                        color = HelpNavy,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(
                            text = "Back",
                            color = HelpOrange
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Welcome banner
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = HelpNavy
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(alpha = 0.12f),
                                shape = CircleShape
                            )
                            .padding(12.dp)
                    ) {
                        Text("🆘", fontSize = 30.sp)
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "How can we help you?",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Find answers, troubleshoot issues, " +
                                "and get help using FireGuide.",
                        color = Color(0xFFE1E8EF),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            // Quick help options
            Text(
                text = "QUICK HELP",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = HelpNavy
            )

            HelpActionCard(
                emoji = "📚",
                title = "Frequently Asked Questions",
                description = "Find answers to common questions",
                onClick = {
                    expandedFaq = if (expandedFaq == -2) -1 else -2
                }
            )

            HelpActionCard(
                emoji = "🔧",
                title = "Troubleshooting",
                description = "Resolve common app problems",
                onClick = {
                    expandedFaq = if (expandedFaq == -3) -1 else -3
                }
            )

            if (expandedFaq == -2) {
                HelpInfoCard(
                    title = "Using FireGuide",
                    description = "Browse the FAQs below for guidance " +
                            "on app navigation, videos, privacy, and reporting issues."
                )
            }

            if (expandedFaq == -3) {
                HelpInfoCard(
                    title = "Troubleshooting checklist",
                    description = "1. Close and reopen FireGuide.\n" +
                            "2. Restart your device if necessary.\n" +
                            "3. Check for app updates.\n" +
                            "4. Note the affected screen and any error message.\n" +
                            "5. Contact support if the issue persists."
                )
            }

            // Emergency reminder
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEDE7)
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "🔥 Emergency Safety",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = HelpOrange
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "FireGuide is an educational resource, " +
                                "not a substitute for emergency services. " +
                                "In a real emergency, evacuate if safe and " +
                                "contact your local emergency responders. " +
                                "Never delay evacuation to use the app.",
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = HelpNavy
                    )
                }
            }

            // FAQs
            Text(
                text = "FREQUENTLY ASKED QUESTIONS",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = HelpNavy
            )

            faqs.forEachIndexed { index, faq ->
                FaqCard(
                    question = faq.question,
                    answer = faq.answer,
                    expanded = expandedFaq == index,
                    onClick = {
                        expandedFaq =
                            if (expandedFaq == index) -1 else index
                    }
                )
            }

            // Contact support
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("✉️", fontSize = 32.sp)

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Still need help?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = HelpNavy
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Contact our support team and describe " +
                                "the issue you're experiencing.",
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = Color.DarkGray
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = onContactSupport,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HelpOrange
                        )
                    ) {
                        Text(
                            text = "Contact Support",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Support email: $SUPPORT_EMAIL",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // About FireGuide
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "About FireGuide",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = HelpNavy
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "FireGuide helps users learn about fire " +
                                "prevention, emergency preparedness, and " +
                                "safety procedures.",
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = Color.DarkGray
                    )

                    Spacer(Modifier.height(8.dp))

                    Divider(color = Color.LightGray)

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "App: FireGuide",
                        fontSize = 13.sp,
                        color = HelpNavy
                    )

                    Text(
                        text = "For app assistance, use Contact Support above.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "FireGuide • Help & Support",
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
private fun HelpActionCard(
    emoji: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 28.sp)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = HelpNavy
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    description,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "›",
                fontSize = 28.sp,
                color = HelpOrange
            )
        }
    }
}

@Composable
private fun FaqCard(
    question: String,
    answer: String,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HelpNavy
                )

                Text(
                    text = if (expanded) "−" else "+",
                    fontSize = 23.sp,
                    color = HelpOrange
                )
            }

            if (expanded) {
                Divider(color = Color(0xFFEEEEEE))

                Text(
                    text = answer,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
private fun HelpInfoCard(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF4EE)
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = HelpNavy
            )

            Spacer(Modifier.height(8.dp))

            Text(
                description,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = HelpNavy
            )
        }
    }
}

@Preview(
    name = "FireGuide Help & Support",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HelpSupportScreenPreview() {
    MaterialTheme {
        HelpSupportScreen(
            onBack = {},
            onContactSupport = {}
        )
    }
}
