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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_idx` bigint(20) NOT NULL DEFAULT '0',
  `cst_number` varchar(40) NOT NULL DEFAULT ' ' COMMENT '客户编号',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '客户状态',
  `phone1` varchar(11) NOT NULL DEFAULT '0' COMMENT '送气实际联系电话',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `Idx_number` (`cst_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息表';

-- 正在导出表  bjdb.t_customer 的数据：~0 rows (大约)
DELETE FROM `t_customer`;
/*!40000 ALTER TABLE `t_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_customer` ENABLE KEYS */;

-- 导出  表 bjdb.t_customer_address 结构
CREATE TABLE IF NOT EXISTS `t_customer_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_idx` bigint(20) NOT NULL DEFAULT '0',
  `province` varchar(50) NOT NULL DEFAULT '0',
  `city` varchar(50) NOT NULL DEFAULT '0',
  `county` varchar(50) NOT NULL DEFAULT '0',
  `detail` varchar(50) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户地址';

-- 正在导出表  bjdb.t_customer_address 的数据：~1 rows (大约)
DELETE FROM `t_customer_address`;
/*!40000 ALTER TABLE `t_customer_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_customer_address` ENABLE KEYS */;

-- 导出  表 bjdb.t_customer_character 结构
CREATE TABLE IF NOT EXISTS `t_customer_character` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '客户性质编码',
  `name` varchar(20) NOT NULL COMMENT '客户性质',
  `note` varchar(20) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_character` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='客户性质表';

-- 正在导出表  bjdb.t_customer_character 的数据：~0 rows (大约)
DELETE FROM `t_customer_character`;
/*!40000 ALTER TABLE `t_customer_character` DISABLE KEYS */;
INSERT INTO `t_customer_character` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, 'cha_001', '一级客户', '一级客户', '2017-10-12 15:39:20', '2017-10-12 15:39:22'),
	(2, 'cha_002', '二级客户', '二级客户', '2017-10-30 14:10:06', '2017-10-30 14:10:08');
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
  `code` varchar(20) NOT NULL COMMENT '客户来源编码',
  `name` varchar(20) NOT NULL COMMENT '客户来源',
  `note` varchar(20) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='客户来源';

-- 正在导出表  bjdb.t_customer_source 的数据：~0 rows (大约)
DELETE FROM `t_customer_source`;
/*!40000 ALTER TABLE `t_customer_source` DISABLE KEYS */;
INSERT INTO `t_customer_source` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, 'cst_src_001', '零售部宣传', '零售部宣传', '2017-10-12 15:40:30', '2017-10-12 15:40:31'),
	(2, 'cst_src_002', '其他', '其他', '2017-10-30 14:11:11', '2017-10-30 14:11:11');
/*!40000 ALTER TABLE `t_customer_source` ENABLE KEYS */;

-- 导出  表 bjdb.t_customer_type 结构
CREATE TABLE IF NOT EXISTS `t_customer_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '客户类型编码',
  `name` varchar(20) NOT NULL COMMENT '客户类型名称',
  `note` varchar(40) NOT NULL COMMENT '客户类型备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '客户类型创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '客户类型修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_type` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='客户类型';

-- 正在导出表  bjdb.t_customer_type 的数据：~2 rows (大约)
DELETE FROM `t_customer_type`;
/*!40000 ALTER TABLE `t_customer_type` DISABLE KEYS */;
INSERT INTO `t_customer_type` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, 'cst_type_001', '一般客户', '一般客户', '2017-10-25 14:17:23', '2017-10-25 14:17:23'),
	(2, 'cst_type_002', '餐饮客户', '餐饮客户', '2017-10-30 14:13:48', '2017-10-30 14:13:48');
/*!40000 ALTER TABLE `t_customer_type` ENABLE KEYS */;

-- 导出  表 bjdb.t_district 结构
CREATE TABLE IF NOT EXISTS `t_district` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '客户片区编码',
  `name` varchar(20) NOT NULL COMMENT '客户片区名称',
  `note` varchar(40) NOT NULL COMMENT '客户片区备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '客户片区创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '客户片区修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_type` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='片区';

-- 正在导出表  bjdb.t_district 的数据：~2 rows (大约)
DELETE FROM `t_district`;
/*!40000 ALTER TABLE `t_district` DISABLE KEYS */;
INSERT INTO `t_district` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, 'dis_001', '严家地', '严家地', '2017-10-25 14:17:44', '2017-10-12 15:53:24'),
	(2, 'dis_002', '鼋头渚', '鼋头渚', '2017-10-30 14:13:44', '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `t_district` ENABLE KEYS */;

-- 导出  表 bjdb.t_gas_store 结构
CREATE TABLE IF NOT EXISTS `t_gas_store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '门店编码',
  `name` varchar(20) NOT NULL COMMENT '门店名称',
  `note` varchar(40) NOT NULL COMMENT '门店备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '门店创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '门店修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_type` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='门店';

-- 正在导出表  bjdb.t_gas_store 的数据：~2 rows (大约)
DELETE FROM `t_gas_store`;
/*!40000 ALTER TABLE `t_gas_store` DISABLE KEYS */;
INSERT INTO `t_gas_store` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, 'gas_store_001', '严家地门店', '严家地门店', '2017-10-12 15:57:56', '2017-10-30 14:12:53'),
	(2, 'gas_store_002', '鼋头渚门店', '鼋头渚门店', '2017-10-30 14:13:20', '2017-10-30 14:13:41');
/*!40000 ALTER TABLE `t_gas_store` ENABLE KEYS */;

-- 导出  表 bjdb.t_group 结构
CREATE TABLE IF NOT EXISTS `t_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `note` varchar(64) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 正在导出表  bjdb.t_group 的数据：~3 rows (大约)
DELETE FROM `t_group`;
/*!40000 ALTER TABLE `t_group` DISABLE KEYS */;
INSERT INTO `t_group` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, 'grc_0001', '管理员', '系统管理员', '2017-11-03 14:34:42', '2017-11-04 10:53:41'),
	(2, 'grc_0002', '客服人员', '客服人员', '2017-11-04 10:54:06', '2017-11-04 10:54:10'),
	(3, 'grc_0003', '配送员', '配送员', '2017-11-04 10:54:27', '2017-11-04 10:54:29'),
	(4, 'grc_0004', '客户', '客户', '2017-11-11 15:07:33', '2017-11-11 15:07:33');
/*!40000 ALTER TABLE `t_group` ENABLE KEYS */;

-- 导出  表 bjdb.t_sysuser 结构
CREATE TABLE IF NOT EXISTS `t_sysuser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_idx` bigint(20) NOT NULL,
  `job_number` varchar(64) NOT NULL,
  `mobile_phone` varchar(16) DEFAULT NULL,
  `office_phone` varchar(16) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- 正在导出表  bjdb.t_sysuser 的数据：~0 rows (大约)
DELETE FROM `t_sysuser`;
/*!40000 ALTER TABLE `t_sysuser` DISABLE KEYS */;
INSERT INTO `t_sysuser` (`id`, `user_idx`, `job_number`, `mobile_phone`, `office_phone`, `email`) VALUES
	(10, 62, '444', '42', '4', '4');
/*!40000 ALTER TABLE `t_sysuser` ENABLE KEYS */;

-- 导出  表 bjdb.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT ' ' COMMENT '客户名称',
  `password` varchar(20) NOT NULL DEFAULT ' ',
  `group_idx` bigint(20) NOT NULL DEFAULT '0',
  `note` varchar(40) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开户时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '资料修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='客户信息表';

-- 正在导出表  bjdb.t_user 的数据：~1 rows (大约)
DELETE FROM `t_user`;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `user_id`, `name`, `password`, `group_idx`, `note`, `create_time`, `update_time`) VALUES
	(62, '444', '42', '42', 1, '4', '2017-11-11 19:13:21', '2017-11-11 19:21:44');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
