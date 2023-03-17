/* 42-UC-GEN-PAG-FAC-201610131124 */

create table pms_sale_invoice_payment(
id	   		 		   			bigint,
id_sale_invoice		   			bigint NOT NULL,
id_currency	 				bigint NOT NULL,
status							varchar(50) not null default 'application.common.status.pending',
amount							numeric(11,2) NOT NULL,
sale_invoice_balance				numeric(11,2) NOT NULL,--SALDO DE FACTURA DE VENTA
payment_number			   			bigint NOT NULL,
registration_date					timestamp not null default now(),
payment_due_date					timestamp not null,
cancellation_date   				timestamp,
annulment_date   					timestamp,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);

--alter table pms_sale_invoice_payment add id_currency	 bigint NOT NULL;

ALTER TABLE pms_sale_invoice_payment ADD CONSTRAINT pms_sale_invoice_payment_id_pk PRIMARY KEY (id);

/*ALTER TABLE pms_sale_invoice_payment ADD CONSTRAINT
pms_sale_invoice_payment_uk_01 UNIQUE (id_sale_invoice,payment_number);
NO APLICA POR LA REGENERACION DE FACTURA Y POSTERIOR REQUERIMIENTO DE RE GENERACION DE PAGOS
*/


ALTER TABLE pms_sale_invoice_payment 
ADD constraint pms_sale_invoice_payment_fk_01
FOREIGN KEY (id_sale_invoice) REFERENCES pms_sale_invoice (id);

ALTER TABLE pms_sale_invoice_payment 
ADD constraint pms_sale_invoice_payment_fk_02
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

/*for payment regeneration*/
ALTER TABLE pms_sale_invoice_payment ADD CONSTRAINT 
chk_cancellation_date CHECK (cancellation_date >= registration_date);

ALTER TABLE pms_sale_invoice_payment ADD CONSTRAINT 
chk_annulment_date CHECK (annulment_date > registration_date);

/*for payment regeneration*/
ALTER TABLE pms_sale_invoice_payment ADD CONSTRAINT 
chk_payment_due_date CHECK (payment_due_date >= registration_date);

ALTER TABLE pms_sale_invoice_payment ADD CONSTRAINT chk_amount CHECK (amount >= 0);

ALTER TABLE pms_sale_invoice_payment ADD CONSTRAINT chk_sale_invoice_balance CHECK (sale_invoice_balance >= 0);


create sequence pms_sale_invoice_payment_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_payment.id;


create table pms_sale_invoice_payment_cancel_documents(
id	   		 		   			bigint,
id_sale_invoice_payment 				bigint not null,
id_credit_note	   					bigint,
resulting_payment_amount				numeric(11,2) NOT NULL,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);


/* un pago por factura de venta puede ser cancelado por mas de una nota de credito */

--ALTER TABLE pms_sale_invoice_payment_cancel_documents add resulting_payment_amount numeric(11,2) NOT NULL;
--ALTER TABLE pms_sale_invoice_payment_cancel_documents 
--ADD CONSTRAINT chk_resulting_payment_amount check (resulting_payment_amount >= 0);

ALTER TABLE pms_sale_invoice_payment_cancel_documents 
ADD CONSTRAINT pms_sale_invoice_payment_cancel_documents_id_pk PRIMARY KEY (id);


ALTER TABLE pms_sale_invoice_payment_cancel_documents 
ADD constraint pms_sale_invoice_payment_cancel_documents_fk_01
FOREIGN KEY (id_sale_invoice_payment) REFERENCES pms_sale_invoice_payment (id);

ALTER TABLE pms_sale_invoice_payment_cancel_documents 
ADD constraint pms_sale_invoice_payment_cancel_documents_fk_02
FOREIGN KEY (id_credit_note) REFERENCES pms_credit_note(id);


create sequence pms_sale_invoice_payment_cancel_documents_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_payment_cancel_documents.id;




select sum(amount) from pms_sale_invoice_payment
select total_amount from pms_sale_invoice
select p_i_pms_sale_invoice_payment(34,'xxx');

create or replace function p_i_pms_sale_invoice_payment
(
pid_sale_invoice	 				bigint,
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
	RAISE INFO 'executing p_i_pms_sale_invoice_payment';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	/* determinate how many payments should be created by sale invoice payment condition */
	declare
		vsale_invoice_record			record;
		vorder_record					record;
		vsale_invoice_payment_record		record;
		vsale_invoice_payment_amount		pms_sale_invoice_payment.amount%type;
		vpayment_due_date				timestamp;
	begin
		select si.* into vsale_invoice_record from pms_sale_invoice si where si.id = pid_sale_invoice;

		/* check the sale invoice status: ¿in how many status values can the payments be generated? */


		if vsale_invoice_record.payment_condition = 'application.common.payment.condition.credit' then
			select o.* into vorder_record from pms_order o where o.id = vsale_invoice_record.id_order;
			/* determinate the payments amount: 1st: 50 % 
			Sucesives: (sale invoice total amount - 1st payment amount) / order.credit_order_fee_quantity */
			for vcounter in 1..vorder_record.credit_order_fee_quantity loop
				if vcounter = 1 then

					vpayment_due_date := f_determinate_due_date((now())::timestamp);
					insert into pms_sale_invoice_payment 
					(id,id_sale_invoice,id_currency,amount,
					sale_invoice_balance,payment_number,registration_date,payment_due_date,
					creation_user,creation_date,status) 
					values
					(nextval('pms_sale_invoice_payment_id_sq'),vsale_invoice_record.id,vsale_invoice_record.id_currency,trunc((vsale_invoice_record.total_amount / 2),2),
					trunc((vsale_invoice_record.total_amount / 2),2),vcounter,now(),vpayment_due_date,
					pcreation_user,now(),'application.common.status.in.progress');
					/* the sucesives payments amount */
					vsale_invoice_payment_amount := trunc(((trunc((vsale_invoice_record.total_amount / 2),2) / (vorder_record.credit_order_fee_quantity - 1))),2);
				else
					select sip.* into vsale_invoice_payment_record
					from pms_sale_invoice_payment sip where sip.id = 
					(select max(id) from pms_sale_invoice_payment where id_sale_invoice = vsale_invoice_record.id);

					vpayment_due_date := f_determinate_due_date((vsale_invoice_payment_record.payment_due_date + (vorder_record.credit_order_fee_periodicity_days_quantity * interval '1 day'))::timestamp);
					
					insert into pms_sale_invoice_payment 
					(id,id_sale_invoice,id_currency,amount,
					sale_invoice_balance,payment_number,registration_date,payment_due_date,
					creation_user,creation_date) 
					values
					(nextval('pms_sale_invoice_payment_id_sq'),vsale_invoice_record.id,vsale_invoice_record.id_currency,vsale_invoice_payment_amount,
					(vsale_invoice_payment_record.sale_invoice_balance - vsale_invoice_payment_amount),vcounter,now(),vpayment_due_date,
					pcreation_user,now());
					
				end if;--if vcounter = 1 then
			end loop;--for vcounter in 1..vorder_record.credit_order_fee_quantity loop
		else--if vsale_invoice_record.payment_condition = 'application.common.payment.condition.credit' then
			/* payment condition cash */
			vpayment_due_date := f_determinate_due_date((now())::timestamp);
			insert into pms_sale_invoice_payment 
				(id,id_sale_invoice,id_currency,amount,
				sale_invoice_balance,payment_number,registration_date,payment_due_date,
				creation_user,creation_date,status) 
			values
				(nextval('pms_sale_invoice_payment_id_sq'),vsale_invoice_record.id,vsale_invoice_record.id_currency,vsale_invoice_record.total_amount,
				0,1,now(),vpayment_due_date,
				pcreation_user,now(),'application.common.status.in.progress');
		end if;--if vsale_invoice_record.payment_condition = 'application.common.payment.condition.credit' then

		/* update sale invoice status */
		update pms_sale_invoice
			set status = 'application.common.status.payment.in.progress',
				last_modif_user = pcreation_user,
				last_modif_date = now()
			where id = vsale_invoice_record.id;			
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

select f_determinate_due_date((now() + interval '3 day')::timestamp)
select f_determinate_due_date((now())::timestamp)
select f_determinate_due_date((now() + interval trim(to_char(3,'999'))||' day')::timestamp);
select f_determinate_due_date((now() + interval to_char(3,'9'))||' day')::timestamp);
select f_determinate_due_date((now() + interval (to_char(3,'9')||' day'))::timestamp);
select trim(to_char(3,'999'))
select trim(to_char(3,'999'))||' day'

create or replace function f_determinate_due_date
(
pdue_date	 				timestamp
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
	RAISE INFO 'executing f_determinate_due_date';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	/* The day of the week as Monday( 1 ) to Sunday( 7 ) */

	declare
		vday_of_week			bigint;
		vdue_date_truncated		timestamp;
		vfinal_due_date		timestamp;
	begin
		vdue_date_truncated := date_trunc('day',pdue_date) + interval '23:59:59';
		vday_of_week := EXTRACT(ISODOW FROM vdue_date_truncated);
		if vday_of_week = 7 then
			vfinal_due_date := (vdue_date_truncated + interval '1 day')::timestamp;
		else
			vfinal_due_date := vdue_date_truncated;
		end if;
		
		return vfinal_due_date;
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



select f_determinate_due_date_test();
create or replace function f_determinate_due_date_test()
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
	RAISE INFO 'executing f_determinate_due_date_test';
	RAISE INFO '--------------------------';

	declare
		vdays_count			bigint;
		vfinal_due_date		timestamp;
	begin
		vdays_count := 3;
		vfinal_due_date := ((now() + (vdays_count * interval '1 day'))::timestamp);
		return vfinal_due_date;
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';
