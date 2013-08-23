alter table external_data add column update_pending bool not null default false;

update external_data set update_pending = true where update_requested is not null;
