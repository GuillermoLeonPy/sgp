package py.com.kyron.sgp.persistence.comercialmanagement.dao;

import java.math.BigDecimal;
import java.util.List;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;

public interface CurrencyDAO {
	public Long pmsCurrencyIdBySequence();
	public void insert(CurrencyDTO currencyDTO);
	public void update(CurrencyDTO currencyDTO);
	public List<CurrencyDTO> listCurrencyDTO(CurrencyDTO currencyDTO);

	public Long pmsCurrencyExchangeRateDTOIdBySequence();
	public void insertCurrencyExchangeRateDTO(CurrencyExchangeRateDTO CurrencyExchangeRateDTO);
	public void updateCurrencyExchangeRateDTO(CurrencyExchangeRateDTO CurrencyExchangeRateDTO);
	public List<CurrencyExchangeRateDTO> listCurrencyExchangeRateDTO(CurrencyExchangeRateDTO CurrencyExchangeRateDTO);
	public Long getCurrencyExchangeRateValidRowId(Long id_currency);
	
	public BigDecimal convertCurrencyAmount(CurrencyDTO currencyDTO);
}
