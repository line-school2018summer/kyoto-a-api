use `kyoto-a`

ALTER TABLE events ADD room_id bigint(13) DEFAULT NULL;
ALTER TABLE events ADD user_id bigint(13) DEFAULT NULL;
ALTER TABLE events ADD message_id bigint(13) DEFAULT NULL;

ALTER TABLE events DROP COLUMN target_id;
