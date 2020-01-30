CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NOT NULL,
  `type` int(11) NOT NULL,
  `commentator` int(11) NOT NULL,
  `gmt_create` bigint(20) NOT NULL DEFAULT '0',
  `gmt_modified` bigint(20) NOT NULL DEFAULT '0',
  `like_count` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
