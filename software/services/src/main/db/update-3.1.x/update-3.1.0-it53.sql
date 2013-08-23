create table allowable_investigator_form_optionalities (id int8 not null, element varchar(255));
create table allowable_sub_investigator_form_optionalities (id int8 not null, element varchar(255));

alter table organization add column last_nes_refresh timestamp;
alter table organization alter column name type varchar(160);

create table managed_investigator (
    id int8 not null, 
    create_date timestamp not null, 
    status varchar(255), 
    status_date timestamp not null, 
    investigator_id int8 not null, 
    firebird_user_id int8 not null, 
    managed_investigator_id int8, 
    primary key (id), 
    unique (firebird_user_id, investigator_id));

alter table person add column last_nes_refresh timestamp;
alter table person add column update_requested timestamp;

alter table protocol alter column protocol_title type varchar(4000);
alter table protocol add column last_update timestamp;

alter table protocol_modification rename description to sponsor_description;
alter table protocol_modification alter column sponsor_description type text;
alter table protocol_modification add column investigator_description text not null;
update protocol_modification set investigator_description = sponsor_description;

alter table registration_coordinator_role drop constraint investigator_fkey;

insert into managed_investigator (id, create_date, status, status_date, investigator_id, firebird_user_id, managed_investigator_id) 
    select id, create_date, status, status_date, investigator_id, firebird_user_id, id from registration_coordinator_role;
    
drop table registration_coordinator_role;
    
create table registration_coordinator_role (
    id int8 not null, 
    firebird_user_id int8 not null, 
    primary key (id), 
    unique (firebird_user_id));
    
insert into registration_coordinator_role (id, firebird_user_id) select distinct id, firebird_user_id from managed_investigator;
    
alter table managed_investigator add constraint investigator_fkey foreign key (investigator_id) references investigator_profile;
alter table managed_investigator add constraint FK1D82B0498B96F89B foreign key (managed_investigator_id) references registration_coordinator_role;
alter table managed_investigator add constraint registration_coordinator_role_user_fkey foreign key (firebird_user_id) references firebird_user;

alter table AuditLogDetail add constraint AUDIT_DETAIL_RECORD_FK foreign key (record_id) references AuditLogRecord;
alter table AuditLogDetail drop constraint AUDIT_DEATIL_RECORD_FK;

update degree_type set name = 'AD - Associate''s Degree' where name = 'AD - Associate’s Degree';
update license_type set name = 'RDHAP - Registered Dental Hygienist in Alternative Practice' where name = 'RDHAP -  ,Registered Dental Hygienist in Alternative Practice ';


insert into allowable_investigator_form_optionalities (id, element) select id, 'NONE' from form_type where form_type_enum = 'FORM_1572';
insert into allowable_investigator_form_optionalities (id, element) select id, 'OPTIONAL' from form_type where form_type_enum = 'FORM_1572';
insert into allowable_investigator_form_optionalities (id, element) select id, 'REQUIRED' from form_type where form_type_enum = 'FORM_1572';

insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'NONE' from form_type where form_type_enum = 'FORM_1572';
insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'OPTIONAL' from form_type where form_type_enum = 'FORM_1572';

insert into allowable_investigator_form_optionalities (id, element) select id, 'NONE' from form_type where form_type_enum = 'CV';
insert into allowable_investigator_form_optionalities (id, element) select id, 'OPTIONAL' from form_type where form_type_enum = 'CV';
insert into allowable_investigator_form_optionalities (id, element) select id, 'REQUIRED' from form_type where form_type_enum = 'CV';

insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'NONE' from form_type where form_type_enum = 'CV';
insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'OPTIONAL' from form_type where form_type_enum = 'CV';
insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'REQUIRED' from form_type where form_type_enum = 'CV';

insert into allowable_investigator_form_optionalities (id, element) select id, 'NONE' from form_type where form_type_enum = 'FINANCIAL_DISCLOSURE_FORM';
insert into allowable_investigator_form_optionalities (id, element) select id, 'OPTIONAL' from form_type where form_type_enum = 'FINANCIAL_DISCLOSURE_FORM';
insert into allowable_investigator_form_optionalities (id, element) select id, 'REQUIRED' from form_type where form_type_enum = 'FINANCIAL_DISCLOSURE_FORM';

insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'NONE' from form_type where form_type_enum = 'FINANCIAL_DISCLOSURE_FORM';
insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'OPTIONAL' from form_type where form_type_enum = 'FINANCIAL_DISCLOSURE_FORM';
insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'REQUIRED' from form_type where form_type_enum = 'FINANCIAL_DISCLOSURE_FORM';

insert into allowable_investigator_form_optionalities (id, element) select id, 'NONE' from form_type where form_type_enum = 'HUMAN_RESEARCH_CERTIFICATE';
insert into allowable_investigator_form_optionalities (id, element) select id, 'OPTIONAL' from form_type where form_type_enum = 'HUMAN_RESEARCH_CERTIFICATE';
insert into allowable_investigator_form_optionalities (id, element) select id, 'REQUIRED' from form_type where form_type_enum = 'HUMAN_RESEARCH_CERTIFICATE';

insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'NONE' from form_type where form_type_enum = 'HUMAN_RESEARCH_CERTIFICATE';
insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'OPTIONAL' from form_type where form_type_enum = 'HUMAN_RESEARCH_CERTIFICATE';
insert into allowable_sub_investigator_form_optionalities (id, element) select id, 'REQUIRED' from form_type where form_type_enum = 'HUMAN_RESEARCH_CERTIFICATE';

update firebird_file set length='93652' where file_name = 'dcp_financial_disclosure_form.pdf';
