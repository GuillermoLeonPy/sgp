package py.com.kyron.sgp.persistence.productionmanagement.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.productionmanagement.domain.TariffDTO;

public interface TariffDAO {
	public Long pmsTariffDTOIdBySequence();
	public void insertTariffDTO(TariffDTO TariffDTO);
	public List<TariffDTO> listTariffDTO(TariffDTO TariffDTO);
}
