package com.example.scheduleapp.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheduleapp.R
import com.example.scheduleapp.ui.theme.Typography
import com.example.scheduleapp.ui.theme.blue
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.gold
import com.example.scheduleapp.ui.theme.redError
import com.example.scheduleapp.ui.theme.whiteAlpha

@Preview(showBackground = true)
@Composable
fun LoginScreen(modifier: Modifier = Modifier, viewModel: LoginViewModel = viewModel()) {
    val emailInput = remember { mutableStateOf("") }
    val passwordInput = remember { mutableStateOf("") }
    val errorState by viewModel.errorState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize().background(blue),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login),
                style = Typography.titleLarge,
                fontSize = 36.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            EmailTextField(emailInput)
            Spacer(Modifier.height(16.dp))
            PasswordTextField(passwordInput)
            Spacer(Modifier.height(32.dp))
            when {
                errorState.isFailure -> {
                    Text(errorState.message, color = redError)
                    Spacer(Modifier.height(32.dp))
                }
            }
            val context = LocalContext.current
            Button(
                modifier = Modifier.fillMaxWidth(0.4f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkBlue
                ),
                onClick = {
                    val userData = UserData(emailInput.value, passwordInput.value)
                    viewModel.signIn(userData, context)
                }
            ) {
                Text(
                    stringResource(R.string.login_button),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun EmailTextField(input: MutableState<String>) {
    TextField(
        value = input.value,
        onValueChange = {
            input.value = it
        },
        placeholder = {
            Text(stringResource(R.string.email), color = whiteAlpha)
        },
        maxLines = 1,
        leadingIcon = {
            Icon(Icons.Outlined.Email, contentDescription = "email", tint = whiteAlpha)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = gold,
            focusedTextColor = whiteAlpha,
            unfocusedTextColor = whiteAlpha
        ),
        modifier = Modifier
            .fillMaxWidth(0.75f)
    )
}

@Composable
fun PasswordTextField(input: MutableState<String>) {
    val (passwordVisible, setPasswordVisible) = remember { mutableStateOf(false) }
    TextField(
        value = input.value,
        onValueChange = {
            input.value = it
        },
        placeholder = {
            Text(stringResource(R.string.password), color = whiteAlpha)
        },
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = gold,
            focusedTextColor = whiteAlpha,
            unfocusedTextColor = whiteAlpha
        ),
        leadingIcon = {
            Icon(
                Icons.Outlined.Lock,
                contentDescription = "password",
                tint = whiteAlpha
            )
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { setPasswordVisible(!passwordVisible) }) {
                if (passwordVisible)
                    Icon(
                        painter = painterResource(R.drawable.outline_visibility_24),
                        contentDescription = "Show password",
                        tint = whiteAlpha
                    )
                else
                    Icon(
                        painter = painterResource(R.drawable.outline_visibility_off_24),
                        contentDescription = "Hide password",
                        tint = whiteAlpha
                    )
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.75f)
    )
}