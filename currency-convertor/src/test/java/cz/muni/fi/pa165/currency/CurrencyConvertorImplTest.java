package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;

import java.math.BigDecimal;
import java.util.Currency;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyConvertorImplTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency INR = Currency.getInstance("INR");


    private final ExchangeRateTable exchangeRateTable = mock(ExchangeRateTable.class);
    private final CurrencyConvertor currencyConvertor= new CurrencyConvertorImpl(exchangeRateTable);



    @Test
    public void testConvert() throws ExternalServiceFailureException {
        // Don't forget to test border values and proper rounding.
        when(exchangeRateTable.getExchangeRate(USD, INR)).thenReturn(new BigDecimal("73.152"));

        assertThat(currencyConvertor.convert(USD,INR,new BigDecimal("1.50")))
                .isEqualTo(new BigDecimal("109.73"));

    }

    @Test
    public void testConvertWithNullSourceCurrency() throws IllegalArgumentException,ExternalServiceFailureException{

       // IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Didnt find source currency in arguments");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(null, INR, BigDecimal.ONE))
                .withMessage("Didnt find source currency in arguments");

    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        //IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Didnt find target currency in arguments");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(USD, null, BigDecimal.ONE))
                .withMessage("Didnt find target currency in arguments");

    }

    @Test
    public void testConvertWithNullSourceAmount() {
        //IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Didnt find source Amount in arguments");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(USD, INR, null))
                .withMessage("Didnt find source Amount in arguments");

    }

    @Test
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException{
        when(exchangeRateTable.getExchangeRate(USD, INR)).thenReturn(null);

        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> currencyConvertor.convert(USD, INR, BigDecimal.ONE))
                .withMessage("The lookup for exchange rate found nothing");
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        ExternalServiceFailureException externalServiceFailureException = new ExternalServiceFailureException("external error");

        when(exchangeRateTable.getExchangeRate(USD, INR)).thenThrow(externalServiceFailureException);

        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> currencyConvertor.convert(USD, INR, BigDecimal.ONE))
                .withMessage("Exchange rate table is unknown")
                .withCause(externalServiceFailureException);


    }

}
