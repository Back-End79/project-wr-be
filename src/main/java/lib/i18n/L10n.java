package lib.i18n;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import lombok.Getter;

@Getter
public enum L10n {

  ID(new Locale("id", "ID")),
  US(Locale.US),

  ;

  private final Locale locale;
  private final NumberFormat numberFormat;
  private final Currency currency;

  L10n(Locale locale) {
    this.locale = locale;
    this.numberFormat = NumberFormat.getCurrencyInstance(locale);
    this.currency = Currency.getInstance(locale);
  }

}
