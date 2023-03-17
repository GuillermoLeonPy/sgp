package py.com.kyron.sgp.persistence.cash.movements.management.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.BranchOfficeDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.BranchOfficeSaleStationDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceItemDTO;

public interface SaleInvoiceDAO {

	public Long pmsSaleInvoiceDTOIdBySequence();
	public void insertSaleInvoiceDTO(SaleInvoiceDTO SaleInvoiceDTO);
	public void reGenerateSaleInvoiceDTO(SaleInvoiceDTO SaleInvoiceDTO);
	public void annulSaleInvoiceDTO(SaleInvoiceDTO SaleInvoiceDTO);
	public void updateSaleInvoiceDTO(SaleInvoiceDTO SaleInvoiceDTO);
	public List<SaleInvoiceDTO> listSaleInvoiceDTO(SaleInvoiceDTO SaleInvoiceDTO);
	/**/
	public List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO(SaleInvoiceItemDTO SaleInvoiceItemDTO);
	public void updateSaleInvoiceItemDTO(SaleInvoiceItemDTO SaleInvoiceItemDTO);
	/**/
	public List<BranchOfficeDTO> listBranchOfficeDTO(BranchOfficeDTO BranchOfficeDTO);
	public List<BranchOfficeSaleStationDTO> listBranchOfficeSaleStationDTO(BranchOfficeSaleStationDTO BranchOfficeSaleStationDTO);
	
	public Date getSaleInvoiceStampingEffectiveBeginningDate(SaleInvoiceDTO SaleInvoiceDTO);
	public Date getSaleInvoiceStampingEffectiveEndDate(SaleInvoiceDTO SaleInvoiceDTO);
	
	public BigDecimal getSaleInvoiceStampingNumber(SaleInvoiceDTO SaleInvoiceDTO);
}
