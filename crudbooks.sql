CREATE TABLE books (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    title VARCHAR(255),
    authors VARCHAR(255),
    isbn VARCHAR(20),
    synopsis TEXT,
    status VARCHAR(10) DEFAULT 'ON'
);

INSERT INTO books (title, authors, isbn, synopsis, status) VALUES
('Clean Code', 'Robert C. Martin', '9780132350884', 'A Handbook of Agile Software Craftsmanship.', 'ON'),
('Effective Java', 'Joshua Bloch', '9780134685991', 'Best practices for Java programming.', 'ON'),
('Design Patterns', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', '9780201633610', 'Elements of Reusable Object-Oriented Software.', 'ON'),
('Refactoring', 'Martin Fowler', '9780201485677', 'Improving the Design of Existing Code.', 'ON'),
('The Pragmatic Programmer', 'Andrew Hunt, David Thomas', '9780201616224', 'Your Journey to Mastery.', 'OFF');
