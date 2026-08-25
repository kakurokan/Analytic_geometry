# Maritime Traffic Simulator (Simulador Marítimo)

A Java-based desktop application designed for simulating, managing, and visualizing maritime traffic in customizable environments. The simulator models ship navigation, route optimization using pathfinding algorithms (such as Dijkstra's algorithm), dynamic obstacle avoidance, environmental conditions (e.g., storms and currents), and harbor/traffic control operations.

---

## 📌 Features

- **Maritime Navigation & Autopilot:**
  - Dynamic simulation of vessels (`Navio`, `Navegante`) moving across geometric coordinates.
  - State management for vessels (`EstadoMovel`: *Waiting*, *At Origin*, *Navigating*, *At Destination*).
  - Autopilot support for path tracking and vector computations.

- **Route Planning & Graph Routing:**
  - Route calculation using graph-based pathfinding strategies (`EstrategiaDijkstra`, `Grafo`, `Route`).
  - Strategy Pattern implementation (`EstrategiaRota`) for modular routing algorithms.

- **Obstacles & Environmental Dynamics:**
  - Geometric collision boundaries and obstacles (`Obstaculo`, `Circulo`, `Poligono`, `Retangulo`, `Quadrado`, `Triangulo`).
  - Environmental events such as ocean currents (`DialogoCorrente`) and moving storms (`Tempestade`).

- **Harbor & Control Operations:**
  - Harbor management (`Porto`) and traffic control tower (`TorreDeControlo`, `GestorMaritimo`).
  - State persistence and snapshot capture (`SnapshotSimulacao`) for tracking or replaying simulations.

- **Interactive GUI (Java Swing):**
  - Real-time 2D map visualization (`PainelMapa`, `JanelaPrincipal`).
  - Interactive simulation controls: play/pause, restart (`AcaoReiniciarSimulacao`), toggle vessel direction (`AcaoAlternarDirecao`), and configure ocean currents.

- **Comprehensive Unit Testing:**
  - Extensive test coverage for engine components, vector math, geometry, and graph routing algorithms.

---

## 🏗 Architecture & Design Patterns

The project follows a clean separation of concerns, divided into three main packages:


```

Simulador_maritimo/
├── src/
│   ├── Cliente.java                 # Entry point / Client application
│   ├── Engine/                      # Core simulation engine and business logic
│   │   ├── AutoPilot.java
│   │   ├── Circulo.java, Retangulo.java, Poligono.java, ...  # Geometric shapes & obstacles
│   │   ├── EstadoMovel.java, Movel.java, Movel*.java         # State Pattern for mobile entities
│   │   ├── EstrategiaRota.java, EstrategiaDijkstra.java     # Strategy Pattern for pathfinding
│   │   ├── GestorMaritimo.java, TorreDeControlo.java         # Traffic & harbor orchestration
│   │   ├── Grafo.java, Route.java, Ponto.java, Vetor.java   # Graph & mathematical foundations
│   │   ├── Navio.java, Navegante.java                       # Ship & navigator models
│   │   ├── Simulador.java, SnapshotSimulacao.java           # Simulation engine & snapshot manager
│   │   └── Tempestade.java                                  # Dynamic weather/storm entity
│   └── GUI/                         # Graphic User Interface (Swing)
│       ├── JanelaPrincipal.java     # Main application window
│       ├── PainelMapa.java          # 2D visual canvas for simulation rendering
│       ├── DialogoCorrente.java     # Dialog for ocean current configuration
│       └── Acao*.java               # Action handlers (Restart, Direction Toggle, etc.)
└── test/
└── Engine/                      # Unit tests for all engine components

```

### Key Design Patterns Used:
- **Strategy Pattern:** `EstrategiaRota` interface implemented by `EstrategiaDijkstra` to swap routing strategies cleanly.
- **State Pattern:** `EstadoMovel` hierarchy (`MovelNaOrigem`, `MovelNavegando`, `MovelAguardando`, `MovelNoDestino`) managing vessel lifecycle.
- **Memento / Snapshot Pattern:** `SnapshotSimulacao` for simulation state capture and playback.
- **MVC / Separation of Concerns:** Decoupled simulation logic (`Engine`) and graphical rendering (`GUI`).

---

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK):** Version 8 or higher (JDK 11+ recommended).
- **IDE (Optional):** IntelliJ IDEA, Eclipse, or VS Code with Java extensions.

### Compilation & Execution

#### 1. Compile the Project
From the project root directory, compile all Java source files:

```bash
# Create output directory
mkdir -p bin

# Compile all source files
javac -d bin -sourcepath src src/Cliente.java src/Engine/*.java src/GUI/*.java

```

#### 2. Run the Application

Launch the simulator via the main entry class:

```bash
java -cp bin Cliente

```

---

## 🧪 Running Tests

Unit tests are located under the `test/` directory. If using JUnit:

```bash
# Example running tests via javac/java with JUnit standalone console runner:
javac -d bin -cp "bin:lib/junit-platform-console-standalone.jar" src/**/*.java test/Engine/*.java
java -jar lib/junit-platform-console-standalone.jar --class-path bin --scan-class-path

```

---

## 📜 License

This project is developed for educational and research purposes in maritime simulation and object-oriented software engineering.
