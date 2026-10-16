package com.example.fireguide

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

private val FireOrange = Color(0xFFFF4B16)
private val FireDark = Color(0xFF102A43)
private val FireGray = Color(0xFF667788)
private val FieldBorder = Color(0xFFE1E5E8)

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FIREGUIDETheme {
                SignUpScreen(
                    onSignInClick = {
                        startActivity(Intent(this, SignInActivity::class.java))
                        finish()
                    },
                    onSignUpSuccess = {
                        startActivity(Intent(this, HomepageActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SignUpScreen(
    onSignInClick: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {}
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmVisible by rememberSaveable { mutableStateOf(false) }
    var nameError by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var confirmError by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var firebaseError by rememberSaveable { mutableStateOf("") }

    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    fun createFirebaseAccount() {
        val cleanName = fullName.trim()
        val cleanEmail = email.trim()
        firebaseError = ""
        isLoading = true

        auth.createUserWithEmailAndPassword(cleanEmail, password)
            .addOnCompleteListener { authTask ->
                if (!authTask.isSuccessful) {
                    isLoading = false
                    val exception = authTask.exception
                    firebaseError = when (exception) {
                        is FirebaseAuthException if exception.errorCode == "ERROR_EMAIL_ALREADY_IN_USE" ->
                            "An account with this email already exists."

                        is FirebaseAuthException if exception.errorCode == "ERROR_INVALID_EMAIL" ->
                            "Please enter a valid email address."

                        is FirebaseAuthException if exception.errorCode == "ERROR_WEAK_PASSWORD" ->
                            "Password is too weak. Please choose a stronger password."

                        is FirebaseAuthException if exception.errorCode == "ERROR_NETWORK_REQUEST_FAILED" ->
                            "Network error. Check your internet connection and try again."

                        else -> exception?.localizedMessage
                            ?: "Unable to create your account. Please try again."
                    }
                    return@addOnCompleteListener
                }

                val firebaseUser = auth.currentUser
                if (firebaseUser == null) {
                    isLoading = false
                    firebaseError =
                        "Account creation completed, but the user session could not be loaded."
                    return@addOnCompleteListener
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(cleanName)
                    .build()

                firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener {
                        val userData = hashMapOf(
                            "fullName" to cleanName,
                            "email" to cleanEmail,
                            "role" to "Fire Guide Learner",
                            "createdAt" to FieldValue.serverTimestamp(),
                            "notificationsEnabled" to true,
                            "darkMode" to false,
                            "language" to "English",
                            "autoPlayAnimations" to true
                        )

                        firestore.collection("users")
                            .document(firebaseUser.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                isLoading = false
                                onSignUpSuccess()
                            }
                            .addOnFailureListener { exception ->
                                isLoading = false
                                firebaseError =
                                    "Account created, but your profile could not be saved. " +
                                            (exception.localizedMessage ?: "Please try again.")
                            }
                    }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.White, Color(0xFFFFFBF8), Color(0xFFFFF3EA))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(220.dp)
                .offset(x = 90.dp, y = 90.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(FireOrange.copy(alpha = 0.12f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 28.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.fire_logo),
                    contentDescription = "Fire Guide",
                    modifier = Modifier.size(65.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Row {
                        Text("FIRE", color = FireDark, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                        Text(" GUIDE", color = FireOrange, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "LEARN • PREPARE • STAY SAFE",
                        color = FireGray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            Row {
                Text("Create ", color = FireDark, fontSize = 33.sp, fontWeight = FontWeight.Bold)
                Text("Your Account", color = FireOrange, fontSize = 33.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "Join Fire Guide and start learning how\nto stay safe and prepared.",
                color = FireGray,
                fontSize = 18.sp,
                lineHeight = 26.sp
            )

            Spacer(Modifier.height(28.dp))

            FieldLabel("Full Name", nameError)
            FireField(
                value = fullName,
                onValueChange = { fullName = it; nameError = false; firebaseError = "" },
                placeholder = "Enter your full name",
                icon = { Icon(Icons.Outlined.Person, "Full Name") },
                keyboardType = KeyboardType.Text,
                error = nameError,
                enabled = !isLoading
            )

            Spacer(Modifier.height(18.dp))

            FieldLabel("Email Address", emailError)
            FireField(
                value = email,
                onValueChange = { email = it; emailError = false; firebaseError = "" },
                placeholder = "Enter your email address",
                icon = { Icon(Icons.Outlined.Email, "Email") },
                keyboardType = KeyboardType.Email,
                error = emailError,
                enabled = !isLoading
            )

            Spacer(Modifier.height(18.dp))

            FieldLabel("Password", passwordError)
            FireField(
                value = password,
                onValueChange = { password = it; passwordError = false; firebaseError = "" },
                placeholder = "Create a password",
                icon = { Icon(Icons.Outlined.Lock, "Password") },
                keyboardType = KeyboardType.Password,
                passwordVisible = passwordVisible,
                onVisibilityClick = { passwordVisible = !passwordVisible },
                error = passwordError,
                enabled = !isLoading
            )

            Spacer(Modifier.height(18.dp))

            FieldLabel("Confirm Password", confirmError)
            FireField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmError = false; firebaseError = "" },
                placeholder = "Confirm your password",
                icon = { Icon(Icons.Outlined.Lock, "Confirm Password") },
                keyboardType = KeyboardType.Password,
                passwordVisible = confirmVisible,
                onVisibilityClick = { confirmVisible = !confirmVisible },
                error = confirmError,
                enabled = !isLoading
            )

            if (firebaseError.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = firebaseError,
                    color = Color(0xFFD32F2F),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(26.dp))

            Button(
                onClick = {
                    nameError = fullName.trim().isBlank()
                    emailError = email.trim().isBlank() ||
                            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
                    passwordError = password.length < 6
                    confirmError = confirmPassword.isBlank() || password != confirmPassword
                    firebaseError = ""

                    if (!nameError && !emailError && !passwordError && !confirmError) {
                        createFirebaseAccount()
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FireOrange,
                    contentColor = Color.White,
                    disabledContainerColor = FireOrange.copy(alpha = 0.55f),
                    disabledContentColor = Color.White.copy(alpha = 0.85f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(23.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Creating Account...", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("Sign Up", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(12.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Sign Up")
                }
            }

            Spacer(Modifier.height(25.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account? ", color = FireGray, fontSize = 16.sp)
                Text(
                    "Sign In",
                    color = FireOrange,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .clickable(enabled = !isLoading) { onSignInClick() }
                        .padding(4.dp)
                )
            }

            Spacer(Modifier.height(25.dp))
        }
    }
}

@Composable
private fun FireField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: @Composable () -> Unit,
    keyboardType: KeyboardType,
    passwordVisible: Boolean = true,
    onVisibilityClick: (() -> Unit)? = null,
    error: Boolean = false,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        placeholder = { Text(placeholder, color = FireGray.copy(alpha = 0.65f)) },
        leadingIcon = icon,
        trailingIcon = if (onVisibilityClick != null) {
            {
                IconButton(onClick = onVisibilityClick, enabled = enabled) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Outlined.Visibility
                        else
                            Icons.Outlined.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            }
        } else null,
        visualTransformation = if (onVisibilityClick != null && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = error,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FireOrange,
            unfocusedBorderColor = FieldBorder,
            focusedTextColor = FireDark,
            unfocusedTextColor = FireDark,
            disabledTextColor = FireGray,
            disabledBorderColor = FieldBorder.copy(alpha = 0.65f),
            cursorColor = FireOrange,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White.copy(alpha = 0.7f),
            errorBorderColor = Color.Red
        )
    )
}

@Composable
private fun FieldLabel(text: String, error: Boolean) {
    Text(
        text = text,
        color = if (error) Color.Red else FireDark,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 7.dp)
    )
}

@Preview(
    name = "Fire Guide Sign Up",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440"
)
@Composable
private fun SignUpPreview() {
    FIREGUIDETheme {
        SignUpScreen()
    }
}