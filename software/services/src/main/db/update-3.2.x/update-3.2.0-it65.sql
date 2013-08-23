create table medical_specialty_certifying_board (id int8 not null, name varchar(255) not null, primary key (id), unique (name));
create table medical_specialty (id int8 not null, name varchar(255) not null, medical_specialty_certifying_board_id int8, primary key (id), unique (name, medical_specialty_certifying_board_id));
alter table medical_specialty add constraint medical_specialty_certifying_board_fkey foreign key (medical_specialty_certifying_board_id) references medical_specialty_certifying_board;

create table medical_subspecialty_certifying_board (id int8 not null, name varchar(255) not null, primary key (id), unique (name));
create table medical_subspecialty (id int8 not null, name varchar(255) not null, medical_subspecialty_certifying_board_id int8, primary key (id), unique (name, medical_subspecialty_certifying_board_id));
alter table medical_subspecialty add constraint medical_subspecialty_certifying_board_fkey foreign key (medical_subspecialty_certifying_board_id) references medical_subspecialty_certifying_board;

alter table credential add column occupation varchar(255);
alter table credential add column medical_specialty_id int8;
alter table credential add column medical_subspecialty_id int8;

alter table credential add constraint medical_specialty_fkey foreign key (medical_specialty_id) references medical_specialty;
alter table credential add constraint medical_subspecialty_fkey foreign key (medical_subspecialty_id) references medical_subspecialty;

alter table firebird_user add column withdrawal_explanation text;

insert into medical_specialty_certifying_board (id, name) values (nextval('hibernate_sequence'), 'ABMS - American Board of Medical Specialties');
insert into medical_specialty (id, name, medical_specialty_certifying_board_id) values
(nextval('hibernate_sequence'), 'Allergy and Immunology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Anesthesiology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Colon and Rectal Surgery', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Dermatology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Emergency Medicine', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Family Medicine', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Internal Medicine', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Medical Genetics', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Neurological Surgery', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Nuclear Medicine', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Obstetrics and Gynecology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Ophthalmology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Orthopaedic Surgery', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Otolaryngology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Pathology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Pediatrics', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Physical Medicine and Rehabilitation', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Plastic Surgery', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Preventive Medicine', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Psychiatry and Neurology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Radiology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Surgery', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Thoracic Surgery', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties')),
(nextval('hibernate_sequence'), 'Urology', (select id from medical_specialty_certifying_board where name = 'ABMS - American Board of Medical Specialties'));

insert into medical_specialty_certifying_board (id, name) values (nextval('hibernate_sequence'), 'AOBOS - American Osteopathic Board of Orthopedic Surgery');
insert into medical_specialty (id, name, medical_specialty_certifying_board_id) values
(nextval('hibernate_sequence'), 'Anesthesiology', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Dermatology', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Emergency Medicine', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Family Physicians', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Internal Medicine', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Neurology and Psychiatry', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Neuromusculoskeletal Medicine', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Nuclear Medicine', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Obstetrics and Gynecology', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Ophthalmology and Otolaryngology', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Orthopedic Surgery', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Pathology', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Pediatrics', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Physical Medicine and Rehabilitation', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Preventive Medicine', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Proctology', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Radiology', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery')),
(nextval('hibernate_sequence'), 'Surgery', (select id from medical_specialty_certifying_board where name = 'AOBOS - American Osteopathic Board of Orthopedic Surgery'));

insert into medical_specialty_certifying_board (id, name) values (nextval('hibernate_sequence'), 'ABPS - American Board of Physician Specialties');
insert into medical_specialty (id, name, medical_specialty_certifying_board_id) values
(nextval('hibernate_sequence'), 'Dermatology', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Diagnostic Radiology', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Disaster Medicine', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Emergency Medicine', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Family Medicine Obstetrics', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Family Practice', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Geriatric Medicine', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Hospital Medicine', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Integrative Medicine', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Internal Medicine', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Obstetrics and Gynecology', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Ophthalmology', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Plastic and Reconstructive Surgery', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Psychiatry', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Radiation Oncology', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Surgery', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties')),
(nextval('hibernate_sequence'), 'Urgent Care', (select id from medical_specialty_certifying_board where name = 'ABPS - American Board of Physician Specialties'));

insert into medical_specialty_certifying_board (id, name) values (nextval('hibernate_sequence'), 'General');
insert into medical_specialty (id, name, medical_specialty_certifying_board_id) values
(nextval('hibernate_sequence'), 'Not Applicable', (select id from medical_specialty_certifying_board where name = 'General'));

insert into medical_subspecialty_certifying_board (id, name) values (nextval('hibernate_sequence'), 'ACGME - Accreditation Council for Graduate Medical Education');
insert into medical_subspecialty (id, name, medical_subspecialty_certifying_board_id) values
(nextval('hibernate_sequence'), 'Allergy, Asthma & Immunology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Cardiology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Critical Care Medicine', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Endocrinology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Gastroenterology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Geriatrics', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Hematology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Hospice and Palliative Medicine', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Infectious Disease', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Nephrology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Oncology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Orthopaedic', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Pulmonology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Rheumatology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Sleep Medicine', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Transplant Hepatology', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education')),
(nextval('hibernate_sequence'), 'Traumatologist', (select id from medical_subspecialty_certifying_board where name = 'ACGME - Accreditation Council for Graduate Medical Education'));