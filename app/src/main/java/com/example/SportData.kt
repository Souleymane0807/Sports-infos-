package com.example

object SportData {
    const val DATE = "Vendredi 18 Septembre 2026"
    const val JOUR = "Vendredi 18 Sept"
    const val AUTEUR = "@s_sanogo"

    // 1. Groupes Éliminatoires CAN 2027 (12 Groupes)
    val canGroups = mapOf(
        "A" to listOf("Maroc", "Gabon", "Niger", "Lesotho"),
        "B" to listOf("Égypte", "Angola", "Malawi", "Soudan du Sud"),
        "C" to listOf("Côte d'Ivoire", "Ghana", "Gambie", "Somalie"),
        "D" to listOf("Afrique du Sud", "Guinée", "Kenya", "Érythrée"),
        "E" to listOf("RD Congo", "Guinée Équatoriale", "Sierra Leone", "Zimbabwe"),
        "F" to listOf("Burkina Faso", "Bénin", "Mauritanie", "Centrafrique"),
        "G" to listOf("Cameroun", "Comores", "Namibie", "Congo"),
        "H" to listOf("Tunisie", "Ouganda", "Libye", "Botswana"),
        "I" to listOf("Algérie", "Zambie", "Togo", "Burundi"),
        "J" to listOf("Sénégal", "Mozambique", "Soudan", "Éthiopie"),
        "K" to listOf("Mali", "Cap-Vert", "Rwanda", "Libéria"),
        "L" to listOf("Nigéria", "Madagascar", "Tanzanie", "Guinée-Bissau")
    )

    // 2. Calendrier CAN 2027
    val canCalendar = listOf(
        CanFixture("Côte d'Ivoire", "Ghana", "24 sept", "19h00", "Stade Alassane Ouattara, Ebimpé", isElephants = true),
        CanFixture("Gambie", "Somalie", "25 sept", "16h00", "Bakau"),
        CanFixture("Maroc", "Gabon", "25 sept", "19h00", "Rabat"),
        CanFixture("Niger", "Lesotho", "25 sept", "15h00", "Niamey")
    )

    // 3. Matchs du jour / Week-end
    val upcomingMatches = listOf(
        MatchItem("20:45", "Monaco", "Lyon", "Ligue 1 - J5 - Stade Louis-II"),
        MatchItem("21:00", "Chelsea", "Liverpool", "Premier League - J5 - Stamford Bridge"),
        MatchItem("21:00", "Al-Ahli", "Al-Nassr", "Saudi Pro League - J8 - King Abdullah Stadium"),
        MatchItem("16:00", "ASEC Mimosas", "Power Dynamos", "LDC CAF - Stade Félix Houphouët-Boigny")
    )

    // 4. Résultats récents / LDC
    val ldcResults = listOf(
        LdcResult("PSG 2-2", "FC Barcelone", "Parc des Princes - Doublé de Dembélé / Raphinha & Yamal"),
        LdcResult("Real Madrid 3-1", "Bayern Munich", "Bernabéu - Doublé Mbappé, Bellingham / Kane"),
        LdcResult("Arsenal 1-0", "Inter Milan", "Emirates Stadium - But de Saka (68e)"),
        LdcResult("Man City 2-0", "Naples", "Etihad Stadium - Haaland & Foden"),
        LdcResult("Liverpool 2-1", "Atlético Madrid", "Anfield - Salah (82e)"),
        LdcResult("Slavia Prague 2-3", "RC Lens", "Lens renverse le match dans les arrêts de jeu")
    )

    // 5. Classements des principaux championnats & Poules
    val classements = listOf(
        LeagueStanding(
            competition = "Ligue 1 (France)",
            lignes = listOf(
                "1. PSG - 12 pts (+10)",
                "2. Marseille - 10 pts (+6)",
                "3. Monaco - 10 pts (+4)",
                "4. Lille - 9 pts (+3)"
            )
        ),
        LeagueStanding(
            competition = "Premier League (Angleterre)",
            lignes = listOf(
                "1. Manchester City - 12 pts (+9)",
                "2. Arsenal - 10 pts (+5)",
                "3. Liverpool - 9 pts (+4)",
                "4. Chelsea - 8 pts (+2)"
            )
        ),
        LeagueStanding(
            competition = "La Liga (Espagne)",
            lignes = listOf(
                "1. FC Barcelone - 15 pts (+11)",
                "2. Real Madrid - 13 pts (+8)",
                "3. Atlético Madrid - 11 pts (+5)"
            )
        ),
        LeagueStanding(
            competition = "Éliminatoires CAN 2027 - Groupe C",
            lignes = listOf(
                "1. Côte d'Ivoire - 0 pt (0 m)",
                "2. Ghana - 0 pt (0 m)",
                "3. Gambie - 0 pt (0 m)",
                "4. Somalie - 0 pt (0 m)"
            )
        )
    )

    // 6. Mondial 2026 - Qualifications
    val mondialInfo = MondialData(
        qualifie = "Maroc : Déjà qualifié pour la phase finale 2026",
        resultats = listOf(
            "Côte d'Ivoire 3-0 Gabon (Stade d'Ebimpé)",
            "Zambie 2-0 Maroc",
            "Soudan du Sud 1-4 RD Congo",
            "Lesotho 0-3 Afrique du Sud"
        )
    )

    // 7. Actualités
    val actus = listOf(
        ActuItem(
            titre = "Éliminatoires CAN 2027 : La Côte d'Ivoire prête pour le choc contre le Ghana",
            detail = "Les Éléphants reçoivent les Black Stars le 24 septembre pour la première journée du Groupe C à Ebimpé."
        ),
        ActuItem(
            titre = "Ligue des Champions : Mbappé et Dembélé régalent en J2",
            detail = "Le Real Madrid s'impose face au Bayern (3-1) tandis que le PSG et le Barça se neutralisent dans un match d'anthologie (2-2)."
        ),
        ActuItem(
            titre = "Ligue 1 : Le PSG et Barcelone intouchables sur le plan national",
            detail = "Les deux géants européens maintiennent leur rythme parfait en championnat avant la trêve internationale."
        )
    )

    // 8. Revue de presse
    val revuePresse = listOf(
        PresseItem(
            journal = "L'Équipe",
            titre = "Le show continue",
            resume = "Analyse des performances du PSG, de Monaco et de Lens en Europe."
        ),
        PresseItem(
            journal = "Marca",
            titre = "Mbappé magistral",
            resume = "Le Real Madrid confirme son statut de favori après sa victoire 3-1 contre le Bayern."
        ),
        PresseItem(
            journal = "Fraternité Matin",
            titre = "Objectif CAN 2027",
            resume = "Focus sur la préparation des Éléphants de Côte d'Ivoire avant d'affronter le Ghana."
        )
    )

    // Formatteur de texte complet pour publication Facebook ou partage rapide
    fun generateFullShareText(): String {
        return buildString {
            appendLine("⚽ SPORT 225 - ÉDITION DU $DATE")
            appendLine("Rédigé par $AUTEUR")
            appendLine("----------------------------------------")
            appendLine("🔥 MATCHS DU JOUR & WEEK-END :")
            upcomingMatches.forEach {
                appendLine("• ${it.time} | ${it.team1} vs ${it.team2} (${it.league})")
            }
            appendLine()
            appendLine("🏆 RÉSULTATS RÉCENTS LDC :")
            ldcResults.forEach {
                appendLine("• ${it.score} vs ${it.adv} (${it.detail})")
            }
            appendLine()
            appendLine("🌍 CAN 2027 - CHOCS À VENIR :")
            canCalendar.forEach {
                appendLine("• ${it.team1} vs ${it.team2} - ${it.date} à ${it.time} (${it.venue})")
            }
            appendLine()
            appendLine("🌍 MONDIAL 2026 (Afrique) :")
            appendLine("• ${mondialInfo.qualifie}")
            mondialInfo.resultats.forEach {
                appendLine("• $it")
            }
            appendLine()
            appendLine("📰 REVUE DE PRESSE :")
            revuePresse.forEach {
                appendLine("• ${it.journal} : « ${it.titre} » - ${it.resume}")
            }
            appendLine()
            appendLine("#Sport225 #CIV #CAN2027 #LigueDesChampions #Football")
        }
    }
}

data class CanFixture(
    val team1: String,
    val team2: String,
    val date: String,
    val time: String,
    val venue: String,
    val isElephants: Boolean = false
)

data class MatchItem(
    val time: String,
    val team1: String,
    val team2: String,
    val league: String
)

data class LdcResult(
    val score: String,
    val adv: String,
    val detail: String
)

data class LeagueStanding(
    val competition: String,
    val lignes: List<String>
)

data class MondialData(
    val qualifie: String,
    val resultats: List<String>
)

data class ActuItem(
    val titre: String,
    val detail: String
)

data class PresseItem(
    val journal: String,
    val titre: String,
    val resume: String
)
