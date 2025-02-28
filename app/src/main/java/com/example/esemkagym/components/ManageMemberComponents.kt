package com.example.esemkagym.components

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esemkagym.R
import com.example.esemkagym.dataclass.User
import com.example.esemkagym.sharedperferences.SharedPreference
import com.example.esemkagym.viewmodel.ManageViewModel
import kotlinx.coroutines.launch

@Composable
fun HeaderMember(manageViewModel: ManageViewModel){
    var selectedCard by remember {
        mutableStateOf("Active Member")
    }

    Box(modifier = Modifier.fillMaxWidth()){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Manage Member",
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )
            Row {
                CustomCard(
                    text = "Active Members",
                    isSelected = selectedCard == "Active Member",
                    onClick = {
                        selectedCard = "Active Member"
                    }
                )
                CustomCard(
                    text = "Past Members", isSelected = selectedCard == "Past Member",
                    onClick = {
                        selectedCard = "Past Member"
                    },
                )
                CustomCard(
                    text = "Pending Approval",
                    isSelected = selectedCard == "Pending Approval",
                    onClick = {
                        selectedCard = "Pending Approval"
                    }
                )
            }

            when(selectedCard){
                "Active Member" -> ActiveMemberListScreen(manageViewModel)
                "Past Member" -> InactiveMemberListScreen(manageViewModel)
                "Pending Approval" -> PendingMemberListScreen(manageViewModel)
            }
        }
    }
}

@Composable
fun CustomCard(text: String, isSelected: Boolean, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp,
                ),
            )
            .background(if (isSelected) Color.LightGray else Color.White)
            .clickable(onClick = onClick)
            .height(40.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            fontStyle = FontStyle.Normal
        )
    }
}


@Composable
fun SearchTextField(onSearchQuery: (String) -> Unit) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        TextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                onSearchQuery(newText)
            },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            placeholder = {
                Text(
                    text = "Search Members",
                    color = Color.LightGray,
                    style = TextStyle(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.round_search_24),
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(5.dp),
        )
    }
}

@Composable
fun ActiveMemberListScreen(manageViewModel: ManageViewModel) {
    val context = LocalContext.current
    var query by rememberSaveable {
        mutableStateOf("")
    }
    val activeMembers by manageViewModel.activeMember.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    val filteredActiveMember by manageViewModel.filterActiveMember.observeAsState(emptyList())

    // Fetch data when the composable is first composed
    LaunchedEffect(Unit) {
        manageViewModel.fetchActiveMember()
    }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        SearchTextField(
            onSearchQuery = {
                query = it
                manageViewModel.searchActiveMember(it)
            },
        )

        val displayData = if(query.isEmpty()) activeMembers else filteredActiveMember

        displayData.forEach { am ->
            ManageMemberActive(
                name = am.name,
                onClick = {
                    coroutineScope.launch {
                        try{
                            val response = manageViewModel.resumeMember(am.id.toString())
                            Toast.makeText(context, "Membership updated", Toast.LENGTH_SHORT).show()
                            manageViewModel.fetchActiveMember()
                            Log.d("Check", "$response")
                        }catch (e: Exception){
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                member = am.membershipEnd
            )
        }
    }
}

@Composable
fun InactiveMemberListScreen(manageViewModel: ManageViewModel) {
    val inactiveMember by manageViewModel.inactiveMember.observeAsState(emptyList())
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var query by rememberSaveable {
        mutableStateOf("")
    }
    val filteredInactiveMember by manageViewModel.filterInactiveMember.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        manageViewModel.fetchInactiveMember()
    }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        SearchTextField(
            onSearchQuery = {
                query = it
                manageViewModel.searchInactiveMember(it)
            },
        )

        val displayData = if(query.isEmpty()) inactiveMember else filteredInactiveMember
        displayData.forEach { im ->
            ManageMemberInactive(
                name = im.name,
                onClick = {
                    coroutineScope.launch {
                        try {
                            manageViewModel.resumeMember(im.id.toString())
                            Toast.makeText(context, "Membership extended", Toast.LENGTH_LONG).show()
                            manageViewModel.fetchInactiveMember()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                member = im.membershipEnd
            )
        }
    }
}

@Composable
fun PendingMemberListScreen(manageViewModel: ManageViewModel) {
    val pendingMember by manageViewModel.pendingMember.observeAsState(emptyList())
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var query by rememberSaveable {
        mutableStateOf("")
    }

    val filteredPendingMember by manageViewModel.filterPendingMember.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        manageViewModel.fetchPendingMember()
    }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        SearchTextField(
            onSearchQuery = {
                query = it
                manageViewModel.searchPendingMember(it)
            },
        )

        val displayData = if (query.isEmpty()) pendingMember else filteredPendingMember
        displayData.forEach { pm ->
            ManageMemberPendingActive(
                name = pm.name,
                onClick = {
                    coroutineScope.launch {
                        try {
                            val response = manageViewModel.approveMember(pm.id.toString())
                            Toast.makeText(context, "Membership approved", Toast.LENGTH_LONG).show()
                            manageViewModel.fetchPendingMember()
                            Log.d("Response", "$response")
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                member = pm.registerAt
            )
        }
    }
}

@Composable
fun ManageMemberActive(
    name: String,
    onClick: () -> Unit,
    member: String
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Member until $member",
                    color = Color.LightGray,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 10.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(
                        text = "Resume",
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.heightIn(5.dp))
            Divider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.clip(
                RoundedCornerShape(5.dp)
            ))
        }
    }
}

@Composable
fun ManageMemberInactive(
    name: String,
    onClick: () -> Unit,
    member: String
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Last Membership at $member",
                    color = Color.LightGray,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 10.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(
                        text = "Resume",
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.heightIn(5.dp))
            Divider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.clip(
                RoundedCornerShape(5.dp)
            ))
        }
    }
}

@Composable
fun ManageMemberPendingActive(
    name: String,
    onClick: () -> Unit,
    member: String
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Register at $member",
                    color = Color.LightGray,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 10.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(
                        text = "Confirm",
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.heightIn(5.dp))
            Divider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.clip(
                RoundedCornerShape(5.dp)
            ))
        }
    }
}