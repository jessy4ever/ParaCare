# 🏥 ParaCare - Système d'Assistance Paramédical Distribué

## 📋 Description

ParaCare est une application distribuée pour la gestion des soins paramédicaux à domicile, utilisant :
- **RMI** (Remote Method Invocation) pour les services métier
- **TCP** pour le transfert fiable de données volumineuses
- **UDP** pour les notifications et alertes en temps réel
- **JavaFX** pour l'interface graphique moderne

---

## 📁 Structure du Projet

```
ParaCare/
├── src/
│   ├── common/
│   │   └── models/
│   │       ├── HealthRecord.java
│   │       ├── Treatment.java
│   │       ├── Observation.java
│   │       └── Appointment.java
│   ├── services/
│   │   ├── HealthRecordService.java
│   │   └── ScheduleService.java
│   ├── server/
│   │   ├── ParaCareServer.java
│   │   ├── rmi/
│   │   │   ├── HealthRecordServiceImpl.java
│   │   │   └── ScheduleServiceImpl.java
│   │   ├── tcp/
│   │   │   └── TCPServer.java
│   │   └── udp/
│   │       └── UDPServer.java
│   └── client/
│       ├── ParaCareClientSimple.java
│       ├── network/
│       │   ├── RMIClient.java
│       │   ├── TCPClient.java
│       │   └── UDPClient.java
│       └── ui/
│           └── DashboardController.java
└── README.md
```

---

## 🔧 Prérequis

- **Java JDK 11** ou supérieur
- **JavaFX SDK 11** ou supérieur (si non inclus dans votre JDK)
- **IDE** : IntelliJ IDEA, Eclipse, ou NetBeans

---

## 📦 Installation et Configuration

### Étape 1 : Télécharger JavaFX (si nécessaire)

Si votre JDK n'inclut pas JavaFX :
1. Téléchargez JavaFX SDK depuis : https://gluonhq.com/products/javafx/
2. Extrayez l'archive dans un dossier (ex: `C:\javafx-sdk-11.0.2`)

### Étape 2 : Configuration dans IntelliJ IDEA

#### A. Créer le projet
1. File → New → Project
2. Sélectionnez "Java"
3. Nommez le projet "ParaCare"
4. Cliquez sur "Create"

#### B. Ajouter JavaFX aux bibliothèques
1. File → Project Structure → Libraries
2. Cliquez sur "+" → Java
3. Naviguez vers le dossier `javafx-sdk-XX/lib`
4. Sélectionnez tous les fichiers JAR
5. Cliquez sur "OK"

#### C. Configuration des modules VM
1. Run → Edit Configurations
2. Pour chaque configuration (Server et Client), ajoutez dans "VM options" :
```
--module-path "CHEMIN_VERS_JAVAFX/lib" --add-modules javafx.controls,javafx.fxml
```
Remplacez `CHEMIN_VERS_JAVAFX` par le chemin réel.

### Étape 3 : Importer le Code

1. Créez les packages dans `src/` selon la structure ci-dessus
2. Copiez chaque fichier Java dans son package correspondant
3. Assurez-vous que les déclarations de package correspondent

---

## 🚀 Compilation et Exécution

### Option 1 : Avec IntelliJ IDEA

#### Démarrer le Serveur
1. Ouvrez `server/ParaCareServer.java`
2. Clic droit → Run 'ParaCareServer.main()'
3. Vérifiez que la console affiche :
```
✅ Registry RMI créé sur le port 1099
✅ Service HealthRecordService enregistré
✅ Service ScheduleService enregistré
✅ Serveur TCP démarré
✅ Serveur UDP démarré
✅ Serveur ParaCare prêt !
```

#### Démarrer le Client
1. Ouvrez `client/ParaCareClientSimple.java`
2. Clic droit → Run 'ParaCareClientSimple.main()'
3. L'interface graphique devrait s'afficher

### Option 2 : En Ligne de Commande

#### Compiler le projet
```bash
# Depuis la racine du projet
javac -d bin -cp "javafx-sdk/lib/*" src/**/*.java
```

#### Démarrer le serveur
```bash
java -cp bin server.ParaCareServer
```

#### Démarrer le client (dans un autre terminal)
```bash
java --module-path javafx-sdk/lib --add-modules javafx.controls \
     -cp bin client.ParaCareClientSimple
```

---

## 🎯 Utilisation de l'Application

### Interface Client

1. **Dashboard Principal**
   - Vue d'ensemble des patients
   - Liste des rendez-vous du jour
   - Statistiques en temps réel

2. **Gestion des Patients**
   - Double-cliquez sur un patient pour voir ses détails
   - Utilisez la barre de recherche pour filtrer

3. **Fonctionnalités Réseau**
   - **Bouton "Actualiser"** : Recharge les données via RMI
   - **Bouton "Sync TCP"** : Synchronise via TCP
   - **Zone d'alertes** (à droite) : Reçoit les notifications UDP en temps réel

### Démonstration des Protocoles

#### RMI (Remote Method Invocation)
- Au démarrage, les données sont chargées automatiquement
- Cliquez sur "Actualiser" pour recharger via RMI
- Double-cliquez sur un patient pour voir les détails (appel RMI)

#### TCP (Transfert de Données)
- Cliquez sur "Sync TCP" pour une synchronisation complète
- Utilisé pour les transferts de données volumineuses

#### UDP (Notifications)
- Les alertes apparaissent automatiquement dans le panneau de droite
- Le serveur envoie périodiquement des notifications
- Heartbeat toutes les 30 secondes

---

## 🧪 Tests Fonctionnels

### Test 1 : RMI - Consultation de Dossiers
1. Démarrez le serveur
2. Démarrez le client
3. Observez le chargement automatique des patients
4. ✅ **Vérifie** : Communication RMI fonctionnelle

### Test 2 : TCP - Synchronisation
1. Cliquez sur "Sync TCP"
2. Observez le message de confirmation
3. ✅ **Vérifie** : Transfert TCP opérationnel

### Test 3 : UDP - Alertes Temps Réel
1. Attendez 1-2 minutes
2. Observez l'apparition d'alertes dans le panneau de droite
3. ✅ **Vérifie** : Notifications UDP actives

### Test 4 : Types Complexes
1. Double-cliquez sur un patient
2. Observez les traitements, observations, allergies
3. ✅ **Vérifie** : Objets complexes sérialisés correctement

---

## 🐛 Résolution de Problèmes

### Erreur : "Registry not found"
**Cause** : Le serveur n'est pas démarré ou le port RMI est bloqué  
**Solution** : 
1. Vérifiez que le serveur est bien lancé
2. Vérifiez que le port 1099 n'est pas utilisé par une autre application

### Erreur : "Connection refused" (TCP/UDP)
**Cause** : Pare-feu bloque les ports  
**Solution** :
- Windows : Autorisez Java dans le pare-feu
- Linux/Mac : `sudo ufw allow 5000` et `sudo ufw allow 6000`

### Erreur : "Module javafx.controls not found"
**Cause** : JavaFX n'est pas correctement configuré  
**Solution** :
1. Vérifiez que JavaFX SDK est téléchargé
2. Ajoutez `--module-path` et `--add-modules` dans les VM options

### Interface ne s'affiche pas
**Cause** : Erreur de connexion aux serveurs  
**Solution** :
1. Vérifiez que le serveur est démarré EN PREMIER
2. Attendez 2-3 secondes avant de lancer le client

---

## 🎓 Points Clés du Projet

### Architecture Distribuée
- **3 niveaux** : Modèles / Services / Clients
- **3 protocoles** : RMI, TCP, UDP utilisés de façon complémentaire

### Types Complexes
- `HealthRecord` contient :
  - `List<Treatment>` (collection d'objets)
  - `List<Observation>` (collection d'objets)
  - `Map<String, String>` (allergies)
  - `Map<LocalDate, String>` (notes indexées)
- Démonstration complète de la sérialisation Java

### Cas d'Usage Réel
- Gestion de patients
- Planification de rendez-vous
- Notifications d'urgence
- Coordination entre soignants

---

## 📊 Démonstration Vidéo (Script)

### Introduction (30s)
"Bonjour, je vais vous présenter ParaCare, un système distribué de gestion paramédical utilisant RMI, TCP et UDP."

### Architecture (1min)
"L'architecture utilise :
- RMI pour les services métier distants
- TCP pour le transfert fiable de dossiers médicaux
- UDP pour les alertes temps réel
- JavaFX pour l'interface graphique"

### Démonstration Live (2min)
1. **Démarrage serveur** : "Voici les services qui se lancent..."
2. **Démarrage client** : "L'interface se charge, les données sont récupérées via RMI..."
3. **Consultation patient** : "Double-clic pour voir les détails..."
4. **Sync TCP** : "Synchronisation complète via TCP..."
5. **Alertes UDP** : "Voici les notifications temps réel qui arrivent..."

### Conclusion (30s)
"Ce projet démontre une maîtrise complète des systèmes distribués avec des protocoles variés et des cas d'usage réels."

---

## 📝 Checklist d'Évaluation

✅ **RMI utilisé** : Services HealthRecord et Schedule  
✅ **Types complexes** : HealthRecord avec List, Map, objets imbriqués  
✅ **TCP implémenté** : TCPServer et TCPClient pour transferts fiables  
✅ **UDP implémenté** : UDPServer et UDPClient pour alertes  
✅ **Interface graphique** : JavaFX avec dashboard complet  
✅ **Cas d'usage réel** : Système paramédical fonctionnel  
✅ **Code commenté** : Documentation complète  
✅ **Architecture claire** : Packages bien organisés  

---

## 👥 Auteurs

Projet ParaCare - Application Répartie  
Développé pour le cours de Systèmes Distribués
Préparé par: Yasmine Abbes & Amira Krid

## 📄 Licence

Ce projet est à usage éducatif uniquement.
