package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;

public interface MeasurmentUnitDAO {
	public Long pmsMeasurmentUnitIdBySequence();
	public void insertMeasurmentUnitDTO(MeasurmentUnitDTO MeasurmentUnitDTO);
	public List<MeasurmentUnitDTO> listMeasurmentUnitDTO(MeasurmentUnitDTO MeasurmentUnitDTO);

}
