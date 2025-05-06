-- Create authors table
  CREATE TABLE authors (
      id SERIAL PRIMARY KEY,
      name VARCHAR(255) NOT NULL
  );

  -- Create genres table
  CREATE TABLE genres (
      id SERIAL PRIMARY KEY,
      name VARCHAR(255) NOT NULL UNIQUE
  );

  -- Create books table
  CREATE TABLE books (
      id SERIAL PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      author_id INTEGER NOT NULL REFERENCES authors(id) ON DELETE RESTRICT,
      description TEXT
  );

  -- Create book_genres table (many-to-many)
  CREATE TABLE book_genres (
      book_id INTEGER NOT NULL REFERENCES books(id) ON DELETE CASCADE,
      genre_id INTEGER NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
      PRIMARY KEY (book_id, genre_id)
  );

  -- Insert sample data
  INSERT INTO authors (name) VALUES
      ('Frank Herbert'),
      ('J.R.R. Tolkien');

  INSERT INTO genres (name) VALUES
      ('Sci-Fi'),
      ('Fantasy');

  INSERT INTO books (title, author_id, description) VALUES
      ('Dune', 1, 'A sci-fi epic about a desert planet.'),
      ('The Hobbit', 2, 'A fantasy adventure with hobbits and dragons.');

  INSERT INTO book_genres (book_id, genre_id) VALUES
      (1, 1), -- Dune: Sci-Fi
      (2, 2); -- The Hobbit: Fantasy