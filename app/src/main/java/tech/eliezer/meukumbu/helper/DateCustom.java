package tech.eliezer.meukumbu.helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateCustom {


    public static String dataActual() {
        long date = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);

        return dataString;
    }


    public static String mesAnoDataEscolhida(String data) {
        String retornoData[] = data.split("/");
        String dia = retornoData[0];//dia 23
        String mes = retornoData[1];//dia 01
        String ano = retornoData[2];//dia 01

        String mesANo = mes + ano;

        return mesANo;

    }


}
