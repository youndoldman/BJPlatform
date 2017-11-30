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

-- 导出  表 bjdb.t_customer 结构
CREATE TABLE IF NOT EXISTS `t_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_idx` bigint(20) NOT NULL DEFAULT '0',
  `cst_number` varchar(40) NOT NULL DEFAULT ' ' COMMENT '客户编号',
  `have_cylinder` tinyint(4) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '客户状态',
  `cst_type` bigint(20) NOT NULL DEFAULT '0',
  `cst_source` bigint(20) NOT NULL DEFAULT '0',
  `cst_level` bigint(20) NOT NULL DEFAULT '0',
  `cst_company_name` bigint(20) NOT NULL DEFAULT '0',
  `phone` varchar(11) NOT NULL DEFAULT '0' COMMENT '电话',
  `addr_province` varchar(50) NOT NULL DEFAULT '0',
  `addr_city` varchar(50) NOT NULL DEFAULT '0',
  `addr_county` varchar(50) NOT NULL DEFAULT '0',
  `addr_detail` varchar(50) NOT NULL DEFAULT '0',
  `note` varchar(50) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updata_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `Idx_number` (`cst_number`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='客户信息表';

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_customer_callin 结构
CREATE TABLE IF NOT EXISTS `t_customer_callin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL DEFAULT '0' COMMENT '电话',
  `addr_province` varchar(50) NOT NULL DEFAULT '0',
  `addr_city` varchar(50) NOT NULL DEFAULT '0',
  `addr_county` varchar(50) NOT NULL DEFAULT '0',
  `addr_detail` varchar(50) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updata_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='客户信息表';

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_customer_company 结构
CREATE TABLE IF NOT EXISTS `t_customer_company` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '客户性质编码',
  `name` varchar(20) NOT NULL COMMENT '客户性质',
  `note` varchar(20) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_character` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='客户性质表';

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_customer_level 结构
CREATE TABLE IF NOT EXISTS `t_customer_level` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '客户性质编码',
  `name` varchar(20) NOT NULL COMMENT '客户性质',
  `note` varchar(20) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Idx_character` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='客户级别表';

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_customer_source 结构
CREATE TABLE IF NOT EXISTS `t_customer_source` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '客户来源编码',
  `name` varchar(20) NOT NULL COMMENT '客户来源',
  `note` varchar(20) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='客户来源';

-- 数据导出被取消选择。
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='客户类型';

-- 数据导出被取消选择。
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

-- 数据导出被取消选择。
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

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_goods 结构
CREATE TABLE IF NOT EXISTS `t_goods` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type_idx` bigint(20) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `price` float NOT NULL,
  `info` varchar(50) DEFAULT NULL,
  `note` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_goods_type 结构
CREATE TABLE IF NOT EXISTS `t_goods_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `note` varchar(64) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_group 结构
CREATE TABLE IF NOT EXISTS `t_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `note` varchar(64) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_order 结构
CREATE TABLE IF NOT EXISTS `t_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(50) NOT NULL COMMENT '订单编号 ',
  `customer_idx` bigint(20) NOT NULL COMMENT '订单客户ID',
  `amount` float NOT NULL COMMENT '金额总计',
  `pay_type` smallint(6) NOT NULL DEFAULT '0' COMMENT '支付方式',
  `access_type` smallint(6) NOT NULL DEFAULT '0',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '订单状态',
  `recv_address` varchar(50) NOT NULL DEFAULT '0' COMMENT '收货地址',
  `recv_name` varchar(50) NOT NULL DEFAULT '0' COMMENT '收货人',
  `recv_phone` varchar(50) NOT NULL DEFAULT '0' COMMENT '收货电话',
  `comment` varchar(50) NOT NULL DEFAULT '0' COMMENT '订单附言',
  `note` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单';

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_order_detail 结构
CREATE TABLE IF NOT EXISTS `t_order_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_idx` bigint(20) NOT NULL COMMENT '订单编号 idx',
  `goods_idx` bigint(20) NOT NULL COMMENT '商品ID',
  `deal_price` float NOT NULL COMMENT '成交价格',
  `original_price` float NOT NULL COMMENT '原价',
  `quantity` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  `subtotal` float NOT NULL DEFAULT '0' COMMENT '金额小计',
  `note` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单详情';

-- 数据导出被取消选择。
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

-- 数据导出被取消选择。
-- 导出  表 bjdb.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT ' ' COMMENT '客户名称',
  `identity` varchar(40) NOT NULL DEFAULT ' ',
  `password` varchar(20) NOT NULL DEFAULT ' ',
  `group_idx` bigint(20) NOT NULL DEFAULT '0',
  `note` varchar(40) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开户时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '资料修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='客户信息表';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
