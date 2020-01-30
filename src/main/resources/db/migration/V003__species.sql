CREATE TABLE `species`
(
  `id`    varchar(64) NOT NULL,
  `name`  varchar(190) DEFAULT NULL,
  `emoji` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
