INSERT INTO courses (level, title, description, price_cents, currency) VALUES
('BEGINNER',
 'Darbuka Foundations',
 'Learn how to hold the drum, produce clean Doum and Tek strokes, and play your first rhythms.',
 2900, 'USD'),
('INTERMEDIATE',
 'Rhythms and Technique',
 'Master Maqsum, Baladi, Saidi and Chiftetelli. Build speed, control and clean fills.',
 4900, 'USD'),
('PROFESSIONAL',
 'Advanced Performance',
 'Solo improvisation, complex time signatures, finger rolls, and playing live with an ensemble.',
 7900, 'USD');

-- BEGINNER
INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Meet the Darbuka', 'Parts of the drum, types, and how to choose one.', 'dQw4w9WgXcQ', 420, 1, true
FROM courses WHERE level = 'BEGINNER';

INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Holding Position', 'Sitting posture, arm placement, and hand relaxation.', 'dQw4w9WgXcQ', 380, 2, false
FROM courses WHERE level = 'BEGINNER';

INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Doum and Tek', 'The two core strokes. Tone, placement, and common mistakes.', 'dQw4w9WgXcQ', 540, 3, false
FROM courses WHERE level = 'BEGINNER';

INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Your First Rhythm', 'Playing a basic Maqsum slowly with a metronome.', 'dQw4w9WgXcQ', 610, 4, false
FROM courses WHERE level = 'BEGINNER';

-- INTERMEDIATE
INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Maqsum Variations', 'Adding ornaments and ghost notes to Maqsum.', 'dQw4w9WgXcQ', 700, 1, true
FROM courses WHERE level = 'INTERMEDIATE';

INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Baladi and Saidi', 'Two essential Egyptian rhythms and how they differ.', 'dQw4w9WgXcQ', 820, 2, false
FROM courses WHERE level = 'INTERMEDIATE';

INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Speed Building', 'Drills and metronome routines to build clean speed.', 'dQw4w9WgXcQ', 650, 3, false
FROM courses WHERE level = 'INTERMEDIATE';

-- PROFESSIONAL
INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Finger Rolls', 'The technique that separates amateurs from professionals.', 'dQw4w9WgXcQ', 900, 1, true
FROM courses WHERE level = 'PROFESSIONAL';

INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Solo Improvisation', 'Building a solo with tension, release, and dynamics.', 'dQw4w9WgXcQ', 1100, 2, false
FROM courses WHERE level = 'PROFESSIONAL';

INSERT INTO lessons (course_id, title, description, video_id, duration_seconds, position, is_free_preview)
SELECT id, 'Odd Time Signatures', 'Playing in 7/8 and 9/8 without losing the groove.', 'dQw4w9WgXcQ', 980, 3, false
FROM courses WHERE level = 'PROFESSIONAL';