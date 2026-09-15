package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SportDashboard(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

// Data Models
data class Match(val team1: String, val team2: String, val score: String, val competition: String)
data class News(val title: String, val source: String, val summary: String)
data class GroupStandings(val team: String, val points: Int, val played: Int)

// Mock Data
val todayMatches = listOf(
    Match("PSG", "Barça", "2 - 3", "Ligue des Champions"),
    Match("Real Madrid", "Bayern", "2 - 2", "Ligue des Champions"),
    Match("Côte d'Ivoire", "Gabon", "1 - 0", "Éliminatoires CM 2026")
)

val canGroupC = listOf(
    GroupStandings("Côte d'Ivoire", 0, 0),
    GroupStandings("Zambie", 0, 0),
    GroupStandings("Sierra Leone", 0, 0),
    GroupStandings("Tchad", 0, 0)
)

val pressReviews = listOf(
    News("Mbappé vers le Real Madrid ?", "L'Équipe", "Le transfert de la star française semble imminent selon les dernières rumeurs..."),
    News("La crise au Bayern", "Bild", "Après une série de mauvais résultats, l'entraîneur est sur la sellette..."),
    News("Juventus en reconstruction", "Tuttosport", "Le club turinois prépare une large revue d'effectif pour la saison prochaine...")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportDashboard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH).format(Date())

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Sport 225", fontWeight = FontWeight.Bold)
                        Text("@s_sanogo • $currentDate", style = MaterialTheme.typography.labelSmall)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Découvrez les dernières actualités sur Sport 225 !")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Partager avec..."))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Partager")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                SectionTitle("Matchs du jour & LDC")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(todayMatches) { match ->
                        MatchCard(match)
                    }
                }
            }
            
            item {
                SectionTitle("CAN 2027 - Groupe C")
                GroupStandingsCard(canGroupC)
            }
            
            item {
                SectionTitle("Revue de Presse")
            }
            items(pressReviews) { news ->
                NewsCard(news)
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 12.dp, end = 16.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun MatchCard(match: Match) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = match.competition,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(match.team1, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(
                    match.score,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(match.team2, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun GroupStandingsCard(standings: List<GroupStandings>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Équipe", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("J", fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp))
                Text("Pts", fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp))
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            standings.forEach { team ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(team.team, modifier = Modifier.weight(1f))
                    Text(team.played.toString(), modifier = Modifier.width(32.dp))
                    Text(team.points.toString(), fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp))
                }
            }
        }
    }
}

@Composable
fun NewsCard(news: News) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = news.source,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
