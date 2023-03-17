package py.com.kyron.sgp.persistence.cash.movements.management.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.IncomeExpeditureReportDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.ManPowerExpenditurePerFunctionaryDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.ProductCostSaleComparisonDTO;

public interface CashMovementsReportDAO {

	public List<IncomeExpeditureReportDTO> incomeExpeditureReport(IncomeExpeditureReportDTO IncomeExpeditureReportDTO);
	public IncomeExpeditureReportDTO purchasesExpeditureReport(IncomeExpeditureReportDTO IncomeExpeditureReportDTO);
	public IncomeExpeditureReportDTO salesIncomeReport(IncomeExpeditureReportDTO IncomeExpeditureReportDTO);
	
	public List<ProductCostSaleComparisonDTO> productCostSaleComparisonReport(ProductCostSaleComparisonDTO ProductCostSaleComparisonDTO);
	
	public List<ManPowerExpenditurePerFunctionaryDTO> manPowerExpenditurePerFunctionaryReport(ManPowerExpenditurePerFunctionaryDTO ManPowerExpenditurePerFunctionaryDTO);
}
