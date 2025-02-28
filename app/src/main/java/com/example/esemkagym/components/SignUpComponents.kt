package com.example.esemkagym.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esemkagym.R

@Composable
fun HeaderSignUp(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 30.dp)){
        Column {
            Text(
                text = "Sign Up",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 26.sp,
                color = Color.Black
            )
            
            Text(
                text = "Hi there! Register your gym member!",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                color = Color.LightGray,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}

@Composable
fun EmailSignUp(
    email: String,
    onEmailChange: (String) -> Unit
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 30.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Email",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = colorResource(id = R.color.orange)
            )

            TextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = { Text(text = "Email")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun PasswordSignUp(
    password: String,
    onPasswordChange: (String) -> Unit
){
    var passwordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 20.dp)
    ){
        Column {
            Text(
                text = "Password",
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                color =  colorResource(id = R.color.orange)
            )
            TextField(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = { Text(text = "Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ){
                        Icon(
                            painter = painterResource(id = if(passwordVisible) R.drawable.round_visibility_24 else R.drawable.round_visibility_off_24),
                            contentDescription = if(passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun NameTextField(name: String, onNameChange: (String) -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 20.dp)){
        Column {
            Text(
                text = "Name",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                color = colorResource(id = R.color.orange)
            )

            TextField(
                value = name,
                onValueChange = onNameChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                placeholder = { Text(text = "Your Name") }
            )
        }
    }
}

@Composable
fun GenderRadio(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
){
    val options = listOf("Male", "Female")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(selectedGender) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 20.dp,)){
        Column {
            Text(
                text = "Gender",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                color = colorResource(id = R.color.orange)
            )
            
            options.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                onGenderSelected(text)
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = text == selectedOption,
                        onClick = {
                            onOptionSelected(text)
                            onGenderSelected(text)
                        },
                    )
                    Text(text = text)
                }
            }
        }
    }
}

@Composable
fun SignUpButton(onClick: () -> Unit){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 20.dp, start = 30.dp, end = 30.dp),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange))
    ) {
        Text(
            text = "Sign Up",
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ClickAbleSignIn(onClick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 20.dp),
        contentAlignment = Alignment.Center
    ){
        Row {
            Text(
                text = "Have an account?",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp,
                color = Color.LightGray
            )
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(id = R.color.orange),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        ),
                    ) {
                        append(" Sign In")
                    }
                },
                onClick = { offset ->
                    onClick()
                }
            )
        }
    }
}

