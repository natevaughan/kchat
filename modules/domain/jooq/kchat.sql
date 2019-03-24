DROP DATABASE IF EXISTS kchat; CREATE DATABASE kchat; USE kchat;

CREATE TABLE `chat` (
  id binary(16) NOT NULL PRIMARY KEY,
  id_text varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `date_created` DATETIME DEFAULT NOW(),
  `name` varchar(255) NOT NULL,
  `type` int(11) DEFAULT 0,
  `space_id` binary(16) NOT NULL,
  `space_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`space_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `creator_id` binary(16) NOT NULL,
  `creator_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`creator_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `chat_user` (
  `chat_id` binary(16) NOT NULL,
  `chat_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`chat_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `user_id` binary(16) NOT NULL,
  `user_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`user_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `contextual_privilege` int(11) NOT NULL DEFAULT 10
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `message` (
  `id` binary(16) NOT NULL PRIMARY KEY,
  `id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `date_created` DATETIME DEFAULT NOW(),
  `last_edited` DATETIME DEFAULT NOW() ON UPDATE NOW(),
  `text` varchar(255) DEFAULT NULL,
  `author_id` binary(16) NOT NULL,
  `author_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`author_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `chat_id` binary(16) NOT NULL,
  `chat_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`chat_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` binary(16) NOT NULL PRIMARY KEY,
  `id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `name` varchar(255) NOT NULL UNIQUE,
  `date_created` DATETIME DEFAULT NOW(),
  `api_key` varchar(255) NOT NULL,
  `role` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `invite` (
  `token` varchar(255) NOT NULL PRIMARY KEY,
  `date_created` DATETIME DEFAULT NOW(),
  `space_id` binary(16) NOT NULL,
  `space_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`space_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `creator_id` binary(16) NOT NULL,
  `creator_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`creator_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `memo` varchar(255) DEFAULT NULL,
  `expires` DATETIME DEFAULT NULL,
  `recipient_id` binary(16) DEFAULT NULL,
  `recipient_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`recipient_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `date_redeemed` DATETIME DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `space` (
  `id` binary(16) NOT NULL PRIMARY KEY,
  `id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `name` varchar(255) NOT NULL,
  `date_created` DATETIME DEFAULT NOW()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `space_user` (
  `space_id` binary(16) NOT NULL,
  `space_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`space_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `user_id` binary(16) NOT NULL,
  `user_id_text` varchar(36) generated always as
  (insert(
      insert(
          insert(
              insert(hex(`user_id`),9,0,'-'),
              14,0,'-'),
          19,0,'-'),
      24,0,'-')
  ) virtual,
  `user_alias` varchar(255) NOT NULL,
  `contextual_privilege` int(11) NOT NULL DEFAULT 10,
  PRIMARY KEY (`user_id`, `space_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `chat`
ADD CONSTRAINT `UK_5NUHPu0cZV0e8sL` UNIQUE (`name`,`space_id`),
ADD CONSTRAINT `FK_RWmjFwBpGn86W1p` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`),
ADD CONSTRAINT `FK_M9zE3mecFxq1hxK` FOREIGN KEY (`space_id`) REFERENCES `space` (`id`);

ALTER TABLE `chat_user`
ADD CONSTRAINT `FK_ia7rMnDE3JP9cHs` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
ADD CONSTRAINT `FK_noljBcORVgUuN0p` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`);

ALTER TABLE `message`
ADD CONSTRAINT `FK_7PNYAW7PxplN82b` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`),
ADD CONSTRAINT `FK_bUgtZ1c9YjFJDOZ` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`);

ALTER TABLE `invite`
ADD CONSTRAINT `FK_zYVuj3m6sJv0BTP` FOREIGN KEY (`space_id`) REFERENCES `space` (`id`),
ADD CONSTRAINT `FK_5zz2sYBCC7bffec` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`);

ALTER TABLE `space_user`
ADD CONSTRAINT `UK_Yrf8C6fomC9YlgZ` UNIQUE (`user_id`,`space_id`),
ADD CONSTRAINT `UK_BsdoTh5WckBCFZJ` UNIQUE (`user_alias`,`space_id`),
ADD CONSTRAINT `FK_VgrTiOEockHTBKU` FOREIGN KEY (`space_id`) REFERENCES `space` (`id`),
ADD CONSTRAINT `FK_DhRtLCowyFUWqb6` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

CREATE INDEX `IDX_1PjBcet7DZ2KZm` ON `user` (`api_key`);