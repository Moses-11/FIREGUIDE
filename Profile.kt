package com.example.fireguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val FireOrange = Color(0xFFFF4B16)
private val FireDark = Color(0xFF10233D)
private val FireGray = Color(0xFF718096)
private val SoftOrange = Color(0xFFFFF0E5)
private val CardBorder = Color(0xFFECEFF2)

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FIREGUIDETheme {
                ProfileScreen(
                    onBack = {
                        startActivity(
                            Intent(this, SettingsActivity::class.java)
                        )
                        finish()
                    },
                    onEditProfile = {
                        startActivity(
                            Intent(this, ProfileInformationActivity::class.java)
                        )
                        finish()
                    },
                    onPrivacy = {
                        startActivity(
                            Intent(this, PrivacySecurityActivity ::class.java)
                        )
                        finish()
                    },
                    onHelp = {
                        startActivity(
                            Intent(this, HelpSupportActivity::class.java)
                        )
                        finish()
                    },
                    onLogout = {
                        FirebaseAuth.getInstance().signOut()

                        // Return to Sign In after signing out.
                        startActivity(
                            Intent(this, SignInActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        )
                        finish()
                    }
                )
            }
        }
    }
}

data class UserProfile(
    val name: String = "Loading...",
    val email: String = "Loading...",
    val role: String = "Fire Guide Learner"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onHelp: () -> Unit = {},
    onLogout: () -> Unit = {},
    auth: FirebaseAuth? = if (LocalInspectionMode.current) null else try { FirebaseAuth.getInstance() } catch (_: Exception) { null },
    firestore: FirebaseFirestore? = if (LocalInspectionMode.current) null else try { FirebaseFirestore.getInstance() } catch (_: Exception) { null }
) {
    val inPreview = LocalInspectionMode.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    var user by remember {
        mutableStateOf(
            if (inPreview) {
                UserProfile(
                    name = "Alex Johnson",
                    email = "alex.johnson@fireguide.com",
                    role = "Fire Guide Safety Officer"
                )
            } else {
                UserProfile(
                    name = "Loading...",
                    email = auth?.currentUser?.email ?: "Loading...",
                    role = "Fire Guide Learner"
                )
            }
        )
    }

    var isLoading by remember { mutableStateOf(!inPreview) }
    var loadError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(inPreview) {
        if (inPreview) return@LaunchedEffect

        val firebaseUser = auth?.currentUser

        if (firebaseUser == null) {
            user = UserProfile(
                name = "Guest User",
                email = "No signed-in account",
                role = "Fire Guide Learner"
            )
            isLoading = false
            loadError = "No signed-in user was found."
            return@LaunchedEffect
        }

        val uid = firebaseUser.uid

        if (firestore == null) {
            user = UserProfile(
                name = firebaseUser.displayName
                    ?.takeIf { it.isNotBlank() }
                    ?: "Fire Guide Learner",
                email = firebaseUser.email ?: "No email available",
                role = "Fire Guide Learner"
            )
            isLoading = false
            return@LaunchedEffect
        }

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val savedName = document.getString("fullName")
                    ?.takeIf { it.isNotBlank() }
                    ?: firebaseUser.displayName
                        ?.takeIf { it.isNotBlank() }
                    ?: "Fire Guide Learner"

                val savedEmail = document.getString("email")
                    ?.takeIf { it.isNotBlank() }
                    ?: firebaseUser.email
                    ?: "No email available"

                val savedRole = document.getString("role")
                    ?.takeIf { it.isNotBlank() }
                    ?: "Fire Guide Learner"

                user = UserProfile(
                    name = savedName,
                    email = savedEmail,
                    role = savedRole
                )

                isLoading = false
                loadError = null
            }
            .addOnFailureListener { exception ->
                // Authentication data is still available even if Firestore
                // temporarily fails.
                user = UserProfile(
                    name = firebaseUser.displayName
                        ?.takeIf { it.isNotBlank() }
                        ?: "Fire Guide Learner",
                    email = firebaseUser.email ?: "No email available",
                    role = "Fire Guide Learner"
                )

                isLoading = false
                loadError = exception.localizedMessage
                    ?: "Unable to load your profile from Firestore."
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Profile",
                        fontWeight = FontWeight.Bold,
                        color = FireDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = FireDark
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit Profile",
                            tint = FireOrange
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF9FAFB)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(SoftOrange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Profile photo",
                    tint = FireOrange,
                    modifier = Modifier.size(58.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                user.name,
                color = FireDark,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            Text(
                user.role,
                color = FireOrange,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (isLoading) {
                Spacer(Modifier.height(10.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = FireOrange,
                    strokeWidth = 2.5.dp
                )
            }

            if (!loadError.isNullOrBlank() && !isLoading) {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Some profile information could not be loaded. " +
                            "Your account email is still shown below.",
                    color = FireGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(22.dp))

            ProfileInfoCard(user)

            Spacer(Modifier.height(18.dp))

            ProfileSectionCard(
                "Account",
                listOf(
                    ProfileMenuItem(
                        Icons.Outlined.Person,
                        "Personal Information",
                        "Manage your personal details",
                        onEditProfile
                    ),

                    ProfileMenuItem(
                        Icons.Outlined.Security,
                        "Privacy & Security",
                        "Manage your account security",
                        onPrivacy
                    )
                )
            )

            Spacer(Modifier.height(18.dp))

            ProfileSectionCard(
                "Support",
                listOf(
                    ProfileMenuItem(
                        Icons.AutoMirrored.Outlined.HelpOutline,
                        "Help & Support",
                        "Get help using Fire Guide",
                        onHelp
                    )
                )
            )

            Spacer(Modifier.height(22.dp))

            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = FireOrange
                ),
                border = BorderStroke(1.5.dp, FireOrange)
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Log Out"
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    "Log Out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "FIRE GUIDE\nLEARN • PREPARE • STAY SAFE",
                color = FireGray,
                fontSize = 11.sp,
                lineHeight = 17.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(20.dp))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            title = {
                Text(
                    "Log Out?",
                    fontWeight = FontWeight.Bold,
                    color = FireDark
                )
            },
            text = {
                Text("Are you sure you want to log out of Fire Guide?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FireOrange
                    )
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(22.dp)
        )
    }
}

@Composable
private fun ProfileInfoCard(user: UserProfile) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ContactRow(
                Icons.Outlined.Email,
                "Email",
                user.email
            )

            HorizontalDivider(color = CardBorder)
        }
    }
}

@Composable
private fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(SoftOrange),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = FireOrange,
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(Modifier.width(13.dp))

        Column(Modifier.weight(1f)) {
            Text(
                title,
                color = FireGray,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(2.dp))

            Text(
                value,
                color = FireDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private data class ProfileMenuItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)

@Composable
private fun ProfileSectionCard(
    title: String,
    items: List<ProfileMenuItem>
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(Modifier.padding(vertical = 7.dp)) {

            Text(
                title,
                color = FireDark,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = 17.dp,
                    top = 9.dp,
                    bottom = 7.dp
                )
            )

            items.forEachIndexed { index, item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { item.onClick() }
                        .padding(
                            horizontal = 17.dp,
                            vertical = 14.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(SoftOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            tint = FireOrange,
                            modifier = Modifier.size(23.dp)
                        )
                    }

                    Spacer(Modifier.width(13.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            item.title,
                            color = FireDark,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(3.dp))

                        Text(
                            item.subtitle,
                            color = FireGray,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = FireGray
                    )
                }

                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 17.dp),
                        color = CardBorder
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Fire Guide Profile",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440"
)
@Composable
private fun ProfileScreenPreview() {
    FIREGUIDETheme {
        ProfileScreen()
    }
}