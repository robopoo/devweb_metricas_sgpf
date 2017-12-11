package br.com.sgpf.metrica.core.util;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtil {

	public static String dateToString(Date value) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(value);
	}

	public static String dateToString(Date value, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(value);
	}

	public static Date stringToDate(String value, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(value);
		} catch (ParseException pe) {
			return null;
		}
	}

	public static Date stringToDate(String value) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.parse(value);
		} catch (ParseException pe) {
			return null;
		}
	}

	public static boolean equalsDate(Date d1, Date d2) {
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);

		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);

		if ((cal1.get(DATE) == cal2.get(DATE)) && (cal1.get(MONTH) == cal2.get(MONTH)) && (cal1.get(YEAR) == cal2.get(YEAR)))
			return true;

		return false;
	}

	public static int compareOnlyDate(Date d1, Date d2) {
		return zeraHorasEMinutos(d1).compareTo(zeraHorasEMinutos(d2));
	}

	public static int compareOnlyTime(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.set(Calendar.YEAR, 2000);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		c2.set(Calendar.DAY_OF_MONTH, 1);
		c2.set(Calendar.MONTH, Calendar.JANUARY);
		c2.set(Calendar.YEAR, 2000);

		return c1.getTime().compareTo(c2.getTime());
	}

	public static Date incrementaDias(Date data, Integer dias) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return new Date(cal.getTimeInMillis());
	}

	public static Date incrementaMes(Date data, Integer meses) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.MONTH, meses);
		return new Date(cal.getTimeInMillis());
	}

	public static Date formataData(Date data, String mascara) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(mascara);
		String sData = df.format(data);
		return df.parse(sData);
	}

	public static Date somaMaisUmAno(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 1);
		return c.getTime();
	}

	public static Date zeraHorasEMinutos(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTime();
	}

	public static final int getIdade(Date dataNascimento) {
		Calendar dataNasc = new GregorianCalendar();
		dataNasc.setTime(dataNascimento);
		Calendar hoje = Calendar.getInstance();
		int idade = hoje.get(Calendar.YEAR) - dataNasc.get(Calendar.YEAR);
		if (dataNasc.get(Calendar.MONTH) > hoje.get(Calendar.MONTH)) {
			idade--;
		} else if (dataNasc.get(Calendar.MONTH) == hoje.get(Calendar.MONTH)) {
			if (dataNasc.get(Calendar.DAY_OF_MONTH) > hoje.get(Calendar.DAY_OF_MONTH)) {
				idade--;
			}
		}
		return idade;
	}

	public static final boolean diferencaMaiorUmDia(Date dataInicio, Date dataFim) {
		long diff = dataFim.getTime() - dataInicio.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);
		if ((diffHours < 24) || (diffHours == 24 && diffMinutes == 0 && diffSeconds == 0))
			return false;
		else
			return true;
	}

	public static final int getDia(java.util.Date data) {
		SimpleDateFormat format = new SimpleDateFormat("dd");
		return Integer.parseInt(format.format(data));
	}

	public static final java.util.Date getUltimoDiaMes(java.util.Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, (-1 * getDia(cal.getTime())));
		cal.add(Calendar.DAY_OF_MONTH, 33);
		cal.add(Calendar.DAY_OF_MONTH, (-1 * getDia(cal.getTime())));
		return cal.getTime();
	}

	public static Date incrementaHora(Date data, Integer horas) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.HOUR_OF_DAY, horas);
		return new Date(cal.getTimeInMillis());
	}
	
	public static Integer getAnoCorrente(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.YEAR);
	}
	
	public static Date getDataSemHora(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
}