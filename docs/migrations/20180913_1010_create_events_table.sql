use `kyoto-a`;

create table events (
    id bigint(13) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    event_type int(11) NOT NULL,
    target_id bigint(13) NOT NULL,
    created_at datetime NOT NULL default current_timestamp,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
