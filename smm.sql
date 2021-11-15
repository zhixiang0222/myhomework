/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.5.28 : Database - smm
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`smm` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `smm`;

/*Table structure for table `efunction` */

DROP TABLE IF EXISTS `efunction`;

CREATE TABLE `efunction` (
  `fid` int(11) NOT NULL AUTO_INCREMENT,
  `fcode` varchar(50) DEFAULT NULL,
  `fname` varchar(50) DEFAULT NULL,
  `furl` varchar(50) DEFAULT NULL,
  `flevel` int(11) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  `remark1` varchar(50) DEFAULT NULL,
  `remark2` varchar(50) DEFAULT NULL,
  `remark3` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `efunction` */

insert  into `efunction`(`fid`,`fcode`,`fname`,`furl`,`flevel`,`parentid`,`remark1`,`remark2`,`remark3`) values (1,'001','系统功能','www',1,0,'NO','一级功能',NULL),(2,'002','消息管理','www',1,0,'NO','一级功能',NULL),(3,'004','人员管理','/employeeList.html',2,1,'YES','系统功能',NULL),(4,'003','角色管理','/roleList.html',2,1,'YES','系统功能',NULL),(5,'005','权限管理','/functionList.html',2,1,'YES','系统功能',NULL),(6,'006','发件箱','/sendMessageList.html',2,2,'YES','消息管理',NULL),(7,'007','发送消息','/sendNormalMsg.html',3,2,'YES','消息管理',NULL),(8,'008','推送消息','https://www.baidu.com',2,2,'YES','消息管理',NULL);

/*Table structure for table `employee` */

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `eid` int(11) NOT NULL AUTO_INCREMENT,
  `ename` varchar(50) DEFAULT NULL,
  `esex` int(11) DEFAULT NULL,
  `age` varchar(50) DEFAULT NULL,
  `phonenumber` varchar(50) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `jobnumber` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `remark1` varchar(50) DEFAULT NULL,
  `remark2` varchar(50) DEFAULT NULL,
  `remark3` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`eid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `employee` */

insert  into `employee`(`eid`,`ename`,`esex`,`age`,`phonenumber`,`hire_date`,`jobnumber`,`password`,`remark1`,`remark2`,`remark3`) values (5,'张经理',0,'24','13473940328','2021-02-26','111','f544d24e19ac50ac335715586ff2411b','8ead0765c5acc2213cf6bba818119acf',NULL,NULL),(6,'李秘书',1,'18','1234353532','2021-03-05','123','2e301cae8bb7daae67e99615e2d25b96','05342220aeb1432636d570535e5d9cd7',NULL,NULL),(7,'超级管理员',0,'99','9999999','2021-03-21','999','2c0063151f13207d859d21f880d25f41','86e2726979fef350dda067ec81c48f06',NULL,NULL),(8,'门卫保安大爷',0,'30','119110120','2021-02-26','666','0ac5ee1f6d8c5220e54c8b090446bc80','2043b1b2e84589376f24ea7dcd90adf2',NULL,NULL);

/*Table structure for table `employee_role` */

DROP TABLE IF EXISTS `employee_role`;

CREATE TABLE `employee_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eid` int(11) DEFAULT NULL,
  `rid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

/*Data for the table `employee_role` */

insert  into `employee_role`(`id`,`eid`,`rid`) values (50,7,3),(51,5,1),(52,6,2),(53,6,4),(54,8,6);

/*Table structure for table `erole` */

DROP TABLE IF EXISTS `erole`;

CREATE TABLE `erole` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `rcode` varchar(50) DEFAULT NULL,
  `rname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `erole` */

insert  into `erole`(`rid`,`rcode`,`rname`) values (1,'001','项目经理'),(2,'002','财务'),(3,'003','超级管理员'),(4,'004','hr'),(5,'005','员工'),(6,'006','门卫');

/*Table structure for table `role_function` */

DROP TABLE IF EXISTS `role_function`;

CREATE TABLE `role_function` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` int(11) DEFAULT NULL,
  `fid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8;

/*Data for the table `role_function` */

insert  into `role_function`(`id`,`rid`,`fid`) values (68,5,2),(69,5,6),(70,5,7),(71,5,8),(98,2,1),(99,2,3),(100,2,4),(101,2,2),(102,2,6),(103,2,7),(104,2,8),(105,4,1),(106,4,3),(107,4,4),(108,4,2),(109,4,6),(110,4,7),(111,4,8),(112,1,1),(113,1,3),(114,1,2),(115,1,6),(116,1,7),(117,1,8),(118,6,1),(119,6,3),(120,6,2),(121,6,6),(122,6,8),(123,3,1),(124,3,3),(125,3,4),(126,3,5),(129,3,2),(130,3,6),(131,3,7),(132,3,8);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
