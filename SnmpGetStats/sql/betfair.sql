DROP TABLE IF EXISTS `history_note`;
DROP TABLE IF EXISTS `history`;
DROP TABLE IF EXISTS `sport`;

--
-- Table structure for table `sport`
--
CREATE TABLE IF NOT EXISTS `sport` (
  `sport_id` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `created` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  `updated` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`sport_id`)
--  KEY `sport_created_k` (`created`),
--  KEY `sport_updated_k` (`updated`),
--  KEY `sport_notes_id_updated_k` (`notes_id`, `updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

INSERT INTO sport VALUES(1, "Soccer", 1, now(), now());
INSERT INTO sport VALUES(2, "Tennis", 1, now(), now());
INSERT INTO sport VALUES(3, "Golf", 1, now(), now());
INSERT INTO sport VALUES(4, "Cricket", 1, now(), now());
INSERT INTO sport VALUES(5, "Rugby Union", 1, now(), now());
INSERT INTO sport VALUES(6, "Boxing", 1, now(), now());
INSERT INTO sport VALUES(7, "Horse Racing", 1, now(), now());
INSERT INTO sport VALUES(8, "Motor Sport", 1, now(), now());
INSERT INTO sport VALUES(10, "Special Bets", 1, now(), now());
INSERT INTO sport VALUES(11, "Cycling", 1, now(), now());
INSERT INTO sport VALUES(12, "Rowing", 1, now(), now());
INSERT INTO sport VALUES(13, "Horse Racing - Todays Card", 1, now(), now());
INSERT INTO sport VALUES(14, "Soccer - Fixtures", 1, now(), now());
INSERT INTO sport VALUES(15, "Greyhound - Todays Card", 1, now(), now());
INSERT INTO sport VALUES(1477, "Rugby League", 1, now(), now());
INSERT INTO sport VALUES(3503, "Darts", 1, now(), now());
INSERT INTO sport VALUES(3988, "Athletics", 1, now(), now());
INSERT INTO sport VALUES(4339, "Greyhound Racing", 1, now(), now());
INSERT INTO sport VALUES(6231, "Financial Bets", 1, now(), now());
INSERT INTO sport VALUES(6422, "Snooker", 1, now(), now());
INSERT INTO sport VALUES(6423, "American Football", 1, now(), now());
INSERT INTO sport VALUES(7511, "Baseball", 1, now(), now());
INSERT INTO sport VALUES(7522, "Basketball", 1, now(), now());
INSERT INTO sport VALUES(7523, "Hockey", 1, now(), now());
INSERT INTO sport VALUES(7524, "Ice Hockey", 1, now(), now());
INSERT INTO sport VALUES(7525, "Sumo Wrestling", 1, now(), now());
INSERT INTO sport VALUES(61420, "Australian Rules", 1, now(), now());
INSERT INTO sport VALUES(66598, "Gaelic Football", 1, now(), now());
INSERT INTO sport VALUES(66599, "Hurling", 1, now(), now());
INSERT INTO sport VALUES(72382, "Pool", 1, now(), now());
INSERT INTO sport VALUES(136332, "Chess", 1, now(), now());
INSERT INTO sport VALUES(256284, "Trotting", 1, now(), now());
INSERT INTO sport VALUES(300000, "Commonwealth Games", 1, now(), now());
INSERT INTO sport VALUES(315220, "Poker", 1, now(), now());
INSERT INTO sport VALUES(451485, "Winter Sports", 1, now(), now());
INSERT INTO sport VALUES(468328, "Handball", 1, now(), now());
INSERT INTO sport VALUES(627555, "Badminton", 1, now(), now());
INSERT INTO sport VALUES(678378, "International Rules", 1, now(), now());
INSERT INTO sport VALUES(982477, "Bridge", 1, now(), now());
INSERT INTO sport VALUES(998917, "Volleyball", 1, now(), now());
INSERT INTO sport VALUES(998919, "Bowls", 1, now(), now());
INSERT INTO sport VALUES(998920, "Floorball", 1, now(), now());
INSERT INTO sport VALUES(606611, "Netball", 1, now(), now());
INSERT INTO sport VALUES(998916, "Yachting", 1, now(), now());
INSERT INTO sport VALUES(620576, "Swimming", 1, now(), now());
INSERT INTO sport VALUES(1444073, "Exchange Poker", 1, now(), now());
INSERT INTO sport VALUES(1938544, "Backgammon", 1, now(), now());
INSERT INTO sport VALUES(2030972, "GAA Sports", 1, now(), now());
INSERT INTO sport VALUES(2152880, "Gaelic Games", 1, now(), now());
INSERT INTO sport VALUES(2264869, "International Markets", 1, now(), now());
INSERT INTO sport VALUES(2378961, "Politics", 1, now(), now());


--
-- Table structure for table `history`
--
CREATE TABLE IF NOT EXISTS `history` (
  `history_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sport_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `settled_date` timestamp NULL DEFAULT NULL,
  `full_description` varchar(255) NOT NULL,
  `scheduled_off_date` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  `event` varchar(255) NOT NULL,
  `actual_off_date` timestamp NULL DEFAULT NULL,
  `selection_id` bigint(20) NOT NULL,
  `selection` varchar(255) NOT NULL,
  `odds` double NOT NULL,
  `number_bets` bigint(20) NOT NULL,
  `volume_matched` double NOT NULL,
  `latest_taken` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  `first_taken` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  `win_flag` tinyint(1) NOT NULL,
  `in_play` varchar(2) NOT NULL,
  `created` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  `updated` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`history_id`),
  KEY `history_sport_id_k` (`sport_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

ALTER TABLE `history`
  ADD CONSTRAINT `history_sport_id_fk` FOREIGN KEY (`sport_id`) REFERENCES `sport` (`sport_id`)
  ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Table structure for table `history_note`
--
CREATE TABLE IF NOT EXISTS `history_note` (
  `history_note_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `history_id` bigint(20) NOT NULL,
  `note` varchar(255) NOT NULL,
  `created` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  `updated` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`history_note_id`),
  KEY `history_note_history_id_k` (`history_id`)
--  KEY `history_note_created_k` (`created`),
--  KEY `history_note_updated_k` (`updated`),
--  KEY `history_note_note_id_updated_k` (`history_note_id`, `updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

ALTER TABLE `history_note`
  ADD CONSTRAINT `history_note_history_id_fk` FOREIGN KEY (`history_id`) REFERENCES `history` (`history_id`)
  ON DELETE CASCADE ON UPDATE CASCADE;

  
