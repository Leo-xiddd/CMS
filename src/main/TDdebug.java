package main;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.*;

import org.json.JSONObject;
import base.*;

@SuppressWarnings("unused")
public class TDdebug {
	static XMLDriver xml=new XMLDriver();
	static Inform inf=new Inform();
	static DBDriver dbd=new DBDriver();
	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
		try {
			String a="123";
			String[] b=a.split(",");
			
			System.out.println(b[0]);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		

	}

}
