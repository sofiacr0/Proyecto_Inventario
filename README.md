# 📦 Sistema de Inventario v2.0

[![Java](https://img.shields.io/badge/Java-21-007396?logo=java)](https://openjdk.org/projects/jdk/21/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.11-1B6CA8)](https://openjfx.io/)
[![ORMLite](https://img.shields.io/badge/ORMLite-6.1-green)](https://ormlite.com/)
[![JUnit](https://img.shields.io/badge/JUnit-5.10-25A162)](https://junit.org/junit5/)
[![SQLite](https://img.shields.io/badge/SQLite-3.45-003B57)](https://sqlite.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Sistema de gestión de inventario desarrollado con JavaFX, SQLite y ORMLite bajo una arquitectura MVC en capas.
La aplicación permite administrar productos y almacenes con autenticación por roles y persistencia local mediante SQLite.

---

# ⚙️ Requisitos

| Dependencia  | Versión  |
| ------------ | -------- |
| Java JDK     | 21       |
| JavaFX SDK   | 21.0.11  |
| Maven        | 3.8+     |
| SQLite JDBC  | 3.45.1.0 |
| ORMLite Core | 6.1      |
| ORMLite JDBC | 6.1      |
| JUnit        | 5.10     |

---

# 📦 Dependencias Maven utilizadas

```xml
<dependencies>

    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.2</version>
    </dependency>

    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.2</version>
    </dependency>

    <!-- SQLite -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.45.1.0</version>
    </dependency>

    <!-- ORMLite -->
    <dependency>
        <groupId>com.j256.ormlite</groupId>
        <artifactId>ormlite-core</artifactId>
        <version>6.1</version>
    </dependency>

    <dependency>
        <groupId>com.j256.ormlite</groupId>
        <artifactId>ormlite-jdbc</artifactId>
        <version>6.1</version>
    </dependency>

    <!-- JUnit -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>

</dependencies>
```

---

# 🚀 Ejecución del proyecto

## Ejecutar desde IntelliJ IDEA

Configurar:

* SDK del proyecto: Java 21
* Language Level: SDK Default
* VM Options:

```bash
--module-path "C:\Users\HP\IdeaProjects\Proyecto_Inventario\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml
```

---

# ▶️ Ejecutar el archivo JAR

Para ejecutar correctamente el `.jar`, JavaFX debe cargarse manualmente mediante módulos.

Ejemplo:

```bash
java --module-path "C:\Users\HP\IdeaProjects\Proyecto_Inventario\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml -jar proyecto_inventario.jar
```

---

# 📁 Estructura requerida para distribución

```text
proyecto inventario/
│
├── proyecto_inventario.jar
├── javafx-sdk-21.0.11/
│   └── lib/
│
└── db/
    └── inventario_v2.db
```

La carpeta `db` debe existir junto al `.jar`, ya que SQLite utiliza una ruta relativa para almacenar la base de datos.

---

# ⚠️ Problemas comunes

## Error:

```text
JavaFX runtime components are missing
```

### Solución:

Ejecutar el `.jar` usando `--module-path` y `--add-modules`.

---

## Error:

```text
Unsupported major.minor version 67.0
```

### Causa:

Dependencias JavaFX compiladas para Java 23/24 mientras el proyecto usa Java 21.

### Solución:

Usar exclusivamente JavaFX SDK 21 y eliminar dependencias mezcladas de otras versiones.

---

## Error:

```text
Module javafx.controls not found
```

### Solución:

Verificar que el `--module-path` apunte a la carpeta `/lib` del SDK JavaFX y no a la carpeta raíz.

Correcto:

```bash
--module-path "C:\javafx-sdk-21.0.11\lib"
```

Incorrecto:

```bash
--module-path "C:\javafx-sdk-21.0.11"
```

---

# 🧪 Ejecutar pruebas

```bash
mvn test
```

---

# 📖 Generar JavaDoc

```bash
mvn javadoc:javadoc
```

---

# 👤 Usuarios predefinidos

| Usuario   | Contraseña  | Rol                  |
| --------- | ----------- | -------------------- |
| ADMIN     | admin23     | Acceso total         |
| PRODUCTOS | productos19 | Gestión de productos |
| ALMACENES | almacenes11 | Gestión de almacenes |

---

# 📄 Licencia

MIT License.

---

Sistema de Inventario v2.0
Universidad de Sonora — 2026
