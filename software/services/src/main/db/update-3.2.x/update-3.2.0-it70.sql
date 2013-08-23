update firebird_user set status = null where ctep_user = false;

update firebird_user
    set
        disqualification_acknowledged_by_investigator = null,
        disqualification_acknowledged_by_coordinator = null,
        status_change_notification_required_for_investigator = null,
        status_change_notification_required_for_coordinator = null
    where investigator_role_profile_id is null;

update firebird_user
    set
        disqualification_acknowledged_by_investigator = null,
        disqualification_acknowledged_by_coordinator = null
    where disqualification_reason is null;

alter table firebird_user alter column disqualification_acknowledged_by_investigator drop default;
alter table firebird_user alter column disqualification_acknowledged_by_coordinator drop default;
alter table firebird_user alter column status_change_notification_required_for_investigator drop default;
alter table firebird_user alter column status_change_notification_required_for_coordinator drop default;

update ctepform1572_irbs irbs set irb_id = (
    select o.id
    from organization o, organization_role r
    where o.id = r.organization_id
    and o.player_identifier = (select nes_id from organization where id = irbs.irb_id)
    and r.roletype = 'IRB'
) where irb_id in (select id from organization where player_identifier is null);

update ctepform1572_labs labs set lab_id = (
    select o.id
    from organization o, organization_role r
    where o.id = r.organization_id
    and o.player_identifier = (select nes_id from organization where id = labs.lab_id)
    and r.roletype = 'CLINICAL_LABORATORY'
    and o.id is not null
) where lab_id in (select id from organization where player_identifier is null);

update ctepform1572_practicesites ps set practice_site_id = (
    select o.id
    from organization o, organization_role r
    where o.id = r.organization_id
    and o.player_identifier = (select nes_id from organization where id = ps.practice_site_id)
    and r.roletype = 'PRACTICE_SITE'
) where practice_site_id in (select id from organization where player_identifier is null);

update form1572_irbs irbs set irb_id = (
    select o.id
    from organization o, organization_role r
    where o.id = r.organization_id
    and o.player_identifier = (select nes_id from organization where id = irbs.irb_id)
    and r.roletype = 'IRB'
) where irb_id in (select id from organization where player_identifier is null);

update form1572_labs labs set lab_id = (
    select o.id
    from organization o, organization_role r
    where o.id = r.organization_id
    and o.player_identifier = (select nes_id from organization where id = labs.lab_id)
    and r.roletype = 'CLINICAL_LABORATORY'
) where lab_id in (select id from organization where player_identifier is null);

update form1572_practicesites ps set practice_site_id = (
    select o.id
    from organization o, organization_role r
    where o.id = r.organization_id
    and o.player_identifier = (select nes_id from organization where id = ps.practice_site_id)
    and r.roletype = 'PRACTICE_SITE'
) where practice_site_id in (select id from organization where player_identifier is null);


alter table protocol_lead_organization drop constraint lead_organization_protocol_fkey;
alter table protocol_lead_organization drop constraint lead_organization_person_fkey;
alter table protocol_lead_organization add constraint lead_organization_to_protocol_fkey foreign key (protocol_id) references protocol;
alter table protocol_lead_organization add constraint lead_organization_to_person_fkey foreign key (person_id) references person;

alter table organization_role add column primary_organization_type varchar(255);

alter table organization_role alter column roleType type varchar(20);
alter table organization_role rename column roleType to role_type;

alter table investigator_profile add column primary_organization_id int8;
alter table investigator_profile drop constraint investigatorprofile_organization_fkey;
alter table investigator_profile drop column organization_id;
alter table investigator_profile add constraint investigatorprofile_primary_organization_fkey foreign key (primary_organization_id) references organization_role;