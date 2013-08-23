alter table firebird_user add column status varchar(25);

alter table registration alter column annual_registration_type type varchar(11);

create table property_holder (
    id int8 not null, 
    discriminator varchar(10) not null, 
    name varchar(255) not null unique, 
    date_value timestamp, 
    string_value varchar(255), 
    primary key (id));

insert into property_holder (id, discriminator, name, date_value) 
    values (nextval('hibernate_sequence'), 'DATE', 'ctep_investigator_status_update_timestamp', now());