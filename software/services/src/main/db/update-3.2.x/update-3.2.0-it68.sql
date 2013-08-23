create table sub_investigator (id int8 not null, person_id int8 not null, profile_id int8 not null, primary key (id), unique (profile_id, person_id));
create table shipping_designee (id int8 not null, city varchar(255), country varchar(3), delivery_address varchar(255), postal_code varchar(255), state_or_province varchar(50), street_address varchar(255), person_id int8 not null, profile_id int8 not null, organization_id int8 not null, primary key (id), unique (profile_id, person_id));
create table ordering_designee (id int8 not null, person_id int8 not null, profile_id int8 not null, primary key (id), unique (profile_id, person_id));

insert into sub_investigator (id, person_id, profile_id) (select id, person_id, profile_id from person_association where type = 'SUBINVESTIGATOR');
insert into ordering_designee (id, person_id, profile_id) (select id, person_id, profile_id from person_association where type = 'ORDERING_DESIGNEE');

alter table sub_investigator add constraint person_association_person_fkey foreign key (person_id) references person;
alter table sub_investigator add constraint person_association_profile_fkey foreign key (profile_id) references investigator_profile;

alter table shipping_designee add constraint person_association_person_fkey foreign key (person_id) references person;
alter table shipping_designee add constraint person_association_profile_fkey foreign key (profile_id) references investigator_profile;
alter table shipping_designee add constraint shipping_designee_organization_fkey foreign key (organization_id) references organization;

alter table ordering_designee add constraint person_association_person_fkey foreign key (person_id) references person;
alter table ordering_designee add constraint person_association_profile_fkey foreign key (profile_id) references investigator_profile;

drop table person_association;

update organization set nes_id = '2.16.840.1.113883.3.26.4.2:' || nes_id;
