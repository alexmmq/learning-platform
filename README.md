# Learning Platform — учебная платформа на Spring Boot + PostgreSQL

Учебный проект: веб-приложение (backend) для онлайн-платформы, на которой:

- создаются курсы, модули и уроки;
- студенты записываются на курсы;
- преподаватели выдают задания;
- студенты отправляют решения;
- есть тесты (quiz) и сохранение результатов.

Проект демонстрирует работу с **Spring Boot**, **Spring Data JPA (Hibernate)**, связями `ONE-TO-ONE`, `ONE-YO-MANY`, `MANY-TO-MANY`, ленивой загрузкой, репозиториями и интеграционными тестами.

---

## 1. Стек технологий

- Java 17+
- Spring Boot 3.5.7
    - spring-boot-starter-web
    - spring-boot-starter-data-jpa
- Hibernate (через Spring Data JPA)
- PostgreSQL
- Maven
- Lombok 
- JUnit 5 

---

## 2. Основные сущности

Модель данных ~18 сущностей, покрывающих основные сценарии учебной платформы:

- **User** — пользователь системы (STUDENT / TEACHER / ADMIN)
- **Profile** — профиль пользователя (био, аватар)
- **Course** — курс
- **Category** — категория курса (Programming, Design и т.п.)
- **Tag** — тег курса (Java, Hibernate, Beginner)
- **Enrollment** — запись студента на курс (student + course + статус)
- **CourseReview** — отзыв студента о курсе

Контент:

- **Module** — модуль курса
- **Lesson** — урок
- **Assignment** — задание (домашка)
- **Submission** — решение задания студентом

Тестирование:

- **Quiz** — тест по модулю
- **Question** — вопрос теста
- **AnswerOption** — вариант ответа
- **QuizSubmission** — результат теста (итоговый балл студента)

Используются связи `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany` (Course–Tag).

Все коллекции связей настроены на **ленивую загрузку** (`FetchType.LAZY`), чтобы можно было увидеть типичные проблемы `LazyInitializationException`.

---

## 3. Требования для развертывания приложения

Ручная установка:
- Установлен JDK 17+
- Установлен Maven
- Установлен PostgreSQL
- Создана база данных и пользователь для приложения (использовать файл скрипта postgre.sql в директории /init)

Автоматическая установка:
- Установлен Docker
- Запустить в директории docker-compose.yaml  

## 4. Работа с эндпойнтами
- Создание пользователя
```  POST /api/users
  Content-Type: application/json

{
"name": "Alice",
"email": "alice@example.com",
"role": "STUDENT",
"bio": "Student of Java",
"avatarUrl": "https://example.com/avatar.png"
}
```
- Получение пользователя
```
GET /api/users/{userId}
```
- Получить курсы, на которые записан студент
```
GET /api/users/{userId}/courses
```
- Решения заданий студентом
```
GET /api/users/{userId}/submissions
```
- Результаты тестов студента
```
GET /api/users/{userId}/quiz-results
```
- Создать курс
```
POST /api/courses
Content-Type: application/json

{
  "title": "Hibernate Basics",
  "description": "Intro to JPA",
  "categoryId": 1,
  "teacherId": 10,
  "durationInHours": 20,
  "startDate": "2025-01-10",
  "tags": ["Java", "Hibernate"]
}
```
- Получить список курсов
```
GET /api/courses
GET /api/courses?category=Programming
```
- Получить курс с модулями и уроками
```
GET /api/courses/{courseId}
```
- Обновить данные о курсе
```
PUT /api/courses/{courseId}
Content-Type: application/json

{
  "title": "Hibernate & JPA",
  "description": "Updated description",
  "categoryId": 1,
  "durationInHours": 24,
  "startDate": "2025-02-01",
  "tags": ["Java", "Hibernate", "JPA"]
}
```
- Удлить курс
```
DELETE /api/courses/{courseId}
```
- Добавить модуль в курс
```
POST /api/courses/{courseId}/modules
Content-Type: application/json

{
  "title": "Module 1: Basics",
  "orderIndex": 1
}
```
- Добавить урок в модуль
```
POST /api/courses/modules/{moduleId}/lessons
Content-Type: application/json

{
  "title": "Lesson 1: Intro",
  "content": "Text content of the lesson",
  "videoUrl": "https://example.com/video.mp4"
}
```

- Запись студента на курс
```
POST /api/courses/{courseId}/enroll
Content-Type: application/json

{
  "studentId": 15
}
```
- Удалить студента из слушателей курса
```
POST /api/courses/{courseId}/enroll
Content-Type: application/json

{
  "studentId": 15
}
```
- Получить список студентов-слушателей курса
```
GET /api/courses/{courseId}/students
```

- Создать задание для урока
```
POST /api/lessons/{lessonId}/assignments
Content-Type: application/json

{
  "title": "Homework #1",
  "description": "Implement simple DAO",
  "dueDate": "2025-01-20T23:59:00",
  "maxScore": 100
}
```
- Получить задание
```
GET /api/assignments/{assignmentId}
```
- Отправить решение
```
POST /api/assignments/{assignmentId}/submit
Content-Type: application/json

{
  "studentId": 15,
  "content": "My answer or link to file"
}
```
- Посмотреть все решения по заданию
```
GET /api/assignments/{assignmentId}/submissions
```

- Проверить решение - выставить оценку и комментарий
```
PATCH /api/submissions/{submissionId}/grade
Content-Type: application/json

{
  "score": 90,
  "feedback": "Good job."
}
```

- Создать, обновить тесты модуля
```
POST /api/modules/{moduleId}/quiz
Content-Type: application/json

{
  "title": "Quiz for module 1",
  "timeLimitMinutes": 30,
  "questions": [
    {
      "text": "What is JPA?",
      "type": "SINGLE_CHOICE",
      "options": [
        {"text": "Java Persistence API", "correct": true},
        {"text": "Java Performance Analyzer", "correct": false}
      ]
    }
  ]
}
```

- Получить тест *для студента
```
GET /api/quizzes/{quizId}
```

- Пройти тест
```
POST /api/quizzes/{quizId}/take
Content-Type: application/json

{
  "studentId": 15,
  "answersByQuestionId": {
    "1": [2],
    "2": [5, 6]
  }
}
```

- Получить результаты по тесту
```
GET /api/quizzes/{quizId}/results
```
