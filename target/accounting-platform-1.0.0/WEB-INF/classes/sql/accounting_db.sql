/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80406
 Source Host           : localhost:3306
 Source Schema         : accounting_db

 Target Server Type    : MySQL
 Target Server Version : 80406
 File Encoding         : 65001

 Date: 25/11/2025 21:11:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for accounting_subject
-- ----------------------------
DROP TABLE IF EXISTS `accounting_subject`;
CREATE TABLE `accounting_subject`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '科目ID',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科目编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科目名称',
  `type` tinyint NOT NULL COMMENT '科目类型(1:资产,2:负债,3:共同,4:所有者权益,5:成本,6:损益)',
  `balance_direction` tinyint NULL DEFAULT 1 COMMENT '余额方向(1:借方,2:贷方)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '会计科目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of accounting_subject
-- ----------------------------
INSERT INTO `accounting_subject` VALUES (1, '1001', '库存现金', 1, 1, 1);
INSERT INTO `accounting_subject` VALUES (2, '1002', '银行存款', 1, 1, 1);
INSERT INTO `accounting_subject` VALUES (3, '1012', '其他货币资金', 1, 1, 1);
INSERT INTO `accounting_subject` VALUES (4, '1101', '交易性金融资产', 1, 1, 1);
INSERT INTO `accounting_subject` VALUES (5, '1121', '应收票据', 1, 1, 1);
INSERT INTO `accounting_subject` VALUES (6, '1122', '应收账款', 1, 1, 1);
INSERT INTO `accounting_subject` VALUES (7, '1123', '预付账款', 1, 1, 1);
INSERT INTO `accounting_subject` VALUES (8, '1131', '应收股利', 1, 1, 1);
INSERT INTO `accounting_subject` VALUES (9, '1132', '应收利息', 1, 2, 1);
INSERT INTO `accounting_subject` VALUES (119, '1', '11', 2, 1, 1);

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` int NULL DEFAULT 1 COMMENT '状态',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '123456', '管理员', '13800138000', 'admin@example.com', 1, 1764064049);

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `customer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户编码',
  `customer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_company_customer_code`(`customer_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '客户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, 'KH000001', '测试客户名称', 1);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `employee_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '员工编码',
  `employee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '员工姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0:离职,1:在职)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_company_employee_code`(`employee_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '员工表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, 'EMP1001', '测试员工', '18322222222', 1);

-- ----------------------------
-- Table structure for employee_expense
-- ----------------------------
DROP TABLE IF EXISTS `employee_expense`;
CREATE TABLE `employee_expense`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '费用ID',
  `expense_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '费用单号',
  `employee_id` bigint NOT NULL COMMENT '员工ID',
  `amount` decimal(18, 4) NOT NULL COMMENT '金额',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0:待审批,1:已批准,2:已报销)',
  `expense_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_company_expense_no`(`expense_no`) USING BTREE,
  INDEX `employee_id`(`employee_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '员工费用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee_expense
-- ----------------------------
INSERT INTO `employee_expense` VALUES (1, '11', 1, 999.0000, '测试员工费用', 1, '2025-11-25 20:45:23');

-- ----------------------------
-- Table structure for enterprise
-- ----------------------------
DROP TABLE IF EXISTS `enterprise`;
CREATE TABLE `enterprise`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业名称',
  `scale` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业规模',
  `registration_capital` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册资本',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业地址',
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enterprise
-- ----------------------------
INSERT INTO `enterprise` VALUES (1, '测试企业名称', 'small', '111', '222', '测试', '18322222222', '2025-11-25 17:57:14', '2025-11-25 17:57:14');

-- ----------------------------
-- Table structure for purchase_order
-- ----------------------------
DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE `purchase_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购单ID',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '采购单号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `order_date` date NOT NULL COMMENT '订单日期',
  `total_amount` decimal(18, 4) NOT NULL COMMENT '总金额',
  `paid_amount` decimal(18, 4) NULL DEFAULT 0.0000 COMMENT '已付金额',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0:草稿,1:已确认,2:已付款,3:已完成)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_company_order_no`(`order_number`) USING BTREE,
  INDEX `supplier_id`(`supplier_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '采购单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of purchase_order
-- ----------------------------
INSERT INTO `purchase_order` VALUES (1, 'PO1764071341128', 1, '2025-11-25', 999.0000, 0.0000, 0, '测试采购订单');

-- ----------------------------
-- Table structure for sales_order
-- ----------------------------
DROP TABLE IF EXISTS `sales_order`;
CREATE TABLE `sales_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售单ID',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '销售单号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `order_date` date NOT NULL COMMENT '订单日期',
  `total_amount` decimal(18, 4) NOT NULL COMMENT '总金额',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0:草稿,1:已确认,2:已收款,3:已完成)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_company_order_no`(`order_number`) USING BTREE,
  INDEX `customer_id`(`customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '销售单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sales_order
-- ----------------------------
INSERT INTO `sales_order` VALUES (1, '1', 1, '2025-11-25', 1111.0000, 1, '测试销售单');

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商编码',
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商名称',
  `status` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '供应商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES (1, 'A110', '测试供应商', 1);

-- ----------------------------
-- Table structure for journal_entry
-- ----------------------------
DROP TABLE IF EXISTS `journal_entry`;
CREATE TABLE `journal_entry`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '凭证ID',
  `voucher_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '凭证编号',
  `voucher_date` date NOT NULL COMMENT '凭证日期',
  `summary` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '摘要',
  `total_debit` decimal(18, 4) NOT NULL COMMENT '借方总额',
  `total_credit` decimal(18, 4) NOT NULL COMMENT '贷方总额',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0:未过账,1:已过账,2:已取消)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_voucher_no`(`voucher_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '记账凭证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for journal_entry_detail
-- ----------------------------
DROP TABLE IF EXISTS `journal_entry_detail`;
CREATE TABLE `journal_entry_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `entry_id` bigint NOT NULL COMMENT '凭证ID',
  `subject_id` bigint NOT NULL COMMENT '科目ID',
  `debit_amount` decimal(18, 4) NOT NULL DEFAULT 0.0000 COMMENT '借方金额',
  `credit_amount` decimal(18, 4) NOT NULL DEFAULT 0.0000 COMMENT '贷方金额',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_entry_id`(`entry_id`) USING BTREE,
  INDEX `idx_subject_id`(`subject_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '记账凭证明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for posting
-- ----------------------------
DROP TABLE IF EXISTS `posting`;
CREATE TABLE `posting`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '过账ID',
  `entry_id` bigint NOT NULL COMMENT '凭证ID',
  `posting_date` datetime NOT NULL COMMENT '过账日期',
  `posting_user_id` bigint NOT NULL COMMENT '过账人ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_entry_id`(`entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '过账记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reconciliation
-- ----------------------------
DROP TABLE IF EXISTS `reconciliation`;
CREATE TABLE `reconciliation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '对账ID',
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `reconciliation_date` date NOT NULL COMMENT '对账日期',
  `start_balance` decimal(18, 4) NOT NULL COMMENT '期初余额',
  `end_balance` decimal(18, 4) NOT NULL COMMENT '期末余额',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0:未完成,1:已完成)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '对账记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tax_handling
-- ----------------------------
DROP TABLE IF EXISTS `tax_handling`;
CREATE TABLE `tax_handling`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '税务处理ID',
  `tax_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '税种',
  `tax_period` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '纳税期间',
  `tax_amount` decimal(18, 4) NOT NULL COMMENT '税额',
  `payment_date` date NULL DEFAULT NULL COMMENT '缴款日期',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0:未申报,1:已申报,2:已缴款)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '税务处理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for regular_business
-- ----------------------------
DROP TABLE IF EXISTS `regular_business`;
CREATE TABLE `regular_business`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '定期业务ID',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业务类型',
  `business_date` date NOT NULL COMMENT '业务日期',
  `amount` decimal(18, 4) NOT NULL COMMENT '金额',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0:未处理,1:已处理)',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定期业务处理表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
