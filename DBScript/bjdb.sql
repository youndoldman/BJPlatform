-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.18-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 bjdb 的数据库结构
CREATE DATABASE IF NOT EXISTS `bjdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bjdb`;

-- 导出  表 bjdb.t_customer 结构
CREATE TABLE IF NOT EXISTS `t_customer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  --`cst_seq` varchar(20) NOT NULL DEFAULT '0' COMMENT '客户流水号',
  `cst_number` varchar(20) NOT NULL DEFAULT '0' COMMENT '客户编号',
  `name` varchar(40) NOT NULL DEFAULT ' ' COMMENT '客户名称',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '客户状态',
  `cst_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '客户类型',
  `cst_character` tinyint(4) NOT NULL DEFAULT '0' COMMENT '客户性质',
  --`cst_coporation` tinyint(4) NOT NULL DEFAULT '0' COMMENT '客户所属集团',
  `district` tinyint(4) NOT NULL DEFAULT '0' COMMENT '所属片区',
  `gas_store` tinyint(4) NOT NULL DEFAULT '0' COMMENT '所属门店',
  `contacts` varchar(20) NOT NULL DEFAULT '0' COMMENT '送气实际联系人',
  `cat_phone` varchar(11) NOT NULL DEFAULT '0' COMMENT '送气实际联系电话',
  `phone_1` varchar(11) NOT NULL DEFAULT '0' COMMENT '客户电话1',
  `phone_2` varchar(11) NOT NULL DEFAULT '0' COMMENT '客户电话2',
  `phone_3` varchar(11) NOT NULL DEFAULT '0' COMMENT '客户电话3',
  `addr` varchar(50) NOT NULL DEFAULT '0' COMMENT '客户地址',
  `floor` smallint(6) NOT NULL DEFAULT '0' COMMENT '客户楼层',
  `source` tinyint(4) NOT NULL DEFAULT '0' COMMENT '客户来源',
  `note` varchar(50) NOT NULL DEFAULT '0' COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开户时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '资料修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息表';

-- 正在导出表  bjdb.t_customer 的数据：~0 rows (大约)
DELETE FROM `t_customer`;
/*!40000 ALTER TABLE `t_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_customer` ENABLE KEYS */;

-- 导出  表 bjdb.t_customer_character 结构
CREATE TABLE IF NOT EXISTS `t_customer_character` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL COMMENT '客户性质编码',
  `name` varchar(20) NOT NULL COMMENT '客户性质',
  `note` varchar(20) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_character` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='客户性质表';

-- 正在导出表  bjdb.t_customer_character 的数据：~0 rows (大约)
DELETE FROM `t_customer_character`;
/*!40000 ALTER TABLE `t_customer_character` DISABLE KEYS */;
INSERT INTO `t_customer_character` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, '1', '一般客户', '一般客户', '2017-10-12 15:39:20', '2017-10-12 15:39:22');
/*!40000 ALTER TABLE `t_customer_character` ENABLE KEYS */;

-- 导出  表 bjdb.t_customer_coporation 结构
CREATE TABLE IF NOT EXISTS `t_customer_coporation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL COMMENT '客户集团编码',
  `name` varchar(20) NOT NULL COMMENT '客户集团',
  `note` varchar(20) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='客户集团';

-- 正在导出表  bjdb.t_customer_coporation 的数据：~0 rows (大约)
DELETE FROM `t_customer_coporation`;
/*!40000 ALTER TABLE `t_customer_coporation` DISABLE KEYS */;
INSERT INTO `t_customer_coporation` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, '1', 'aaa', 'aaa', '2017-10-12 15:42:03', '2017-10-12 15:42:04');
/*!40000 ALTER TABLE `t_customer_coporation` ENABLE KEYS */;

-- 导出  表 bjdb.t_customer_source 结构
CREATE TABLE IF NOT EXISTS `t_customer_source` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL COMMENT '客户来源编码',
  `name` varchar(20) NOT NULL COMMENT '客户来源',
  `note` varchar(20) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='客户来源';

-- 正在导出表  bjdb.t_customer_source 的数据：~0 rows (大约)
DELETE FROM `t_customer_source`;
/*!40000 ALTER TABLE `t_customer_source` DISABLE KEYS */;
INSERT INTO `t_customer_source` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, '1', '报纸广告', '报纸广告', '2017-10-12 15:40:30', '2017-10-12 15:40:31');
/*!40000 ALTER TABLE `t_customer_source` ENABLE KEYS */;

-- 导出  表 bjdb.t_customer_type 结构
CREATE TABLE IF NOT EXISTS `t_customer_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL COMMENT '客户类型编码',
  `name` varchar(20) NOT NULL COMMENT '客户类型名称',
  `note` varchar(40) NOT NULL COMMENT '客户类型备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '客户类型创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '客户类型修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_type` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='客户类型';

-- 正在导出表  bjdb.t_customer_type 的数据：~1 rows (大约)
DELETE FROM `t_customer_type`;
/*!40000 ALTER TABLE `t_customer_type` DISABLE KEYS */;
INSERT INTO `t_customer_type` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(2, '1', '一般客户', '一般客户', '2017-10-12 15:56:46', '2017-10-12 15:56:47');
/*!40000 ALTER TABLE `t_customer_type` ENABLE KEYS */;

-- 导出  表 bjdb.t_district 结构
CREATE TABLE IF NOT EXISTS `t_district` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL COMMENT '客户片区编码',
  `name` varchar(20) NOT NULL COMMENT '客户片区名称',
  `note` varchar(40) NOT NULL COMMENT '客户片区备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '客户片区创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '客户片区修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_type` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='片区';

-- 正在导出表  bjdb.t_district 的数据：~0 rows (大约)
DELETE FROM `t_district`;
/*!40000 ALTER TABLE `t_district` DISABLE KEYS */;
INSERT INTO `t_district` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(2, '1', '严家地', '严家地', '2017-10-12 15:53:50', '2017-10-12 15:53:24');
/*!40000 ALTER TABLE `t_district` ENABLE KEYS */;

-- 导出  表 bjdb.t_gas_store 结构
CREATE TABLE IF NOT EXISTS `t_gas_store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL COMMENT '门店编码',
  `name` varchar(20) NOT NULL COMMENT '门店名称',
  `note` varchar(40) NOT NULL COMMENT '门店备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '门店创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '门店修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_type` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='门店';

-- 正在导出表  bjdb.t_gas_store 的数据：~0 rows (大约)
DELETE FROM `t_gas_store`;
/*!40000 ALTER TABLE `t_gas_store` DISABLE KEYS */;
INSERT INTO `t_gas_store` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(2, '1', '一般客户', '一般客户', '2017-10-12 15:57:56', '2017-10-12 15:57:57');
/*!40000 ALTER TABLE `t_gas_store` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
