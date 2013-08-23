alter table credential ADD column nihOerIssued bool;

create table annual_registration_notification_email_addresses (
    registration_id int8 not null, 
    email_address varchar(255) not null, 
    primary key (registration_id, email_address));

alter table annual_registration_notification_email_addresses 
    add constraint annual_registration_notification_email_addresses_registration_fkey 
    foreign key (registration_id) references registration;