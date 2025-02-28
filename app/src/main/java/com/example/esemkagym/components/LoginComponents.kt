package com.example.esemkagym.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun ImageLogo(){
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .padding(top = 10.dp),
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo"
    )
}

@Composable
fun HeaderLogin(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 50.dp,
                end = 50.dp,
                top = 20.dp,
                bottom = 20.dp
            ),
    ){
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Sign In",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    fontSize = 26.sp
                ),
                color = Color.Black
            )
            
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                text = "Hi there! Build your muscle with us!",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                ),
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 50.dp, end = 50.dp)){
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
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit
){
    var passwordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 50.dp, end = 50.dp, bottom = 25.dp)){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Password",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.orange)
            )

            TextField(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = { Text(text = "Password")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            painter = painterResource(id = if(passwordVisible) R.drawable.round_visibility_24 else R.drawable.round_visibility_off_24),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun LogButton(onClick: () -> Unit){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 10.dp, start = 50.dp, end = 50.dp, bottom = 10.dp),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange))
    ) {
        Text(
            text = "Sign In",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontStyle = FontStyle.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ClickAbleText(onClick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp),
        contentAlignment = Alignment.Center,
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "or",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp
            )
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(id = R.color.orange),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    ) {
                        append("Sign Up")
                    }
                },
                onClick = { offset ->
                    onClick()
                }
            )
        }
    }

}