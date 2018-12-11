/** 类说明：本模块用于实现为官网站点提供内容获取服务相关的API
 *  作   者：LeoLee  李昊
 *  时   间：2018/09/30
 *  版   本：V1.0.1
 *  方   法：本模块支持的方法包括：
 *  	1. 上传测试脚本				String 	UploadTS(String usr,String proj,String mod,HttpServletRequest req)
 *  	2. 删除测试脚本				String 	DelTS(String usr,String TS)
 *  	3. 列出脚本						String	ListTS(String filter, String page_count, String page_num) 
 */
package main;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.log4j.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import base.*;

public class ContentServer {
	DBDriver dbd = new DBDriver();
//	配置日志属性文件位置
	static String confpath=System.getProperty("user.dir").replace(File.separator+"bin", "");
	static String logconf=confpath+File.separator+"conf"+File.separator+"CMS"+File.separator+"logconf2.properties";
	static String sysconf=confpath+File.separator+"conf"+File.separator+"CMS"+File.separator+"Sys_config.xml";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
	Logger logger = Logger.getLogger(ContentServer.class.getName());
	
	public String DoAPI(String API,Map<String, String[]> Param,String body){						
		PropertyConfigurator.configure(logconf);		
		logger.info("API: "+API+" "+" [Body]"+body);
		String backvalue="412,http 请求的参数缺失或无效";
		String News_id=checkpara(Param,"News_id");
		String Channel_id=checkpara(Param,"Channel_id");
		String type=checkpara(Param,"type");
		String page_count=checkpara(Param,"page_count");
		String page_num=checkpara(Param,"page_num");
//		开始处理API
		try {
			switch(API){
			case "GetPageContent":   
				if(!Channel_id.equals("")) {
					logger.info("获取页面素材...");				
					return GetPageContent(Channel_id);
				}
			case "ListNews":   
				logger.info("列出所有新闻...");				
				return ListNews(type,page_count,page_num);
			case "GetNews":   
				logger.info("获取新闻"+News_id+"...");			
				if(!News_id.equals("")) {
					return GetNews(News_id);				
				}		
				break;
			default:
				logger.error("无效API: "+API);
				backvalue="400,无效API!";
			}
		}catch (Throwable e) {
			backvalue=e.getMessage();
			int firtag=backvalue.lastIndexOf("[info]");
			if(firtag>-1) backvalue=backvalue.substring(firtag+6);
			else backvalue="500,"+backvalue;
			logger.error(backvalue,e);
		}	
		String code=backvalue.substring(0,backvalue.indexOf(","));
		String message=backvalue.substring(backvalue.indexOf(",")+1);
		backvalue="{\"code\":"+code+",\"message\":\""+message+"\"}";
		return backvalue;
	}
	/**
	 * 函数说明：列出指定ID号的图片或文本素材
	 * @param  channel_id	页面频道编号
	 * @return	  JSONArray格式字符串
	 * @throws Exception 500,数据库故障
	 */
	String GetPageContent(String channel_id) throws Exception {
		PropertyConfigurator.configure(logconf);	
//		检查参数是否有效
		try {			
			String Colname="position_id,type,policy,content,url";
			String[][] content=dbd.readDB("cms_content", Colname, "channel_id='"+channel_id+"' and state='已发布'");
			String cols[]=Colname.split(",");

			JSONObject ContentList=new JSONObject();
			JSONArray jas= new JSONArray();
			if(!content[0][0].equals("")) {
				for(int i=0;i<content.length;i++) {
					JSONObject jsb=new JSONObject();
					for(int k=0;k<cols.length;k++) {
						jsb.put(cols[k], content[i][k]);
					}
					jas.put(jsb);
				}
			}			
			
			ContentList.put("contentlist", jas);
			ContentList.put("code", 200);
			return ContentList.toString();
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}		
	}
	/**
	 * 函数说明：列出当前所有新闻
	 * @param  type	新闻类型
	 * @param  page_count		分页显示时，每页显示的条数
	 * @param  page_num		分页显示时，显示的页码
	 * @return	  JSONArray格式字符串
	 * @throws Exception 500,数据库故障
	 */
	String ListNews(String type,String page_count,String page_num) throws Exception {
		PropertyConfigurator.configure(logconf);	
//		检查参数是否有效
		try {
			String temp_filt="";
			if(!type.equals("*"))temp_filt="type='"+type+"' and";
			String filter=temp_filt+" state='已发布' order by dtime desc limit "+Integer.parseInt(page_count)*(Integer.parseInt(page_num)-1)+","+page_count;
			int sum_num=dbd.check("cms_news");
			String Colname="news_id,title,dtime,locate,summary,pic1,level";
			String cols[]=Colname.split(",");
			String[][] ul=dbd.readDB("cms_news", Colname, filter);
			JSONObject NewList=new JSONObject();
			JSONArray jas= new JSONArray();
			int num=ul.length;
			if(!ul[0][0].equals("")) {
				for(int i=0;i<num;i++) {
					JSONObject jsb=new JSONObject();
					ul[i][2]=sdf1.format(sdf.parse(ul[i][2]));
					if(ul[i][2].equals("0001年01月01"))ul[i][2]="";
					for(int k=0;k<cols.length;k++) {
						jsb.put(cols[k], ul[i][k]);
					}
					jas.put(jsb);
				}
			}			
			NewList.put("total_num", sum_num);
			NewList.put("newslist", jas);
			NewList.put("code", 200);
			return NewList.toString();
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}		
	}
	/**
	 * 函数说明：获取新闻
	 * @param 	Nid		新闻编号
	 * @throws 	Exception 	404,内容不存在
	 * @throws 	Exception 	412,参数不正确
	 * @throws 	Exception 	500,系统错误
	 * @return		完整的JSON格式返回值
	 */
	String GetNews(String Nid) throws Exception {
		PropertyConfigurator.configure(logconf);	
		try {
			String Colname="id,title,dtime,locate,summary,content,pic1,pic2,pic3,pic4,level";
			String[][] news=dbd.readDB("cms_news", Colname, "news_id='"+Nid+"'");
			if(news[0][0].equals(""))throw new Exception("[info]404,内容["+Nid+"]不存在");
			
			JSONObject reqdata=new JSONObject();
			String[] cols=Colname.split(",");
			news[0][2]=sdf1.format(sdf.parse(news[0][2]));
			if(news[0][2].equals("0001年01月01"))news[0][2]="";
			
			if(!news[0][6].equals("")) {
				news[0][5]=news[0][5].replace("<img src=\"1\"","<img src=\"img/news/"+news[0][6]+"\"");
			}
			if(!news[0][7].equals("")) {
				news[0][5]=news[0][5].replace("<img src=\"2\"","<img src=\"img/news/"+news[0][7]+"\"");
			}
			if(!news[0][8].equals("")) {
				news[0][5]=news[0][5].replace("<img src=\"3\"","<img src=\"img/news/"+news[0][8]+"\"");
			}
			if(!news[0][9].equals("")) {
				news[0][5]=news[0][5].replace("<img src=\"4\"","<img src=\"img/news/"+news[0][9]+"\"");
			}
			for(int i=0;i<cols.length;i++) {
				reqdata.put(cols[i], news[0][i]);
			}
			reqdata.put("code", 200);
			return reqdata.toString();
		}catch (JSONException e) {
			throw new Exception("[info]500,JSON语法错误："+e.toString());
		}
		catch (Throwable e) {
			logger.error(e.toString());
			throw new Exception(e);
		}	
	}
	/**[Function] 				获取http请求报文中的参数值
	 * @author para		请求报文中的参数序列
	 * @author key			预期的参数名
	 * @return [String]		返回参数结果，如果请求的参数序列为空，或者没有要查询的参数，返回“”，否则返回查询到的参数值
	 */
	String checkpara(Map<String,String[]> para,String key){
		PropertyConfigurator.configure(logconf);
		String ba="";		
		if(para.size()>0){
			try{
				String[] val=para.get(key);
				if(null!=val)ba=val[0];
				if(ba.indexOf(" ")>-1)ba=ba.substring(0,ba.indexOf(" "));
			}catch(NullPointerException e){
				logger.error(e.toString());
			}
		}	
		return ba;
	}
	
	/**[Function] 				验证Token参数是否正确，以及用户名和密码是否匹配
	 * @return [int]			返回执行结果代码，200对应操作成功，412对应校验不通过，500为数据库或系统错误
	 */
	int TokenVerify(String token){
		int code=0;
		return code;
	}
}