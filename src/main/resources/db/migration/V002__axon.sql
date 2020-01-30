CREATE TABLE IF NOT EXISTS `domain_event_entry`
(
  `global_index`         bigint(20)                                              NOT NULL,
  `event_identifier`     varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci NOT NULL,
  `meta_data`            longblob,
  `payload`              longblob                                                NOT NULL,
  `payload_revision`     varchar(255)                                            DEFAULT NULL,
  `payload_type`         varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci NOT NULL,
  `time_stamp`           varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci NOT NULL,
  `aggregate_identifier` varchar(40)                                             NOT NULL,
  `sequence_number`      bigint(20)                                              NOT NULL,
  `type`                 varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`global_index`),
  UNIQUE KEY `idx_aggregate_identifier_sequence_number_type` (`aggregate_identifier`, `sequence_number`, `type`),
  UNIQUE KEY `idx_event_identifier` (`event_identifier`),
  KEY `idx_type` (`type`),
  KEY `idx_time_stamp_payload_type` (`time_stamp`, `payload_type`),
  KEY `idx_type_payload_type` (`type`, `payload_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE `snapshot_event_entry`
(
  `id`                   bigint(20)                                              NOT NULL AUTO_INCREMENT,
  `event_identifier`     varchar(40) CHARACTER SET utf8 collate utf8_unicode_ci  DEFAULT NULL,
  `aggregate_identifier` varchar(40) CHARACTER SET utf8 collate utf8_unicode_ci  DEFAULT NULL,
  `sequence_number`      bigint(20)                                              NOT NULL,
  `serialized_event`     longblob,
  `time_stamp`           varchar(40)                                             NOT NULL,
  `type`                 varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci DEFAULT NULL,
  `meta_data`            longblob                                                NOT NULL,
  `payload_type`         varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci NOT NULL,
  `payload_revision`     varchar(10)                                             DEFAULT NULL,
  `payload`              longblob                                                NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_type_aggregate_identifier_sequence_number` (`type`, `aggregate_identifier`, `sequence_number`),
  KEY `idx_type_aggregate_identifier` (`type`, `aggregate_identifier`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `association_value_entry`
(
  `id`                bigint(20)                                              NOT NULL AUTO_INCREMENT,
  `saga_id`           varchar(40)                                             NOT NULL,
  `association_key`   varchar(40)                                             NOT NULL,
  `association_value` varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci NOT NULL,
  `saga_type`         varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `saga_entry`
(
  `saga_id`         varchar(40)                                             NOT NULL,
  `serialized_saga` longblob                                                NOT NULL,
  `revision`        varchar(10) DEFAULT NULL,
  `saga_type`       varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`saga_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `token_entry`
(
  `processor_name` varchar(255) CHARACTER SET utf8 collate utf8_unicode_ci NOT NULL,
  `segment`        int(11)                                                 NOT NULL,
  `owner`          varchar(255) DEFAULT NULL,
  `timestamp`      varchar(255)                                            NOT NULL,
  `token`          longblob,
  `token_type`     varchar(255) DEFAULT NULL,
  PRIMARY KEY (`processor_name`, `segment`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
