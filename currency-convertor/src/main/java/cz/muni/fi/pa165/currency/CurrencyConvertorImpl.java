package cz.muni.fi.pa165.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private ExchangeRateTable exchangeRateTable;
    //private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        BigDecimal retval = null;
        BigDecimal exchangeRate;

        if (sourceCurrency == null)
            throw new IllegalArgumentException("Didnt find source currency in arguments");
        if (targetCurrency == null)
            throw new IllegalArgumentException("Didnt find target currency in arguments");
        if (sourceAmount == null)
            throw new IllegalArgumentException("Didnt find source Amount in arguments");

        try {
            exchangeRate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
            if(exchangeRate == null)
                throw new UnknownExchangeRateException("The lookup for exchange rate found nothing");

            retval = sourceAmount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN);
        } catch (ExternalServiceFailureException e) {
            throw new UnknownExchangeRateException("Exchange rate table is unknown", e);
        }
        return retval;
    }

}
