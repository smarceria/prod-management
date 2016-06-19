# --- !Ups

create table "product"
("id" BIGSERIAL NOT NULL PRIMARY KEY,
"name" VARCHAR(254) NOT NULL,
"categoryId" BIGINT NOT NULL,
"color" VARCHAR(254) NOT NULL, 
"price" BIGINT NOT NULL,
"size" VARCHAR(254) NOT NULL
);

alter table "product" add constraint "product_category_FK" foreign key("categoryId") references "category"("id") on update NO ACTION on delete NO ACTION

# --- !Downs
;
drop table "product";