DROP TABLE IF EXISTS compilations, requests, events, users, categories, locations, compilations_events, grades  CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(100) UNIQUE,
    rate  NUMERIC(10, 2) DEFAULT 0,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(200),
    CONSTRAINT UQ_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat FLOAT,
    lon FLOAT
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         TEXT
        CONSTRAINT annotation_length CHECK (char_length(annotation) >= 20 AND char_length(annotation) <= 2000),
    category_id        BIGINT NOT NULL
        CONSTRAINT events_categories_fkey REFERENCES categories,
    confirmed_requests INTEGER DEFAULT 0,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        TEXT,
    CONSTRAINT description_length CHECK (char_length(description) >= 20 AND char_length(description) <= 7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       BIGINT NOT NULL
        CONSTRAINT events_users_fkey REFERENCES users,
    location_id        BIGINT NOT NULL
        CONSTRAINT events_locations_fkey REFERENCES locations,
    paid               BOOLEAN default false,
    participant_limit  INTEGER DEFAULT 0,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT true,
    state              VARCHAR(15),
    title              VARCHAR(120)
        CONSTRAINT title_length CHECK (char_length(title) >= 3),
    views              BIGINT  DEFAULT 0,
    rate               INTEGER DEFAULT 0,
    user_rate          REAL  DEFAULT 0
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title  VARCHAR(120),
    pinned BOOLEAN default false
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id       BIGINT REFERENCES events (id),
    compilation_id BIGINT REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS grades
(
    user_id  BIGINT REFERENCES users (id) NOT NULL,
    event_id BIGINT REFERENCES events (id) NOT NULL,
    type     VARCHAR(7) NOT NULL,
    CONSTRAINT PK_Tmp PRIMARY KEY (user_id, event_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requester_id BIGINT REFERENCES users (id),
    created      TIMESTAMP WITHOUT TIME ZONE,
    status       VARCHAR(15),
    event_id     BIGINT REFERENCES events (id),
    UNIQUE (event_id, requester_id)
);