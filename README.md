# Jeu d'Échecs et de Dames en Java ♟️
<p align="center">
  <img src="https://github.com/user-attachments/assets/cfe7cf27-150f-43be-ab1c-70705411ba43" alt="Capture d’écran 1" width="500"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/f7ef6892-fca8-42c5-be49-f3cc2dd8a727" alt="Capture d’écran 2" width="500"/>
</p>


## 📌 Présentation

Ce projet Java implémente un jeu graphique permettant de jouer aux **Échecs** et aux **Dames**, soit contre un **autre joueur humain**, soit contre une **IA**. Il s'appuie sur un modèle de type **MVC (Modèle - Vue - Contrôleur)**, avec des fonctionnalités modulaires et extensibles (utilisation de décorateurs pour les déplacements).

---

## 🚀 Fonctionnalités principales

- 🎮 Mode Échecs et mode Dames
- 👥 Mode Humain vs Humain ou Humain vs IA
- ♟️ Déplacements spécifiques à chaque pièce implémentés via des **décorateurs**
- 🔄 Système de **roque**, **promotion**, **pat**, **échec** et **échec & mat**
- 💡 Détection automatique des coups valides
- 🔊 Sons pour les déplacements
- 🧠 IA simple basée sur des choix aléatoires parmi les coups valides

---

## 🗂️ Structure du projet (MVC)
src/ ├── Main.java # Point d'entrée de l'application
     ├── modele/ 
     │         ├── jeu/ # Logique du jeu (Jeu, Joueurs, Pièces, etc.)
     │         └── plateau/ # Plateau, cases, décorateurs, directions 
     └── vueControleur/ # Interface graphique 

---

## ⚙️ Exécution

1. Compile le projet (par exemple via IntelliJ ou `javac`)
2. Lance la classe `Main.java`
3. Une fenêtre graphique s’ouvrira pour choisir le mode de jeu

---

## 🧩 Technologies utilisées

- Java (JDK 8+)
- Swing pour l’interface graphique
- Design patterns : **MVC**, **Décorateur**, **Observateur**

---

## ✍️ Auteur

Projet réalisé par **Cheikh Ali Abdoul-Anzizi et Diop Mory**

---

