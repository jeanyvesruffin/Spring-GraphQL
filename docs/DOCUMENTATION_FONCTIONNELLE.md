# Documentation Fonctionnelle — Retro-League Manager

> **Version** : 1.0  
> **Date** : 01/04/2026  
> **Projet** : Retro-League Manager  
> **Stack** : Java 24 / Spring Boot 4.0.5 / Spring GraphQL / Angular 21 / IBM DB2

---

## Table des matières

1. [Présentation générale](#1-présentation-générale)
2. [Architecture technique](#2-architecture-technique)
3. [Modèle de données](#3-modèle-de-données)
4. [Navigation & Structure de l'interface](#4-navigation--structure-de-linterface)
5. [Pages fonctionnelles](#5-pages-fonctionnelles)
   - 5.1 [Dashboard (Tableau de bord)](#51-dashboard-tableau-de-bord)
   - 5.2 [Ligues](#52-ligues)
   - 5.3 [Tournoi / Bracket](#53-tournoi--bracket)
   - 5.4 [Joueurs](#54-joueurs)
   - 5.5 [Profil joueur](#55-profil-joueur)
   - 5.6 [Succès & Badges](#56-succès--badges)
   - 5.7 [Chaos Engine](#57-chaos-engine)
6. [API GraphQL](#6-api-graphql)
7. [Fonctionnalités temps réel](#7-fonctionnalités-temps-réel)
8. [Règles métier](#8-règles-métier)
9. [Glossaire](#9-glossaire)

---

## 1. Présentation générale

**Retro-League Manager** est une plateforme de gestion et de simulation de tournois d'e-sport "vintage". Elle propose une interface au design rétro-futuriste (thème sombre néon) permettant de :

- Gérer des ligues, équipes et joueurs
- Suivre des matchs en temps réel avec scores live
- Simuler des tournois à élimination directe (brackets)
- Injecter des événements aléatoires via le **Chaos Engine**
- Débloquer des succès et badges basés sur les performances
- Consulter des statistiques détaillées par joueur, équipe et ligue

### Public cible

Administrateurs de ligues e-sport, joueurs et spectateurs souhaitant une expérience immersive de gestion de tournois.

### Objectifs du produit

| Objectif | Description |
|---|---|
| **Gestion complète** | CRUD sur les ligues, équipes, joueurs et matchs |
| **Temps réel** | Scores live, événements Chaos Engine, subscriptions GraphQL |
| **Gamification** | Système de succès/badges, classements, niveaux joueurs |
| **Flexibilité** | API GraphQL permettant de consommer uniquement les données nécessaires |
| **Performance** | Virtual Threads Java 24 pour la simulation massive de matchs |

---

## 2. Architecture technique

```
┌─────────────────────┐       GraphQL        ┌──────────────────────┐
│   Frontend Angular   │◄────────────────────►│  Backend Spring Boot │
│   (Angular 21)       │   Queries/Mutations  │  (Java 24)           │
│   Signals + Router   │   Subscriptions WS   │  Spring GraphQL      │
└─────────────────────┘                       └──────────┬───────────┘
                                                         │
                                                         │ JDBC
                                                         ▼
                                              ┌──────────────────────┐
                                              │      IBM DB2         │
                                              │  (Base relationnelle)│
                                              └──────────────────────┘
```

| Composant | Technologie | Rôle |
|---|---|---|
| **Frontend** | Angular 21 | Interface utilisateur réactive (Signals) |
| **Backend** | Spring Boot 4.0.5 / Java 24 | API GraphQL, logique métier, Chaos Engine |
| **Base de données** | IBM DB2 | Stockage transactionnel (scores, classements, succès) |
| **API** | Spring GraphQL | Queries, Mutations, Subscriptions temps réel |

---

## 3. Modèle de données

> Référence visuelle :  ![`docs/schema_bdd.png`](schema_bdd.png)

Le modèle comprend **6 tables** principales :

### 3.1 LEAGUE (Ligue)

| Colonne | Type | Description |
|---|---|---|
| `ID` | BIGINT (PK, auto) | Identifiant unique |
| `NAME` | VARCHAR(200) | Nom de la ligue (ex: "Gold Division") |
| `CODE` | VARCHAR(50) | Code unique (ex: "GOLD_DIV") |
| `COUNTRY` | VARCHAR(100) | Pays d'origine |
| `CREATED_AT` | TIMESTAMP | Date de création |
| `UPDATED_AT` | TIMESTAMP | Dernière mise à jour |

### 3.2 TEAM (Équipe)

| Colonne | Type | Description |
|---|---|---|
| `ID` | BIGINT (PK, auto) | Identifiant unique |
| `LEAGUE_ID` | BIGINT (FK → LEAGUE) | Ligue d'appartenance |
| `NAME` | VARCHAR(200) | Nom de l'équipe (ex: "Cyber Kings") |
| `SHORT_NAME` | VARCHAR(50) | Nom abrégé |
| `FOUNDED_YEAR` | INTEGER | Année de fondation |
| `CITY` | VARCHAR(100) | Ville |
| `STADIUM` | VARCHAR(200) | Stade / Arène |
| `CREATED_AT` | TIMESTAMP | Date de création |

**Contrainte** : Unicité `(LEAGUE_ID, NAME)` — pas deux équipes du même nom dans une même ligue.

### 3.3 PLAYER (Joueur)

| Colonne | Type | Description |
|---|---|---|
| `ID` | BIGINT (PK, auto) | Identifiant unique |
| `TEAM_ID` | BIGINT (FK → TEAM) | Équipe d'appartenance |
| `FIRST_NAME` | VARCHAR(100) | Prénom |
| `LAST_NAME` | VARCHAR(100) | Nom de famille |
| `BIRTHDATE` | DATE | Date de naissance |
| `POSITION` | VARCHAR(50) | Poste (ex: "Capitaine") |
| `NUMBER` | INTEGER | Numéro de maillot |
| `NATIONALITY` | VARCHAR(100) | Nationalité |
| `CONTRACT_UNTIL` | DATE | Fin de contrat |
| `CREATED_AT` | TIMESTAMP | Date de création |

### 3.4 MATCHES (Matchs)

| Colonne | Type | Description |
|---|---|---|
| `ID` | BIGINT (PK, auto) | Identifiant unique |
| `LEAGUE_ID` | BIGINT (FK → LEAGUE) | Ligue du match |
| `SEASON` | VARCHAR(20) | Saison (ex: "S12") |
| `MATCH_DATE` | TIMESTAMP | Date et heure du match |
| `HOME_TEAM_ID` | BIGINT (FK → TEAM) | Équipe à domicile |
| `AWAY_TEAM_ID` | BIGINT (FK → TEAM) | Équipe à l'extérieur |
| `HOME_SCORE` | INTEGER | Score domicile |
| `AWAY_SCORE` | INTEGER | Score extérieur |
| `STATUS` | VARCHAR(50) | Statut : `SCHEDULED`, `LIVE`, `COMPLETED` |
| `VENUE` | VARCHAR(200) | Lieu du match |
| `CREATED_AT` | TIMESTAMP | Date de création |

**Contrainte** : `HOME_TEAM_ID ≠ AWAY_TEAM_ID` — une équipe ne peut pas jouer contre elle-même.

### 3.5 PLAYER_STATISTIC (Statistiques joueur par match)

| Colonne | Type | Description |
|---|---|---|
| `ID` | BIGINT (PK, auto) | Identifiant unique |
| `MATCH_ID` | BIGINT (FK → MATCHES) | Match concerné |
| `PLAYER_ID` | BIGINT (FK → PLAYER) | Joueur concerné |
| `TEAM_ID` | BIGINT | Équipe au moment du match |
| `MINUTES_PLAYED` | INTEGER | Minutes jouées |
| `GOALS` | INTEGER | Buts marqués |
| `ASSISTS` | INTEGER | Passes décisives |
| `YELLOW_CARDS` | INTEGER | Cartons jaunes |
| `RED_CARDS` | INTEGER | Cartons rouges |
| `SHOTS_ON_TARGET` | INTEGER | Tirs cadrés |
| `CREATED_AT` | TIMESTAMP | Date de création |

**Contrainte** : Unicité `(MATCH_ID, PLAYER_ID)` — une seule ligne de stats par joueur par match.

### 3.6 ACHIEVEMENT (Succès / Badge)

| Colonne | Type | Description |
|---|---|---|
| `ID` | BIGINT (PK, auto) | Identifiant unique |
| `PLAYER_ID` | BIGINT (FK → PLAYER) | Joueur ayant débloqué le succès |
| `TEAM_ID` | BIGINT (FK → TEAM) | Équipe associée |
| `TITLE` | VARCHAR(200) | Titre du succès (ex: "Champion Invaincu") |
| `DESCRIPTION` | CLOB | Description détaillée |
| `ACHIEVED_AT` | DATE | Date d'obtention |
| `CREATED_AT` | TIMESTAMP | Date de création |

### Relations entre entités

```
LEAGUE  1──N  TEAM
TEAM    1──N  PLAYER
LEAGUE  1──N  MATCHES
TEAM    1──N  MATCHES (home / away)
MATCHES 1──N  PLAYER_STATISTIC
PLAYER  1──N  PLAYER_STATISTIC
PLAYER  1──N  ACHIEVEMENT
TEAM    1──N  ACHIEVEMENT
```

---

## 4. Navigation & Structure de l'interface

> Référence visuelle : toutes les maquettes `docs/page_*.png`

### 4.1 Thème visuel

L'interface adopte un style **rétro-futuriste / cyberpunk** :

- **Fond sombre** : nuances de bleu-noir (`#0a0e1a` approx.)
- **Couleurs d'accent** : cyan (`#00ffff`), magenta (`#ff00ff`), jaune néon (`#e6ff00`), vert néon (`#00ff88`)
- **Typographie** : polices monospace / pixel-art pour les titres, style terminal
- **Éléments UI** : bordures lumineuses, barres de progression colorées, icônes pixelisées

### 4.2 Barre de navigation latérale (sidebar)

La sidebar gauche est présente sur toutes les pages et contient les éléments suivants (de haut en bas) :

| Icône | Page | Description |
|---|---|---|
| 🏆 | **Dashboard** | Tableau de bord principal |
| ⚡ | **Ligues** | Gestion et consultation des ligues |
| 📊 | **Classement** | Classements et statistiques |
| 🎮 | **Tournois** | Brackets et arbres de tournois |
| 🏅 | **Succès** | Succès et badges |
| 🌐 | **Paramètres** | Configuration globale |

### 4.3 Barre supérieure (header)

Présente sur chaque page :

- **Logo** : "RL" (Retro League) — coin supérieur gauche
- **Fil d'Ariane** : `RETRO · LEAGUE / [NOM DE LA PAGE]`
- **Badge contextuel** : indicateur d'état (ex: `● LIVE`, `RANG #1`, `42 / 120`)
- **Barre de recherche** : `🔍 Rechercher...`
- **Icône notifications** : cloche
- **Avatar utilisateur** : icône pixelisée

---

## 5. Pages fonctionnelles

### 5.1 Dashboard (Tableau de bord)

> Référence : ![`docs/retro-league-mockup.png`](retro-league-mockup.png)

#### Description

Page d'accueil offrant une vue d'ensemble en temps réel de l'activité de la plateforme.

#### Composants

| Zone | Contenu | Donnée affichée |
|---|---|---|
| **KPI 1** | Matchs joués | Nombre total + variation récente (ex: `2,847` / `↑ +124`) |
| **KPI 2** | Joueurs actifs | Nombre total + variation (ex: `14,392` / `↑ +89`) |
| **KPI 3** | Ligues en cours | Nombre actif + variation (ex: `38` / `↑ +3`) |
| **KPI 4** | Succès débloqués | Nombre total + variation (ex: `847` / `— 0`) |
| **Match en cours** | Scoreboard live | Équipes, score en direct, phase, timer, barre de domination |
| **Programme du jour** | Liste matchs | Horaire, équipes, badge `● LIVE` |

#### Détail du scoreboard live

- Affiche les icônes des deux équipes face à face
- Score en gros chiffres (ex: `14 : 09`)
- Indicateur `● EN JEU` avec détail de la phase (ex: "QUART DE FINALE · MAP 3/5")
- Timer du match (ex: `LIVE · 00:47:32`)
- Barre de domination en pourcentage (ex: "CYBER KINGS 65%" vs "NEON EAGLES 35%")

#### Comportement

- Les KPI se mettent à jour en temps réel via les **Subscriptions GraphQL**
- Le scoreboard affiche le match en cours prioritaire
- Le programme du jour liste les matchs planifiés avec possibilité de cliquer pour accéder au détail

---

### 5.2 Ligues

> Référence : ![`docs/page_ligues.png`](page_ligues.png)

#### Description

Page de gestion et consultation de toutes les ligues enregistrées.

#### Barre d'outils

| Élément | Fonction |
|---|---|
| **Onglets de filtre** | `TOUTES` · `EN COURS` · `TERMINÉES` · `À VENIR` |
| **Filtre saison** | `🔍 Filtrer par saison...` |
| **Bouton action** | `+ CRÉER UNE LIGUE` |
| **Badge header** | `38 LIGUES ACTIVES` |

#### Carte de ligue

Chaque ligue est affichée sous forme de carte contenant :

| Champ | Description | Exemple |
|---|---|---|
| **Statut** | Badge coloré | `● EN COURS`, `● LIVE NOW`, `PLAYOFFS`, `ROOKIE` |
| **Nom** | Nom de la ligue | "GOLD DIVISION" |
| **Sous-titre** | Détails complémentaires | "Ligue principale — Saison 12 — Division 1" |
| **Équipes** | Nombre d'équipes participantes | `32` |
| **Matchs** | Nombre de matchs total | `128` |
| **Avancement** | Pourcentage de complétion | `76%` |
| **Icônes équipes** | Avatars des équipes participantes | Icônes pixelisées |
| **Barre de progression** | Avancement visuel coloré | Barre jaune/cyan/magenta |
| **Tag catégorie** | Type de ligue | `S12`, `SPÉCIAL`, `FINAL` |
| **Icône décoration** | Icône thématique de la ligue | Trophée, éclair, lune, étoile |

#### Ligues visibles sur la maquette

| Ligue | Statut | Équipes | Matchs | Avancement | Catégorie |
|---|---|---|---|---|---|
| Gold Division | En cours | 32 | 128 | 76% | S12 |
| Cyber Arena | Live now | 16 | 48 | 52% | Spécial |
| Neon Circuit | Playoffs | 8 | 24 | 88% | Final |
| Emerald League | Rookie | 64 | 256 | 34% | — |

---

### 5.3 Tournoi / Bracket

> Référence : ![`docs/page_bracket.png`](page_bracket.png)

#### Description

Visualisation de l'arbre de tournoi (bracket) à élimination directe pour une ligue en phase finale.

#### En-tête du tournoi

| KPI | Valeur |
|---|---|
| Nom du tournoi | "NEON CIRCUIT" |
| Statut | Tournoi actif |
| Équipes | 8 |
| Phase actuelle | QF (Quarts de finale) |
| Matchs restants | 3 |
| Jours restants | 5 |

#### Structure du bracket

Le bracket affiche un arbre à élimination directe avec les phases :

| Phase | Description |
|---|---|
| **Quarts de finale** | 4 matchs, 8 équipes |
| **Demi-finales** | 2 matchs, 4 équipes qualifiées |
| **Grande finale** | 1 match, 2 équipes qualifiées |
| **Champion** | Vainqueur final |

#### Détail d'un match dans le bracket

Chaque nœud du bracket affiche :

- Nom de l'équipe avec icône
- Score du match
- Indicateur visuel de victoire (nom en gras, couleur d'accent) ou défaite (grisé)
- Les matchs non joués affichent `? À DÉTERMINER` avec un score `—`

#### Résultats visibles sur la maquette (Quarts de finale)

| Match | Équipe 1 | Score | Équipe 2 | Score | Vainqueur |
|---|---|---|---|---|---|
| QF1 | Cyber Kings | 3 | Phantom Ops | 1 | Cyber Kings |
| QF2 | Thunder Clan | 3 | Star Force | 2 | Thunder Clan |
| QF3 | Dragon Force | 2 | Inferno FC | 2 | Dragon Force |
| QF4 | Neon Eagles | 1 | Dark Moon | 3 | Dark Moon |

#### Boutons d'action

- `● LIVE` : indique que le tournoi est actif avec mise à jour en temps réel
- `PARTAGER` : permet de partager le bracket

---

### 5.4 Joueurs

> Référence : ![`docs/page_joueurs.png`](page_joueurs.png)

#### Description

Page de classement et gestion des joueurs.

#### Barre d'outils

| Élément | Fonction |
|---|---|
| **Onglets** | `CLASSEMENT` · `PAR ÉQUIPE` · `PAR LIGUE` |
| **Bouton action** | `+ AJOUTER JOUEUR` |
| **Badge header** | `14 392 JOUEURS` |

#### Carte joueur (classement)

Chaque joueur est affiché dans une carte contenant :

| Champ | Description | Exemple |
|---|---|---|
| **Rang** | Position au classement | `#1`, `#2`, `#3`, `#4` |
| **Icône** | Avatar pixelisé du joueur | Icône robot/éclair/dragon |
| **Pseudo** | Nom du joueur | "ZeroKelvin" |
| **Équipe** | Nom de l'équipe | "CYBER KINGS" |
| **Rôle** | Rôle spécial | "MVP" |
| **Score** | Score total cumulé | `9842` |
| **Win%** | Pourcentage de victoires | `84%` |
| **Matchs** | Nombre de matchs joués | `247` |
| **Barre de performance** | Barre colorée proportionnelle | Jaune (#1), cyan (#2), magenta (#3) |

#### Classement visible sur la maquette

| Rang | Pseudo | Équipe | Score | Win% | Matchs | Couleur |
|---|---|---|---|---|---|---|
| #1 🏆 | ZeroKelvin | Cyber Kings · MVP | 9842 | 84% | 247 | Jaune |
| #2 | L3g3nd_Fr | Thunder Clan | 8971 | 76% | 198 | Cyan |
| #3 | DragonSlayer | Dragon Force | 8205 | 71% | 211 | Magenta |
| #4 | WaveBreaker | Wave Riders | 7880 | 65% | 183 | Cyan |

---

### 5.5 Profil joueur

> Référence : ![`docs/page_profil.png`](page_profil.png)

#### Description

Page de profil détaillé d'un joueur avec ses statistiques complètes et son historique.

#### Section identité (colonne gauche)

| Champ | Description | Exemple |
|---|---|---|
| **Avatar** | Grande icône pixelisée | Robot avec badge doré |
| **Pseudo** | Nom du joueur | "ZeroKelvin" |
| **Équipe** | Équipe actuelle | "CYBER KINGS" |
| **Rôle** | Poste dans l'équipe | "CAPITAINE" |
| **Badges** | Tags de distinction | `MVP S12`, `ACTIF`, `PRO` |
| **Niveau** | Niveau du joueur + XP | "NIVEAU 42 — 87%" / "8 700 / 10 000 XP" |
| **Rang** | Position au classement global | `RANG #1` (header) |

#### Section statistiques globales (colonne droite)

| Statistique | Valeur | Description |
|---|---|---|
| **Score total** | 9842 | Score cumulé toutes saisons |
| **Win Rate** | 84% | Pourcentage de victoires |
| **Matchs** | 247 | Nombre de matchs joués |
| **K/D Ratio** | 4.2 | Ratio kills/deaths |
| **Kills** | 1 847 | Nombre total de kills |
| **Titres** | 23 | Nombre de titres remportés |

Un filtre `SAISON 12` permet de limiter les statistiques à une saison spécifique.

#### Section historique récent

Liste des derniers matchs avec :

| Champ | Description | Exemple |
|---|---|---|
| **Résultat** | Badge victoire/défaite | `V` (vert) / `D` (rouge) |
| **Adversaire** | Nom de l'équipe adverse | "vs NEON EAGLES" |
| **Compétition** | Ligue et phase | "NEON CIRCUIT · QF" |
| **Score** | Score du match | `14-9` |
| **Temporalité** | Ancienneté | "23min", "1h", "3h" |
| **Limite** | Bouton de filtre | `10 DERNIERS` |

#### Historique visible sur la maquette

| Résultat | Adversaire | Compétition | Score |
|---|---|---|---|
| ✅ V | Neon Eagles | Neon Circuit · QF | 14-9 |
| ✅ V | Phantom Ops | Gold Div · S12 | 16-7 |
| ❌ D | Thunder Clan | Gold Div · S12 | 11-13 |
| ✅ V | Wave Riders | Gold Div · S12 | 16-4 |

---

### 5.6 Succès & Badges

> Référence : ![`docs/page_succes.png`](page_succes.png)

#### Description

Page de gamification présentant le système de succès et badges débloquables.

#### KPI en en-tête

| KPI | Valeur | Description |
|---|---|---|
| **Débloqués** | 42 | Nombre de succès débloqués |
| **Total** | 120 | Nombre total de succès disponibles |
| **Progression** | 35% | Pourcentage global de complétion |
| **Points succès** | 7 450 | Points cumulés via les succès |
| **Légendaires** | 3 | Nombre de succès légendaires obtenus |

Une **barre de progression multicolore** (jaune → magenta) illustre l'avancement global.

#### Filtres de succès

| Filtre | Description |
|---|---|
| `TOUS` | Affiche tous les succès |
| `DÉBLOQUÉS` | Seulement les succès obtenus |
| `VERROUILLÉS` | Succès non encore débloqués |
| `★ LÉGENDAIRES` | Filtre par rareté légendaire |
| `◆ RARES` | Filtre par rareté rare |

#### Carte de succès

Chaque succès est affiché avec :

| Champ | Description | Exemple |
|---|---|---|
| **Icône** | Symbole du succès | Trophée, éclair, cible |
| **Titre** | Nom du succès (majuscules, style terminal) | "CHAMPION INVAINCU" |
| **Description** | Condition de déblocage | "Gagner 10 matchs consécutifs sans défaite" |
| **Points** | Récompense en points | `+500 pts`, `+200 pts` |
| **Indicateur rareté** | Icône de rareté | `★` (légendaire), `◆` (rare) |
| **Bordure** | Couleur selon rareté | Jaune doré (légendaire), cyan (rare) |

#### Succès visibles sur la maquette

| Succès | Description | Points | Rareté |
|---|---|---|---|
| Champion Invaincu | Gagner 10 matchs consécutifs sans défaite | +500 pts | ★ Légendaire |
| Speed Demon | Terminer un match en moins de 5 minutes | +200 pts | ◆ Rare |
| Sniper Elite | *(description tronquée)* | — | — |

---

### 5.7 Chaos Engine

> Référence : ![`docs/page_chaos.png`](page_chaos.png)

#### Description

Interface de contrôle du moteur d'événements aléatoires. Le Chaos Engine injecte des imprévus durant les matchs en cours.

#### KPI en en-tête

| KPI | Valeur | Description |
|---|---|---|
| **Événements déclenchés** | 147 | Total des événements injectés |
| **Types d'événements** | 23 | Nombre de types distincts |
| **Taux de renversement** | 38% | Pourcentage de matchs dont l'issue a été inversée par un événement |
| **Statut moteur** | ACTIF | État actuel du Chaos Engine |

#### Contrôles du moteur

| Contrôle | Type | Description |
|---|---|---|
| **Toggle moteur** | Interrupteur ON/OFF | Active/désactive le Chaos Engine (`● MOTEUR ACTIF`) |
| **Fréquence globale** | Slider | Règle la fréquence d'injection : `RARE ← 65% → FRÉQUENT` |
| **Intensité max** | Slider | Règle la puissance des événements : `DOUX ← 88% → EXTRÊME` |

#### Journal d'événements (log temps réel)

Le journal affiche un flux en temps réel des événements injectés avec un badge `● LIVE`.

Chaque entrée contient :

| Champ | Description | Exemple |
|---|---|---|
| **Indicateur** | Icône colorée d'état | 🔴 (négatif), 🟢 (positif), 🟡 (neutre) |
| **Nom de l'événement** | Titre coloré de l'événement | "Panne de manette" |
| **Cible** | Équipe affectée | "sur NEON EAGLES" |
| **Effet** | Description de l'impact | "malus -15 SPD pendant 2min" |
| **Temporalité** | Ancienneté de l'événement | "il y a 34s" |

#### Événements visibles sur la maquette

| Événement | Cible | Effet | Type | Ancienneté |
|---|---|---|---|---|
| 🔴 Panne de manette | Neon Eagles | malus -15 SPD pendant 2min | Négatif | 34s |
| 🟢 Boost d'énergie | Thunder Clan | +30% attaque pendant 90s | Positif | 2min |
| 🟡 Multiplicateur ×2 | Dragon Force | doublé score 60s | Neutre/Bonus | 5min |
| 🔵 Inversion de contrôles | Dark Moon | 45s | Négatif | 8min |

---

## 6. API GraphQL

### 6.1 Scalaires personnalisés

| Scalaire | Description |
|---|---|
| `DateTime` | Timestamp ISO 8601 |
| `JSON` | Objet JSON libre (configuration widgets) |

### 6.2 Queries principales

| Query | Paramètres | Retour | Description |
|---|---|---|---|
| `dashboard(id)` | `ID!` | `Dashboard` | Récupère un tableau de bord |
| `dashboards` | — | `[Dashboard!]!` | Liste tous les tableaux de bord |
| `simulation(id)` | `ID!` | `Simulation` | Détail d'une simulation |
| `simulations` | `status`, `limit`, `offset` | `[Simulation!]!` | Liste les simulations (filtrable) |
| `simulationsSummary` | `limit` | `[SimulationSummary!]!` | Résumés de simulations |
| `metrics` | `simulationId!`, `since` | `[Metric!]!` | Métriques d'une simulation |

### 6.3 Mutations principales

| Mutation | Paramètres | Description |
|---|---|---|
| `createSimulation` | `CreateSimulationInput!` | Crée une nouvelle simulation |
| `startSimulation` | `ID!` | Démarre une simulation |
| `stopSimulation` | `ID!` | Arrête une simulation |
| `cancelSimulation` | `ID!` | Annule une simulation |
| `updateDashboard` | `UpdateDashboardInput!` | Met à jour un tableau de bord |
| `addWidget` | `dashboardId`, `WidgetInput!` | Ajoute un widget |
| `removeWidget` | `ID!` | Supprime un widget |
| `publishMetric` | `simulationId`, `MetricInput!` | Publie une métrique |

### 6.4 Subscriptions (temps réel)

| Subscription | Paramètre | Retour | Description |
|---|---|---|---|
| `simulationProgress` | `simulationId!` | `SimulationProgress!` | Progression d'une simulation |
| `simulationCompleted` | `simulationId!` | `SimulationResult!` | Notification de fin de simulation |
| `metricUpdated` | `simulationId!` | `Metric!` | Mise à jour de métrique |
| `dashboardUpdated` | `dashboardId!` | `Dashboard!` | Mise à jour d'un dashboard |

### 6.5 Types secondaires

| Type | Description |
|---|---|
| `Widget` | Composant visuel d'un dashboard (type: CHART, TABLE, KPI, TEXT) |
| `SimulationConfig` | Configuration d'une simulation (durée, paramètres, seed, scénario) |
| `SimulationResult` | Résultat d'une simulation (métrique, valeur, timestamp) |
| `Metric` | Point de donnée (nom, valeur, timestamp) |

---

## 7. Fonctionnalités temps réel

Le projet exploite les **Subscriptions GraphQL** (via WebSocket) pour offrir une expérience temps réel :

| Fonctionnalité | Page concernée | Subscription utilisée |
|---|---|---|
| Score live des matchs | Dashboard, Bracket | `simulationProgress` |
| Mise à jour KPI | Dashboard | `dashboardUpdated` |
| Événements Chaos Engine | Chaos Engine | `metricUpdated` |
| Notifications de fin de match | Dashboard, Bracket | `simulationCompleted` |
| Progression des tournois | Bracket | `simulationProgress` |

### Protocole

- Transport : **WebSocket** (protocole `graphql-ws`)
- Le frontend Angular utilise les **Signals** pour propager les mises à jour réactives dans l'arbre de composants

---

## 8. Règles métier

### 8.1 Gestion des ligues

- Une ligue possède un code unique (`CODE`)
- Une ligue peut contenir N équipes
- Les statuts possibles d'une ligue : `À VENIR`, `EN COURS`, `PLAYOFFS`, `LIVE NOW`, `TERMINÉE`
- Une ligue appartient à une saison identifiée par un code (ex: "S12")

### 8.2 Gestion des équipes

- Une équipe appartient à exactement une ligue
- Deux équipes de la même ligue ne peuvent pas porter le même nom
- Chaque équipe possède un nom court (`SHORT_NAME`) et une icône pixelisée

### 8.3 Gestion des joueurs

- Un joueur appartient à une équipe
- Un joueur possède un pseudo, un niveau (XP), un rang global
- Les rôles spéciaux incluent : `MVP`, `CAPITAINE`, `PRO`, `ACTIF`
- Le classement global est basé sur le **score total** cumulé

### 8.4 Gestion des matchs

- Un match oppose deux équipes distinctes (`HOME ≠ AWAY`)
- Statuts d'un match : `SCHEDULED` → `LIVE` → `COMPLETED`
- Les scores sont mis à jour en temps réel pendant le statut `LIVE`
- Un match appartient à une ligue et une saison
- Format de match : Bo5 (Best of 5 maps, visible sur le scoreboard)

### 8.5 Statistiques

- Les stats sont calculées par joueur et par match
- Métriques : minutes jouées, goals, assists, cartons jaunes/rouges, tirs cadrés
- Les stats agrégées (score total, win rate, K/D ratio) sont dérivées des stats individuelles

### 8.6 Succès et badges

- Un succès est lié à un joueur et/ou une équipe
- Les succès possèdent une rareté : standard, `◆ RARE`, `★ LÉGENDAIRE`
- Chaque succès donne des points de succès
- La progression est calculée : `succès débloqués / succès total × 100`
- Exemples de conditions de déblocage :
  - "Gagner 10 matchs consécutifs sans défaite" → Champion Invaincu (+500 pts)
  - "Terminer un match en moins de 5 minutes" → Speed Demon (+200 pts)

### 8.7 Chaos Engine

- Le moteur peut être activé/désactivé globalement
- Deux paramètres réglables : **fréquence** (rare → fréquent) et **intensité** (doux → extrême)
- Les événements sont ciblés sur une équipe spécifique
- Types d'événements : positifs (boost), négatifs (malus), neutres (modificateur)
- Exemples : "Panne de manette", "Boost d'énergie", "Multiplicateur ×2", "Inversion de contrôles"
- Le **taux de renversement** mesure l'impact du Chaos Engine sur l'issue des matchs

### 8.8 Tournoi (Bracket)

- Format à élimination directe
- Phases : Quarts de finale → Demi-finales → Grande Finale → Champion
- Le bracket se remplit automatiquement à mesure que les matchs sont terminés
- Les matchs non joués affichent "? À DÉTERMINER"

---

## 9. Glossaire

| Terme | Définition |
|---|---|
| **Bracket** | Arbre de tournoi à élimination directe |
| **Chaos Engine** | Moteur d'injection d'événements aléatoires pendant les matchs |
| **KPI** | Key Performance Indicator — indicateur chiffré clé |
| **K/D Ratio** | Ratio kills/deaths (éliminations/morts) |
| **MVP** | Most Valuable Player — meilleur joueur |
| **Win Rate** | Pourcentage de victoires sur le total de matchs |
| **Bo5** | Best of 5 — format de match en 5 manches |
| **Subscription** | Mécanisme GraphQL de mise à jour temps réel via WebSocket |
| **Signal** | Primitive réactive Angular pour la propagation d'état |
| **Virtual Thread** | Thread léger Java 24 pour la concurrence massive |
| **Seed** | Valeur initiale pour le générateur de nombres aléatoires (reproductibilité) |
| **Taux de renversement** | Pourcentage de matchs dont le résultat a été modifié par le Chaos Engine |

---

> **Fichiers de référence :**
> - Maquettes : `docs/page_*.png`, `docs/retro-league-mockup.png`
> - Schéma BDD : `docs/schema_bdd.png`, `back/db/db2_create_tables.sql`
> - Schéma GraphQL : `back/src/main/resources/graphql/schema.graphqls`
> - README projet : `README.md`
