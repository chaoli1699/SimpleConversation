package cn.cienet.simpleconversation.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.text.TextUtils;

public class FormatUtils {

	/**
	 * ��ʽ������
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDate(long date, String format){
    	DateFormat dateFormat=new SimpleDateFormat(format);
    	return dateFormat.format(new Date(date));
    }

    /**
     * ��ʽ������ ###.000
     *
     * @return ###.000
     */
    public static float format2Bit(float number) {
        DecimalFormat decimalFormat = new DecimalFormat("###.00");
        String target = decimalFormat.format(number);
        if (target.startsWith(".")) {
            target = "0" + target;
        }
        return Float.parseFloat(target);
    }

    /**
     * ��ʽ������ ###.000
     *
     * @param numberStr ��ʽ������ַ�����ʽ
     * @return ###.000
     */
    public static String format2Bit(String numberStr) {
        if (TextUtils.isEmpty(numberStr)) {
            return "0.00";
        }
        float numberFloat = Float.parseFloat(numberStr);
        DecimalFormat decimalFormat = new DecimalFormat("###.00");
        String target = decimalFormat.format(numberFloat);
        if (target.startsWith(".")) {
            target = "0" + target;
        }
        return target;
    }
}
