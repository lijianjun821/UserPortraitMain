# UserPortraitMain
用户画像

1.基础信息画像分析
    基础数据来自业务表，主要有两张表的信息
	
	1.用户表：用户ID、用户名、密码、性别、手机号、邮箱、年龄、注册时间、收货地址、终端类型
	2.用户详情补充表：学历、收入、职业、婚姻、是否有小孩、是否有车有房、使用手机品牌、用户id
	CREATE TABLE `user_info` (
	  `user_id` int(20) DEFAULT NULL COMMENT '用户ID',
	  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
	  `password` varchar(50) DEFAULT NULL COMMENT '密码',
	  `sex` int(1) DEFAULT NULL COMMENT '性别',
	  `tel_phone` varchar(50) DEFAULT NULL COMMENT '手机号',
	  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
	  `age` int(20) DEFAULT NULL COMMENT '年龄',
	  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间',
	  `user_type` int(1) DEFAULT NULL COMMENT '终端类型：0、pc端；1、移动端；2、小程序端'
	) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户表';
	
	CREATE TABLE `user_detail` (
	  `user_detail_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '详情id',
	  `user_id` int(20) DEFAULT NULL COMMENT '用户ID',
	  `edu` int(1) DEFAULT NULL COMMENT '学历',
	  `profession` varchar(20) DEFAULT NULL COMMENT '职业',
	  `marriage` int(1) DEFAULT NULL COMMENT '婚姻状态：1、未婚；2、已婚；3、离异；4、未知',
	  `has_child` int(1) DEFAULT NULL COMMENT '是否有小孩：1、没有；2、有；3、未知',
	  `has_car` int(1) DEFAULT NULL COMMENT '是否有车：1、有；2、没有；3、未知',
	  `has_hourse` int(1) DEFAULT NULL COMMENT '是否有房：1、有；2、没有；3、未知',
	  `tel_phone_brand` varchar(50) DEFAULT NULL COMMENT '手机品牌',
	  PRIMARY KEY (`user_detail_id`)
	) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户详情表';
	
	
2.项目框架搭建

首先创建主项目：UserPortraitMain
分析模块：userAnalyServer

给main方法传参  --input hdfs://192.168.2.100:9000/test.txt

hadoop环境搭建：
zookeeper搭建：
hbase环境搭建：

启动集群：
启动zk：zkServer.sh start
启动hbase：start-hbase.sh  hbase访问路径：http://192.168.2.100:60010
启动MongoDB：mongod --dbpath=data（注释：data为之前创建好的目录） 验证MongoDB：另开一个窗口，输入 mongo 进入命令行，输入show dbs; 


3.年代标签代码编写：
	年代：40年代 50年代 60年代 70年代 80年代 90年代 00后 10后
	统计每个年代群里的数量，做到近实时统计，每半小时会进行一次任务统计
	

4.手机运营商标签代码编写：

5.邮件运营商标签代码编写：

6.还原真实消费信息表结构定义
	1.用户订单表
	2.商品表
	3.商品类别表
CREATE TABLE `order_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `product_id` int(20) DEFAULT NULL COMMENT '商品ID',
  `product_type_id` int(20) DEFAULT NULL COMMENT '商品类型ID',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `amount` double(20,2) DEFAULT NULL COMMENT '支付金额',
  `pay_type` int(2) DEFAULT NULL COMMENT '支付方式',
  `pay_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '支付时间',
  `pay_status` int(2) DEFAULT NULL COMMENT '支付状态',
  `coupon_amount` double(20,2) DEFAULT NULL COMMENT '优惠金额',
  `toatl_amount` double(20,2) DEFAULT NULL COMMENT '总金额',
  `refund_amount` double(20,2) DEFAULT NULL COMMENT '退款',
  `num` int(20) DEFAULT NULL COMMENT '商品数量',
  `user_id` int(20) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='订单表';
CREATE TABLE `product_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_type_id` int(20) DEFAULT NULL COMMENT '商品类别ID',
  `product_name` varchar(50) DEFAULT NULL COMMENT '商品名',
  `product_description` varchar(1500) DEFAULT NULL COMMENT '商品描述',
  `price` double(20,2) DEFAULT NULL COMMENT '价格',
  `num` int(20) DEFAULT NULL COMMENT '库存',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `mechart_id` int(20) DEFAULT NULL COMMENT '商家ID',
  `product_url` varchar(20) DEFAULT NULL COMMENT '商品图片路径',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='商品表';
CREATE TABLE `product_type_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品类别表',
  `product_type_name` varchar(50) DEFAULT NULL COMMENT '商品类别名称',
  `product_type_description` varchar(200) DEFAULT NULL COMMENT '商品类别描述',
  `product_type_leave` int(11) DEFAULT NULL COMMENT '商品类别等级',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='商品类别表';


7.败家指数：
	败家指数 = 支付金额平均值*0.3、最大支付金额*0.3、下单频率*0.4
