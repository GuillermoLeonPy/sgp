package py.com.kyron.sgp.persistence.comercialmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CreditOrderChargeConditionDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderItemRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderItemRawMaterialSufficiencyReportDetailDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderRawMaterialSufficiencyReportDTO;

public interface OrderDAO {

	public Long pmsOrderDTOIdBySequence();
	public Long pmsOrderDTOIdentifierNumberBySequence();
	public void insertOrderDTO(OrderDTO OrderDTO);
	public List<OrderDTO> listOrderDTO(OrderDTO OrderDTO);
	public void updateOrderDTO(OrderDTO OrderDTO);

	public Long pmsCreditOrderChargeConditionDTOIdBySequence();
	public void insertCreditOrderChargeConditionDTO(CreditOrderChargeConditionDTO CreditOrderChargeConditionDTO);
	public void updateCreditOrderChargeConditionDTO(CreditOrderChargeConditionDTO CreditOrderChargeConditionDTO);
	public List<CreditOrderChargeConditionDTO> listCreditOrderChargeConditionDTO(CreditOrderChargeConditionDTO CreditOrderChargeConditionDTO);
	public Long getCreditOrderChargeConditionValidRowId();
	
	public OrderRawMaterialSufficiencyReportDTO getOrderRawMaterialSufficiencyReportDTO(OrderDTO orderDTO);
	public List<OrderItemRawMaterialSufficiencyReportDTO> listOrderItemRawMaterialSufficiencyReportDTO(OrderItemRawMaterialSufficiencyReportDTO OrderItemRawMaterialSufficiencyReportDTO);
	public List<OrderItemRawMaterialSufficiencyReportDetailDTO> listOrderItemRawMaterialSufficiencyReportDetailDTO(OrderItemRawMaterialSufficiencyReportDetailDTO OrderItemRawMaterialSufficiencyReportDetailDTO);
	public List<OrderItemRawMaterialSufficiencyReportDetailDTO> listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder(Long id_order);
}
