package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) { innerPadding ->
                    Sport225App(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

enum class SportTab(val label: String) {
    TOUT("À la Une"),
    MATCHS("Matchs & LDC"),
    CAN("CAN 2027"),
    CLASSEMENTS("Classements"),
    MONDIAL("Mondial 2026"),
    PRESSE("Presse & Actus")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sport225App(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedTab by remember { mutableStateOf(SportTab.TOUT) }

    fun shareDirect() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, SportData.generateFullShareText())
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Partager le flash Sport 225 sur Facebook ou d'autres applis"))
    }

    fun copyForFacebook() {
        clipboardManager.setText(AnnotatedString(SportData.generateFullShareText()))
        Toast.makeText(context, "Texte complet copié pour Facebook !", Toast.LENGTH_LONG).show()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SPORT 225",
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(OrangePrimary)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "CIV",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            text = "${SportData.AUTEUR} • ${SportData.JOUR}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { copyForFacebook() }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copier pour Facebook",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { shareDirect() }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Partager",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { shareDirect() },
                containerColor = OrangePrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Share, contentDescription = "Partager") },
                text = { Text("Partager sur Facebook", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(16.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Barre d'onglets défilable
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = GreenPrimary,
                divider = {}
            ) {
                SportTab.values().forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Text(
                                text = tab.label,
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == tab) GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                // Section Hero Flash Info (visible dans TOUT et CAN)
                if (selectedTab == SportTab.TOUT || selectedTab == SportTab.CAN) {
                    item {
                        ElephantsHeroCard()
                    }
                }

                // MATCHS DU JOUR & LDC
                if (selectedTab == SportTab.TOUT || selectedTab == SportTab.MATCHS) {
                    item {
                        SectionHeader(
                            title = "Matchs du Jour & Week-End",
                            subtitle = "Ligue 1, Premier League, Saudi League & CAF",
                            icon = Icons.Default.SportsSoccer
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(SportData.upcomingMatches) { match ->
                                UpcomingMatchCard(match)
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Résultats Récents Ligue des Champions",
                            subtitle = "Chocs européens J2 & exploits",
                            icon = Icons.Default.EmojiEvents
                        )
                    }

                    items(SportData.ldcResults) { result ->
                        LdcResultItem(result)
                    }
                }

                // CAN 2027
                if (selectedTab == SportTab.TOUT || selectedTab == SportTab.CAN) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Éliminatoires CAN 2027",
                            subtitle = "Calendrier des rencontres & 12 Groupes (A à L)",
                            icon = Icons.Default.Public
                        )
                        CanCalendarSection()
                        CanGroupsViewer()
                    }
                }

                // CLASSEMENTS
                if (selectedTab == SportTab.TOUT || selectedTab == SportTab.CLASSEMENTS) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Classements des Championnats",
                            subtitle = "Ligue 1, Premier League, La Liga & CAN Grp C",
                            icon = Icons.Default.FormatListNumbered
                        )
                        StandingsInteractiveSection()
                    }
                }

                // MONDIAL 2026
                if (selectedTab == SportTab.TOUT || selectedTab == SportTab.MONDIAL) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Mondial 2026 - Zone Afrique",
                            subtitle = "Statut des qualifications & Résultats récents",
                            icon = Icons.Default.Flag
                        )
                        MondialSection()
                    }
                }

                // ACTUALITÉS & REVUE DE PRESSE
                if (selectedTab == SportTab.TOUT || selectedTab == SportTab.PRESSE) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Actualités Brûlantes",
                            subtitle = "Les dernières nouvelles du ballon rond",
                            icon = Icons.Default.Campaign
                        )
                    }
                    items(SportData.actus) { actu ->
                        ActuCard(actu)
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(
                            title = "Revue de Presse",
                            subtitle = "L'Équipe, Marca, Fraternité Matin (cliquer pour lire)",
                            icon = Icons.Default.MenuBook
                        )
                    }
                    items(SportData.revuePresse) { presse ->
                        PresseCard(presse)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GreenPrimary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ElephantsHeroCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(OrangePrimary, OrangeDark)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "CHOC CAN 2027 • J1",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Text(
                        text = "24 SEPT • 19H00",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🇨🇮",
                            fontSize = 36.sp
                        )
                        Text(
                            text = "Côte d'Ivoire",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Les Éléphants",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "VS",
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = Color.White
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🇬🇭",
                            fontSize = 36.sp
                        )
                        Text(
                            text = "Ghana",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Black Stars",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Stade Alassane Ouattara d'Ebimpé, Abidjan",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun UpcomingMatchCard(match: MatchItem) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.league,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(GreenLight)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = match.time,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenDark
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.team1,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "vs",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = match.team2,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                text = "Coup d'envoi à ${match.time}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LdcResultItem(result: LdcResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(GreenLight)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = result.score,
                    fontWeight = FontWeight.Black,
                    color = GreenDark,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "vs ${result.adv}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = result.detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CanCalendarSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Calendrier des Rencontres",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = GreenPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            SportData.canCalendar.forEachIndexed { index, fixture ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (fixture.isElephants) OrangeLight else MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${fixture.date} • ${fixture.time}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (fixture.isElephants) OrangeDark else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${fixture.team1} - ${fixture.team2}",
                            fontWeight = if (fixture.isElephants) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = fixture.venue,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (index < SportData.canCalendar.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Composable
fun CanGroupsViewer() {
    var selectedGroupKey by remember { mutableStateOf("C") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Les 12 Groupes Éliminatoires (A à L)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = GreenPrimary
                )
                Text(
                    text = "Groupe $selectedGroupKey",
                    fontWeight = FontWeight.Bold,
                    color = OrangePrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sélecteur de groupes A à L
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SportData.canGroups.keys.sorted().forEach { groupKey ->
                    val isSelected = groupKey == selectedGroupKey
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) GreenPrimary else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { selectedGroupKey = groupKey },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = groupKey,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val teams = SportData.canGroups[selectedGroupKey] ?: emptyList()
            Text(
                text = "Équipes du Groupe $selectedGroupKey :",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            teams.forEachIndexed { i, team ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (team.contains("Côte d'Ivoire")) OrangePrimary else GreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${i + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (team.contains("Côte d'Ivoire")) Color.White else GreenDark
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = team,
                        fontWeight = if (team.contains("Côte d'Ivoire")) FontWeight.Bold else FontWeight.Medium,
                        color = if (team.contains("Côte d'Ivoire")) OrangeDark else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun StandingsInteractiveSection() {
    var selectedLeagueIndex by remember { mutableStateOf(0) }
    val currentStanding = SportData.classements.getOrNull(selectedLeagueIndex) ?: SportData.classements.first()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Chips pour basculer de championnat
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SportData.classements.forEachIndexed { index, item ->
                    FilterChip(
                        selected = selectedLeagueIndex == index,
                        onClick = { selectedLeagueIndex = index },
                        label = { Text(item.competition.split(" (").first()) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GreenLight,
                            selectedLabelColor = GreenDark
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = currentStanding.competition,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = GreenPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            currentStanding.lignes.forEach { ligne ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ligne,
                        fontWeight = if (ligne.contains("Côte d'Ivoire") || ligne.startsWith("1.")) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun MondialSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GreenLight)
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = SportData.mondialInfo.qualifie,
                        fontWeight = FontWeight.Bold,
                        color = GreenDark,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Derniers Résultats des Éliminatoires :",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            SportData.mondialInfo.resultats.forEach { matchScore ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = null,
                        tint = OrangePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = matchScore,
                        fontWeight = if (matchScore.contains("Côte d'Ivoire")) FontWeight.Bold else FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ActuCard(actu: ActuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = actu.titre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = actu.detail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PresseCard(presse: PresseItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = spring()),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (presse.journal.contains("Fraternité")) OrangeLight else GreenLight)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = presse.journal,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (presse.journal.contains("Fraternité")) OrangeDark else GreenDark
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Réduire" else "Développer",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "« ${presse.titre} »",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = presse.resume,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Appuyez pour lire l'analyse...",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
