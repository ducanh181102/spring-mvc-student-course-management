CREATE TABLE IF NOT EXISTS student (
    id BIGINT PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS course (
    id BIGINT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    capacity INT NOT NULL CHECK (capacity >= 0)
);

CREATE TABLE IF NOT EXISTS enrollment (
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);