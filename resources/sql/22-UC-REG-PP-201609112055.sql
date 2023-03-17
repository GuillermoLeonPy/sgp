/*UC-REG-PP*/
--drop table pms_production_process
create table pms_production_process(
id	   		 		   bigint,
process_id	 		   varchar(100) NOT NULL,
process_description	 	varchar(250)  NOT NULL,
id_product					bigint NOT NULL,
is_active							varchar(1) default 'S',
registration_date		   			timestamp NOT NULL default now(),
validity_end_date		   timestamp,
is_enable					varchar(1) NOT NULL default 'N',
creation_user	 		   varchar(50),
creation_date			   timestamp NOT NULL default now(),
last_modif_user 		   varchar(50),
last_modif_date		   timestamp);

--ALTER TABLE pms_production_process ADD is_active varchar(1) default 'S'
ALTER TABLE pms_production_process ADD CHECK (is_active in ('S'));

ALTER TABLE pms_production_process ADD CHECK (is_enable in ('S', 'N'));
ALTER TABLE pms_production_process ADD CHECK (validity_end_date > registration_date);

ALTER TABLE pms_production_process ADD CONSTRAINT pms_production_process_id_pk PRIMARY KEY (id);

ALTER TABLE pms_production_process ADD CONSTRAINT pms_production_process_id_uk UNIQUE (process_id);

ALTER TABLE pms_production_process ADD CONSTRAINT 
pms_production_process_id_uk_02 UNIQUE (id_product,is_active);


ALTER TABLE pms_production_process 
ADD constraint pms_production_process_fk_01
FOREIGN KEY (id_product) REFERENCES pms_product (id);

create sequence pms_production_process_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_production_process.id;



create or replace function p_i_pms_production_process
(
pid	   		 		   bigint,
pprocess_id		 		varchar,
pprocess_description	  	varchar,
pid_product					bigint,
pregistration_date		   		inout timestamp,
pcreation_user	 		   varchar
)
returns timestamp as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_i_pms_production_process';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	if (pprocess_id is null or length(trim(pprocess_id))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.productionprocessdao.productionprocessdto.processid.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (pprocess_description is null or length(trim(pprocess_description))=0)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.productionprocessdao.productionprocessdto.processdescription.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;


	declare
		vid					pms_production_process.id%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select id into vid
		from pms_production_process
		where process_id = upper(trim(pprocess_id));

		if vid is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.productionprocessdao.productionprocessdto.processid.already.exists.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';			
		end if;
	end;

	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select pp.process_id,prod.product_id into vrecord
		from pms_production_process pp, pms_product prod
		where pp.id_product = pid_product
		and pp.validity_end_date is null
		and pp.id_product = prod.id;

		if vrecord.process_id is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.productionprocessdao.productionprocessdto.process.in.valid.status.exists.for.product.error'||''||vrecord.process_id||''||vrecord.product_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';			
		end if;
	end;

	/*END OF VALIDATIONS*/

	pregistration_date:=now();

	insert into pms_production_process
			(id,process_id,process_description,id_product,registration_date,creation_user,creation_date)
	values
			(pid,
			upper(trim(pprocess_id)),
			upper(trim(pprocess_description)),
			pid_product,
			pregistration_date,
			pcreation_user,
			now());
			
	EXCEPTION
		WHEN SQLSTATE 'P9989' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';
