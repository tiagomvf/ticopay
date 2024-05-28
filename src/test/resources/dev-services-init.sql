CREATE TABLE Wallet
(
    ownerId BIGINT NOT NULL,
    balance DECIMAL,
    CONSTRAINT pk_wallet PRIMARY KEY (ownerId)
);

insert INTO wallet (balance, ownerid) values (1000,4);
insert INTO wallet (balance, ownerid) values (0,15);


