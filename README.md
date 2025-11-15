# 🚀 Task Tracker

Full-featured Spring Boot Task Tracker — REST-сервис для управления задачами с CRUD, фильтрацией, пагинацией и сортировкой.

---

## 💡 О проекте
Task Tracker — это сервис для управления задачами: создание, изменение, удаление и фильтрация по статусу и приоритету. Проект полностью реализован на Spring Boot с использованием PostgreSQL и Docker.

---

## ⚙️ Технологический стек

| Категория | Инструменты |
|-----------|------------|
| Backend   | Java 17+, Spring Boot 3+, Spring Web, Spring Data JPA, Hibernate |
| БД       | PostgreSQL (или H2 для локальной отладки) |
| Миграции | Flyway |
| Тесты    | JUnit 5, Mockito |
| Сборка   | Maven |
| Документация API | Springdoc OpenAPI (Swagger UI) |

---

## 🧩 Функционал

### CRUD для задач
- `POST /api/tasks` — создать задачу
- `GET /api/tasks` — получить список всех задач
- `GET /api/tasks/{id}` — получить задачу по id
- `PUT /api/tasks/{id}` — обновить задачу
- `DELETE /api/tasks/{id}` — удалить задачу

### Фильтрация и сортировка
- `GET /api/tasks?status=IN_PROGRESS` — фильтр по статусу (NEW, IN_PROGRESS, DONE)
- `GET /api/tasks?priority=HIGH` — фильтр по приоритету (LOW, MEDIUM, HIGH)
- Параметры пагинации: `page`, `size`
- Сортировка: `sort=title,asc&sort=createdAt,desc`

### Поля задачи
- `id` (Long)
- `title` (String)
- `description` (String, ≤500 символов)
- `status` (Enum: NEW, IN_PROGRESS, DONE)
- `priority` (Enum: LOW, MEDIUM, HIGH)
- `createdAt` (LocalDateTime)
- `updatedAt` (LocalDateTime)

---

## 🐳 Docker / Запуск

1. Скопируйте `application-example.yml` в `src/main/resources/application.yml`.
2. Создайте `.env` файл в корне проекта:

```env
DB_NAME=tasktracker
DB_USER=myuser
DB_PASSWORD=mysecretpassword
```
3. Поднимите Docker Compose:
```env
docker-compose up -d
```

4. Запустите проект:
```
mvn spring-boot:run
```

5. Swagger UI доступен по: http://localhost:8080/swagger-ui.html

