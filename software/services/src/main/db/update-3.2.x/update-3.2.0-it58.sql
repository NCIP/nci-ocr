update firebird_file set length=288290 where file_name='ctep_form_fda_1572.pdf';

alter table ctep_form_1572 add column phase_one bool not null;
alter table ctep_form_1572 add column phase_two_or_three bool not null;

alter table person add column ctep_id varchar(255) unique;

alter table ctep_financial_disclosure add column equity_in_sponsor bool;
alter table ctep_financial_disclosure add column financial_interest bool;
alter table ctep_financial_disclosure add column monetary_gain bool;
alter table ctep_financial_disclosure add column other_sponsor_payments bool;

create table ctep_financial_disclosure_documentation(disclosure bigint not null,firebirdfile bigint not null);
alter table ctep_financial_disclosure_documentation add constraint ctep_financial_disclosure_documentation_pkey primary key (disclosure, firebirdfile);
alter table ctep_financial_disclosure_documentation add constraint ctep_financial_disclosure_fkey foreign key (disclosure) references ctep_financial_disclosure (id);
alter table ctep_financial_disclosure_documentation add constraint firebirdfile_fkey foreign key (firebirdfile) references firebird_file (id);

CREATE TABLE ctep_financial_disclosure_pharmaceuticals
(
  disclosure bigint NOT NULL,
  pharmaceutical bigint NOT NULL,
  CONSTRAINT ctep_financial_disclosure_fkey FOREIGN KEY (disclosure) REFERENCES ctep_financial_disclosure (id),
  CONSTRAINT pharmaceutical_fkey FOREIGN KEY (pharmaceutical) REFERENCES organization (id)
);

update firebird_file set length=60948 where file_name='ctep_financial_disclosure_form.pdf';

alter table person add column provider_number varchar(50);