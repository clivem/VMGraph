DROP TABLE IF EXISTS `history`;
DROP TABLE IF EXISTS `notes`;
DROP TABLE IF EXISTS `stats`;


--
-- Table structure for table `stats`
--
CREATE TABLE IF NOT EXISTS `stats` (
  `stats_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `millis` bigint(20) NOT NULL,
  `rx_bytes` bigint(20) NOT NULL,
  `tx_bytes` bigint(20) NOT NULL,
  `created` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
--  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`stats_id`),
--  KEY `stats_created_k` (`created`),
--  KEY `stats_updated_k` (`updated`),
  KEY `stats_stats_id_updated_k` (`stats_id`, `updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


--
-- Table structure for table `notes`
--
CREATE TABLE IF NOT EXISTS `notes` (
  `notes_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stats_id` bigint(20) NOT NULL,
  `note` varchar(255) NOT NULL,
  `created` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
--  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY (`notes_id`),
  KEY `notes_stats_id_k` (`stats_id`),
--  KEY `notes_created_k` (`created`),
--  KEY `notes_updated_k` (`updated`),
  KEY `notes_notes_id_updated_k` (`notes_id`, `updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


ALTER TABLE `notes`
  ADD CONSTRAINT `notes_stats_id_fk` FOREIGN KEY (`stats_id`) REFERENCES `stats` (`stats_id`)
  ON DELETE CASCADE ON UPDATE CASCADE;
  

--
-- Table structure for table `history`
--
CREATE TABLE IF NOT EXISTS `history` (
  `history_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sports_id` bigint(20) NOT NULL,
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
  `in_play` varchar(255) NOT NULL,
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=29901 ;

