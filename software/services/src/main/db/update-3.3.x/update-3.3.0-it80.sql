create table external_data (
	discriminator varchar(31) not null, 
	external_id varchar(255) not null, 
	last_nes_refresh timestamp, 
	update_requested timestamp, 
	player_id varchar(255), 
	oversight_committee_type varchar(26), 
	research_organization_type varchar(17), 
	primary key (external_id));

alter table person add column external_data_id varchar(255);
alter table person add constraint person_external_data_fkey foreign key (external_data_id) references external_data;

alter table organization add column external_data_id varchar(255);
alter table organization add constraint organization_external_data_fkey foreign key (external_data_id) references external_data;

insert into external_data (discriminator, external_id, last_nes_refresh, update_requested)
select 'NES_PERSON_DATA', external_id, last_nes_refresh, update_requested
from person;

insert into external_data (discriminator, external_id, last_nes_refresh)
select 'NES_ORGANIZATION_DATA', external_id, last_nes_refresh
from organization 
where external_id like '2.16.840.1.113883.3.26.4.2:%';

insert into external_data (discriminator, external_id, player_id, last_nes_refresh)
select 'HEALTH_CARE_FACILITY_DATA', external_id, player_identifier, last_nes_refresh
from organization 
where external_id like '2.16.840.1.113883.3.26.4.4.3:%';

insert into external_data (discriminator, external_id, player_id, last_nes_refresh, oversight_committee_type)
select 'OVERSIGHT_COMMITTEE_DATA', external_id, player_identifier, last_nes_refresh, 'INSTITUTIONAL_REVIEW_BOARD'
from organization 
where external_id like '2.16.840.1.113883.3.26.4.4.4:%';

insert into external_data (discriminator, external_id, player_id, last_nes_refresh)
select 'RESEARCH_ORGANIZATION_DATA', external_id, player_identifier, last_nes_refresh
from organization 
where external_id like '2.16.840.1.113883.3.26.4.4.5:%';

update person set external_data_id = external_id;
update organization set external_data_id = external_id;

alter table person alter external_data_id set not null;
alter table organization alter external_data_id set not null;

update external_data set research_organization_type = 'CLINICAL_CENTER'
where external_id in
(select external_data_id 
from organization 
where external_data_id like '2.16.840.1.113883.3.26.4.4.5:%'
and id in
	(select organization_id from organization_role where practice_site_type = 'CLINICAL_CENTER' or primary_organization_type = 'CLINICAL_CENTER'));

update external_data set research_organization_type = 'CANCER_CENTER'
where external_id in
(select external_data_id 
from organization 
where external_data_id like '2.16.840.1.113883.3.26.4.4.5:%'
and id in
	(select organization_id from organization_role where practice_site_type = 'CANCER_CENTER' or primary_organization_type = 'CANCER_CENTER'));
	
alter table person drop column external_id;
alter table person drop column last_nes_refresh;
alter table person drop column update_requested;

alter table person_snapshot drop column last_nes_refresh;
alter table person_snapshot drop column update_requested;

alter table organization drop column external_id;
alter table organization drop column last_nes_refresh;
alter table organization drop column player_identifier;

alter table organization_snapshot drop column player_identifier;
alter table organization_snapshot drop column last_nes_refresh;