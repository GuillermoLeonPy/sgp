package py.com.kyron.sgp.gui.view.utils;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceItemDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderItemDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class TableNumericColumnCellLabelHelper {

	private static final Logger logger = LoggerFactory.getLogger(TableNumericColumnCellLabelHelper.class);
	private static Map<String,String> messages;	
	public TableNumericColumnCellLabelHelper() {
		// TODO Auto-generated constructor stub		
	}

	public static Label buildNumericLabelByClass(Object number){
		
		try{
			if(number == null)
				return buildNumericLabelByLongNumber(0L);
			else if(number instanceof Long)
				return buildNumericLabelByLongNumber((Long)number);
			else if(number instanceof BigDecimal)
				return buildNumericLabelByBigDecimalNumber((BigDecimal)number);
			else
				throw new PmsServiceException("application.common.gui.exception.by.programmer", null, null);
		}catch(Exception e){
			logger.error("\n error", e);
			return null;
		}
	}
	
	private static Label buildNumericLabelByLongNumber(Long number){
		final ObjectProperty<Long> property =	new ObjectProperty<Long>(number);
		return new Label(property);
	}
	
	public static Label buildNumericLabelByBigDecimalNumber(BigDecimal number){
		final ObjectProperty<BigDecimal> property =	new ObjectProperty<BigDecimal>(number);
		return new Label(property);
	}
	
	public static HorizontalLayout buildValueAddedTaxAmountLabel(final OrderItemDTO vOrderItemDTO){
		if(messages == null)prepareMessages();
		final ObjectProperty<String> property;
		final Label taxQuantityIndicator;
		if(vOrderItemDTO.getValue_added_tax_10_unit_price_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.10.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vOrderItemDTO.getValue_added_tax_10_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else if(vOrderItemDTO.getValue_added_tax_5_unit_price_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.5.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vOrderItemDTO.getValue_added_tax_5_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else{
			property =	new ObjectProperty<String>(messages.get("application.common.excempt.tax.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vOrderItemDTO.getValue_added_tax_5_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}
	}
	
	public static HorizontalLayout buildValueAddedTaxAmountLabel(final SaleInvoiceItemDTO vSaleInvoiceItemDTO){
		if(messages == null)prepareMessages();
		final ObjectProperty<String> property;
		final Label taxQuantityIndicator;
		if(vSaleInvoiceItemDTO.getValue_added_tax_10_unit_price_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.10.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoiceItemDTO.getValue_added_tax_10_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else if(vSaleInvoiceItemDTO.getValue_added_tax_5_unit_price_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.5.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoiceItemDTO.getValue_added_tax_5_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else{
			property =	new ObjectProperty<String>(messages.get("application.common.excempt.tax.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoiceItemDTO.getValue_added_tax_5_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}
	}
	
	
	public static HorizontalLayout buildValueAddedTaxAmountLabel(final CreditNoteItemDTO vCreditNoteItemDTO){
		if(messages == null)prepareMessages();
		final ObjectProperty<String> property;
		final Label taxQuantityIndicator;
		if(vCreditNoteItemDTO.getValue_added_tax_10_unit_price_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.10.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vCreditNoteItemDTO.getValue_added_tax_10_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else if(vCreditNoteItemDTO.getValue_added_tax_5_unit_price_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.5.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vCreditNoteItemDTO.getValue_added_tax_5_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else{
			property =	new ObjectProperty<String>(messages.get("application.common.excempt.tax.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vCreditNoteItemDTO.getValue_added_tax_5_unit_price_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}
	}
	
	
	public static HorizontalLayout buildValueAddedTaxAmountLabel(final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO){
		if(messages == null)prepareMessages();
		final ObjectProperty<String> property;
		final Label taxQuantityIndicator;
		if(vPurchaseInvoiceItemDTO.getValue_added_tax_10_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.10.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceItemDTO.getValue_added_tax_10_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else if(vPurchaseInvoiceItemDTO.getValue_added_tax_5_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.5.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceItemDTO.getValue_added_tax_5_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else{
			property =	new ObjectProperty<String>(messages.get("application.common.excempt.tax.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceItemDTO.getExempt_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}
	}
	
	public static HorizontalLayout buildValueAddedTaxAmountLabel(final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO){
		if(messages == null)prepareMessages();
		final ObjectProperty<String> property;
		final Label taxQuantityIndicator;
		if(vPurchaseInvoiceCreditNoteItemDTO.getValue_added_tax_10_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.10.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceCreditNoteItemDTO.getValue_added_tax_10_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else if(vPurchaseInvoiceCreditNoteItemDTO.getValue_added_tax_5_amount() != null){
			property =	new ObjectProperty<String>(messages.get("application.common.tax.5.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceCreditNoteItemDTO.getValue_added_tax_5_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}else{
			property =	new ObjectProperty<String>(messages.get("application.common.excempt.tax.indicator.label"));
			taxQuantityIndicator = new Label(property);
			taxQuantityIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout(
					taxQuantityIndicator,
					TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceCreditNoteItemDTO.getExempt_amount()));
			vHorizontalLayout.setComponentAlignment(taxQuantityIndicator, Alignment.MIDDLE_LEFT);
			vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(1), Alignment.MIDDLE_RIGHT);
			vHorizontalLayout.setSpacing(true);
			return vHorizontalLayout;
		}
	}
	private static void prepareMessages(){
		try {
			messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale("xxx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("\n error",e);
		}
	}
}
