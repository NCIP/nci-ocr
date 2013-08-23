alter table organization add column player_identifier varchar(255);
alter table firebird_user add column disqualification_reason text;
alter table firebird_user add column disqualification_acknowledged_by_investigator boolean default false;
alter table firebird_user add column disqualification_acknowledged_by_coordinator boolean default false;

UPDATE firebird_file set description = 'Form FDA 1572: Statement of Investigator' WHERE file_name = 'ctep_form_fda_1572.pdf';
UPDATE form_type SET description = 'Form FDA 1572: Statement of Investigator', name = 'Form FDA 1572' WHERE name = '1572';
UPDATE form_type SET description = 'Financial Disclosure Form', name = 'FDF' WHERE name = 'Financial Disclosure';

alter table organization_role add column practice_site_type varchar(255);
update organization_role set practice_site_type = 'HEALTH_CARE_FACILITY' where discriminator = 'PRACTICE_SITE'; --All originally created Practice Sites were Health care facilities

create table protocol_lead_organization (id int8 not null, organization_id int8 not null, person_id int8, protocol_id int8 not null, primary key (id));
alter table protocol_lead_organization add constraint lead_organization_to_organization_fkey foreign key (organization_id) references organization;
alter table protocol_lead_organization add constraint lead_organization_protocol_fkey foreign key (protocol_id) references protocol;
alter table protocol_lead_organization add constraint lead_organization_person_fkey foreign key (person_id) references person;

insert into protocol_lead_organization (id, organization_id, protocol_id)
Select nextVal('hibernate_sequence'), lead_organization_id, protocol.id
from protocol
where protocol.lead_organization_id != null;

alter table protocol drop column lead_organization_id;

alter table organization drop constraint organization_ctep_id_key;

--PRACTICE_SITE --> HEALTH_CARE_FACILITY (2.16.840.1.113883.3.26.4.4.3:###)
insert into organization (id, ctep_id, email, name, nes_id, curation_status, phone_number, city, country,
delivery_address, postal_code, state_or_province, street_address, last_nes_refresh, player_identifier)
(
    select nextVal('hibernate_sequence'), o.ctep_id, o.email, name, '2.16.840.1.113883.3.26.4.4.3:' || r.nes_id,
    r.curation_status, o.phone_number, o.city, o.country, o.delivery_address, o.postal_code, o.state_or_province, o.street_address, null, o.nes_id
    from organization o, organization_role r
    where o.id = r.organization_id and r.roletype = 'PRACTICE_SITE'
);

update organization_role r set organization_id = 
(
    select o.id from organization o
    where o.nes_id = '2.16.840.1.113883.3.26.4.4.3:' || r.nes_id 
    and r.roletype = 'PRACTICE_SITE'
) where r.roletype = 'PRACTICE_SITE';

--IRB --> OVERSIGHT_COMMITTEE (2.16.840.1.113883.3.26.4.4.4:###)
insert into organization (id, ctep_id, email, name, nes_id, curation_status, phone_number, city, country,
delivery_address, postal_code, state_or_province, street_address, last_nes_refresh, player_identifier)
(
    select nextVal('hibernate_sequence'), o.ctep_id, o.email, name, '2.16.840.1.113883.3.26.4.4.4:' || r.nes_id,
    r.curation_status, o.phone_number, o.city, o.country, o.delivery_address, o.postal_code, o.state_or_province, o.street_address, null, o.nes_id
    from organization o, organization_role r
    where o.id = r.organization_id and r.roletype = 'IRB'
);

update organization_role r set organization_id = 
(
    select o.id from organization o
    where o.nes_id = '2.16.840.1.113883.3.26.4.4.4:' || r.nes_id 
    and r.roletype = 'IRB'
) where r.roletype = 'IRB';

--CLINICAL_LABORATORY --> HEALTH_CARE_FACILITY (2.16.840.1.113883.3.26.4.4.3:###)
insert into organization (id, ctep_id, email, name, nes_id, curation_status, phone_number, city, country,
delivery_address, postal_code, state_or_province, street_address, last_nes_refresh, player_identifier)
(
    select nextVal('hibernate_sequence'), o.ctep_id, o.email, name, '2.16.840.1.113883.3.26.4.4.3:' || r.nes_id,
    r.curation_status, o.phone_number, o.city, o.country, o.delivery_address, o.postal_code, o.state_or_province, o.street_address, null, o.nes_id
    from organization o, organization_role r
    where o.id = r.organization_id and r.roletype = 'CLINICAL_LABORATORY'
    and '2.16.840.1.113883.3.26.4.4.3:' || r.nes_id not in (select nes_id from organization)
);

update organization_role r set organization_id = 
(
    select o.id from organization o
    where o.nes_id = '2.16.840.1.113883.3.26.4.4.3:' || r.nes_id 
    and r.roletype = 'CLINICAL_LABORATORY'
) where r.roletype = 'CLINICAL_LABORATORY';

alter table organization_role drop column nes_id;
alter table organization_role drop column curation_status;