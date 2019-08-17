CREATE TABLE PRODUCTS
(
    ID    INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    NAME  VARCHAR(255),
    PRICE INT NOT NULL
);

CREATE TABLE USERS
(
    ID        INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    NICKNAME  VARCHAR(70) NOT NULL UNIQUE,
    PASSWORD  VARCHAR(70) NOT NULL,
    NAME      VARCHAR(70) NOT NULL,
    ISADMIN   BIT DEFAULT NULL,
    ISBLOCKED BIT DEFAULT FALSE
);

CREATE TABLE ORDERS
(
    ORDER_ID  INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    CUSTOMERID INT,
    CREATEDAT  DATE,
    STATE      ENUM ('PAID','NOT_ORDERED','ORDERED') DEFAULT 'NOT_ORDERED',
    FOREIGN KEY (CUSTOMERID) REFERENCES USERS (ID)

);
CREATE TABLE PRODUCTS_ORDERS
(
    PRODUCT_ID INT,
    ORDER_ID   INT,
    COUNT      INT DEFAULT 1,
    FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS (ID),
    FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ORDER_ID)
);

