alter table registration ADD column renewal_date date;

alter table registration ADD column second_renewal_notification_sent bool default false;

alter table registration add column annual_registration_renewal_id int8;

alter table registration add constraint annual_registration_renewal_fkey foreign key (annual_registration_renewal_id) references registration;

alter table firebird_user ADD column ctep_user bool default false;

alter table managed_investigator ADD column ctep_associate bool default false;
