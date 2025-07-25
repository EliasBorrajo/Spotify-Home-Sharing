# Spotify Home Sharing 

> **Spotify Home Sharing** is a Java-based project developed as part of a school assignment to practice TCP socket programming and basic peer-to-peer networking. Despite the name, this project has no connection to Spotify. It allows users to locally share and stream music files across a network.

---

## Concept

Each instance of the program acts both as a **client and a server**. A central directory server keeps track of all connected peers and the music files they share. Users expose a local folder containing `.wav` files. Through a command-line interface, they can:

* Discover available peers
* List available songs shared by others
* Stream music from remote peers using TCP

---

## Key Features

* Peer discovery via a central server
* P2P music streaming over TCP
* Local folder sharing (with basic metadata scanning)
* No graphical interface; command-line interaction only
* `.wav` file support only
* Simplified protocol for listing and requesting tracks

---

## 🖊️ Diagrams & Documentation

### Architecture Overview

The central server coordinates connections between peers. Music is streamed directly between clients after discovery.

![Architecture Diagram](https://github.com/EliasBorrajo/Spotify-Home-Sharing/blob/master/01_Annexes/Architecture.png)


### Client UML Diagram

Illustrates the different responsibilities of the client module: networking, file handling, and interaction.

![Client UML](https://github.com/EliasBorrajo/Spotify-Home-Sharing/blob/master/01_Annexes/UML/Client.jpg)


### Scanner UML Diagram

Details how the local music directory is scanned to detect `.wav` files and share metadata with peers.

![Scanner UML](https://github.com/EliasBorrajo/Spotify-Home-Sharing/blob/master/01_Annexes/UML/Scanner.jpg)

---

## Technologies Used

* Java 17
* TCP Sockets
* CLI-based interaction

---

## 🚫 Disclaimer

This is a **pedagogical project only**. All music files are stored and shared **locally**, and no third-party API (such as Spotify) is used.

---

## 📄 Authors

Project developed by:

* Elias Borrajo
* Arthur Avez

---

## Original Readme archive (FR)




<details>
  <summary>Click here to see the original version of the readme</summary>

<h1>VSFY - Spotify Valais</h1>
Toute la documentation est disponible sous le dossier [Annexes](Annexes)
- [Le manuel d'utilisation](Annexes/Manuel d'utilisation.docx)

<h2>Description</h2>
Dans le cadre du cours programmation distribuée (JavaSocket) de la formation Bachelor of Science en informatique de gestion de la HES-SO Valais Wallis, 4 ème semestre, nous avons dû réaliser une application Spotify fonctionnant en Peer to Peer.

Le but est d’avoir un serveur central (Scanner) sur un réseau privé, auquel tous les utilisateurs (Users) peuvent accéder via son ip & port connus.

Une fois qu'un utilisateur se connecte au scanner, les informations de l’utilisateur y sont envoyées.

Les autres utilisateurs se connectant au scanner peuvent demander la liste des utilisateurs connectés eux aussi au scanner et ainsi obtenir leurs informations tel que ip, port, musiques à dispositions.

Ainsi, je peux me connecter à n’importe quel autre utilisateur et streamer chez moi les musiques qu’il me met à disposition, et n’importe qui peut se connecter à mon PC pour écouter les musiques que je mets à disposition.

Durant le cours, nous avons appris à utiliser les sockets sur java, et le but de ce projet est donc d’exploiter un maximum la communication entre des machines différentes via des sockets.
De plus, du multithreading a du être implémenté afin de subvenir à nos besoins pour pouvoir faire toutes les tâches nécessaires en parallèle.

<h2>Configuration réquise</h2>
- Variable d’environnement appelé « VSSPOTIFY » 
  - Peut indiquer le chemin d’accès au bureau par exemple.
  Indique le chemin d’accès au répertoire ou l’on veut créer nos dossiers contenants les fichiers que l’on met à disposition des autres utilisateurs & les fichiers de LOG qui permettront d’avoir un historique des activités.
- Java version jdk-17.0.1 Minimum. Ou plus récent.


<h2>Améliorations futures</h2>
A cause de la durée du projet qui est limtié, nous n'avons pas pu implementér toutes les fonctionnalitées voulues.

- Quand on sélectionne une musique, que on se connecte automatiquement au client sans devoir s'y connecter manuellement.
- Quand on sélectionne une musique, que l'on peut voir la musique en cours de lecture.
- Que on puisse télécharger des musiques ou d'autres types de fichiers, comme un dropBox.
- Que l'on puisse streamer aussi des videos.
- Qu'au lieu de lancer un stream dans l'application, que on utilise VLC ou une application par défaut sur le PC.
- Ajouter une interface graphique.

<h2>Conclusion</h2>
Ce projet a pris beaucoup de temps, on a fait une première itération du projet en partant avec nos connaissances et nos idées,
puis on a rencontré des problèmes pour lesquelles on a fait de la recherche. 
Grâce à cette recherche, on a fait un refactor complêt du projet, on a modélisé toute la structure sur papier, 
les interractions entre nos éléments (à cause du multithreading), et ensuite une fois que tout a été modelisé, on a re-implementé le projet.

En procédant de la sorte, on a un code qui est bien structuré, et pour ajouter de nouvelles améliorations, ce sera bien lus simple au futur.

<h2>Crédits</h2>
Projet réalisé par :
[Arthur Avez](https://gitlab.com/ArthurAvez) & [Borrajo Elias](https://gitlab.com/EliasKelliwich).

  
</details>
