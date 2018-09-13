CREATE DATABASE IF NOT EXISTS `kyoto-a`;
use `kyoto-a`;

create table users (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `uid` VARCHAR(255) COLLATE utf8mb4_bin NOT NULL,
    `name` VARCHAR(255) COLLATE utf8mb4_bin NOT NULL,
    created_at datetime default current_timestamp,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

create table rooms (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) COLLATE utf8mb4_bin NOT NULL DEFAULT "",
    created_at datetime default current_timestamp,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

create table users_rooms (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id bigint(13) NOT NULL,
    room_id bigint(13) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    UNIQUE (room_id, user_id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

create table messages (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    room_id bigint(13) NOT NULL,
    user_id bigint(13) NOT NULL,
    `text` longtext NOT NULL,
    created_at datetime NOT NULL default current_timestamp,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

create table events (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    event_type int(11) NOT NULL,
    target_id bigint(13) NOT NULL,
    created_at datetime NOT NULL default current_timestamp,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
