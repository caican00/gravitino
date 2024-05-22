--
-- Copyright 2024 Datastrato Pvt Ltd.
-- This software is licensed under the Apache License version 2.
--

CREATE TABLE IF NOT EXISTS `metalake_meta` (
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `metalake_name` VARCHAR(128) NOT NULL COMMENT 'metalake name',
    `metalake_comment` VARCHAR(256) DEFAULT '' COMMENT 'metalake comment',
    `properties` MEDIUMTEXT DEFAULT NULL COMMENT 'metalake properties',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'metalake audit info',
    `schema_version` MEDIUMTEXT NOT NULL COMMENT 'metalake schema version info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'metalake current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'metalake last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'metalake deleted at',
    PRIMARY KEY (metalake_id),
    CONSTRAINT uk_mn_del UNIQUE (metalake_name, deleted_at)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `catalog_meta` (
    `catalog_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'catalog id',
    `catalog_name` VARCHAR(128) NOT NULL COMMENT 'catalog name',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `type` VARCHAR(64) NOT NULL COMMENT 'catalog type',
    `provider` VARCHAR(64) NOT NULL COMMENT 'catalog provider',
    `catalog_comment` VARCHAR(256) DEFAULT '' COMMENT 'catalog comment',
    `properties` MEDIUMTEXT DEFAULT NULL COMMENT 'catalog properties',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'catalog audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'catalog current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'catalog last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'catalog deleted at',
    PRIMARY KEY (catalog_id),
    CONSTRAINT uk_mid_cn_del UNIQUE (metalake_id, catalog_name, deleted_at)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `schema_meta` (
    `schema_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'schema id',
    `schema_name` VARCHAR(128) NOT NULL COMMENT 'schema name',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `catalog_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'catalog id',
    `schema_comment` VARCHAR(256) DEFAULT '' COMMENT 'schema comment',
    `properties` MEDIUMTEXT DEFAULT NULL COMMENT 'schema properties',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'schema audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'schema current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'schema last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'schema deleted at',
    PRIMARY KEY (schema_id),
    CONSTRAINT uk_cid_sn_del UNIQUE (catalog_id, schema_name, deleted_at),
    -- Aliases are used here, and indexes with the same name in H2 can only be created once.
    KEY idx_smid (metalake_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `table_meta` (
    `table_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'table id',
    `table_name` VARCHAR(128) NOT NULL COMMENT 'table name',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `catalog_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'catalog id',
    `schema_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'schema id',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'table audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'table current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'table last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'table deleted at',
    PRIMARY KEY (table_id),
    CONSTRAINT uk_sid_tn_del UNIQUE (schema_id, table_name, deleted_at),
    -- Aliases are used here, and indexes with the same name in H2 can only be created once.
    KEY idx_tmid (metalake_id),
    KEY idx_tcid (catalog_id)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `fileset_meta` (
    `fileset_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'fileset id',
    `fileset_name` VARCHAR(128) NOT NULL COMMENT 'fileset name',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `catalog_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'catalog id',
    `schema_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'schema id',
    `type` VARCHAR(64) NOT NULL COMMENT 'fileset type',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'fileset audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'fileset current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'fileset last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'fileset deleted at',
    PRIMARY KEY (fileset_id),
    CONSTRAINT uk_sid_fn_del UNIQUE (schema_id, fileset_name, deleted_at),
    -- Aliases are used here, and indexes with the same name in H2 can only be created once.
    KEY idx_fmid (metalake_id),
    KEY idx_fcid (catalog_id)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS `fileset_version_info` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `catalog_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'catalog id',
    `schema_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'schema id',
    `fileset_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'fileset id',
    `version` INT UNSIGNED NOT NULL COMMENT 'fileset info version',
    `fileset_comment` VARCHAR(256) DEFAULT '' COMMENT 'fileset comment',
    `properties` MEDIUMTEXT DEFAULT NULL COMMENT 'fileset properties',
    `storage_location` MEDIUMTEXT DEFAULT NULL COMMENT 'fileset storage location',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'fileset deleted at',
    PRIMARY KEY (id),
    CONSTRAINT uk_fid_ver_del UNIQUE (fileset_id, version, deleted_at),
    -- Aliases are used here, and indexes with the same name in H2 can only be created once.
    KEY idx_fvmid (metalake_id),
    KEY idx_fvcid (catalog_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `topic_meta` (
    `topic_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'topic id',
    `topic_name` VARCHAR(128) NOT NULL COMMENT 'topic name',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `catalog_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'catalog id',
    `schema_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'schema id',
    `comment` VARCHAR(256) DEFAULT '' COMMENT 'topic comment',
    `properties` MEDIUMTEXT DEFAULT NULL COMMENT 'topic properties',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'topic audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'topic current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'topic last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'topic deleted at',
    PRIMARY KEY (topic_id),
    CONSTRAINT uk_cid_tn_del UNIQUE (schema_id, topic_name, deleted_at),
    -- Aliases are used here, and indexes with the same name in H2 can only be created once.
    KEY idx_tvmid (metalake_id),
    KEY idx_tvcid (catalog_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `user_meta` (
    `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'user id',
    `user_name` VARCHAR(128) NOT NULL COMMENT 'username',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'user audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'user current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'user last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'user deleted at',
    PRIMARY KEY (`user_id`),
    CONSTRAINT `uk_mid_us_del` UNIQUE (`metalake_id`, `user_name`, `deleted_at`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `role_meta` (
    `role_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'role id',
    `role_name` VARCHAR(128) NOT NULL COMMENT 'role name',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `properties` MEDIUMTEXT DEFAULT NULL COMMENT 'schema properties',
    `securable_object_full_name` VARCHAR(256) NOT NULL COMMENT 'securable object full name',
    `securable_object_type` VARCHAR(32) NOT NULL COMMENT 'securable object type',
    `privileges` VARCHAR(64) NOT NULL COMMENT 'securable privileges',
    `privilege_conditions` VARCHAR(64) NOT NULL COMMENT 'securable privilege conditions',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'role audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'role current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'role last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'role deleted at',
    PRIMARY KEY (`role_id`),
    CONSTRAINT `uk_mid_rn_del` UNIQUE (`metalake_id`, `role_name`, `deleted_at`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `user_role_rel` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
    `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'user id',
    `role_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'role id',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'relation audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'relation current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'relation last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'relation deleted at',
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_ui_ri_del` UNIQUE (`user_id`, `role_id`, `deleted_at`),
    KEY `idx_rid` (`role_id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `group_meta` (
    `group_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'group id',
    `group_name` VARCHAR(128) NOT NULL COMMENT 'group name',
    `metalake_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'metalake id',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'group audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'group current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'group last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'group deleted at',
    PRIMARY KEY (`group_id`),
    CONSTRAINT `uk_mid_gr_del` UNIQUE (`metalake_id`, `group_name`, `deleted_at`)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `group_role_rel` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
    `group_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'group id',
    `role_id` BIGINT(20) UNSIGNED NOT NULL COMMENT 'role id',
    `audit_info` MEDIUMTEXT NOT NULL COMMENT 'relation audit info',
    `current_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'relation current version',
    `last_version` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'relation last version',
    `deleted_at` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'relation deleted at',
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_gi_ri_del` UNIQUE (`group_id`, `role_id`, `deleted_at`),
    KEY `idx_gid` (`group_id`)
    ) ENGINE=InnoDB;