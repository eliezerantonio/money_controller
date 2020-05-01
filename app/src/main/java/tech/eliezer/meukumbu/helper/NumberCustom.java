package tech.eliezer.meukumbu.helper;

import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberCustom {

    public static String formatarNumero(double numero) {

        Locale locale = new Locale("pt", "br");
        String pattern = "###,###.00";

        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);

        String format = decimalFormat.format(numero);

        return format;
    }



}
