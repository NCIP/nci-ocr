alter table registration add column revision_date timestamp;

alter table form_1572 add column submitted_form_1572_data_id int8;

create table laboratory_certificate_snapshot (
    id int8 not null, 
    effective_date date, 
    expiration_date date, 
    type varchar(64), 
    certificate_file_id int8, 
    clinical_laboratory_snapshot_id int8 not null, 
    primary key (id));

create table organization_role_snapshot (
    discriminator varchar(31) not null, 
    id int8 not null, 
    cap_exemption varchar(14), 
    ohrp_assurance_number varchar(255), 
    practice_site_type varchar(255), 
    organization_snapshot_id int8, 
    primary key (id));
    
create table organization_snapshot (
    id int8 not null, 
    ctep_id varchar(255), 
    email varchar(255) not null, 
    last_nes_refresh timestamp, 
    name varchar(160) not null, 
    nes_id varchar(255) not null, 
    curation_status varchar(18) not null, 
    phone_number varchar(255), 
    player_identifier varchar(255), 
    city varchar(255), 
    country varchar(3), 
    delivery_address varchar(255), 
    postal_code varchar(255), 
    state_or_province varchar(50), 
    street_address varchar(255), 
    primary key (id));
    
create table person_snapshot (
    id int8 not null, 
    ctep_id varchar(255), 
    email varchar(50) not null, 
    first_name varchar(50) not null, 
    last_name varchar(50) not null, 
    last_nes_refresh timestamp, 
    middle_name varchar(50), 
    nes_id varchar(255) not null, 
    curation_status varchar(18) not null, 
    phone_number varchar(255), 
    city varchar(255), 
    country varchar(3), 
    delivery_address varchar(255), 
    postal_code varchar(255), 
    state_or_province varchar(50), 
    street_address varchar(255), 
    prefix varchar(10), 
    provider_number varchar(50), 
    suffix varchar(10), 
    update_requested timestamp, 
    primary key (id));

create table submitted_form_1572_data (
    id int8 not null,
    person_snapshot_id int8, 
    primary_organization_snapshot_id int8, 
    primary key (id));
    
create table submitted_form_1572_irb_snapshots (
    submitted_form_1572_data_id int8 not null, 
    irb_snapshot_id int8 not null, 
    primary key (submitted_form_1572_data_id, irb_snapshot_id), unique (irb_snapshot_id));

create table submitted_form_1572_lab_snapshots (
    submitted_form_1572_data_id int8 not null, 
    lab_snapshot_id int8 not null, 
    primary key (submitted_form_1572_data_id, lab_snapshot_id), unique (lab_snapshot_id));

create table submitted_form_1572_practice_site_snapshots (
    submitted_form_1572_data_id int8 not null, 
    practice_site_snapshot_id int8 not null, 
    primary key (submitted_form_1572_data_id, practice_site_snapshot_id), unique (practice_site_snapshot_id));

alter table form_1572 
    add constraint form_1572_submitted_data_fkey 
    foreign key (submitted_form_1572_data_id) references submitted_form_1572_data;

alter table laboratory_certificate_snapshot 
    add constraint laboratory_certificate_snapshot_clinical_laboratory_fkey 
    foreign key (clinical_laboratory_snapshot_id) references organization_role_snapshot;
    
alter table laboratory_certificate_snapshot 
    add constraint laboratory_certificate_snapshot_firebird_file_fkey 
    foreign key (certificate_file_id) references firebird_file;

alter table organization_role_snapshot 
    add constraint organization_role_snapshot_organization_fkey 
    foreign key (organization_snapshot_id) references organization_snapshot;
    
alter table submitted_form_1572_data 
    add constraint submitted_form_1572_person_snapshot_fkey 
    foreign key (person_snapshot_id) references person_snapshot;

alter table submitted_form_1572_data 
    add constraint submitted_form_1572_primary_organization_snapshot_fkey 
    foreign key (primary_organization_snapshot_id) references organization_snapshot;

alter table submitted_form_1572_irb_snapshots 
    add constraint submitted_form_1572_irb_snapshot_snapshot_fkey 
    foreign key (irb_snapshot_id) references organization_role_snapshot;

alter table submitted_form_1572_irb_snapshots 
    add constraint submitted_form_1572_irb_snapshot_form_data_fkey 
    foreign key (submitted_form_1572_data_id) references submitted_form_1572_data;

alter table submitted_form_1572_lab_snapshots 
    add constraint submitted_form_1572_lab_snapshot_snapshot_fkey 
    foreign key (lab_snapshot_id) references organization_role_snapshot;

alter table submitted_form_1572_lab_snapshots 
    add constraint submitted_form_1572_lab_snapshot_form_data_fkey 
    foreign key (submitted_form_1572_data_id) references submitted_form_1572_data;

alter table submitted_form_1572_practice_site_snapshots 
    add constraint submitted_form_1572_practice_site_snapshot_form_data_fkey 
    foreign key (submitted_form_1572_data_id) references submitted_form_1572_data;

alter table submitted_form_1572_practice_site_snapshots 
    add constraint submitted_form_1572_practice_site_snapshot_snapshot_fkey 
    foreign key (practice_site_snapshot_id) references organization_role_snapshot;

create table credential_snapshot (discriminator varchar(31) not null, id int8 not null, effective_date date, expiration_date date, status varchar(10), certificateType varchar(255), country varchar(3), license_id varchar(255), state varchar(2), occupation varchar(255), issuer_snapshot_id int8, certified_specialty_type_id int8, file_id int8, certification_type_id int8, degree_type_id int8, license_type_id int8, medical_specialty_id int8, medical_subspecialty_id int8, primary key (id));

alter table curriculum_vitae add column submitted_credential_data_id int8;

create table submitted_credential_data (
    id int8 not null, 
    primary key (id));
    
create table submitted_credential_data_snapshots (
    submitted_credential_data_id int8 not null, 
    credential_snapshot_id int8 not null, 
    primary key (submitted_credential_data_id, credential_snapshot_id));

alter table credential_snapshot 
    add constraint certificate_snapshot_firebird_file_fkey 
    foreign key (file_id) references firebird_file;

alter table credential_snapshot 
    add constraint specialty_snapshot_certifiedspecialtytype_fkey 
    foreign key (certified_specialty_type_id) references certified_specialty_type;

alter table credential_snapshot 
    add constraint license_snapshot_licensetype_fkey 
    foreign key (license_type_id) references license_type;

alter table credential_snapshot 
    add constraint degree_snapshot_degreetype_fkey 
    foreign key (degree_type_id) references degree_type;

alter table credential_snapshot 
    add constraint fellowship_snapshot_medical_subspecialty_fkey 
    foreign key (medical_subspecialty_id) references medical_subspecialty;

alter table credential_snapshot 
    add constraint certification_snapshot_certificationtype_fkey 
    foreign key (certification_type_id) references certification_type;

alter table credential_snapshot 
    add constraint credential_snapshot_medical_specialty_fkey 
    foreign key (medical_specialty_id) references medical_specialty;

alter table credential_snapshot 
    add constraint credential_snapshot_issuer_fkey 
    foreign key (issuer_snapshot_id) references organization_snapshot;

alter table curriculum_vitae 
    add constraint curriculum_vitae_submitted_data_fkey 
    foreign key (submitted_credential_data_id) references submitted_credential_data;

alter table submitted_credential_data_snapshots 
    add constraint submitted_credential_data_snapshots_snapshot_fkey 
    foreign key (credential_snapshot_id) references credential_snapshot;
    
alter table submitted_credential_data_snapshots 
    add constraint submitted_credential_data_snapshots_data_fkey 
    foreign key (submitted_credential_data_id) references submitted_credential_data;

alter table registration alter column last_submission_date type timestamp;