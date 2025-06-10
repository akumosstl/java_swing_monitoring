# AI Swing Agent 🎯

A Java Agent for monitoring and automating user interactions on any Java Swing application.

- 🏗️ Built with Java 1.8 and Maven.
- 🔗 Provides a REST API to **record** and **replay** Swing UI interactions.
- 🗂️ Saves interaction data in **JSON files**.
- 🚀 Deployable via **JNLP (Java Web Start)**.

---

## 🚀 Features

- ✅ Capture user interactions like clicks, text input, JTable selections, dropdown selections, and more.
- ✅ Supports components inside nested `JFrame`, `JDialog`, and dynamic UI elements.
- ✅ REST endpoints to start/stop recording and to automate playback.
- ✅ Persistence in JSON file system.
- ✅ Can be attached to any Java Swing application as a **Java Agent**.

---

## 🧰 Tech Stack

- Java 1.8
- Maven
- Embedded HTTP server (`com.sun.net.httpserver`)

---

## 🏗️ Installation

### ✅ Build the Agent

```bash
git clone https://github.com/akumosstl/java_swing_monitoring.git
cd chat_gpt_swing/agent
mvn clean package
```

The JAR will be located in:

```bash
agent/target/agent.jar
```

---

## 🚀 Running with a Java Swing Application

Attach the agent to any Swing app:

```bash
java -javaagent:path/to/agent.jar -jar applicationB.jar
```

---

## 🌐 REST API Endpoints

| Endpoint        | Description                       |
|-----------------|-----------------------------------|
| `/start-record` | Start recording user interactions |
| `/stop-record`  | Stop recording and save data      |
| `/automation`   | Replay the recorded interactions  |

---

## 📂 Output

- JSON file saved with all user interactions.

---

## 💾 Deployment via JNLP

The project includes a JNLP descriptor to launch the agent remotely.

---

## 🤝 Contributing

Contributions are welcome! Please read the [Contributing Guide](CONTRIBUTING.md)
and [Code of Conduct](CODE_OF_CONDUCT.md).

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---

## 🧠 Author

- 👤 Alisson Pedrina  
  [GitHub Profile](https://github.com/akumosstl)
