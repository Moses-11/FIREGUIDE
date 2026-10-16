package com.example.fireguide

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.example.fireguide.ui.theme.FIREGUIDETheme

private val FireOrange = Color(0xFFFF4B16)
private val FireDark = Color(0xFF10233D)
private val FireGray = Color(0xFF718096)
private val Border = Color(0xFFE4E8ED)
private const val SETTINGS_PREFS = "fire_guide_settings"
private const val KEY_DARK_MODE = "dark_mode"
private const val KEY_TEXT_SIZE = "text_size"
private const val KEY_NOTIFICATIONS = "notifications"
private const val KEY_AUTOPLAY = "auto_play"

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FIREGUIDETheme {
                SettingsScreen(
                    onBackHome = {
                        startActivity(Intent(this, HomepageActivity::class.java))
                        finish()
                    },
                    onProfileClick = {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    },
                    showSettingsMessage = { /* No additional settings actions are required. */ }
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onBackHome: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    showSettingsMessage: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE) }

    var notifications by remember { mutableStateOf(prefs.getBoolean(KEY_NOTIFICATIONS, true)) }
    var darkMode by remember { mutableStateOf(prefs.getBoolean(KEY_DARK_MODE, false)) }
    var autoPlay by remember { mutableStateOf(prefs.getBoolean(KEY_AUTOPLAY, true)) }
    var textSize by remember { mutableStateOf(prefs.getString(KEY_TEXT_SIZE, "Normal") ?: "Normal") }
    var textMenu by remember { mutableStateOf(false) }
    var showResetPasswordDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.email.orEmpty()) }
    var resetEmailError by remember { mutableStateOf<String?>(null) }
    var resetSending by remember { mutableStateOf(false) }
    var resetMessage by remember { mutableStateOf<String?>(null) }
    var resetMessageIsError by remember { mutableStateOf(false) }

    val pageBackground = if (darkMode) Color(0xFF101820) else Color.White
    val cardBackground = if (darkMode) Color(0xFF1B2835) else Color.White
    val primaryText = if (darkMode) Color(0xFFF3F6FA) else FireDark
    val secondaryText = if (darkMode) Color(0xFFB8C4D0) else FireGray
    val dividerColor = if (darkMode) Color(0xFF394857) else Border
    val textScale = when (textSize) {
        "Small" -> 0.88f
        "Large" -> 1.15f
        else -> 1f
    }
    fun scaled(value: Int): TextUnit = (value * textScale).sp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackground)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.fire_logo),
                contentDescription = "Fire Guide",
                modifier = Modifier.size(62.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Row {
                    Text("FIRE", color = primaryText, fontSize = scaled(28), fontWeight = FontWeight.Bold)
                    Text(" GUIDE", color = FireOrange, fontSize = scaled(28), fontWeight = FontWeight.Bold)
                }
                Text("LEARN • PREPARE • STAY SAFE", color = secondaryText, fontSize = scaled(10))
            }
        }

        Spacer(Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(205.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    if (darkMode) Brush.horizontalGradient(listOf(Color(0xFF293745), Color(0xFF3A302D)))
                    else Brush.horizontalGradient(listOf(Color(0xFFFFF1E8), Color(0xFFFFDCC6)))
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp, end = 145.dp)
            ) {
                Text("Settings", color = primaryText, fontSize = scaled(34), fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(7.dp))
                Text(
                    "Customize your experience\nand manage app preferences.",
                    color = secondaryText,
                    fontSize = scaled(17),
                    lineHeight = scaled(24)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 20.dp)
                    .size(155.dp)
                    .clip(CircleShape)
                    .background((if (darkMode) Color(0xFF55433B) else Color(0xFFFFC9A8)).copy(alpha = .65f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = FireOrange.copy(alpha = .85f), modifier = Modifier.size(82.dp))
            }
        }

        Spacer(Modifier.height(20.dp))
        SectionTitle("Account", primaryText, scaled(22))

        SettingsCard(cardBackground, dividerColor) {
            SettingsRow(Icons.Outlined.Person, FireOrange, "Profile", "View and edit your personal information", primaryText, secondaryText, scaled(17), scaled(14), onProfileClick)
            DividerLine(dividerColor)
            SettingsRow(Icons.Outlined.Lock, Color(0xFF526DFF), "Change Password", "Receive a password reset link by email", primaryText, secondaryText, scaled(17), scaled(14), onClick = {
                resetEmail = FirebaseAuth.getInstance().currentUser?.email.orEmpty()
                resetEmailError = null
                resetMessage = null
                showResetPasswordDialog = true
            })
        }

        Spacer(Modifier.height(20.dp))
        SectionTitle("App Preferences", primaryText, scaled(22))

        SettingsCard(cardBackground, dividerColor) {
            SettingsRow(Icons.Outlined.Notifications, Color(0xFFFF4B55), "Notifications", "Receive fire safety alerts and updates", primaryText, secondaryText, scaled(17), scaled(14), onClick = {
                notifications = !notifications
                prefs.edit().putBoolean(KEY_NOTIFICATIONS, notifications).apply()
            }) {
                Switch(checked = notifications, onCheckedChange = {
                    notifications = it
                    prefs.edit().putBoolean(KEY_NOTIFICATIONS, it).apply()
                }, colors = fireSwitchColors())
            }
            DividerLine(dividerColor)

            SettingsRow(Icons.Outlined.DarkMode, Color(0xFF8654E8), "Dark Mode", "Switch between light and dark theme", primaryText, secondaryText, scaled(17), scaled(14), onClick = {
                darkMode = !darkMode
                prefs.edit().putBoolean(KEY_DARK_MODE, darkMode).apply()
            }) {
                Switch(checked = darkMode, onCheckedChange = {
                    darkMode = it
                    prefs.edit().putBoolean(KEY_DARK_MODE, it).apply()
                }, colors = fireSwitchColors())
            }
            DividerLine(dividerColor)

            SettingsRow(Icons.Outlined.TextFields, Color(0xFF2679E8), "Text Size", "Adjust text size on this Settings page", primaryText, secondaryText, scaled(17), scaled(14), onClick = { textMenu = true }) {
                Box {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { textMenu = true }) {
                        Text(textSize, color = primaryText, fontSize = scaled(15))
                        Icon(Icons.Outlined.ExpandMore, contentDescription = "Select text size", tint = primaryText)
                    }
                    DropdownMenu(expanded = textMenu, onDismissRequest = { textMenu = false }) {
                        listOf("Small", "Normal", "Large").forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = {
                                textSize = option
                                prefs.edit().putString(KEY_TEXT_SIZE, option).apply()
                                textMenu = false
                            })
                        }
                    }
                }
            }
            DividerLine(dividerColor)

            SettingsRow(Icons.Outlined.PlayCircleOutline, Color(0xFF16A65A), "Auto Play Animations", "Automatically play animations", primaryText, secondaryText, scaled(17), scaled(14), onClick = {
                autoPlay = !autoPlay
                prefs.edit().putBoolean(KEY_AUTOPLAY, autoPlay).apply()
            }) {
                Switch(checked = autoPlay, onCheckedChange = {
                    autoPlay = it
                    prefs.edit().putBoolean(KEY_AUTOPLAY, it).apply()
                }, colors = fireSwitchColors())
            }
        }

        Spacer(Modifier.height(22.dp))
        OutlinedButton(
            onClick = onBackHome,
            modifier = Modifier.fillMaxWidth().height(62.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, FireOrange),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = FireOrange)
        ) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back to Home", modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(12.dp))
            Text("Back to Home", fontSize = scaled(18), fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(22.dp))
    }

    if (showResetPasswordDialog) {
        AlertDialog(
            onDismissRequest = { if (!resetSending) showResetPasswordDialog = false },
            title = { Text("Reset Password", fontWeight = FontWeight.Bold, color = primaryText) },
            text = {
                Column {
                    Text("Enter your email address below. Firebase will send you a password reset link.", color = secondaryText)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it; resetEmailError = null; resetMessage = null },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Email address") },
                        placeholder = { Text("name@example.com") },
                        isError = resetEmailError != null,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Email),
                        supportingText = resetEmailError?.let { message -> ({ Text(message) }) }
                    )
                    resetMessage?.let { message ->
                        Spacer(Modifier.height(8.dp))
                        Text(message, color = if (resetMessageIsError) MaterialTheme.colorScheme.error else Color(0xFF168447), fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = !resetSending,
                    onClick = {
                        val email = resetEmail.trim()
                        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            resetEmailError = "Enter a valid email address."
                        } else {
                            resetSending = true
                            resetEmailError = null
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    resetSending = false
                                    if (task.isSuccessful) {
                                        resetMessageIsError = false
                                        resetMessage = "If an account exists for this email, a reset link will be sent."
                                    } else {
                                        resetMessageIsError = true
                                        resetMessage = task.exception?.localizedMessage ?: "Unable to send the reset link. Please try again."
                                    }
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FireOrange)
                ) {
                    if (resetSending) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Send Reset Link")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetPasswordDialog = false }, enabled = !resetSending) {
                    Text("Cancel", color = FireOrange)
                }
            },
            containerColor = cardBackground,
            shape = RoundedCornerShape(22.dp)
        )
    }
}

@Composable
private fun SectionTitle(title: String, color: Color, size: TextUnit) {
    Text(text = title, color = color, fontSize = size, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp, bottom = 9.dp))
}

@Composable
private fun SettingsCard(
    backgroundColor: Color,
    borderColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), color = backgroundColor, border = BorderStroke(1.dp, borderColor), shadowElevation = 1.dp) {
        Column(content = content)
    }
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    titleColor: Color,
    subtitleColor: Color,
    titleSize: TextUnit,
    subtitleSize: TextUnit,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit = { Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = subtitleColor) }
) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(iconColor), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(27.dp))
        }
        Spacer(Modifier.width(15.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = titleColor, fontSize = titleSize, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, color = subtitleColor, fontSize = subtitleSize, lineHeight = subtitleSize * 1.35f)
        }
        Spacer(Modifier.width(10.dp))
        trailing()
    }
}

@Composable
private fun DividerLine(color: Color) {
    HorizontalDivider(modifier = Modifier.padding(start = 82.dp), color = color)
}

@Composable
private fun fireSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor = Color.White,
    checkedTrackColor = FireOrange,
    uncheckedThumbColor = Color.White,
    uncheckedTrackColor = Color(0xFFCBD3DE),
    uncheckedBorderColor = Color.Transparent
)

@Preview(name = "Fire Guide Settings", showBackground = true, showSystemUi = true, device = "spec:width=393dp,height=852dp,dpi=440")
@Composable
private fun SettingsPreview() {
    FIREGUIDETheme {
        SettingsScreen(onBackHome = {}, onProfileClick = {}, showSettingsMessage = {})
    }
}