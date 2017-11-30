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

-- 正在导出表  bjdb.t_customer 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_customer` ENABLE KEYS */;

-- 正在导出表  bjdb.t_customer_callin 的数据：~11 rows (大约)
/*!40000 ALTER TABLE `t_customer_callin` DISABLE KEYS */;
INSERT INTO `t_customer_callin` (`id`, `phone`, `addr_province`, `addr_city`, `addr_county`, `addr_detail`, `create_time`, `updata_time`) VALUES
	(17, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:54', '2017-11-29 13:57:54'),
	(18, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:54', '2017-11-29 13:57:54'),
	(19, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:55', '2017-11-29 13:57:55'),
	(20, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:55', '2017-11-29 13:57:55'),
	(21, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:55', '2017-11-29 13:57:55'),
	(22, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:55', '2017-11-29 13:57:55'),
	(23, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:55', '2017-11-29 13:57:55'),
	(24, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:55', '2017-11-29 13:57:55'),
	(25, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:56', '2017-11-29 13:57:56'),
	(26, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:56', '2017-11-29 13:57:56'),
	(27, '08978912345', 'jiangsu', 'nanjing', 'qinghuai', 'baixiakejiyuan', '2017-11-29 13:57:56', '2017-11-29 13:57:56');
/*!40000 ALTER TABLE `t_customer_callin` ENABLE KEYS */;

-- 正在导出表  bjdb.t_customer_company 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_customer_company` DISABLE KEYS */;
INSERT INTO `t_customer_company` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, '00000', '未定义', '未定义', '2017-10-12 15:39:20', '2017-10-12 15:39:22'),
	(2, '00001', '千和酒店', '千和酒店', '2017-10-30 14:10:06', '2017-10-30 14:10:08');
/*!40000 ALTER TABLE `t_customer_company` ENABLE KEYS */;

-- 正在导出表  bjdb.t_customer_level 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `t_customer_level` DISABLE KEYS */;
INSERT INTO `t_customer_level` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, '00000', '未定义', '未定义', '2017-10-12 15:39:20', '2017-10-12 15:39:22'),
	(2, '00001', '1级客户', '1级客户', '2017-10-30 14:10:06', '2017-10-30 14:10:08'),
	(7, '00002', '2级客户', '2级客户', '2017-10-30 14:10:06', '2017-10-30 14:10:08');
/*!40000 ALTER TABLE `t_customer_level` ENABLE KEYS */;

-- 正在导出表  bjdb.t_customer_source 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `t_customer_source` DISABLE KEYS */;
INSERT INTO `t_customer_source` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, '00000', '未定义', '未定义', '2017-10-12 15:40:30', '2017-10-12 15:40:31'),
	(2, '00001', '广告', '销售宣传', '2017-10-30 14:11:11', '2017-10-30 14:11:11'),
	(6, '00002', '朋友推荐', '无', '2017-10-30 14:11:11', '2017-10-30 14:11:11');
/*!40000 ALTER TABLE `t_customer_source` ENABLE KEYS */;

-- 正在导出表  bjdb.t_customer_type 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `t_customer_type` DISABLE KEYS */;
INSERT INTO `t_customer_type` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, '00000', '未定义', '未定义', '2017-11-27 14:07:50', '2017-11-27 14:07:50'),
	(2, '00001', '普通住宅客户', '普通住宅客户', '2017-11-27 14:08:16', '2017-11-27 14:08:16'),
	(3, '00002', '餐饮客户', '餐饮客户', '2017-11-30 13:14:28', '2017-11-30 13:14:28');
/*!40000 ALTER TABLE `t_customer_type` ENABLE KEYS */;

-- 正在导出表  bjdb.t_district 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_district` DISABLE KEYS */;
INSERT INTO `t_district` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, 'dis_001', '严家地', '严家地', '2017-10-25 14:17:44', '2017-10-12 15:53:24'),
	(2, 'dis_002', '鼋头渚', '鼋头渚', '2017-10-30 14:13:44', '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `t_district` ENABLE KEYS */;

-- 正在导出表  bjdb.t_gas_store 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_gas_store` DISABLE KEYS */;
INSERT INTO `t_gas_store` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, 'gas_store_001', '严家地门店', '严家地门店', '2017-10-12 15:57:56', '2017-10-30 14:12:53'),
	(2, 'gas_store_002', '鼋头渚门店', '鼋头渚门店', '2017-10-30 14:13:20', '2017-10-30 14:13:41');
/*!40000 ALTER TABLE `t_gas_store` ENABLE KEYS */;

-- 正在导出表  bjdb.t_goods 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `t_goods` DISABLE KEYS */;
INSERT INTO `t_goods` (`id`, `type_idx`, `name`, `price`, `info`, `note`, `create_time`, `update_time`) VALUES
	(4, 13, '桶装水', 13, '333', '33', '2017-11-21 15:14:04', '2017-11-21 15:21:52');
/*!40000 ALTER TABLE `t_goods` ENABLE KEYS */;

-- 正在导出表  bjdb.t_goods_type 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_goods_type` DISABLE KEYS */;
INSERT INTO `t_goods_type` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(12, 'gt_0001', '液化气', '', '2017-11-20 16:39:15', '2017-11-20 16:39:15'),
	(13, 'gt_0002', '桶装水', '', '2017-11-20 17:24:01', '2017-11-20 17:24:01');
/*!40000 ALTER TABLE `t_goods_type` ENABLE KEYS */;

-- 正在导出表  bjdb.t_group 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `t_group` DISABLE KEYS */;
INSERT INTO `t_group` (`id`, `code`, `name`, `note`, `create_time`, `update_time`) VALUES
	(1, '00001', '管理员', '系统管理员', '2017-11-03 14:34:42', '2017-11-27 16:19:29'),
	(2, '00002', '客服人员', '客服人员', '2017-11-04 10:54:06', '2017-11-27 16:19:32'),
	(3, '00003', '配送员', '配送员', '2017-11-04 10:54:27', '2017-11-27 16:19:35'),
	(4, '00004', '客户', '客户', '2017-11-11 15:07:33', '2017-11-27 16:19:38'),
	(5, '00005', '门店店长', '门店店长', '2017-11-30 11:41:36', '2017-11-30 11:41:36');
/*!40000 ALTER TABLE `t_group` ENABLE KEYS */;

-- 正在导出表  bjdb.t_order 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_order` ENABLE KEYS */;

-- 正在导出表  bjdb.t_order_detail 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_order_detail` DISABLE KEYS */;
INSERT INTO `t_order_detail` (`id`, `order_idx`, `goods_idx`, `deal_price`, `original_price`, `quantity`, `subtotal`, `note`, `create_time`, `update_time`) VALUES
	(4, 4, 9, 200, 300, 1, 200, NULL, '2017-11-21 16:12:11', '2017-11-21 16:12:11'),
	(5, 4, 10, 200, 300, 1, 200, NULL, '2017-11-21 16:47:51', '2017-11-21 16:47:51');
/*!40000 ALTER TABLE `t_order_detail` ENABLE KEYS */;

-- 正在导出表  bjdb.t_sysuser 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_sysuser` DISABLE KEYS */;
INSERT INTO `t_sysuser` (`id`, `user_idx`, `job_number`, `mobile_phone`, `office_phone`, `email`) VALUES
	(1, 1, '00001', NULL, NULL, NULL);
/*!40000 ALTER TABLE `t_sysuser` ENABLE KEYS */;

-- 正在导出表  bjdb.t_user 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `user_id`, `name`, `identity`, `password`, `group_idx`, `note`, `create_time`, `update_time`) VALUES
	(1, 'admin', 'admin', ' ', ' 111111', 1, '0', '2017-11-30 13:23:24', '2017-11-30 13:27:17');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
