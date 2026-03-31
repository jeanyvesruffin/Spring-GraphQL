# Spring-GraphQL

# Démarrage rapide

## Compilation

```sh
mvn clean install
```

## Execution

```sh
mvn spring-boot:run
```

# 🚀 Projet : Retro-League Manager

## 📝 Concept
**Retro-League Manager** est une plateforme de gestion et de simulation de tournois d'e-sport "vintage". Le projet détourne des technologies robustes de type "entreprise" pour créer un univers ludique où les utilisateurs gèrent des ligues, suivent des statistiques de joueurs en temps réel et simulent des matchs via un moteur d'événements aléatoires.

---

## 🛠 Stack Technique

| Composant          | Technologie                 | Rôle                                                                 |
| :----------------- | :-------------------------- | :------------------------------------------------------------------- |
| **Langage** | **Java 24** | Utilisation des *Virtual Threads* et du *Structured Concurrency*.    |
| **Framework Pro** | **Spring Boot 3.4+** | Socle de l'application (Spring Data, Spring GraphQL).                |
| **Base de Données**| **SQL DB2** | Gestion transactionnelle des scores, paris et classements.           |
| **API** | **Spring GraphQL** | Interface flexible pour consommer uniquement les stats nécessaires.  |
| **Frontend** | **Angular 19+** | Interface réactive utilisant les *Signals* pour les scores en direct.|

---

## 🌟 Points Forts & Originalité

### 1. Performance "Next-Gen" avec Java 24
Grâce aux **Virtual Threads**, le serveur peut simuler des milliers de matchs simultanément sans consommer de ressources excessives. Le **Structured Concurrency** permet de gérer les événements de jeu (bonus, malus, scores) de manière isolée et sécurisée.

### 2. Flexibilité GraphQL
Plutôt que des endpoints REST rigides, GraphQL permet au frontend Angular de demander exactement les données souhaitées (ex: uniquement le ratio victoire/défaite pour un mobile, mais l'historique complet pour un desktop).

### 3. La Robustesse de DB2 au service du Fun
Utilisation de la puissance d'IBM DB2 pour exécuter des requêtes analytiques complexes (Window Functions) afin de calculer des classements mondiaux en temps réel et de garantir l'intégrité des données de la ligue.

---

## 🎮 Fonctionnalités Clés

* **Tableau de Bord "Arcade" :** Une interface Angular au look rétro-futuriste.
* **Chaos Engine :** Un service backend qui injecte des imprévus durant les matchs (ex: "Panne de manette", "Bonus de vitesse").
* **Live Subscriptions :** Mise à jour en temps réel des scores via les *Subscriptions* GraphQL.
* **Système de Succès :** Badges débloqués via des procédures stockées ou des requêtes SQL complexes.

---

# IBM DB2

Utiliser l'extension vscode IBM Db2 Developer Extension

## Lignes de commandes (Toujours être en administrateur)

* initialiser l'environnement Db2, à l'initialisation

```cmd
cd C:\Program Files\IBM\SQLLIB\BIN
db2setcp.bat
db2 UPDATE DBM CFG USING SVCENAME 50000
db2set DB2COMM=TCPIP
```

* Vérification installation

```cmd
netstat -an | findstr 50000
db2 GET DBM CFG
```

| Fichier | Rôle |
|---|---|
| `db2clpsetcp.bat` | Initialise les variables d'environnement Db2 |
| `db2cmd.exe` | Lance le Db2 Command Window |
| `db2cmdadmin.exe` | Lance le Db2 Command Window en administrateur |
| `db2clp.bat` | Démarre le Command Line Processor |

* Creation d'une base de données (apres avoir fais db2cmdadmin.exe) :

```cmd
db2 CREATE DATABASE graphql USING CODESET UTF-8 TERRITORY FR PAGESIZE 8192
```

* Se connecter à la base de données (apres avoir fais db2cmdadmin.exe) :

```cmd
db2 CONNECT TO graphql USER db2admin USING root
```

