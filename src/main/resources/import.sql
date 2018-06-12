INSERT INTO employee (username, password, firstname, lastname) VALUES ('admin', '123456', 'Admin', 'Mr');

INSERT INTO authority (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO employee_authority (employee_name, authority_id) VALUES ('admin', 2);

-- for test
INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user (id, addr_line1, addr_line2, cash, availablebalance, city, firstname, lastname, password, state, username, zip) VALUES (1, 'Centre Avenue', 'Apt 111', 0, 0, 'Pittsburgh', 'Jeff', 'Eppinger','123456','PA', 'user', '12345');
insert into fund (id, name, symbol) values (1, 'eBizEducation1', 'EBIZA');
insert into fund (id, name, symbol) values (2, 'eBizEducation2', 'EBIZE');
insert into fund (id, name, symbol) values (3, 'eBizEducation3', 'EBIZC');
insert into funddetails (fund_id, price_date, fundprice) values (1, '2018-01-01', 12);
insert into funddetails (fund_id, price_date, fundprice) values (1, '2018-01-02', 13);
insert into funddetails (fund_id, price_date, fundprice) values (1, '2018-01-03', 14);
insert into funddetails (fund_id, price_date, fundprice) values (1, '2018-01-04', 23);
insert into funddetails (fund_id, price_date, fundprice) values (1, '2018-01-05', 9);
insert into position (cus_id, fund_id, fundshares) values (1, 1, 30);