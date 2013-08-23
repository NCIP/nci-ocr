-- Annual registration forms supported in FIREBIRD
-- CTEP 1572
-- CTEP FDF
-- CTEP SIDF

create table form_type_registration_types (
    id int8 not null,
    element varchar(255));

alter table form_type_registration_types
    add constraint form_type_registration_types_form_type_fkey foreign key (id) references form_type;

insert into form_type_registration_types
    select id, 'PROTOCOL' from form_type;

create table annual_registration_configuration (
    id int8 not null,
    timestamp timestamp not null,
    form_set_configuration_id int8, primary key (id),
    sponsor_id int8 not null);

alter table annual_registration_configuration
    add constraint annual_registration_configuration_form_set_configuration_fkey
    foreign key (form_set_configuration_id)
    references registration_form_set_configuration;

alter table annual_registration_configuration
    add constraint annual_registration_sponsor_fkey
    foreign key (sponsor_id)
    references organization;

insert into registration_form_set_configuration (id) values (nextval('hibernate_sequence'));

alter table form_type alter column form_type_enum type varchar(35);

insert into byte_data_source (id, discriminator, resource_path) values (
    nextval('hibernate_sequence'),
    'CLASSPATH_RESOURCE',
    '/files/ctep_form_fda_1572.pdf.gz'
);

insert into firebird_file (id, content_type, description, length, file_name, upload_date, byte_data_source_id) values
(
    currval('hibernate_sequence'),
    'application/pdf',
    'FDA Form 1572: Statement of Investigator',
    549103,
    'ctep_form_fda_1572.pdf',
    'now',
    currval('hibernate_sequence')
);

insert into form_type (id, name, description, form_type_enum, investigator_default, subinvestigator_default, sample_id, template_id, signing_field) values
(
    currval('hibernate_sequence'),
    '1572',
    'FDA Form 1572: Statement of Investigator',
    'CTEP_FORM_1572',
    'REQUIRED',
    'NONE',
    currval('hibernate_sequence'),
    currval('hibernate_sequence'),
    'signature'
);

insert into form_type_registration_types values (currval('hibernate_sequence'), 'ANNUAL');

insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'NONE');
insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'OPTIONAL');
insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'REQUIRED');
insert into allowable_sub_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'NONE');

insert into byte_data_source (id, discriminator, resource_path) values (
    nextval('hibernate_sequence'),
    'CLASSPATH_RESOURCE',
    '/files/ctep_financial_disclosure_form.pdf.gz'
);

insert into firebird_file (id, content_type, description, length, file_name, upload_date, byte_data_source_id) values
(
    currval('hibernate_sequence'),
    'application/pdf',
    'Financial Disclosure Form',
    445850,
    'ctep_financial_disclosure_form.pdf',
    'now',
    currval('hibernate_sequence')
);

insert into form_type (id, name, description, form_type_enum, investigator_default, subinvestigator_default, sample_id, template_id, signing_field) values
(
    currval('hibernate_sequence'),
    'Financial Disclosure',
    'Financial Disclosure Form',
    'CTEP_FINANCIAL_DISCLOSURE_FORM',
    'REQUIRED',
    'NONE',
    currval('hibernate_sequence'),
    currval('hibernate_sequence'),
    'signature'
);

insert into form_type_registration_types values (currval('hibernate_sequence'), 'ANNUAL');

insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'NONE');
insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'OPTIONAL');
insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'REQUIRED');
insert into allowable_sub_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'NONE');


insert into byte_data_source (id, discriminator, resource_path) values (
    nextval('hibernate_sequence'),
    'CLASSPATH_RESOURCE',
    '/files/ctep_supplemental_investigator_data_form.pdf.gz'
);

insert into firebird_file (id, content_type, description, length, file_name, upload_date, byte_data_source_id) values
(
    currval('hibernate_sequence'),
    'application/pdf',
    'Supplemental Investigator Data Form',
    569823,
    'ctep_supplemental_investigator_data_form.pdf',
    'now',
    currval('hibernate_sequence')
);

insert into form_type (id, name, description, form_type_enum, investigator_default, subinvestigator_default, sample_id, template_id, signing_field) values
(
    currval('hibernate_sequence'),
    'Supplemental Investigator Data Form',
    'Supplemental Investigator Data Form',
    'SUPPLEMENTAL_INVESTIGATOR_DATA_FORM',
    'REQUIRED',
    'NONE',
    currval('hibernate_sequence'),
    currval('hibernate_sequence'),
    'signature'
);

insert into form_type_registration_types values (currval('hibernate_sequence'), 'ANNUAL');

insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'NONE');
insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'OPTIONAL');
insert into allowable_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'REQUIRED');
insert into allowable_sub_investigator_form_optionalities (id, element) values (currval('hibernate_sequence'), 'NONE');

insert into form_type_registration_types select id, 'ANNUAL' from form_type where form_type_enum = 'ADDITIONAL_ATTACHMENTS';

-- Persistence changes to support investigator annual registrations

alter table protocol_registration rename to registration;

alter table registration alter column invitation_change_date drop not null;

alter table registration alter column invitation_status drop not null;

alter table registration alter column protocol_id drop not null;

alter table registration drop constraint protocol_registration_protocol_id_key;

alter table registration add column configuration_id int8;

alter table registration add column annual_registration_type varchar(7);

alter table registration add column due_date date;

alter table registration add constraint registration_configuration_fkey 
    foreign key (configuration_id) references annual_registration_configuration;
    
create table ctep_financial_disclosure (
    id int8 not null, 
    comments text, 
    form_status varchar(255), 
    status_date timestamp not null, 
    flattened_pdf_id int8, 
    form_type_id int8 not null, 
    signed_pdf_id int8, 
    registration_id int8 not null, 
    primary key (id), 
    unique (registration_id));
    
create table ctep_form_1572 (
    id int8 not null, 
    comments text, 
    form_status varchar(255), 
    status_date timestamp not null, 
    flattened_pdf_id int8, 
    form_type_id int8 not null, 
    signed_pdf_id int8, 
    registration_id int8 not null, 
    primary key (id), 
    unique (registration_id));
    
create table supplemental_investigator_data_form (
    id int8 not null, 
    comments text, 
    form_status varchar(255), 
    status_date timestamp not null, 
    flattened_pdf_id int8, 
    form_type_id int8 not null, 
    signed_pdf_id int8, 
    registration_id int8 not null, 
    primary key (id), 
    unique (registration_id));

alter table ctep_financial_disclosure add constraint registration_form_signed_pdf_fkey foreign key (signed_pdf_id) references firebird_file;
alter table ctep_financial_disclosure add constraint form_form_type_fkey foreign key (form_type_id) references form_type;
alter table ctep_financial_disclosure add constraint registration_form_flattened_pdf_fkey foreign key (flattened_pdf_id) references firebird_file;
alter table ctep_financial_disclosure add constraint ctep_financial_disclosure_registration_fkey foreign key (registration_id) references registration;

alter table ctep_form_1572 add constraint registration_form_signed_pdf_fkey foreign key (signed_pdf_id) references firebird_file;
alter table ctep_form_1572 add constraint form_form_type_fkey foreign key (form_type_id) references form_type;
alter table ctep_form_1572 add constraint registration_form_flattened_pdf_fkey foreign key (flattened_pdf_id) references firebird_file;
alter table ctep_form_1572 add constraint ctep_form_1572_registration_fkey foreign key (registration_id) references registration;

alter table supplemental_investigator_data_form add constraint registration_form_signed_pdf_fkey foreign key (signed_pdf_id) references firebird_file;
alter table supplemental_investigator_data_form add constraint form_form_type_fkey foreign key (form_type_id) references form_type;
alter table supplemental_investigator_data_form add constraint registration_form_flattened_pdf_fkey foreign key (flattened_pdf_id) references firebird_file;
alter table supplemental_investigator_data_form add constraint ctep_form_1572_registration_fkey foreign key (registration_id) references registration;


