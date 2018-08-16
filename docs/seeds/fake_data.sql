use `kyoto-a`;

INSERT INTO users(uid, `name`) VALUES('fakeid1', 'John');
INSERT INTO users(uid, `name`) VALUES('fakeid2', 'Mike');
INSERT INTO users(uid, `name`) VALUES('fakeid3', 'Ken');
INSERT INTO users(uid, `name`) VALUES('X4oRPWJrQUbvQwFLzF9l98pczdh1', 'Hoge')

INSERT INTO rooms(`name`) VALUES('');
INSERT INTO rooms(`name`) VALUES('my awesome group');

INSERT INTO users_rooms(room_id, user_id) VALUES(1, 1);
INSERT INTO users_rooms(room_id, user_id) VALUES(2, 1);
INSERT INTO users_rooms(room_id, user_id) VALUES(2, 2);
INSERT INTO users_rooms(room_id, user_id) VALUES(2, 4);

INSERT INTO messages(room_id, user_id, `text`) VALUES(1, 1, 'This is my secret group!');
INSERT INTO messages(room_id, user_id, `text`) VALUES(1, 1, 'This group has no members without me!');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 1, 'Hello Mike!');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 2, 'Hello John!');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 2, 'What is this group?');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 1, 'This group exists for our friendship more deeply!');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 1, 'Let\'s have a good night with me?');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 2, 'Yeah...');

INSERT INTO users_rooms(room_id, user_id) VALUES(2, 3);

INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 3, 'Hello! I\'ve joined here!');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 3, 'What you are doing??');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 1, 'Oh no! This is our love nest!!!!');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 2, 'My dear John...');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 3, '俺日本語もしゃべれるんだぜ！！');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 3, '🍰🍣🍺');
INSERT INTO messages(room_id, user_id, `text`) VALUES(2, 1, 'I hate you!!!!!');
