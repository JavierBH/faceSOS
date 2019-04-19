use faceSOS;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `username` varchar(32) NOT NULL,
  `email` varchar(100) NOT NULL,
  `biography` text,
  `created_at` TIMESTAMP NOT NULL DEFAULT NOW(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
  PRIMARY KEY (`user_id`),
  CONSTRAINT user UNIQUE (`user_id`, `username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;


CREATE TABLE IF NOT EXISTS `chats` (
  `chat_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_user_id` int(10) unsigned NOT NULL,
  `receiver_user_id` int(10) unsigned NOT NULL,
  `content` text NOT NULL,
  `sent_at` TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`chat_id`),
  FOREIGN KEY (`sender_user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`receiver_user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;


CREATE TABLE IF NOT EXISTS `friends` (
  UserID1 INT NOT NULL REFERENCES users(user_id),
     UserID2 INT NOT NULL REFERENCES users(user_id),
     CONSTRAINT CheckOneWay CHECK (UserID1 < UserID2),
     CONSTRAINT PK_Friends_UserID1_UserID2 PRIMARY KEY (UserID1, UserID2),
     CONSTRAINT UQ_Friends_UserID2_UserID1 UNIQUE (UserID2, UserID1)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `posts` (
  `post_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `content` text NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT NOW(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
  PRIMARY KEY (`post_id`),
  KEY `users_posts_FK` (`user_id`),
  CONSTRAINT `users_posts_FK` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;
