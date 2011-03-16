CREATE TABLE cars (
         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         price INT,
         short_descrpt VARCHAR(1000),
         long_descrpt VARCHAR(2048),
         city VARCHAR(30),
         product_year INT,
         cluster INT
       ) CHARACTER SET utf8;


-- WARNING: All changes in schema
-- should be affected in src/Harvester/DatabaseAdapter file!