CREATE TABLE `animal`
(
  `id`         varchar(64)  NOT NULL,
  `name`       varchar(190)          DEFAULT NULL,
  `nr_likes`   bigint(20)   NOT NULL,
  `species_id` varchar(64)           DEFAULT NULL,
  `status`     varchar(190) NOT NULL DEFAULT 'AWAKE',
  PRIMARY KEY (`id`),
  UNIQUE KEY (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
