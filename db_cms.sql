/*
Navicat MySQL Data Transfer

Source Server         : HRM_server
Source Server Version : 50720
Source Host           : 192.168.4.60:3306
Source Database       : db_cms

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-12-11 17:47:38
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `cms_channel`
-- ----------------------------
DROP TABLE IF EXISTS `cms_channel`;
CREATE TABLE `cms_channel` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `channel_id` varchar(30) NOT NULL DEFAULT '',
  `name` varchar(40) NOT NULL DEFAULT '',
  `note` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_channel
-- ----------------------------
INSERT INTO `cms_channel` VALUES ('1', 'home', '首页', '官网首页中文版');
INSERT INTO `cms_channel` VALUES ('7', 'news', '新闻中心', '新闻页，二级页面');
INSERT INTO `cms_channel` VALUES ('8', 'product', '产品系列', '产品页，二级页面');
INSERT INTO `cms_channel` VALUES ('9', 'aboutus', '关于我们', '关于我们，二级页面');
INSERT INTO `cms_channel` VALUES ('10', 'joinus', '加入我们', '加入我们，二级页面');
INSERT INTO `cms_channel` VALUES ('11', 'solution', '解决方案', '解决方案页，二级页面');
INSERT INTO `cms_channel` VALUES ('12', 'service', '安全服务', '安全服务页，二级页面');

-- ----------------------------
-- Table structure for `cms_content`
-- ----------------------------
DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE `cms_content` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `channel_id` varchar(20) NOT NULL DEFAULT '',
  `position_id` varchar(60) NOT NULL DEFAULT '',
  `name` varchar(40) NOT NULL DEFAULT '',
  `size` varchar(20) NOT NULL DEFAULT '',
  `type` varchar(20) NOT NULL DEFAULT '',
  `policy` varchar(20) NOT NULL DEFAULT '',
  `state` varchar(20) NOT NULL DEFAULT '',
  `dtime` datetime NOT NULL DEFAULT '0001-01-01 00:00:00',
  `url` varchar(200) NOT NULL DEFAULT '',
  `content` varchar(2000) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_content
-- ----------------------------
INSERT INTO `cms_content` VALUES ('8', 'home', 'top_banner_pic1', '顶部Banner1', '1920*626', '图片', '静态', '已发布', '2018-11-01 14:39:00', 'product', 'top_banner_pic1.jpg');
INSERT INTO `cms_content` VALUES ('10', 'home', 'top_banner_pic2', '顶部banner2', '1920*626', '图片', '静态', '已发布', '2018-10-29 11:48:00', '', 'top_banner_pic2.jpg');
INSERT INTO `cms_content` VALUES ('11', 'home', 'top_banner_pic3', '顶部Banner3', '1920*626', '图片', '静态', '已发布', '2018-10-29 11:48:00', '', 'top_banner_pic3.jpg');
INSERT INTO `cms_content` VALUES ('12', 'home', 'top_banner_pic4', '顶部Banner4', '1920*626', '图片', '静态', '已发布', '2018-11-01 18:04:00', 'http://www.baidu.com', 'top_banner_pic4.jpg');
INSERT INTO `cms_content` VALUES ('13', 'home', 'middle_banner_pic', '中部Banner', '1920*327', '图片', '静态', '已发布', '2018-10-29 11:48:00', '', 'middle_banner_pic.jpg');
INSERT INTO `cms_content` VALUES ('14', 'news', 'news_top_banner_pic', '顶部Banner', '1920*526', '图片', '静态', '已发布', '2018-10-29 12:00:00', '', 'news_top_banner_pic.jpg');
INSERT INTO `cms_content` VALUES ('15', 'joinus', 'joinus_top_banner_pic', '顶部banner', '1920*626', '图片', '静态', '已发布', '2018-10-29 17:53:00', '', 'joinus_top_banner_pic.jpg');
INSERT INTO `cms_content` VALUES ('16', 'aboutus', 'aboutus_top_banner_pic', '顶部Banner', '1920*626', '图片', '静态', '已发布', '2018-10-29 17:53:00', '', 'aboutus_top_banner_pic.jpg');
INSERT INTO `cms_content` VALUES ('17', 'solution', 'solution_top_banner_pic', '顶部Banner', '1920*700', '图片', '静态', '已发布', '2018-10-29 17:53:00', '', 'solution_top_banner_pic.jpg');
INSERT INTO `cms_content` VALUES ('18', 'product', 'product_top_banner_pic', '顶部Banner', '1920*626', '图片', '静态', '已发布', '2018-10-29 17:52:00', '', 'product_top_banner_pic.jpg');
INSERT INTO `cms_content` VALUES ('20', 'service', 'service_top_banner_pic', '顶部Banner', '1920*626', '图片', '静态', '已发布', '2018-10-29 18:32:00', '', 'service_top_banner_pic.jpg');
INSERT INTO `cms_content` VALUES ('21', 'news', 'news_main_title', '主图片标题', '*', '文本', '静态', '已发布', '2018-11-01 17:26:00', '', '2018年第十届密码学与信息安全教学研讨会');

-- ----------------------------
-- Table structure for `cms_news`
-- ----------------------------
DROP TABLE IF EXISTS `cms_news`;
CREATE TABLE `cms_news` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `news_id` varchar(15) NOT NULL,
  `dtime` datetime NOT NULL DEFAULT '0001-01-01 00:00:00',
  `state` varchar(20) NOT NULL DEFAULT '待发布',
  `title` varchar(200) NOT NULL DEFAULT '',
  `locate` varchar(20) NOT NULL DEFAULT '北京',
  `summary` varchar(500) NOT NULL DEFAULT '',
  `content` varchar(2000) NOT NULL DEFAULT '',
  `level` varchar(20) NOT NULL DEFAULT '1',
  `type` varchar(20) NOT NULL DEFAULT '',
  `pic1` varchar(40) NOT NULL DEFAULT '',
  `pic2` varchar(40) NOT NULL DEFAULT '',
  `pic3` varchar(40) NOT NULL DEFAULT '',
  `pic4` varchar(40) NOT NULL DEFAULT '',
  `pic5` varchar(40) NOT NULL DEFAULT '',
  `pic6` varchar(40) NOT NULL DEFAULT '',
  `pic7` varchar(40) NOT NULL DEFAULT '',
  `pic8` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_news
-- ----------------------------
INSERT INTO `cms_news` VALUES ('5', '201810251123', '2018-10-31 09:44:00', '已发布', '安码科技独家协办第十届全国密码学与信息安全教学研讨会', '北京', '2018年8月23-25日由中国密码学会教育与科普工作委员会主办、北京印刷学院承办、北京安码科技有限公司（以下简称“安码科技”）独家协办的“201８年第十届全国密码学与信息安全教学研讨会”在北京隆重召开。', '<div class=\"content3\">2018年8月23-25日由中国密码学会教育与科普工作委员会主办、北京印刷学院承办、北京安码科技有限公司（以下简称“安码科技”）独家协办的“201８年第十届全国密码学与信息安全教学研讨会”在北京隆重召开。会议邀请了国内从事网络空间安全领域教学科研工作的专家学者就我国密码学和信息安全专业的教育体系建设、教学改革、专业建设、学科建设、产学研结合等问题展开深入交流，促进密码学、网络安全与教育的融合创新，推动教育信息化2.0、网络安全3.0的实践。中国密码学会副理事长、北京邮电大学信息安全中心主任、安码科技创始人杨义先教授，国家密码管理局商用密码管理办公室副主任王永传，中国密码学会副秘书长陈灏，中国密码学会教育与科普工作委员会副主任委员李子臣教授，北京印刷学院副校长田忠利，清华大学罗平教授，上海交通大学谷大武教授、陈恭亮教授，南开大学贾春福教授，哈尔滨工程大学马春光教授，北京邮电大学信息安全系主任彭海朋教授，安码科技创始人兼首席科学家、灾备技术国家工程实验室副主任、云安全技术北京市工程实验室主任辛阳博士、安码科技战略合作总监朱洪亮博士以及全国高校密码学、信息安全领域的专家大咖受邀请出席会议。</div>\n<div class=\"newspic\"><img src=\"1\" width=\"76%\"></div>\n<div class=\"content4\">参会人员合影</div>', '1', '安码动态', '201810251123_1.jpg', '201810251123_2.jpg', '', '', '', '', '', '');
INSERT INTO `cms_news` VALUES ('6', '201810291505', '2018-10-31 09:44:00', '已发布', '安码科技与南京理工大学携手打造网络空间安全联合实验室 开启合作共建新篇章', '南京', '2018年10月17日，安码科技与南京理工大学网络空间安全联合实验室揭牌仪式暨合作交流会在南京隆重举行。南京理工大学计算机科学与工程学院副院长张功萱，计算机网络与通信技术系主任俞研，计算机科学与技术系主任王永利...', '<div class=\"content3\">2018年10月17日，安码科技与南京理工大学网络空间安全联合实验室揭牌仪式暨合作交流会在南京隆重举行。南京理工大学计算机科学与工程学院副院长张功萱，计算机网络与通信技术系主任俞研，计算机科学与技术系主任王永利，安码科技创始人兼首席科学家辛阳博士，安码科技云安全实验室负责人韩挺博士，南京地区销售经理郑涌，以及客户经理刘琳琳等出席了揭牌仪式。</div>\n<div class=\"newspic\"><img src=\"1\" width=\"76%\"></div>\n<div class=\"content4\">图1：揭牌仪式现场</div>\n<div class=\"content3\">张功萱院长首先发表讲话，表示此次能与安码科技开展更深层次的合作，将继续强化双方在技术和科研能力方面的优势，南京理工大学愿同安码科技一道，在联合实验室建设、学科建设、科研创新、人才培养、联合研发等方面开展广泛合作，树立校企合作的标杆和典范，实现高校学科建设和企业技术提升的有序发展，共创良性合作双赢局面。</div>\n<div class=\"newspic\"><img src=\"2\" width=\"76%\"></div>\n<div class=\"content4\">图2：合作交流会现场</div>\n<div class=\"content3\">随后，辛阳博士也表示很高兴能和南京理工大学共同打造网络空间安全联合实验室。安码科技将积极发挥在教育培训、科研创新和工程服务等方面的优势，深度参与联合实验室建设、学科建设、师资培养和人才培养。在实验室建设方面，安码科技已经为学院提供基础教学实验环境建设，并将持续在网络安全技术服务和相关解决方案等方面提供支持，保障实验室教学和科研能力。同时，在教育和人才培养方面，联合实验室也将作为南京理工大学创新实践基地，为网络安全相关专业师生提供平台支持。此外，在科研创新、联合申报等方面，双方将实现资源共享、全面合作，共同打造成开放式实验室和技术研发中心，联合开展学术交流、学术互访等活动。</div>\n<div class=\"newspic\"><img src=\"3\" width=\"76%\"></div>\n<div class=\"content4\">图3：双方探讨联合实验室运行机制</div>\n<div class=\"content3\">揭牌仪式后，双方举行座谈会，就网络空间安全学科共建、人才培养等议题展开深入讨论。双方均表示此次的的揭牌仪式只是个开端，双方坚信，网络空间安全联合实验室将在双方的共同建设下取得圆满成功和累累硕果！</div>\n<div class=\"newspic\"><img src=\"4\" width=\"76%\"></div>\n<div class=\"content4\">图4：双方交流建设规划</div>\n<div class=\"content3\">此次网络空间安全联合实验室揭牌仪式暨合作交流会的成功举行，标志着双方的合作又迈出了坚实的一步。双方将持续围绕联合实验室建设、学科建设、人才培养、科研合作等方面建立互访机制，加强双方合作，增进双方友谊，共同为网络空间安全建设贡献力量。</div>', '1', '安码动态', '201810291505_1.jpg', '201810291505_2.jpg', '201810291505_3.jpg', '201810291505_4.jpg', '', '', '', '');
INSERT INTO `cms_news` VALUES ('7', '201810291601', '2018-10-30 17:01:00', '已发布', '安全盛宴：《安全通论》师资培训与网络空间安全学科前沿讲座', '贵阳', '2018年7月16日，第一届《安全通论》师资培训与网络空间安全学科前沿讲座在爽爽的贵阳隆重启幕。本次讲座由公共大数据国家重点实验室、灾备技术国家工程实验室联合主办...', '<div class=\"content3\">2018年7月16日，第一届《安全通论》师资培训与网络空间安全学科前沿讲座在爽爽的贵阳隆重启幕。本次讲座由公共大数据国家重点实验室、灾备技术国家工程实验室联合主办，北京安码科技有限公司（以下简称“安码科技”）协办，北京大学、电子科技大学、四川大学、西安电子科技大学、信息工程大学等全国近100所高校网络空间安全、信息安全相关专业老师及湖南省公安厅、贵州省公安厅、贵州省经开区、湖南省党委等20余家行业企事业单位负责人共计200余人参加了本次讲座，贵州大学副校长李军旗，《安全通论》、《安全简史》作者、公共大数据国家重点实验室主任、安码科技创始人杨义先教授，贵州省公共大数据重点实验室常务副主任彭长根，安码科技总经理、灾备技术国家工程实验室副主任辛阳博士受邀参加了本次讲座开幕式。</div>\n<div class=\"newspic\"><img src=\"1\" width=\"76%\"></div>\n<div class=\"content4\">（图-杨义先教授在培训讲座中授课）</div>\n<div class=\"content3\">开幕式由贵州省公共大数据重点实验室常务副主任彭长根教授主持，贵州大学副校长李军旗做开幕致辞。李校长首先对安全届各位专家、各位老师、博士硕士研究生的莅临表示欢迎，然后重点介绍了公共大数据国家重点实验室的筹备建设的背景以及贵州大学与杨义先教授合作的渊源，详细介绍杨义先教授在贵大担任国家重点实验室主任期间所作出的《安全简史》、《安全通论》、《黑客心理学》、《黑客管理学》等重要研究成果，弥补了网络空间安全一级学科在基础理论研究及关键研究领域的空白；李校长也重点介绍了贵州大学的情况以及在引进人才方面的优厚政策，希望更多的专家学者能够加入共同推动国家重点实验室及学科建设。最后，李校长对安码科技在本次培训讲座活动中给予的支持表示感谢！</div>\n<div class=\"newspic\"><img src=\"2\" width=\"76%\"></div>\n<div class=\"content4\">（图-贵州大学副校长李军旗做开幕致辞）</div>\n<div class=\"content3\">开幕式后，《安全通论》培训讲座由杨义先教授正式开讲，杨教授首先从安全通论研究的初衷、产生的系列成果以及在学术界引起的广泛关注讲起，提到希望通过建立安全通论这一安全领域的基础理论，一方面解决网络空间安全一级学科没有统一基础理论的尴尬局面，另一方面抛砖引玉，通过搭建学术交流平台使更多的学者、老师、研究生来挖掘基础理论研究这一金矿。杨教授以信通领域的香农信息论、计算机领域的冯诺依曼架构、自动化领域的维纳控制论为类比，从安全再认识到红客、黑客、安全概念的建立再到安全熵、安全攻防极限、安全态势纳什均衡等理论的论证提供了安全观念的全新视角。</div>\n<div class=\"newspic\"><img src=\"3\" width=\"76%\"></div>\n<div class=\"content4\">（图-参会老师观看安码科技产品演示）</div>\n<div class=\"content3\">此次研讨会为期两周，旨在健全国家“网络空间安全一级学科”课程培养体系，提高安全专业高层次人才的综合素质，及时展现安全领域的国际研究前沿，帮助青年教师和硕博研究生们探索前沿研究课题，以建设合理的、专业的、符合当前网络安全形势的专业教育体系为目的，着力推动网络空间安全一级学科基础理论的建设与实践。会后，参会老师对安码科技在具体落实安全通论实践方面的工作给予了肯定，纷纷到展台参观安码科技在网络空间安全实验室建设方面的成果，并积极交流学科建设、实验室建设、靶场建设方面的经验。</div>\n<div class=\"content3\">安码科技也秉承贯彻教育部（教高司函〔2018〕4号）文件精神，深化产教融合协同育人，以网络安全产业和技术发展的最新需求推动高校网络安全人才培养与学科建设，为国内网络空间安全行业的发展贡献一份力量。安码科技也衷心祝愿这次培训及讲座圆满成功，祝愿各位学员老师在本次培训期间：收获满满、不虚此行。</div>\n<div class=\"newspic\"><img src=\"4\" width=\"76%\"></div>\n<div class=\"content4\">（图-参会人员合影）</div>', '1', '安码动态', '201810291601_1.jpg', '201810291601_2.jpg', '201810291601_3.jpg', '201810291601_4.jpg', '', '', '', '');
INSERT INTO `cms_news` VALUES ('8', '201811051542', '2018-11-01 00:00:00', '已发布', '11111', '好好', '1111', '示例：\n<div class=\"content3\">正文段落</div>\n<div class=\"newspic\"><img src=\"1(图片顺序号)\" width=\"76%\"></div>\n<div class=\"content4\">图片标题</div>', '1', '行业动态', '201811051542_1.jpg', '', '', '', '', '', '', '');

-- ----------------------------
-- Table structure for `cms_position`
-- ----------------------------
DROP TABLE IF EXISTS `cms_position`;
CREATE TABLE `cms_position` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `channel_id` varchar(20) NOT NULL DEFAULT '',
  `position_id` varchar(60) NOT NULL DEFAULT '',
  `name` varchar(40) NOT NULL DEFAULT '',
  `size_w` varchar(6) NOT NULL DEFAULT '',
  `size_h` varchar(6) NOT NULL,
  `type` varchar(20) NOT NULL DEFAULT '图片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_position
-- ----------------------------
INSERT INTO `cms_position` VALUES ('9', 'home', 'top_banner_pic1', '顶部Banner1', '1920', '626', '图片');
INSERT INTO `cms_position` VALUES ('11', 'home', 'top_banner_pic2', '顶部banner2', '1920', '626', '图片');
INSERT INTO `cms_position` VALUES ('12', 'home', 'top_banner_pic3', '顶部Banner3', '1920', '626', '图片');
INSERT INTO `cms_position` VALUES ('13', 'home', 'top_banner_pic4', '顶部Banner4', '1920', '626', '图片');
INSERT INTO `cms_position` VALUES ('14', 'home', 'middle_banner_pic', '中部Banner', '1920', '327', '图片');
INSERT INTO `cms_position` VALUES ('15', 'news', 'news_top_banner_pic', '顶部Banner', '1920', '526', '图片');
INSERT INTO `cms_position` VALUES ('16', 'joinus', 'joinus_top_banner_pic', '顶部banner', '1920', '626', '图片');
INSERT INTO `cms_position` VALUES ('17', 'aboutus', 'aboutus_top_banner_pic', '顶部Banner', '1920', '626', '图片');
INSERT INTO `cms_position` VALUES ('18', 'solution', 'solution_top_banner_pic', '顶部Banner', '1920', '700', '图片');
INSERT INTO `cms_position` VALUES ('19', 'product', 'product_top_banner_pic', '顶部Banner', '1920', '626', '图片');
INSERT INTO `cms_position` VALUES ('21', 'service', 'service_top_banner_pic', '顶部Banner', '1920', '626', '图片');
INSERT INTO `cms_position` VALUES ('22', 'news', 'news_main_title', '主图片标题', '', '', '文本');

-- ----------------------------
-- Table structure for `sys_usrdb`
-- ----------------------------
DROP TABLE IF EXISTS `sys_usrdb`;
CREATE TABLE `sys_usrdb` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `usrname` varchar(40) DEFAULT NULL,
  `passwd` varchar(40) DEFAULT NULL,
  `fullname` varchar(40) DEFAULT NULL,
  `dept` varchar(60) DEFAULT '',
  `role` varchar(30) DEFAULT NULL,
  `email` varchar(40) DEFAULT '',
  `mobile` varchar(20) DEFAULT '',
  `type` varchar(20) DEFAULT '本地用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_usrdb
-- ----------------------------
INSERT INTO `sys_usrdb` VALUES ('1', 'admin', 'dcg', '系统管理员', '', '管理员', 'lihao@safe-code.com', '', '本地用户');
