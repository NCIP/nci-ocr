alter table registration add column current_registration_id int8;

alter table registration add constraint current_registration_fkey foreign key (current_registration_id) references registration;

create table revised_registration_subinvestigator_registration (
    registration_id int8 not null, 
    subinvestigator_registration_id int8 not null, 
    primary key (registration_id, subinvestigator_registration_id));

alter table revised_registration_subinvestigator_registration add constraint subinvestigator_registration_fkey foreign key (subinvestigator_registration_id) references registration;
alter table revised_registration_subinvestigator_registration add constraint revised_registration_fkey foreign key (registration_id) references registration;
