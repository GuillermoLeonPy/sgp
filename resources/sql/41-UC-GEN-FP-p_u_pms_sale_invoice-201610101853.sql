/*41-UC-GEN-FP-p_u_pms_sale_invoice-201610101853*/

create or replace function p_u_pms_sale_invoice
(
pid	   		 		   			bigint,
pid_person						bigint,
pid_currency						bigint,
ppayment_condition					varchar,
ptotal_amount						numeric,
pexempt_amount						numeric,
pvalue_added_amount					numeric,
pvalue_added_tax_10_amount			numeric,
pvalue_added_tax_5_amount			numeric,
ptotal_tax_amount					numeric,
ptax_10_amount						numeric,
ptax_5_amount						numeric,
pbussines_name						varchar,
pbussines_ci_ruc					varchar,
plast_modif_user					varchar,
pcancellation_date					timestamp,
pannulment_date					timestamp,
pannulment_reason_description			varchar
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
	RAISE INFO 'executing p_i_pms_sale_invoice';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */		
		
		update pms_sale_invoice
			set 
				id_person = pid_person,
				id_currency = pid_currency,
				payment_condition = ppayment_condition,
				total_amount = ptotal_amount,				
				exempt_amount = pexempt_amount,
				value_added_amount = pvalue_added_amount,
				value_added_tax_10_amount = pvalue_added_tax_10_amount,
				value_added_tax_5_amount = pvalue_added_tax_5_amount,
				total_tax_amount = ptotal_tax_amount,
				tax_10_amount = ptax_10_amount,
				tax_5_amount = ptax_5_amount,
				bussines_name = pbussines_name,
				bussines_ci_ruc = pbussines_ci_ruc,
				last_modif_user = plast_modif_user,
				last_modif_date = now(),
				cancellation_date = pcancellation_date,
				annulment_date = pannulment_date,
				annulment_reason_description = pannulment_reason_description,
				status = pstatus
			where
				id = pid;

	
	
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