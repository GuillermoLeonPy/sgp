/*60-f_determinate_product_price-recording-order-unique-nbr-201610311501*/

create table pms_order_budget_product(
id	   		 		   				bigint,
order_identifier_number					bigint not null,/*can not be foreign key because the order record does not exists*/
id_product							bigint not null,
creation_user							varchar(50),
creation_date							timestamp not null default now()
);


ALTER TABLE pms_order_budget_product ADD CONSTRAINT
pms_order_budget_product_uk_02 UNIQUE (order_identifier_number,id_product);

ALTER TABLE pms_order_budget_product 
ADD CONSTRAINT pms_order_budget_product_id_pk PRIMARY KEY (id);

ALTER TABLE pms_order_budget_product 
ADD constraint pms_order_budget_product_fk_01
FOREIGN KEY (id_product) 
REFERENCES pms_product (id);


create sequence pms_order_budget_product_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_order_budget_product.id;


create or replace function p_i_pms_order_budget_product
(
porder_identifier_number			 	bigint,
pid_product						bigint,
pcreation_user	 		   			varchar
)
returns bigint as
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
	RAISE INFO 'executing p_i_pms_order_budget_product';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vid_order_budget_product			bigint;
		vorder_budget_product_record		record;
	begin
		select * into vorder_budget_product_record from 
		pms_order_budget_product 
		where order_identifier_number = porder_identifier_number
		and id_product = pid_product;

		if vorder_budget_product_record.id != null then
			--already recorded
			return null;
		else
			vid_order_budget_product := nextval('pms_order_budget_product_id_sq');
			insert into pms_order_budget_product
			(id,order_identifier_number,id_product,creation_user)
			values
			(vid_order_budget_product,porder_identifier_number,pid_product,pcreation_user);

			return vid_order_budget_product;
		end if;
	end;

	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' or SQLSTATE 'P9998' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';




create table pms_order_budget_production_process_activity(
id	   		 		   				bigint,
id_order_budget_product					bigint not null,
id_production_process					bigint not null,
id_production_process_activity			bigint not null,
creation_user							varchar(50),
creation_date							timestamp not null default now()
);

ALTER TABLE pms_order_budget_production_process_activity ADD CONSTRAINT
pms_order_budget_production_process_activity_uk_01 UNIQUE 
(id_order_budget_product,id_production_process,id_production_process_activity);


ALTER TABLE pms_order_budget_production_process_activity 
ADD CONSTRAINT pms_order_budget_production_process_activity_id_pk PRIMARY KEY (id);


ALTER TABLE pms_order_budget_production_process_activity 
ADD constraint pms_order_budget_production_process_activity_fk_01
FOREIGN KEY (id_order_budget_product) 
REFERENCES pms_order_budget_product (id);

ALTER TABLE pms_order_budget_production_process_activity 
ADD constraint pms_order_budget_production_process_activity_fk_02
FOREIGN KEY (id_production_process) 
REFERENCES pms_production_process (id);

ALTER TABLE pms_order_budget_production_process_activity 
ADD constraint pms_order_budget_production_process_activity_fk_03
FOREIGN KEY (id_production_process_activity) 
REFERENCES pms_production_process_activity (id);

create sequence pms_order_budget_production_process_activity_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_order_budget_production_process_activity.id;


create or replace function p_i_pms_order_budget_production_process_activity
(
pid_order_budget_product			 	bigint,
pid_production_process				bigint,
pid_production_process_activity		bigint,
pcreation_user	 		   			varchar
)
returns bigint as
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
	RAISE INFO 'executing p_i_pms_order_budget_production_process_activity';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vid_order_budget_production_process_activity			bigint;
	begin

		vid_order_budget_production_process_activity := nextval('pms_order_budget_production_process_activity_id_sq');
		insert into pms_order_budget_production_process_activity
		(id,
		id_order_budget_product,id_production_process,id_production_process_activity,creation_user)
		values
		(vid_order_budget_production_process_activity,
		pid_order_budget_product,pid_production_process,pid_production_process_activity,pcreation_user);

		return vid_order_budget_production_process_activity;

	end;

	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' or SQLSTATE 'P9998' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';




create table pms_order_budget_production_process_activity_raw_m(
id	   		 		   				bigint,
id_order_budget_production_process_activity	bigint not null,
id_raw_material_requirement				bigint not null,
id_raw_material_cost					bigint not null,
creation_user							varchar(50),
creation_date							timestamp not null default now()
);


ALTER TABLE pms_order_budget_production_process_activity_raw_m ADD CONSTRAINT
pms_order_budget_production_process_activity_raw_m_uk_01 UNIQUE 
(id_order_budget_production_process_activity,id_raw_material_requirement);

ALTER TABLE pms_order_budget_production_process_activity_raw_m 
ADD CONSTRAINT pms_order_budget_production_process_activity_raw_m_id_pk PRIMARY KEY (id);


ALTER TABLE pms_order_budget_production_process_activity_raw_m 
ADD constraint pms_order_budget_production_process_activity_raw_m_fk_01
FOREIGN KEY (id_order_budget_production_process_activity) 
REFERENCES pms_order_budget_production_process_activity (id);

ALTER TABLE pms_order_budget_production_process_activity_raw_m 
ADD constraint pms_order_budget_production_process_activity_raw_m_fk_02
FOREIGN KEY (id_raw_material_requirement) 
REFERENCES pms_raw_material_requirement (id);

ALTER TABLE pms_order_budget_production_process_activity_raw_m 
ADD constraint pms_order_budget_production_process_activity_raw_m_fk_05
FOREIGN KEY (id_raw_material_cost) 
REFERENCES pms_raw_material_cost (id);

create sequence pms_order_budget_production_process_activity_raw_m_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_order_budget_production_process_activity_raw_m.id;


create or replace function p_i_pms_order_budget_production_process_activity_raw_m
(
pid_order_budget_production_process_activity	 	bigint,
pid_raw_material_requirement					bigint,
pid_raw_material_cost						bigint,
pcreation_user	 		   			varchar
)
returns void as
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
	RAISE INFO 'executing p_i_pms_order_budget_production_process_activity_raw_m';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vid_order_budget_production_process_activity_raw_m			bigint;
	begin

		vid_order_budget_production_process_activity_raw_m := nextval('pms_order_budget_production_process_activity_raw_m_id_sq');
		insert into pms_order_budget_production_process_activity_raw_m
		(id,
		id_order_budget_production_process_activity,
		id_raw_material_requirement,id_raw_material_cost,creation_user)
		values
		(vid_order_budget_production_process_activity_raw_m,
		pid_order_budget_production_process_activity,
		pid_raw_material_requirement,pid_raw_material_cost,pcreation_user);

	end;
	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' or SQLSTATE 'P9998' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



create table pms_order_budget_production_process_activity_machine(
id	   		 		   				bigint,
id_order_budget_production_process_activity	bigint not null,
id_machine_requirement					bigint not null,
id_machine_use_cost						bigint not null,
creation_user							varchar(50),
creation_date							timestamp not null default now()
);

ALTER TABLE pms_order_budget_production_process_activity_machine ADD CONSTRAINT
pms_order_budget_production_process_activity_machine_uk_01 UNIQUE 
(id_order_budget_production_process_activity,id_machine_requirement);

ALTER TABLE pms_order_budget_production_process_activity_machine 
ADD CONSTRAINT pms_order_budget_production_process_activity_machine_id_pk PRIMARY KEY (id);


ALTER TABLE pms_order_budget_production_process_activity_machine 
ADD constraint pms_order_budget_production_process_activity_machine_fk_01
FOREIGN KEY (id_order_budget_production_process_activity) 
REFERENCES pms_order_budget_production_process_activity (id);

ALTER TABLE pms_order_budget_production_process_activity_machine 
ADD constraint pms_order_budget_production_process_activity_machine_fk_02
FOREIGN KEY (id_machine_requirement) 
REFERENCES pms_machine_requirement (id);

ALTER TABLE pms_order_budget_production_process_activity_machine 
ADD constraint pms_order_budget_production_process_activity_machine_fk_03
FOREIGN KEY (id_machine_use_cost) 
REFERENCES pms_machine_use_cost (id);

create sequence pms_order_budget_production_process_activity_machine_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_order_budget_production_process_activity_machine.id;


create or replace function p_i_pms_order_budget_production_process_activity_machine
(
pid_order_budget_production_process_activity	 	bigint,
pid_machine_requirement					bigint,
pid_machine_use_cost						bigint,
pcreation_user	 		   			varchar
)
returns void as
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
	RAISE INFO 'executing p_i_pms_order_budget_production_process_activity_machine';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vid_order_budget_production_process_activity_machine			bigint;
	begin

		vid_order_budget_production_process_activity_machine := nextval('pms_order_budget_production_process_activity_machine_id_sq');
		insert into pms_order_budget_production_process_activity_machine
		(id,
		id_order_budget_production_process_activity,
		id_machine_requirement,id_machine_use_cost,creation_user)
		values
		(vid_order_budget_production_process_activity_machine,
		pid_order_budget_production_process_activity,
		pid_machine_requirement,pid_machine_use_cost,pcreation_user);

	end;
	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' or SQLSTATE 'P9998' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



create table pms_order_budget_production_process_activity_manpower(
id	   		 		   				bigint,
id_order_budget_production_process_activity	bigint not null,
id_manpower_requirement					bigint not null,
id_manpower_cost						bigint not null,
creation_user							varchar(50),
creation_date							timestamp not null default now()
);

ALTER TABLE pms_order_budget_production_process_activity_manpower ADD CONSTRAINT
pms_order_budget_production_process_activity_manpower_uk_01 UNIQUE 
(id_order_budget_production_process_activity,id_manpower_requirement);

ALTER TABLE pms_order_budget_production_process_activity_manpower 
ADD CONSTRAINT pms_order_budget_production_process_activity_manpower_id_pk PRIMARY KEY (id);


ALTER TABLE pms_order_budget_production_process_activity_manpower 
ADD constraint pms_order_budget_production_process_activity_manpower_fk_01
FOREIGN KEY (id_order_budget_production_process_activity) 
REFERENCES pms_order_budget_production_process_activity (id);

ALTER TABLE pms_order_budget_production_process_activity_manpower 
ADD constraint pms_order_budget_production_process_activity_manpower_fk_02
FOREIGN KEY (id_manpower_requirement) 
REFERENCES pms_man_power_requirement (id);

ALTER TABLE pms_order_budget_production_process_activity_manpower 
ADD constraint pms_order_budget_production_process_activity_manpower_fk_03
FOREIGN KEY (id_manpower_cost) 
REFERENCES pms_manpower_cost (id);


create sequence pms_order_budget_production_process_activity_manpower_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_order_budget_production_process_activity_manpower.id;


create or replace function p_i_pms_order_budget_production_process_activity_manpower
(
pid_order_budget_production_process_activity	 	bigint,
pid_manpower_requirement					bigint,
pid_manpower_cost						bigint,
pcreation_user	 		   			varchar
)
returns void as
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
	RAISE INFO 'executing p_i_pms_order_budget_production_process_activity_manpower';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vid_order_budget_production_process_activity_manpower			bigint;
	begin

		vid_order_budget_production_process_activity_manpower := nextval('pms_order_budget_production_process_activity_manpower_id_sq');
		insert into pms_order_budget_production_process_activity_manpower
		(id,
		id_order_budget_production_process_activity,
		id_manpower_requirement,id_manpower_cost,creation_user)
		values
		(vid_order_budget_production_process_activity_manpower,
		pid_order_budget_production_process_activity,
		pid_manpower_requirement,pid_manpower_cost,pcreation_user);

	end;
	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' or SQLSTATE 'P9998' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';


/* ***************************************** ********************************** ******************** */
/* ***************************************** ********************************** ******************** */
/* ***************************************** ********************************** ******************** */
/* ***************************************** ********************************** ******************** */


create or replace function f_determinate_product_price_identifiying_order
(
porder_identifier_number	 		   			bigint,
pid_product	   		 		   			bigint,
porder_id_currency 		 		   			bigint,
pcreation_user								varchar
)
returns numeric as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;

	v_production_process_id						pms_production_process.id%type;
	v_production_process_activity_id				pms_production_process_activity.id%type;
	v_raw_material_cost_record_id					pms_raw_material_cost.id%type;

begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing f_determinate_product_price_identifiying_order';
	RAISE INFO '--------------------------';
	
	/*BEGIN VALIDATIONS*/
	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select prodp.id, product.product_id 
		into vrecord
		from 
		pms_product product left outer join
		pms_production_process prodp 
		on product.id = prodp.id_product 
		where product.id = pid_product
		and prodp.validity_end_date is null;
		
		RAISE INFO '--------------------------';
		RAISE INFO 'vrecord.product_id: %',vrecord.product_id;
		RAISE INFO '--------------------------';
		
		if vrecord.id is null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.determinate.product.price.no.valid.production.process.for.product.error'||''||vrecord.product_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		else
			v_production_process_id:=vrecord.id;
		end if;
	end;

	RAISE INFO '--------------------------';
	RAISE INFO 'v_production_process_id: %',v_production_process_id;
	RAISE INFO '--------------------------';

	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select prod.product_id,pp.process_id,count(ppa.id) process_activities_count
			into vrecord
		from 
			pms_product prod join
			pms_production_process pp
			on pp.id_product = prod.id 
			left outer join
			pms_production_process_activity ppa
			on ppa.id_production_process = pp.id			
		where	
			pp.id = v_production_process_id			
			and pp.validity_end_date is null			
			and ppa.validity_end_date is null
			group by prod.product_id,pp.process_id;

		if vrecord.process_activities_count = 0 then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.determinate.product.price.no.valid.production.activities.for.process.error'||''||vrecord.process_id||''||vrecord.product_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end if;
	end;
	
	declare
		vcursor cursor for
		select * from f_ordered_production_process_activities(v_production_process_id);		
		v_process_acumulated_raw_material_cost			pms_raw_material_cost.tariff_amount%type;
		v_process_acumulated_man_power_cost			pms_manpower_cost.tariff_amount%type;
		v_process_acumulated_machine_use_cost			pms_machine_use_cost.tariff_amount%type;
		
		vid_order_budget_product						bigint;
		vid_order_budget_production_process_activity		bigint;
	begin
		v_process_acumulated_raw_material_cost:=0;
		v_process_acumulated_man_power_cost:=0;
		v_process_acumulated_machine_use_cost:=0;

		vid_order_budget_product := p_i_pms_order_budget_product(porder_identifier_number,pid_product,pcreation_user);
		
		for vrecord in vcursor loop	
			if vid_order_budget_product is not null then
				vid_order_budget_production_process_activity := p_i_pms_order_budget_production_process_activity(vid_order_budget_product,vrecord.id_production_process,vrecord.id,pcreation_user);		
			end if;
			
			v_process_acumulated_raw_material_cost:= v_process_acumulated_raw_material_cost + f_calculate_activity_raw_materials_cost_identifiying_order(vid_order_budget_production_process_activity,vrecord.id,porder_id_currency,pcreation_user);
			v_process_acumulated_machine_use_cost:= v_process_acumulated_machine_use_cost + f_calculate_activity_machine_use_cost_identifiying_order(vid_order_budget_production_process_activity,vrecord.id,porder_id_currency,pcreation_user);
			v_process_acumulated_man_power_cost:= v_process_acumulated_man_power_cost + f_calculate_activity_man_power_cost_identifiying_order(vid_order_budget_production_process_activity,vrecord.id,porder_id_currency,pcreation_user);
		end loop;

		return (v_process_acumulated_raw_material_cost + v_process_acumulated_machine_use_cost + v_process_acumulated_man_power_cost);
	end;

			
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';
