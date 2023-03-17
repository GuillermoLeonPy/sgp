select unit_price_amount, product_unit_manufacture_cost from pms_order_item


create or replace function correct_and_delete_this_function
()
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
	RAISE INFO 'executing correct_and_delete_this_function';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		v_order_cursor_x cursor for
		select * from pms_order;
	begin
		for vorder_record in v_order_cursor_x loop
			declare
				vitem_cursor cursor for
				select * from pms_order_item where id_order = vorder_record.id;
			begin

				for vitem_record in vitem_cursor loop
					update pms_order_item set 
					product_unit_manufacture_cost = 
						f_determinate_product_price_identifiying_order
						(vorder_record.identifier_number,
						vitem_record.id_product,
						vorder_record.id_currency,
						null);
				end loop;
			end;
		end loop;
		
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