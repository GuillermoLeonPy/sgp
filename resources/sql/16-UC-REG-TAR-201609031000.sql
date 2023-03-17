/*UC-REG-TAR*/

create table pms_tariff(
id	   		 		   bigint,
tariff_id		 		   varchar(50) NOT NULL,
id_currency	   		 		   bigint NOT NULL,
id_measurment_unit	   		 		   bigint NOT NULL,
creation_user	 		   varchar(50) ,
creation_date			   timestamp NOT NULL default now(),
last_modif_user 		   varchar(50),
last_modif_date		   timestamp);

ALTER TABLE pms_tariff ADD CONSTRAINT pms_tariff_id_pk PRIMARY KEY (id);
ALTER TABLE pms_tariff ADD CONSTRAINT pms_tariff_tariff_id_uk UNIQUE (tariff_id);

ALTER TABLE pms_tariff 
ADD constraint pms_tariff_fk_01
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

ALTER TABLE pms_tariff 
ADD constraint pms_tariff_fk_02
FOREIGN KEY (id_measurment_unit) REFERENCES pms_measurment_unit (id);

ALTER TABLE pms_tariff ADD CONSTRAINT pms_tariff_tariff_id_uk_02 UNIQUE (id_currency,id_measurment_unit);

create sequence pms_tariff_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_tariff.id;



create or replace function p_i_pms_tariff
(
pid	   		 		   bigint,
ptariff_id	   		 	varchar,
pid_currency	   		bigint,
pid_measurment_unit	   		bigint,
pcreation_user				varchar
)
returns void as
$BODY$
declare
	error_message          varchar(200);
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_i_pms_tariff';
	RAISE INFO '--------------------------';

	/*BEGIN VALIDATIONS*/	
		if (ptariff_id is null or length(trim(ptariff_id))=0)
		then
			declare
				b1_error_message		varchar(200);
				b1_error_message_hint	varchar(200);
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.tariffdao.tariffdto.tariffid.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;	

		declare
			vid					pms_tariff.id%TYPE;
			vtariff_id					pms_tariff.tariff_id%TYPE;
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			select id into vid
			from			
				pms_tariff
			where 				
				id_currency = pid_currency
				and id_measurment_unit = pid_measurment_unit;

			if (vid is not null) then
				select tariff_id into vtariff_id
				from			
					pms_tariff
				where 				
					id_currency = pid_currency
					and id_measurment_unit = pid_measurment_unit;

							
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.tariffdao.tariffdto.currency.measurmentunit.already.asociated.error'||''||vtariff_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';

			end if;
		end;

	insert into pms_tariff
			(id,tariff_id,id_currency,id_measurment_unit,creation_user,creation_date)
	values
			(pid,
			upper(trim(ptariff_id)),
			pid_currency,
			pid_measurment_unit,
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
