
CREATE TABLE todo_lists (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL
);

--;;

INSERT INTO todo_lists (name) VALUES ("default"); 

--;;

ALTER TABLE todos ADD COLUMN list_id INTEGER REFERENCES todo_lists(id);

--;;

UPDATE todos SET list_id = (SELECT id FROM todo_lists WHERE name = "default");
