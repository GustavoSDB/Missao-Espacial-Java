CREATE TABLE IF NOT EXISTS foguete
(
    id          Integer PRIMARY KEY AUTOINCREMENT,
    nome        TEXT,
    combustivel REAL,
    cargaMaxima REAL,
    status      TEXT
);

CREATE TABLE IF NOT EXISTS satelite (
    id Integer PRIMARY KEY AUTOINCREMENT,
    nome TEXT,
    massa REAL,
    orbita TEXT,
    energia REAL,
    status TEXT
);

CREATE TABLE IF NOT EXISTS missao (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fogueteID INTEGER,
    sateliteID INTEGER,
    status TEXT,

    FOREIGN KEY (fogueteID) REFERENCES foguete(id),
    FOREIGN KEY (sateliteID) REFERENCES satelite(id)
)