create database `kyoto-a`;
use `kyoto-a`;

create table Users (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `uid` VARCHAR(255) COLLATE utf8mb4_bin NOT NULL,
    `name` VARCHAR(255) COLLATE utf8mb4_bin NOT NULL,
    created_at datetime default current_timestamp,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

create table Rooms (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) COLLATE utf8mb4_bin NOT NULL DEFAULT "",
    created_at datetime default current_timestamp,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

create table UserRooms (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id bigint(13) NOT NULL,
    room_id bigint(13) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (room_id) REFERENCES Rooms(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

create table Talks (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    room_id bigint(13) NOT NULL,
    user_id bigint(13) NOT NULL,
    `text` longtext NOT NULL,
    created_at datetime NOT NULL default current_timestamp,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (room_id) REFERENCES Rooms(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
