create table Basket (
       basket_id bigint not null auto_increment,
        createdDate datetime(6),
        modifiedDate datetime(6),
        count integer not null,
        item_id bigint,
        user_id bigint,
        primary key (basket_id)
    ) engine=InnoDB

create table Comment (
       comment_id bigint not null auto_increment,
        createdDate datetime(6),
        modifiedDate datetime(6),
        contents varchar(255) not null,
        depth integer,
        parentId bigint,
        viewStatus varchar(255) not null,
        item_id bigint,
        user_id bigint,
        primary key (comment_id)
    ) engine=InnoDB

create table Item (
       item_id bigint not null auto_increment,
        createdDate datetime(6),
        modifiedDate datetime(6),
        bookAuthor varchar(255) not null,
        bookTitle varchar(255) not null,
        category varchar(255) not null,
        commentCount bigint,
        contents TEXT not null,
        likeCount bigint,
        name varchar(255) not null,
        price integer not null,
        saleStatus varchar(255),
        stock integer not null,
        viewCount bigint,
        user_id bigint,
        primary key (item_id)
    ) engine=InnoDB

create table ItemFile (
       item_file_id bigint not null auto_increment,
        fileName varchar(255) not null,
        filePath varchar(255) not null,
        item_id bigint,
        primary key (item_file_id)
    ) engine=InnoDB

create table LikeItem (
       like_item_id bigint not null auto_increment,
        createdDate datetime(6),
        modifiedDate datetime(6),
        item_id bigint,
        user_id bigint,
        primary key (like_item_id)
    ) engine=InnoDB

create table OrderItem (
       order_item_id bigint not null auto_increment,
        createdDate datetime(6),
        modifiedDate datetime(6),
        count integer not null,
        orderPrice integer not null,
        item_id bigint,
        order_id bigint,
        primary key (order_item_id)
    ) engine=InnoDB

create table Orders (
       order_id bigint not null auto_increment,
        createdDate datetime(6),
        modifiedDate datetime(6),
        defaultAddress varchar(255),
        detailAddress varchar(255),
        extraAddress varchar(255),
        postcode varchar(255),
        orderDate datetime(6) not null,
        orderStatus varchar(255),
        payment varchar(255) not null,
        phone varchar(11) not null,
        recipient varchar(255) not null,
        user_id bigint,
        primary key (order_id)
    ) engine=InnoDB

create table User (
       user_id bigint not null auto_increment,
        createdDate datetime(6),
        modifiedDate datetime(6),
        email varchar(255) not null,
        nickname varchar(255) not null,
        password varchar(255) not null,
        provider varchar(255),
        role varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB

CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);

CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);

CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BLOB NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

drop table Basket;
drop table Comment;
drop table Item;
drop table ItemFile;
drop table LikeItem;
drop table OrderItem;
drop table Orders;
drop table User;

show tables;

select * from Item;
