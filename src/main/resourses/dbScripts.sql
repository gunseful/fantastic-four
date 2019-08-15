CREATE TABLE PRODUCTS(
    ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255),
    PRICE INT NOT NULL
);      

CREATE CACHED TABLE PUBLIC.USERS(
    ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    NICKNAME VARCHAR(70) NOT NULL UNIQUE,
    PASSWORD VARCHAR(70) NOT NULL,
    NAME VARCHAR(70) NOT NULL,
    ISADMIN BIT DEFAULT NULL,
    ISBLOCKED BIT DEFAULT FALSE
);       

CREATE CACHED TABLE BASKETS(
    ID INT AUTO_INCREMENT,
    CUSTOMERID INT,
    PRODUCTID INT,
    ISPAID BIT DEFAULT FALSE,
    ISORDERED BIT DEFAULT FALSE,
    CREATEDAT DATE,
    FOREIGN KEY(CUSTOMERID) REFERENCES USERS(ID),
    FOREIGN KEY(PRODUCTID) REFERENCES PRODUCTS(ID)
);         

