/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50623
Source Host           : localhost:3306
Source Database       : zt_manage

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2018-05-04 11:42:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for acc_batch_histables
-- ----------------------------
DROP TABLE IF EXISTS `acc_batch_histables`;
CREATE TABLE `acc_batch_histables` (
  `id` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '历史数据库名称',
  `code` varchar(100) DEFAULT NULL COMMENT '历史数据表名称',
  `remark` text COMMENT '历史数据源编号',
  `type` varchar(100) DEFAULT NULL COMMENT '原数据源编号',
  `status` int(5) DEFAULT NULL COMMENT '状态',
  `parent` varchar(100) DEFAULT NULL COMMENT '原数据库名称',
  `parentid` varchar(100) DEFAULT NULL COMMENT '原数据表名称',
  `createdate` varchar(30) DEFAULT NULL COMMENT '创建时间',
  `modifydate` varchar(30) DEFAULT NULL COMMENT '修改时间',
  `userid` varchar(100) DEFAULT NULL COMMENT '创建人',
  `field1` varchar(200) DEFAULT NULL COMMENT '扩展字段1',
  `field2` varchar(200) DEFAULT NULL COMMENT '扩展字段2',
  `field3` varchar(200) DEFAULT NULL COMMENT '扩展字段3',
  `field4` varchar(200) DEFAULT NULL COMMENT '扩展字段4',
  `field5` varchar(200) DEFAULT NULL COMMENT '扩展字段5',
  `field6` varchar(200) DEFAULT NULL COMMENT '扩展字段6',
  `field7` varchar(200) DEFAULT NULL COMMENT '扩展字段7',
  `field8` varchar(200) DEFAULT NULL COMMENT '扩展字段8',
  `field9` varchar(200) DEFAULT NULL COMMENT '扩展字段9',
  `field10` varchar(200) DEFAULT NULL COMMENT '扩展字段10',
  PRIMARY KEY (`id`),
  KEY `name` (`name`,`status`),
  KEY `parentid` (`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of acc_batch_histables
-- ----------------------------

-- ----------------------------
-- Table structure for acc_batch_situation
-- ----------------------------
DROP TABLE IF EXISTS `acc_batch_situation`;
CREATE TABLE `acc_batch_situation` (
  `id` varchar(100) NOT NULL,
  `taskname` varchar(100) NOT NULL COMMENT '任务名称',
  `taskclassname` varchar(180) NOT NULL COMMENT '任务类名',
  `taskpackagename` varchar(100) NOT NULL COMMENT '组件包名',
  `taskno` int(11) NOT NULL COMMENT '任务序号',
  `tasktype` int(11) NOT NULL COMMENT '任务类型',
  `packagelastmodifydate` varchar(30) NOT NULL COMMENT '组件包最后修改时间',
  `prevtaskclassname` varchar(180) DEFAULT NULL COMMENT '前置任务类名',
  `prevtaskno` int(11) DEFAULT NULL COMMENT '前置任务序号',
  `status` int(11) NOT NULL COMMENT '状态',
  `accountdate` varchar(10) NOT NULL COMMENT '任务开始执行时的会计日期',
  `lastmodifydate` varchar(30) NOT NULL COMMENT '任务最后处理时间',
  `createdate` varchar(30) NOT NULL COMMENT '开始执行时间',
  `description` text COMMENT '情况描述',
  `edituserid` varchar(100) DEFAULT NULL COMMENT '手动触发处理人id',
  `authuserid` varchar(100) DEFAULT NULL COMMENT '授权人id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `taskclassname` (`taskclassname`,`accountdate`),
  KEY `taskclassname_2` (`taskclassname`,`lastmodifydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of acc_batch_situation
-- ----------------------------

-- ----------------------------
-- Table structure for acc_cust_monthstatistics
-- ----------------------------
DROP TABLE IF EXISTS `acc_cust_monthstatistics`;
CREATE TABLE `acc_cust_monthstatistics` (
  `id` varchar(100) NOT NULL,
  `custid` varchar(100) NOT NULL COMMENT 'C户客户号',
  `custsubaccountcode` varchar(100) NOT NULL COMMENT '子账户账号',
  `type` varchar(100) NOT NULL COMMENT '子账户类型',
  `prevbalance` decimal(11,2) NOT NULL COMMENT '上月余额',
  `amontexpend` decimal(11,2) NOT NULL COMMENT '支出额',
  `amontrevenue` decimal(11,2) NOT NULL COMMENT '收入额',
  `balance` decimal(11,2) NOT NULL COMMENT '余额',
  `yearmonth` varchar(7) NOT NULL COMMENT '统计年月',
  `createdate` varchar(30) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `custid` (`custid`,`custsubaccountcode`,`type`,`yearmonth`),
  KEY `custid_2` (`custid`,`yearmonth`),
  KEY `custsubaccountcode` (`custsubaccountcode`),
  KEY `yearmonth` (`yearmonth`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of acc_cust_monthstatistics
-- ----------------------------

-- ----------------------------
-- Table structure for acc_cust_yearstatistics
-- ----------------------------
DROP TABLE IF EXISTS `acc_cust_yearstatistics`;
CREATE TABLE `acc_cust_yearstatistics` (
  `id` varchar(100) NOT NULL,
  `custid` varchar(100) NOT NULL COMMENT 'C户客户号',
  `custsubaccountcode` varchar(100) NOT NULL COMMENT '子账户账号',
  `type` varchar(100) NOT NULL COMMENT '子账户类型',
  `prevbalance` decimal(11,2) NOT NULL COMMENT '上年余额',
  `amontexpend` decimal(11,2) NOT NULL COMMENT '支出额',
  `amontrevenue` decimal(11,2) NOT NULL COMMENT '收入额',
  `balance` decimal(11,2) NOT NULL COMMENT '余额',
  `year` varchar(4) NOT NULL COMMENT '统计年份',
  `createdate` varchar(30) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `custid` (`custid`,`custsubaccountcode`,`type`,`year`),
  KEY `custid_2` (`custid`,`custsubaccountcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of acc_cust_yearstatistics
-- ----------------------------

-- ----------------------------
-- Table structure for acc_inner_statistics
-- ----------------------------
DROP TABLE IF EXISTS `acc_inner_statistics`;
CREATE TABLE `acc_inner_statistics` (
  `id` varchar(100) NOT NULL,
  `businessid` varchar(100) NOT NULL COMMENT 'B户客户号',
  `innerid` varchar(100) NOT NULL COMMENT '内部账号',
  `accountitemfirstname` varchar(100) NOT NULL COMMENT '科目名称',
  `accountitemfirstcode` varchar(100) NOT NULL COMMENT '科目编码',
  `accountitemsecdname` varchar(100) NOT NULL COMMENT '科目名称',
  `accountitemsecdcode` varchar(100) NOT NULL COMMENT '科目编码',
  `accountitemthirdname` varchar(100) NOT NULL COMMENT '科目名称',
  `accountitemthirdcode` varchar(100) NOT NULL COMMENT '科目编码',
  `accountsubitemname` varchar(100) NOT NULL COMMENT '科目名称',
  `accountsubitemcode` varchar(100) NOT NULL COMMENT '科目编码',
  `type` int(11) NOT NULL COMMENT '科目类型',
  `prevbalance` decimal(11,2) NOT NULL COMMENT '上个会计日期余额',
  `amontdebit` decimal(11,2) NOT NULL COMMENT '借方发生额',
  `amontcredit` decimal(11,2) NOT NULL COMMENT '贷方发生额',
  `balance` decimal(11,2) NOT NULL COMMENT '账户余额',
  `accountdate` varchar(10) NOT NULL COMMENT '统计的分录会计日期',
  `createdate` varchar(30) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `innerid` (`innerid`,`accountdate`),
  KEY `innerid_2` (`innerid`),
  KEY `accountdate` (`accountdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of acc_inner_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for acc_inner_summary
-- ----------------------------
DROP TABLE IF EXISTS `acc_inner_summary`;
CREATE TABLE `acc_inner_summary` (
  `id` varchar(100) NOT NULL,
  `businessid` varchar(100) NOT NULL COMMENT 'B户客户号',
  `accountitemname` varchar(100) NOT NULL COMMENT '科目名称',
  `accountitemcode` varchar(100) NOT NULL COMMENT '科目编码',
  `parentname` varchar(100) NOT NULL COMMENT '上级科目名称',
  `parentcode` varchar(100) NOT NULL COMMENT '上级科目编码',
  `level` int(11) NOT NULL COMMENT '科目层级',
  `prevbalance` decimal(11,2) NOT NULL COMMENT '上个会计日期余额',
  `balance` decimal(11,2) NOT NULL COMMENT '余额',
  `type` int(11) NOT NULL COMMENT '科目类型',
  `accountdate` varchar(10) NOT NULL COMMENT '汇总的内部账会计日期',
  `createdate` varchar(30) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `accountdate` (`accountdate`),
  KEY `parentcode` (`parentcode`),
  KEY `accountitemcode` (`accountitemcode`),
  KEY `businessid` (`businessid`,`accountdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of acc_inner_summary
-- ----------------------------
SET FOREIGN_KEY_CHECKS=1;
