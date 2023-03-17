package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;

public interface CreditNoteItemDTOTableFunctions {
	public String getCreditNoteDTOStatus();
	public Long getCreditNoteDTOId();
	public void editCancellationWithdrawalQuantity(CreditNoteItemDTO vCreditNoteItemDTO);
	public void showDeliveredUnitsListToSelectDevolutionUnits(CreditNoteItemDTO vCreditNoteItemDTO, boolean query) throws PmsServiceException;
	public void reBuildTableAndTotalsPanel();
	public void deleteCreditNoteItemDTOFromPreliminaryList(final CreditNoteItemDTO vCreditNoteItemDTO);
}
