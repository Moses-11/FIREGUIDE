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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fireguide.ui.theme.FIREGUIDETheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

private val FireOrange = Color(0xFFF4511E)
private val FireDark = Color(0xFF10243E)
private val FireGray = Color(0xFF66758A)
private val FieldBorder = Color(0xFFE1E5EA)

class SignInActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FIREGUIDETheme {
                SignInScreen(
                    onLoginSuccess = {
                        startActivity(
                            Intent(this, HomepageActivity::class.java)
                        )
                        finish()
                    },
                    onSignUpClick = {
                        startActivity(
                            Intent(this, SignUp::class.java)
                        )
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SignInScreen(
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var emailError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var loginError by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    var showForgotPasswordDialog by rememberSaveable { mutableStateOf(false) }
    var resetEmail by rememberSaveable { mutableStateOf("") }
    var resetEmailError by rememberSaveable { mutableStateOf(false) }
    var resetMessage by rememberSaveable { mutableStateOf("") }
    var resetLoading by rememberSaveable { mutableStateOf(false) }

    val auth = remember { FirebaseAuth.getInstance() }

    fun signInWithFirebase() {
        val cleanEmail = email.trim()

        emailError = false
        passwordError = false
        loginError = ""
        isLoading = true

        auth.signInWithEmailAndPassword(
            cleanEmail,
            password
        ).addOnCompleteListener { task ->
            isLoading = false

            if (task.isSuccessful) {
                onLoginSuccess()
            } else {
                val exception = task.exception

                loginError = when {
                    exception is FirebaseAuthException &&
                            exception.errorCode == "ERROR_INVALID_EMAIL" ->
                        "Please enter a valid email address."

                    exception is FirebaseAuthException &&
                            exception.errorCode == "ERROR_USER_NOT_FOUND" ->
                        "No account was found with this email address."

                    exception is FirebaseAuthException &&
                            exception.errorCode == "ERROR_WRONG_PASSWORD" ->
                        "The password is incorrect."

                    exception is FirebaseAuthException &&
                            exception.errorCode == "ERROR_INVALID_CREDENTIAL" ->
                        "The email or password is incorrect."

                    exception is FirebaseAuthException &&
                            exception.errorCode == "ERROR_USER_DISABLED" ->
                        "This account has been disabled."

                    exception is FirebaseAuthException &&
                            exception.errorCode == "ERROR_NETWORK_REQUEST_FAILED" ->
                        "Network error. Check your internet connection and try again."

                    else ->
                        exception?.localizedMessage
                            ?: "Unable to sign in. Please check your details and try again."
                }
            }
        }
    }

    fun sendPasswordReset() {
        val cleanResetEmail = resetEmail.trim()

        resetEmailError =
            cleanResetEmail.isBlank() ||
                    !Patterns.EMAIL_ADDRESS
                        .matcher(cleanResetEmail)
                        .matches()

        resetMessage = ""

        if (resetEmailError) {
            return
        }

        resetLoading = true

        auth.sendPasswordResetEmail(cleanResetEmail)
            .addOnCompleteListener { task ->
                resetLoading = false

                if (task.isSuccessful) {
                    resetMessage =
                        "Password reset instructions have been sent to your email."
                } else {
                    val exception = task.exception

                    resetMessage = when {
                        exception is FirebaseAuthException &&
                                exception.errorCode == "ERROR_USER_NOT_FOUND" ->
                            "No account was found with this email address."

                        exception is FirebaseAuthException &&
                                exception.errorCode == "ERROR_INVALID_EMAIL" ->
                            "Please enter a valid email address."

                        exception is FirebaseAuthException &&
                                exception.errorCode == "ERROR_NETWORK_REQUEST_FAILED" ->
                            "Network error. Check your internet connection and try again."

                        else ->
                            exception?.localizedMessage
                                ?: "Unable to send the reset email. Please try again."
                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFFFBF8),
                        Color(0xFFFFF3EA)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(190.dp)
                .offset(x = 80.dp, y = (-75).dp)
                .clip(RoundedCornerShape(100.dp))
                .background(FireOrange.copy(alpha = 0.10f))
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(230.dp)
                .offset(x = (-100).dp, y = 100.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(FireOrange.copy(alpha = 0.14f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.fire_logo),
                    contentDescription = "Fire Guide Logo",
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = FireDark,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) {
                                append("FIRE ")
                            }

                            withStyle(
                                SpanStyle(
                                    color = FireOrange,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) {
                                append("GUIDE")
                            }
                        },
                        fontSize = 30.sp,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "LEARN • PREPARE • STAY SAFE",
                        color = FireGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 110.dp,
                            topEnd = 110.dp,
                            bottomStart = 70.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .background(Color(0xFFFFE6D5))
            ) {
                Image(
                    painter = painterResource(R.drawable.firefighter),
                    contentDescription = "Firefighter",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = FireDark,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("Welcome ")
                    }

                    withStyle(
                        SpanStyle(
                            color = FireOrange,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("Back!")
                    }
                },
                fontSize = 39.sp,
                lineHeight = 44.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Sign in to continue learning, stay informed\n" +
                        "and build a safer tomorrow.",
                color = FireGray,
                fontSize = 17.sp,
                lineHeight = 24.sp
            )

            Spacer(Modifier.height(25.dp))

            Text(
                text = "Email Address",
                color = if (emailError) Color.Red else FireDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(7.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                    loginError = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
                placeholder = {
                    Text(
                        "Enter your email",
                        color = FireGray.copy(alpha = 0.7f)
                    )
                },
                shape = RoundedCornerShape(18.dp),
                colors = fireTextFieldColors(),
                isError = emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            if (emailError) {
                Text(
                    text = "Enter a valid email address.",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(17.dp))

            Text(
                text = "Password",
                color = if (passwordError) Color.Red else FireDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(7.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                    loginError = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
                placeholder = {
                    Text(
                        "Enter your password",
                        color = FireGray.copy(alpha = 0.7f)
                    )
                },
                visualTransformation =
                    if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        color = FireOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(enabled = !isLoading) {
                                passwordVisible = !passwordVisible
                            }
                            .padding(8.dp)
                    )
                },
                shape = RoundedCornerShape(18.dp),
                colors = fireTextFieldColors(),
                isError = passwordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )

            if (passwordError) {
                Text(
                    text = "Password cannot be empty.",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = {
                            rememberMe = it
                        },
                        enabled = !isLoading,
                        colors = CheckboxDefaults.colors(
                            checkedColor = FireOrange,
                            uncheckedColor = FireDark,
                            checkmarkColor = Color.White
                        )
                    )

                    Text(
                        text = "Remember me",
                        color = FireDark,
                        fontSize = 15.sp
                    )
                }

                Text(
                    text = "Forgot Password?",
                    color = FireOrange,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(enabled = !isLoading) {
                        resetEmail = email.trim()
                        resetEmailError = false
                        resetMessage = ""
                        showForgotPasswordDialog = true
                        onForgotPasswordClick()
                    }
                )
            }

            if (loginError.isNotBlank()) {
                Spacer(Modifier.height(4.dp))

                Text(
                    text = loginError,
                    color = Color.Red,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    val validEmail =
                        email.trim().isNotEmpty() &&
                                Patterns.EMAIL_ADDRESS
                                    .matcher(email.trim())
                                    .matches()

                    val validPassword = password.isNotEmpty()

                    emailError = !validEmail
                    passwordError = !validPassword
                    loginError = ""

                    if (validEmail && validPassword) {
                        signInWithFirebase()
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FireOrange,
                    contentColor = Color.White,
                    disabledContainerColor = FireOrange.copy(alpha = 0.55f),
                    disabledContentColor = Color.White.copy(alpha = 0.85f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Signing In...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "Sign In",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "›",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = FireGray,
                    fontSize = 15.sp
                )

                Text(
                    text = "Sign Up",
                    color = FireOrange,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .clickable(enabled = !isLoading) {
                            onSignUpClick()
                        }
                        .padding(4.dp)
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }

    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!resetLoading) {
                    showForgotPasswordDialog = false
                }
            },
            title = {
                Text(
                    text = "Reset Password",
                    color = FireDark,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter your account email and Firebase will send you a password reset link.",
                        color = FireGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = {
                            resetEmail = it
                            resetEmailError = false
                            resetMessage = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !resetLoading,
                        singleLine = true,
                        label = { Text("Email Address") },
                        isError = resetEmailError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = fireTextFieldColors()
                    )

                    if (resetEmailError) {
                        Text(
                            text = "Enter a valid email address.",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    if (resetMessage.isNotBlank()) {
                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = resetMessage,
                            color = if (
                                resetMessage.startsWith("Password reset")
                            ) {
                                Color(0xFF168A4A)
                            } else {
                                Color.Red
                            },
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        sendPasswordReset()
                    },
                    enabled = !resetLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FireOrange
                    )
                ) {
                    if (resetLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Send Reset Link")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (!resetLoading) {
                            showForgotPasswordDialog = false
                        }
                    }
                ) {
                    Text(
                        "Cancel",
                        color = FireOrange
                    )
                }
            },
            shape = RoundedCornerShape(22.dp),
            containerColor = Color.White
        )
    }
}

@Composable
private fun fireTextFieldColors() =
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
        unfocusedContainerColor = Color.White,
        disabledTextColor = FireGray,
        disabledBorderColor = FieldBorder.copy(alpha = 0.65f),
        disabledContainerColor = Color.White.copy(alpha = 0.7f)
    )

@Preview(
    name = "Fire Guide Sign In",
    device = "spec:width=393dp,height=852dp,dpi=440",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun SignInPreview() {
    FIREGUIDETheme {
        SignInScreen(
            onLoginSuccess = {},
            onSignUpClick = {},
            onForgotPasswordClick = {}
        )
    }
}