package py.com.kyron.sgp.persistence.cash.movements.management.dao;

import java.math.BigDecimal;
import java.util.List;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;

public interface CreditNoteDAO {

	public Long pmsCreditNoteDTOIdBySequence();
	public void insertCreditNoteDTO(CreditNoteDTO CreditNoteDTO);
	public List<CreditNoteDTO> listCreditNoteDTO(CreditNoteDTO CreditNoteDTO);
	public Long pmsCreditNoteItemDTOIdBySequence(long id);
	public void insertCreditNoteItemDTO(CreditNoteItemDTO CreditNoteItemDTO);
	public List<CreditNoteItemDTO> listCreditNoteItemDTO(CreditNoteItemDTO CreditNoteItemDTO);
	
	public BigDecimal getCreditNoteStampingNumber(CreditNoteDTO CreditNoteDTO);
}
