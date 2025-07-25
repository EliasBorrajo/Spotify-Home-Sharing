# Spotify Home Sharing

> A peer-to-peer music streaming Java application developed as a school project for distributed systems. Despite the name, it is not affiliated with Spotify.

---

## 🚫 Disclaimer

This is a **pedagogical project only**. All music files are stored and shared **locally**, and no third-party API (such as Spotify) is used.

---

## 📚 Project Description

Spotify Home Sharing is a Java-based peer-to-peer music sharing platform built with TCP socket communication. It allows users on the same local network to expose a folder of `.wav` files and stream music directly between each other using a central server (scanner) for peer discovery.

---

## 🧪 Technologies Used

| Type         | Name                                      |   |
| ------------ | ----------------------------------------- | - |
| Language     | Java 12                                   |   |
|              |                                           |   |
| CLI / UI     | Terminal                                  |   |
| Audio Format | ".WAV" only                               |   |
| Architecture | TCP / Sockets. Using P2P + Central Server |   |

---

## 🎯 Learning Objectives

* Understand and implement client-server architecture using Java Sockets
* Handle peer discovery and file metadata exchange
* Build thread-safe, concurrent applications
* Manage live audio streaming over TCP

---

## 🔧 Features

* Local folder sharing of `.wav` files
* Peer discovery through a central scanner
* Real-time audio streaming via TCP between clients
* Logging system with 3 log levels and daily rotation
* Thread-safe multi-client handling
* Command-line interface only (no GUI)

### 🔧 Bonus Features

* Concurrent streams: multiple clients can stream the same song from one peer
* Dynamic file updates in shared folder
* Fault tolerance for improper disconnects

---

## 🧠 Language Paradigm Principles

* Concurrency using Java Threads
* Synchronous TCP communication
* Basic modular design

### Error Handling

Error handling is performed via `try-catch` blocks, with categorized log levels:

* `INFO`: normal operation
* `WARNING`: recoverable errors (e.g. disconnections)
* `SEVERE`: unrecoverable errors or exceptions

---

## 🏗 Project Structure

### Architecture

* A central **Scanner** server maintains a list of peers and their music files
* Each **Client** connects to the scanner to register and discover other users
* Clients connect directly to stream music from each other (peer-to-peer)

---

## 📘 Documentation & Diagrams

* **Architecture Diagram**\\

* **Client UML**\\

* **Scanner UML**\\

* [User Manual (FR)](01_Annexes/Manuel%20d'utilisation.docx) *(TODO: add English version)*

---

## ✅ Tests & Validation

* Manual testing of multi-client streaming

* Validation via project grading criteria from HES-SO

---

---

## 📌 Success Criteria Table

| Criterion                                | Status | Notes                                  |
| ---------------------------------------- | ------ | -------------------------------------- |
| Client-server connection via TCP sockets | ✅ Done | ServerSocket/Socket working correctly  |
| Peer-to-peer music streaming             | ✅ Done | Live audio transfer without storage    |
| CLI-only interface                       | ✅ Done | Terminal interaction only              |
| Concurrency (multi-client support)       | ✅ Done | Thread-safe design                     |
| Logging with 3 levels                    | ✅ Done | Daily log rotation implemented         |
| .wav file detection & playback           | ✅ Done | Uses basic audio libraries             |
| Bonus: multi-client streaming per file   | ✅ Done | Tested with concurrent client playback |
| Bonus: Fault-tolerant peer cleanup       | ✅ Done | Scanner removes ghost clients          |
|                                          |        |                                        |
|                                          |        |                                        |

---

## 🚀 Future Improvements

* Add automatic client connection when selecting a song
* Display currently playing track in terminal
* Support for file download (e.g. Dropbox-like)
* Stream other formats (e.g. `.mp3`) and video
* Launch playback via external players (e.g. VLC)
* Develop a graphical interface (GUI)
* Deployment on Raspberry Pi or embedded devices

---

## 👤 Authors

* **Elias Borrajo**
* **Arthur Avez**

---

***Project realized for the course*** `633-2 - Distributed Architectures & Java Socket Programming`
***Instructor: Antoine Widmer, HES-SO Valais-Wallis***
*Context: Bachelor of Science in Business IT, 4th Semester*

---

*(Old README content available in ************************************************************[README\_FR](README_FR.md)************************************************************)*


---



<details>
  <summary>
    <h2>Original Readme archive (FR)</h2>
  </summary>

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
