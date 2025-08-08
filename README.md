[![Tests](https://github.com/dieegopa/spring-todo-api/actions/workflows/tests.yml/badge.svg)](https://github.com/dieegopa/spring-todo-api/actions/workflows/tests.yml)

# Spring Boot REST API - ToDo

Este proyecto es una API RESTful para gestionar tareas (ToDo), construida con Spring Boot y siguiendo buenas prácticas de desarrollo.

## 🧰 Tecnologías

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Gradle
- Lombok

## ⚙️ Configuración del Proyecto

### Requisitos

- Java 17+
- Gradle 7.5+
- IDE como IntelliJ IDEA o VS Code
- Docker y Docker Compose (opcional para correr con contenedores)

### Instalación y ejecución local sin Docker

```bash
git clone https://github.com/dieegopa/spring-todo-api.git
cd spring-todo-api
./gradlew build
./gradlew bootRun
```
La API estará disponible en:
```
http://localhost:8080
```

### Ejecución con Docker y Docker Compose
Para facilitar la gestión del entorno y la base de datos MySQL, puedes usar Docker Compose.

1. Asegúrate de tener Docker y Docker Compose instalados.
2. Asegurate que el datasourceurl sea como: jdbc:mysql://db:3306/{{db_name}}
3. Construye y levanta los servicios con Docker Compose:

```bash
docker-compose up --build -d
```
La API estará disponible en:
```
http://localhost:8080
```

Si vas a desplegarlo necesitaras tambien SPRING_PROFILES_ACTIVE para definir el perfil de produccion, por ejemplo:

```env
SPRING_PROFILES_ACTIVE=prod
```
