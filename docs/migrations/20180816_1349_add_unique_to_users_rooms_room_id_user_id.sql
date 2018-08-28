use `kyoto-a`;

ALTER TABLE users_rooms ADD UNIQUE (room_id, user_id);
