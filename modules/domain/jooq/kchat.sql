DROP DATABASE IF EXISTS kchat; CREATE DATABASE kchat; USE kchat;

CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  `date_created` DATETIME DEFAULT NOW(),
  `access_key` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `creator_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `chat_user` (
  `chat_id` bigint(20) NOT NULL,
  `participants_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `date_created` DATETIME DEFAULT NOW(),
  `last_edited` DATETIME DEFAULT NOW() ON UPDATE NOW(),
  `text` varchar(255) DEFAULT NULL,
  `timestamp` bigint(20) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  `chat_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  `date_created` DATETIME DEFAULT NOW(),
  `api_key` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `role` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `chat`
ADD CONSTRAINT `FKkM9zE3mecFxq1hxK` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`);

ALTER TABLE `chat_user`
ADD CONSTRAINT `FKtia7rMnDE3JP9cHs` FOREIGN KEY (`participants_id`) REFERENCES `user` (`id`),
ADD CONSTRAINT `FKWnoljBcORVgUuN0p` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`);

ALTER TABLE `message`
ADD CONSTRAINT `FKp7PNYAW7PxplN82b` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`),
ADD CONSTRAINT `FKJbUgtZ1c9YjFJDOZ` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`);