create schema if not exists comic collate utf8mb4_0900_ai_ci;
use comic;
-- --------------------------
-- Table structure for comic
-- --------------------------
create table if not exists comic(
    id         int auto_increment primary key,
    title      varchar(20)                  null comment '漫画名',
    full_title varchar(50)                  null,
    author     varchar(20) default 'No one' null comment '作者',
    unique (title)
);

-- --------------------------
-- Table structure for chapter
-- --------------------------
create table if not exists chapter(
    id       int auto_increment primary key,
    comic_id int                        not null,
    title    varchar(50)                null comment '章节名',
    pages    int         default 0      null comment '卷数',
    suffix   varchar(10) default '.jpg' null comment '图片后缀',
    foreign key (comic_id) references comic (id) on delete cascade
);

-- --------------------------
-- Table structure for user
-- --------------------------
create table if not exists user(
    id          int auto_increment primary key,
    username    varchar(64)                         not null comment '用户名',
    password    varchar(64)                         not null comment '密码',
    email       varchar(100)                        null comment '邮箱',
    nick_name   varchar(200)                        null comment '昵称',
    icon        varchar(500)                        null comment '头像',
    note        varchar(500)                        null comment '备注信息',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    login_time  datetime                            null comment '登陆时间',
    status      int(1)    default 1                 null comment '账号启用状态:0->禁用，1->启用',
    unique (email),
    unique (username)
);
insert into user(username, password, email, nick_name) values
                ('luffy', '$2a$10$rMhxctqzDUaKyoOglIVqR.P/QKPTQ4uPhZQpZ.fVFMYuPdy8q2aF6', 'god@mail.com', 'monkey'),
                ('admin', '$2a$10$E5Exa0M3MRlEBp/bsd5WQOBDHavD7KaQ4xb6oTXdcbPEEjwjwogsW', 'youremail@mail.com', 'admin');

-- --------------------------
-- Table structure for user_comic_relation
-- --------------------------
create table if not exists user_comic_relation(
    id          int auto_increment primary key,
    user_id     int                                 null,
    comic_id    int                                 null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '订阅时间',
    foreign key (comic_id) references comic (id) on delete cascade,
    foreign key (user_id) references user (id) on delete cascade
);

-- --------------------------
-- Table structure for permission
-- --------------------------
create table if not exists permission(
    id          int auto_increment primary key,
    pid         int          null comment '父级权限id',
    name        varchar(100) null comment '名称',
    value       varchar(200) null comment '权限值',
    icon        varchar(500) null comment '图标',
    type        int(1)       null comment '权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）',
    uri         varchar(200) null comment '前端资源路径',
    status      int(1)       null comment '启用状态；0->禁用；1->启用',
    sort        int          null comment '排序',
    create_time datetime     null comment '创建时间'
);
insert into permission values (1, 0, '账号列表', 'comic:user:list' , null , 0, null , 1, 0, now()),
                              (2, 1, '创建账号', 'comic:user:create' , null , 1, null , 1, 0, now()),
                              (3, 1, '删除账号', 'comic:user:delete' , null , 2, null , 1, 0, now()),
                              (4, 1, '编辑账号', 'comic:user:update' , null , 2, null , 1, 0, now()),
                              (5, 0, '漫画阅读', 'comic:comic:read' , null , 0, null , 1, 0, now()),
                              (6, 0, '添加个人漫画', 'comic:comic:create' , null , 2, null , 1, 0, now()),
                              (7, 6, '删除个人漫画', 'comic:comic:delete' , null , 2, null , 1, 0, now()),
                              (8, 6, '编辑个人漫画', 'comic:comic:update' , null , 2, null , 1, 0, now()),
                              (9, 1, '删除章节', 'comic:chapter:delete' , null , 2, null , 1, 0, now()),
                              (10, 0, '本地漫画管理', 'comic:local:manage' , null , 2, null , 1, 0, now()),
                              (11, 0, '漫画收藏', 'comic:comic:favourite' , null , 0, null , 1, 0, now()),
                              (12, 11, '发表评论', 'comic:comment:comment' , null , 2, null , 1, 0, now()),
                              (13, 11, '删除评论', 'comic:comment:delete' , null , 2, null , 1, 0, now()),
                              (14, 1, '特殊处理评论', 'comic:comment:others' , null , 2, null , 1, 0, now()),
                              (15, 1, '漫画编辑', 'comic:admin:update' , null , 2, null , 1, 0, now()),
                              (16, 11,'账号配置', 'comic:account:update', null, 2, null, 1, 0, now());

-- --------------------------
-- Table structure for role
-- --------------------------
create table if not exists role(
    id          int auto_increment primary key,
    name        varchar(100)                        null comment '名称',
    description varchar(500)                        null comment '描述',
    user_count  int                                 null comment '后台用户数量',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    status      int(1)    default 1                 null comment '启用状态: 0->禁用, 1->启用',
    sort        int       default 0                 null
);
insert into role(name, description, user_count) values('ROLE_ADMIN', '管理员', 0),
                                                       ('ROLE_LADMIN', '本地管理员', 0),
                                                       ('ROLE_USER', '一般账号', 0),
                                                       ('ROLE_EDITOR', '漫画编辑', 0);

-- --------------------------
-- Table structure for role_permission_relation
-- --------------------------
create table if not exists role_permission_relation(
    id            int auto_increment primary key,
    role_id       int null,
    permission_id int null
);
insert into role_permission_relation(role_id, permission_id) values (1, 1), (1, 2), (1, 3), (1, 4), (1, 7), (1, 14), (1, 16),
                                                                    (2, 10),
                                                                    (3, 5), (3, 11), (3, 12), (3, 15), (3, 16),
                                                                    (4, 6), (4, 8), (4, 9);

-- --------------------------
-- Table structure for record
-- --------------------------
create table if not exists record(
    id          int auto_increment primary key,
    user_id     int                                   not null,
    comic_id    int                                   null,
    chapter_id  int                                   not null,
    page        varchar(10) default '001'             null comment '页数',
    suffix      varchar(10) default '.jpg'            null,
    last_update timestamp   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    foreign key (comic_id) references comic (id),
    foreign key (chapter_id) references chapter (id) on delete cascade,
    foreign key (user_id) references user (id)
);

-- --------------------------
-- Table structure for user_role_relation
-- --------------------------
create table if not exists user_role_relation(
    id      int auto_increment primary key,
    user_id int null,
    role_id int null,
    unique (user_id, role_id),
    foreign key (user_id) references user (id) on delete cascade
);
insert into user_role_relation(user_id, role_id) values (1, 1), (1, 2), (1, 3), (1, 4),
                                                        (2, 1), (2, 3), (2, 4);

-- --------------------------
-- Table structure for user_login_log
-- --------------------------
create table if not exists user_login_log(
    id          bigint auto_increment primary key,
    user_id     int                                 null,
    ip          varchar(64)                         null,
    address     varchar(100)                        null,
    user_agent  varchar(200)                        null comment '浏览器登录类型',
    create_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

-- --------------------------
-- Table structure for persistent_logins
-- --------------------------
create table if not exists persistent_logins(
    username  varchar(64) not null,
    series    varchar(64) not null primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);
