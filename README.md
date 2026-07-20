# 🚢 Battleship with Java

A terminal-based implementation of the classic **Battleship** strategy game written in Java. Built with clean code practices, object-oriented design, and automated with Gradle.

---

## 🎮 Features

* **Interactive Command-Line Interface:** Easy-to-use console graphics for board state and shots tracking.
* **Ship Placement Validation:** Ensures ships follow rules (no overlapping, correct length, no adjacent placement).
* **Two-Player Gameplay:** Fog of war functionality with pass-and-play mechanics between turns.
* **Error Handling:** Robust user input validation for coordinates and invalid placements.

---

## 🗺️ Roadmap & Future Enhancements

- [ ] **Console UI Improvements:**
  - Add ANSI colors for hits, misses, and ship markers to enhance visual feedback.
  - Improve line spacing and output formatting between turns.
- [ ] **Code Refactoring:** Optimize board rendering logic and state handling.
- [ ] **Network Multiplayer:** Implement TCP/IP sockets to support remote 2-player gameplay over a network.

---

## 🛠️ Tech Stack & Tools

* **Language:** Java 17+ (JDK 21/23 supported)
* **Build Tool:** Gradle (Groovy DSL)
* **IDE:** JetBrains IntelliJ IDEA
* **Version Control:** Git & GitHub

---

## 🚀 How to Run

### Prerequisites
* **Java Development Kit (JDK):** Java 17 or higher (tested on JDK 21 / 23).

### Option 1: Run pre-built JAR file

1. Build the executable `.jar` file using the Gradle wrapper:
   ```bash
   ./gradlew jar