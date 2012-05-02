--<ScriptOptions statementTerminator=";"/>

CREATE TABLE IF NOT EXISTS `stats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `millis` bigint(20) NOT NULL,
  `rxbytes` bigint(20) NOT NULL,
  `txbytes` bigint(20) NOT NULL,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `created` (`created`),
  KEY `updated` (`updated`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8;
