#创建山寨QQ数据库
#create database QQdb;

#使用该数据库
use QQdb;

#创建用户表

create table QQUser
(	QQUserId char(20) primary key,
	QQPassword char(16) not null,
    QQFriend varchar(50),
    sticker varchar(100)
);


#插入测试用例
insert into QQUser values('1','123456',"1",null);
insert into QQUser values('2','123456',"3,4",null);
insert into QQUser values('3','123456',"2",null);
insert into QQUser values('4','123',"2",null);
insert into QQUser values('5','123',null,null);
insert into QQUser values('6','123456',"1",null);
