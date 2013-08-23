alter table registration ADD column renewed bool default false;

update registration set renewed = true where annual_registration_renewal_id is not null;

alter table organization_role drop column cap_exemption;
alter table organization_role_snapshot drop column cap_exemption;