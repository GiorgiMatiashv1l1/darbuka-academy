CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at    TIMESTAMP  NOT NULL DEFAULT now(),
    CONSTRAINT users_role_check CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE courses (
    id            BIGSERIAL PRIMARY KEY,
    level         VARCHAR(20)  NOT NULL UNIQUE,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    price_cents   INTEGER      NOT NULL,
    currency      VARCHAR(3)   NOT NULL DEFAULT 'USD',
    thumbnail_url VARCHAR(500),
    active        BOOLEAN      NOT NULL DEFAULT true,
    CONSTRAINT courses_level_check CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'PROFESSIONAL')),
    CONSTRAINT courses_price_check CHECK (price_cents >= 0)
);

CREATE TABLE lessons (
    id               BIGSERIAL PRIMARY KEY,
    course_id        BIGINT       NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    title            VARCHAR(255) NOT NULL,
    description      TEXT,
    video_id         VARCHAR(255) NOT NULL,
    duration_seconds INTEGER      NOT NULL DEFAULT 0,
    position         INTEGER      NOT NULL,
    is_free_preview  BOOLEAN      NOT NULL DEFAULT false,
    CONSTRAINT lessons_course_position_unique UNIQUE (course_id, position)
);

CREATE TABLE purchases (
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id         BIGINT      NOT NULL REFERENCES courses(id) ON DELETE RESTRICT,
    status            VARCHAR(20) NOT NULL,
    stripe_session_id VARCHAR(255) UNIQUE,
    amount_cents      INTEGER     NOT NULL,
    currency          VARCHAR(3)  NOT NULL DEFAULT 'USD',
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    paid_at           TIMESTAMPTZ,
    CONSTRAINT purchases_status_check CHECK (status IN ('PENDING', 'PAID', 'FAILED', 'REFUNDED'))
);

-- a user can only own a course once; PENDING rows may repeat (abandoned checkouts)
CREATE UNIQUE INDEX purchases_paid_unique
    ON purchases (user_id, course_id)
    WHERE status = 'PAID';

CREATE INDEX purchases_user_idx ON purchases (user_id);

CREATE TABLE lesson_progress (
    id                    BIGSERIAL PRIMARY KEY,
    user_id               BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lesson_id             BIGINT      NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    completed             BOOLEAN     NOT NULL DEFAULT false,
    last_position_seconds INTEGER     NOT NULL DEFAULT 0,
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT lesson_progress_unique UNIQUE (user_id, lesson_id)
);