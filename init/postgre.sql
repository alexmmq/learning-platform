
--  SQL schema for Learning Platform (PostgreSQL)

-- Drop tables in reverse order (если нужно пересоздать)

-- DROP TABLE IF EXISTS quiz_submissions CASCADE;
-- DROP TABLE IF EXISTS answer_options CASCADE;
-- DROP TABLE IF EXISTS questions CASCADE;
-- DROP TABLE IF EXISTS quizzes CASCADE;
-- DROP TABLE IF EXISTS submissions CASCADE;
-- DROP TABLE IF EXISTS assignments CASCADE;
-- DROP TABLE IF EXISTS lessons CASCADE;
-- DROP TABLE IF EXISTS modules CASCADE;
-- DROP TABLE IF EXISTS course_tags CASCADE;
-- DROP TABLE IF EXISTS enrollments CASCADE;
-- DROP TABLE IF EXISTS course_reviews CASCADE;
-- DROP TABLE IF EXISTS profiles CASCADE;
-- DROP TABLE IF EXISTS courses CASCADE;
-- DROP TABLE IF EXISTS tags CASCADE;
-- DROP TABLE IF EXISTS categories CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;

CREATE SCHEMA IF NOT EXISTS learning_platform;

SET search_path TO learning_plattform;

--  USERS

CREATE TABLE IF NOT EXISTS users (
                                     id           BIGSERIAL PRIMARY KEY,
                                     name         VARCHAR(255) NOT NULL,
                                     email        VARCHAR(255) NOT NULL UNIQUE,
                                     role         VARCHAR(50)  NOT NULL CHECK (role IN ('STUDENT', 'TEACHER', 'ADMIN'))
);

--  PROFILES (1:1 с users)

CREATE TABLE IF NOT EXISTS profiles (
                                        id        BIGSERIAL PRIMARY KEY,
                                        user_id   BIGINT NOT NULL UNIQUE,
                                        bio       VARCHAR(2000),
                                        avatar_url VARCHAR(1024),

                                        CONSTRAINT fk_profiles_user
                                            FOREIGN KEY (user_id)
                                                REFERENCES users (id)
                                                ON DELETE CASCADE
);

--  CATEGORIES

CREATE TABLE IF NOT EXISTS categories (
                                          id    BIGSERIAL PRIMARY KEY,
                                          name  VARCHAR(255) NOT NULL UNIQUE
);

--  TAGS

CREATE TABLE IF NOT EXISTS tags (
                                    id    BIGSERIAL PRIMARY KEY,
                                    name  VARCHAR(255) NOT NULL UNIQUE
);

--  COURSES

CREATE TABLE IF NOT EXISTS courses (
                                       id                BIGSERIAL PRIMARY KEY,
                                       title             VARCHAR(255) NOT NULL,
                                       description       VARCHAR(4000),
                                       duration_in_hours INTEGER,
                                       start_date        DATE,

                                       category_id       BIGINT,
                                       teacher_id        BIGINT NOT NULL,

                                       CONSTRAINT fk_courses_category
                                           FOREIGN KEY (category_id)
                                               REFERENCES categories (id)
                                               ON DELETE SET NULL,

                                       CONSTRAINT fk_courses_teacher
                                           FOREIGN KEY (teacher_id)
                                               REFERENCES users (id)
                                               ON DELETE RESTRICT
);

--  COURSE_REVIEWS

CREATE TABLE IF NOT EXISTS course_reviews (
                                              id          BIGSERIAL PRIMARY KEY,
                                              course_id   BIGINT NOT NULL,
                                              student_id  BIGINT NOT NULL,
                                              rating      INTEGER NOT NULL,
                                              comment     VARCHAR(4000),
                                              created_at  TIMESTAMP,

                                              CONSTRAINT fk_course_reviews_course
                                                  FOREIGN KEY (course_id)
                                                      REFERENCES courses (id)
                                                      ON DELETE CASCADE,

                                              CONSTRAINT fk_course_reviews_student
                                                  FOREIGN KEY (student_id)
                                                      REFERENCES users (id)
                                                      ON DELETE CASCADE
);

--  ENROLLMENTS (записи на курс)

CREATE TABLE IF NOT EXISTS enrollments (
                                           id          BIGSERIAL PRIMARY KEY,
                                           student_id  BIGINT NOT NULL,
                                           course_id   BIGINT NOT NULL,
                                           enroll_date DATE,
                                           status      VARCHAR(50),

                                           CONSTRAINT uq_enrollment_student_course
                                               UNIQUE (student_id, course_id),

                                           CONSTRAINT fk_enrollment_student
                                               FOREIGN KEY (student_id)
                                                   REFERENCES users (id)
                                                   ON DELETE CASCADE,

                                           CONSTRAINT fk_enrollment_course
                                               FOREIGN KEY (course_id)
                                                   REFERENCES courses (id)
                                                   ON DELETE CASCADE
);

--  COURSE_TAGS (Many-to-Many: courses <-> tags)

CREATE TABLE IF NOT EXISTS course_tags (
                                           course_id BIGINT NOT NULL,
                                           tag_id    BIGINT NOT NULL,

                                           PRIMARY KEY (course_id, tag_id),

                                           CONSTRAINT fk_course_tags_course
                                               FOREIGN KEY (course_id)
                                                   REFERENCES courses (id)
                                                   ON DELETE CASCADE,

                                           CONSTRAINT fk_course_tags_tag
                                               FOREIGN KEY (tag_id)
                                                   REFERENCES tags (id)
                                                   ON DELETE CASCADE
);

--  MODULES

CREATE TABLE IF NOT EXISTS modules (
                                       id          BIGSERIAL PRIMARY KEY,
                                       title       VARCHAR(255) NOT NULL,
                                       order_index INTEGER,
                                       course_id   BIGINT NOT NULL,

                                       CONSTRAINT fk_modules_course
                                           FOREIGN KEY (course_id)
                                               REFERENCES courses (id)
                                               ON DELETE CASCADE
);

--  LESSONS

CREATE TABLE IF NOT EXISTS lessons (
                                       id        BIGSERIAL PRIMARY KEY,
                                       title     VARCHAR(255) NOT NULL,
                                       content   VARCHAR(8000),
                                       video_url VARCHAR(1024),
                                       module_id BIGINT NOT NULL,

                                       CONSTRAINT fk_lessons_module
                                           FOREIGN KEY (module_id)
                                               REFERENCES modules (id)
                                               ON DELETE CASCADE
);

--  ASSIGNMENTS

CREATE TABLE IF NOT EXISTS assignments (
                                           id          BIGSERIAL PRIMARY KEY,
                                           title       VARCHAR(255) NOT NULL,
                                           description VARCHAR(4000),
                                           due_date    TIMESTAMP,
                                           max_score   INTEGER,
                                           lesson_id   BIGINT NOT NULL,

                                           CONSTRAINT fk_assignments_lesson
                                               FOREIGN KEY (lesson_id)
                                                   REFERENCES lessons (id)
                                                   ON DELETE CASCADE
);

--  SUBMISSIONS (решения заданий)

CREATE TABLE IF NOT EXISTS submissions (
                                           id            BIGSERIAL PRIMARY KEY,
                                           assignment_id BIGINT NOT NULL,
                                           student_id    BIGINT NOT NULL,
                                           submitted_at  TIMESTAMP,
                                           content       VARCHAR(8000),
                                           score         INTEGER,
                                           feedback      VARCHAR(4000),

                                           CONSTRAINT uq_submission_assignment_student
                                               UNIQUE (assignment_id, student_id),

                                           CONSTRAINT fk_submissions_assignment
                                               FOREIGN KEY (assignment_id)
                                                   REFERENCES assignments (id)
                                                   ON DELETE CASCADE,

                                           CONSTRAINT fk_submissions_student
                                               FOREIGN KEY (student_id)
                                                   REFERENCES users (id)
                                                   ON DELETE CASCADE
);

--  QUIZZES

CREATE TABLE IF NOT EXISTS quizzes (
                                       id                 BIGSERIAL PRIMARY KEY,
                                       title              VARCHAR(255),
                                       time_limit_minutes INTEGER,
                                       module_id          BIGINT NOT NULL UNIQUE,

                                       CONSTRAINT fk_quizzes_module
                                           FOREIGN KEY (module_id)
                                               REFERENCES modules (id)
                                               ON DELETE CASCADE
);

--  QUESTIONS

CREATE TABLE IF NOT EXISTS questions (
                                         id          BIGSERIAL PRIMARY KEY,
                                         text        VARCHAR(2000) NOT NULL,
                                         type        VARCHAR(50)   NOT NULL CHECK (type IN ('SINGLE_CHOICE', 'MULTIPLE_CHOICE')),
                                         quiz_id     BIGINT NOT NULL,

                                         CONSTRAINT fk_questions_quiz
                                             FOREIGN KEY (quiz_id)
                                                 REFERENCES quizzes (id)
                                                 ON DELETE CASCADE
);

--  ANSWER_OPTIONS

CREATE TABLE IF NOT EXISTS answer_options (
                                              id          BIGSERIAL PRIMARY KEY,
                                              text        VARCHAR(2000) NOT NULL,
                                              correct     BOOLEAN       NOT NULL,
                                              question_id BIGINT        NOT NULL,

                                              CONSTRAINT fk_answer_options_question
                                                  FOREIGN KEY (question_id)
                                                      REFERENCES questions (id)
                                                      ON DELETE CASCADE
);

--  QUIZ_SUBMISSIONS

CREATE TABLE IF NOT EXISTS quiz_submissions (
                                                id         BIGSERIAL PRIMARY KEY,
                                                quiz_id    BIGINT NOT NULL,
                                                student_id BIGINT NOT NULL,
                                                score      INTEGER,
                                                taken_at   TIMESTAMP,

                                                CONSTRAINT uq_quiz_submission_quiz_student
                                                    UNIQUE (quiz_id, student_id),

                                                CONSTRAINT fk_quiz_submissions_quiz
                                                    FOREIGN KEY (quiz_id)
                                                        REFERENCES quizzes (id)
                                                        ON DELETE CASCADE,

                                                CONSTRAINT fk_quiz_submissions_student
                                                    FOREIGN KEY (student_id)
                                                        REFERENCES users (id)
                                                        ON DELETE CASCADE
);
