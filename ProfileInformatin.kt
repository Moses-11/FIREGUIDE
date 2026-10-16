// File: ProfileInformationActivity.kt
// Package: com.example.fireguide

package com.example.fireguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val ProfileInfoOrange = Color(0xFFFF4B16)
private val ProfileInfoDark = Color(0xFF10233D)
private val ProfileInfoGray = Color(0xFF718096)
private val ProfileInfoSoftOrange = Color(0xFFFFF0E5)
private val ProfileInfoBorder = Color(0xFFECEFF2)
private val ProfileInfoBackground = Color(0xFFF9FAFB)

class ProfileInformationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FIREGUIDETheme {
                ProfileInformationScreen(
                    onBack = { startActivity(
                        Intent(this, ProfileActivity ::class.java)
                    ) }
                )
            }
        }
    }
}

data class ProfileInformationData(
    val fullName: String = "",
    val email: String = "",
    val role: String = "Fire Guide Learner"
)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ProfileInformationScreen(
    onBack: () -> Unit = {},
    previewData: ProfileInformationData? = null
) {
    var profile by remember { mutableStateOf(previewData) }
    var isLoading by remember { mutableStateOf(previewData == null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(previewData) {
        if (previewData != null) {
            profile = previewData
            isLoading = false
        } else {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser == null) {
                profile = null
                errorMessage = "You are not signed in. Please sign in and try again."
                isLoading = false
            } else {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        val firestoreName = document.getString("fullName")
                        val firestoreEmail = document.getString("email")
                        val firestoreRole = document.getString("role")

                        profile = ProfileInformationData(
                            fullName = firestoreName
                                ?: firebaseUser.displayName
                                ?: "Name not provided",
                            email = firestoreEmail ?: firebaseUser.email.orEmpty(),
                            role = firestoreRole ?: "Fire Guide Learner"
                        )
                        isLoading = false
                    }
                    .addOnFailureListener {
                        // Keep the page useful even if the Firestore profile read fails.
                        profile = ProfileInformationData(
                            fullName = firebaseUser.displayName ?: "Name not provided",
                            email = firebaseUser.email.orEmpty(),
                            role = "Fire Guide Learner"
                        )
                        errorMessage = "Some profile details could not be loaded from Firestore."
                        isLoading = false
                    }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile Information",
                        color = ProfileInfoDark,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = ProfileInfoDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = ProfileInfoBackground
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
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(ProfileInfoSoftOrange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile photo",
                    tint = ProfileInfoOrange,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(Modifier.height(14.dp))

            if (isLoading) {
                CircularProgressIndicator(color = ProfileInfoOrange)
                Spacer(Modifier.height(10.dp))
                Text("Loading your profile…", color = ProfileInfoGray, fontSize = 14.sp)
            } else if (profile != null) {
                Text(
                    text = profile!!.fullName,
                    color = ProfileInfoDark,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = profile!!.role,
                    color = ProfileInfoOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(
                    text = errorMessage ?: "Unable to load your profile.",
                    color = Color(0xFFB42318),
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(26.dp))

            if (profile != null && !isLoading) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, ProfileInfoBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(17.dp)
                    ) {
                        InformationRow(
                            icon = { tint ->
                                Icon(
                                    Icons.Outlined.Person,
                                    contentDescription = "Full name",
                                    tint = tint,
                                    modifier = Modifier.size(23.dp)
                                )
                            },
                            label = "Full Name",
                            value = profile!!.fullName
                        )
                        HorizontalDivider(color = ProfileInfoBorder)
                        InformationRow(
                            icon = { tint ->
                                Icon(
                                    Icons.Outlined.Email,
                                    contentDescription = "Email address",
                                    tint = tint,
                                    modifier = Modifier.size(23.dp)
                                )
                            },
                            label = "Email Address",
                            value = profile!!.email
                        )
                        HorizontalDivider(color = ProfileInfoBorder)
                        InformationRow(
                            icon = { tint ->
                                Icon(
                                    Icons.Outlined.Badge,
                                    contentDescription = "Account role",
                                    tint = tint,
                                    modifier = Modifier.size(23.dp)
                                )
                            },
                            label = "Account Type",
                            value = profile!!.role
                        )
                    }
                }
            }

            if (errorMessage != null && profile != null && !isLoading) {
                Spacer(Modifier.height(14.dp))
                Text(
                    text = errorMessage!!,
                    color = ProfileInfoGray,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(24.dp))
            Text(
                text = "Your profile information is linked to your signed-in Fire Guide account.",
                color = ProfileInfoGray,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun InformationRow(
    icon: @Composable (Color) -> Unit,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(ProfileInfoSoftOrange),
            contentAlignment = Alignment.Center
        ) {
            icon(ProfileInfoOrange)
        }
        Spacer(Modifier.width(13.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = ProfileInfoGray, fontSize = 12.sp)
            Spacer(Modifier.height(3.dp))
            Text(
                text = value.ifBlank { "Not provided" },
                color = ProfileInfoDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(
    name = "Profile Information",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440"
)
@Composable
private fun ProfileInformationPreview() {
    FIREGUIDETheme {
        ProfileInformationScreen(
            previewData = ProfileInformationData(
                fullName = "Moses Kyalo",
                email = "moses@example.com",
                role = "Fire Guide Learner"
            )
        )
    }
}
