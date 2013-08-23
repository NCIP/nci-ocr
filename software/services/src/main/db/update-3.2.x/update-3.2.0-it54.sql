select nextval('hibernate_sequence');

create table registration_form_set_configuration (
    id int8 not null,
    primary key (id));

create table configured_form_optionalities (
    registration_form_set_configuration_id int8 not null,
    optionality varchar(13),
    form_type_id int8 not null,
    primary key (registration_form_set_configuration_id, form_type_id));

alter table configured_form_optionalities
    add constraint configured_form_optionalities_form_type_fkey foreign key (form_type_id) references form_type;

alter table configured_form_optionalities
    add constraint configured_form_optionalities_configuration_fkey foreign key (registration_form_set_configuration_id)
    references registration_form_set_configuration;

insert into registration_form_set_configuration (id) select id from protocol;

insert into configured_form_optionalities (registration_form_set_configuration_id, optionality, form_type_id)
    select protocol_id, optionality, form_type_id from protocol_investigator_form_optionality;

insert into registration_form_set_configuration (id) select id + currval('hibernate_sequence') from protocol;

insert into configured_form_optionalities (registration_form_set_configuration_id, optionality, form_type_id)
    select protocol_id + currval('hibernate_sequence'), optionality, form_type_id from protocol_subinvestigator_form_optionality;

alter table protocol add column
    investigator_configuration_id int8;

alter table protocol add column
    subinvestigator_configuration_id int8;

alter table protocol add constraint registration_configuration_investigator_configuration_fkey
    foreign key (investigator_configuration_id) references registration_form_set_configuration;

alter table protocol add constraint registration_configuration_subinvestigator_configuration_fkey
    foreign key (subinvestigator_configuration_id) references registration_form_set_configuration;

update protocol set investigator_configuration_id = id;
update protocol set subinvestigator_configuration_id = id + currval('hibernate_sequence');

select setval('hibernate_sequence', max(subinvestigator_configuration_id)) from protocol;
