/** 类说明：本模块用于实现ATMP服务器脚本相关的API
 *  作   者：Leo
 *  时   间：2017/10/30
 *  版   本：V3.0.1
 *  方   法：本模块支持的方法包括：
 *  	1. 上传测试脚本				String 	UploadTS(String usr,String proj,String mod,HttpServletRequest req)
 *  	2. 删除测试脚本				String 	DelTS(String usr,String TS)
 *  	3. 列出脚本						String	ListTS(String filter, String page_count, String page_num) 
 */
package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import base.*;

public class CMS {
	DBDriver dbd = new DBDriver();
//	配置日志属性文件位置
	static String confpath=System.getProperty("user.dir").replace(File.separator+"bin", "");
	static String logconf=confpath+File.separator+"conf"+File.separator+"CMS"+File.separator+"logconf.properties";
	static String sysconf=confpath+File.separator+"conf"+File.separator+"CMS"+File.separator+"Sys_config.xml";
	Logger logger = Logger.getLogger(CMS.class.getName());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf_sn = new SimpleDateFormat("yyyyMMddHHmm");
	
	public String DoAPI(String API,Map<String, String[]> Param,String body,HttpServletRequest req){						
		PropertyConfigurator.configure(logconf);		
		logger.info("API: "+API+" "+" [Body]"+body);
		String backvalue="412,http 请求的参数缺失或无效";
		String Channel_id=checkpara(Param,"Channel_id");
		String Position_id=checkpara(Param,"Position_id");
		String News_id=checkpara(Param,"News_id");
		String filter=checkpara(Param,"filter");
		String page_count=checkpara(Param,"page_count");
		String page_num=checkpara(Param,"page_num");
//		开始处理API
		try {
			switch(API){
			case "AddChannel":
				if(!body.equals("")) {
					logger.info("添加频道...");		
					AddChannel(body);	
					backvalue="200,ok"; 
				}		
				break;
			case "UpdateChannel":
				if(!Channel_id.equals("") && !body.equals("")) {
					logger.info("更新频道...");		
					UpdateChannel(Channel_id,body);	
					backvalue="200,ok"; 
				}		
				break;
			case "DelChannel":
				if(!Channel_id.equals("")) {
					logger.info("删除频道...");		
					DelChannel(Channel_id);	
					backvalue="200,ok"; 
				}		
				break;
			case "ListChannel":   
				logger.info("列出所有频道...");				
				return ListChannel(filter,page_count,page_num);
			case "AddPosition":
				if(!body.equals("")) {
					logger.info("添加页面位置...");		
					AddPosition(body);	
					backvalue="200,ok"; 
				}		
				break;
			case "UpdatePosition":
				if(!Position_id.equals("") && !body.equals("")) {
					logger.info("更新页面位置...");		
					UpdatePosition(Position_id,body);	
					backvalue="200,ok"; 
				}		
				break;
			case "DelPosition":
				if(!Position_id.equals("")) {
					logger.info("删除页面位置...");		
					DelPosition(Position_id);	
					backvalue="200,ok"; 
				}		
				break;
			case "ListPosition":   
				logger.info("列出页面所有位置...");				
				return ListPosition(Channel_id,filter,page_count,page_num);
			case "AddNews":
				if(!body.equals("")) {
					AddNews(body);	
					backvalue="200,ok"; 
				}		
				break;
			case "DelNews":
				if(!News_id.equals("")) {
					DelNews(News_id);	
					backvalue="200,ok"; 
				}		
				break;
			case "ListNews":   
				logger.info("列出所有新闻...");				
				return ListNews(filter,page_count,page_num);
			case "GetNews":   
				if(!News_id.equals("")) {
					logger.info("获取新闻...");				
					return GetNews(News_id);
				}
				break;
			case "ReleaseNews":   
				if(!News_id.equals("")) {
					ReleaseNews(News_id);	
					backvalue="200,ok"; 
				}		
				break;
			case "UpdateNews":
				if(!News_id.equals("") && !body.equals("")) {
					logger.info("更新新闻...");		
					UpdateNews(News_id,body);	
					backvalue="200,ok"; 
				}		
				break;
			case "UploadNewspic": 
				if(!News_id.equals("")) {
					logger.info("上传新闻图片...");
					UploadNewspic(News_id, req);
					backvalue="200,ok"; 
				}
				break;	
			case "UpdateContent": 
				if(!Position_id.equals("")) {
					logger.info("编辑内容...");
					String policy=checkpara(Param,"policy");
					String url=checkpara(Param,"url");
					String type=checkpara(Param,"type");
					String content=checkpara(Param,"content");
					UpdateContent(Channel_id,Position_id,policy,url,type,content, req);
					backvalue="200,ok"; 
				}
				break;	
			case "ListContent":   
				logger.info("列出所有内容...");				
				return ListContent(Channel_id,filter,page_count,page_num);
			case "ReleaseContent":   
				if(!Position_id.equals("")) {
					ReleaseContent(Position_id);	
					backvalue="200,ok"; 
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
	 * 函数说明：创建一个新的频道
	 * @param 	Rdata	频道的数据JSON格式
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception 	404,用户名参数为空
	 * @throws 	Exception 	412,参数不正确
	 * @throws Exception 	500,数据库故障
	 */
	void AddChannel(String Rdata) throws Exception {
		PropertyConfigurator.configure(logconf);		
		String[] Table_col= {"channel_id","name","note"};
		String[] record= new String[Table_col.length];
		try {
			JSONObject req=new JSONObject(Rdata);
			for(int i=0;i<Table_col.length;i++)record[i]=req.getString(Table_col[i]);
			int row=dbd.check("cms_channel", "channel_id", record[0]);
			if(row>0)throw new Exception("[info]412,频道标签["+record[0]+"]已存在");
			
			row=dbd.check("cms_channel", "name", record[1]);
			if(row>0)throw new Exception("[info]412,频道["+record[1]+"]已存在");
			
			dbd.AppendSQl("cms_channel", Table_col, record, 1, 1);
			
//			为频道创建目录
			String dirpath=confpath+File.separator+"webapps"+File.separator+"CMS"+File.separator+"img"+File.separator+record[0];
			File dir=new File(dirpath);
			if(!dir.exists())dir.mkdirs();
			else if(!dir.isDirectory())dir.mkdirs();

		} catch(JSONException e) {
			logger.error("数据不完整"+e.toString());
			throw new Exception("[info]412,数据不完整,"+e.toString());
		}catch (Throwable e) {
			logger.error(e.toString());
			throw new Exception(e);
		}		
	}
	
	/**
	 * 函数说明：更新频道数据
	 * @param 	Channel_id		频道标签
	 * @param 	Rdata		要更新的频道数据
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception 	404,频道不存在
	 * @throws 	Exception 	412,参数不正确
	 * @throws 	Exception 	500,系统错误
	 */
	void UpdateChannel(String Channel_id,String Rdata) throws Exception {
		PropertyConfigurator.configure(logconf);			
		String Colname="name,note";
		String[] tab_col= {"name","note"};	
		try {		
			JSONObject Cdate=new JSONObject(Rdata);
			String[][] olddate=dbd.readDB("cms_channel", Colname, "channel_id='"+Channel_id+"'");
			if(olddate[0][0].equals("")) throw new Exception("[info]404,频道["+Channel_id+"]不存在");
			
			for(int i=0;i<tab_col.length;i++) {
				String newValue=Cdate.getString(tab_col[i]);
				if(!olddate[0][i].equals(newValue)) {
					dbd.UpdateSQla("cms_channel", "channel_id='"+Channel_id+"'", tab_col[i], newValue);
				}
			}
		} catch(JSONException e) {
			logger.error("数据内容不完整",e);
			throw new Exception("[info]412,数据内容不完整");
		}catch (Throwable e) {
			logger.error(e.toString());
			throw new Exception(e);
		}		
	}
	/**
	 * 函数说明：删除频道
	 * @param 	Channel_id		频道标签
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception	500,数据库故障
	 */
	void DelChannel(String Channel_id)throws Exception  {
		PropertyConfigurator.configure(logconf);	
		try {	
			int row=dbd.check("cms_channel", "channel_id", Channel_id);
			if(row>0) {
				dbd.DelSQl("cms_channel", row, 1, 1);
			}
		} catch (Throwable e) {
			logger.error(e.toString(),e);
			throw new Exception(e);
		}
	}	
	/**
	 * 函数说明：列出当前所有频道
	 * @param  filter	过滤器，由前端设定
	 * @param  page_count		分页显示时，每页显示的条数
	 * @param  page_num		分页显示时，显示的页码
	 * @return	  JSONArray格式字符串
	 * @throws Exception 500,数据库故障
	 */
	String ListChannel(String filter,String page_count,String page_num) throws Exception {
		PropertyConfigurator.configure(logconf);	
//		检查参数是否有效
		try {
			if(filter.equals(""))filter="id>0";	
			else if(filter.indexOf("like")>-1)filter=filter.replace("*", "%");
			String filter1=filter+" order by name";
			filter=filter+" order by name limit "+Integer.parseInt(page_count)*(Integer.parseInt(page_num)-1)+","+page_count;
			int sum_num=dbd.checknum("cms_channel", "name", filter1);
			String Colname="channel_id,name,note";
			String[][] ul=dbd.readDB("cms_channel", Colname, filter);
			JSONObject Channellist=new JSONObject();
			JSONArray jas= new JSONArray();
			int num=ul.length;
			if(!ul[0][0].equals("")) {
				for(int i=0;i<num;i++) {
					JSONObject jsb=new JSONObject();
					jsb.put("channel_id", ul[i][0]);
					jsb.put("name", ul[i][1]);
					jsb.put("note", ul[i][2]);
					jas.put(jsb);
				}
			}			
			Channellist.put("total_num", sum_num);
			Channellist.put("channellist", jas);
			Channellist.put("code", 200);
			return Channellist.toString();
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}		
	}

	/**
	 * 函数说明：创建一个新的页面位置
	 * @param 	Rdata	页面位置的数据JSON格式
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception 	404,用户名参数为空
	 * @throws 	Exception 	412,参数不正确
	 * @throws Exception 	500,数据库故障
	 */
	void AddPosition(String Rdata) throws Exception {
		PropertyConfigurator.configure(logconf);		
		String[] Table_col= {"channel_id","position_id","name","size_w","size_h","type"};
		String[] record= new String[Table_col.length];
		try {
			JSONObject pos=new JSONObject(Rdata);
			for(int i=0;i<Table_col.length;i++)record[i]=pos.getString(Table_col[i]);
			int row=dbd.check("cms_position","position_id", record[1]);
			if(row>0)throw new Exception("[info]412,页面位置标签["+record[1]+"]已存在");
			
			row=dbd.checknum("cms_position", "id", "channel_id='"+record[0]+"' and name='"+record[2]+"'");
			if(row>0)throw new Exception("[info]412,页面位置["+record[2]+"]已存在");
			
			dbd.AppendSQl("cms_position", Table_col, record, 1, 1);
			
			String[] cont_col= {"channel_id","position_id","name","size","type","policy","state","url","content"};
			String[] cont_rec=new String[cont_col.length];
			cont_rec[0]=record[0];
			cont_rec[1]=record[1];
			cont_rec[2]=record[2];
			cont_rec[3]=record[3]+"*"+record[4];
			cont_rec[4]=record[5];
			cont_rec[5]="";
			cont_rec[6]="";
			cont_rec[7]="";
			cont_rec[8]="";
			dbd.AppendSQl("cms_content", cont_col, cont_rec, 1, 1);
		} catch(JSONException e) {
			logger.error("数据不完整"+e.toString());
			throw new Exception("[info]412,数据不完整,"+e.toString());
		}catch (Throwable e) {
			logger.error(e.toString());
			throw new Exception(e);
		}		
	}
	
	/**
	 * 函数说明：更新页面位置数据
	 * @param 	Pos_id		页面位置标签
	 * @param 	Rdata		要更新的页面位置数据
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception 	404,页面位置不存在
	 * @throws 	Exception 	412,参数不正确
	 * @throws 	Exception 	500,系统错误
	 */
	void UpdatePosition(String Pos_id,String Rdata) throws Exception {
		PropertyConfigurator.configure(logconf);			
		String Colname="name,size_w,size_h,type";
		String[] tab_col= {"name","size_w","size_h","type"};	
		try {		
			JSONObject Cdate=new JSONObject(Rdata);
			String[][] olddate=dbd.readDB("cms_position", Colname, "position_id='"+Pos_id+"'");
			if(olddate[0][0].equals("")) throw new Exception("[info]404,页面位置["+Pos_id+"]不存在");
			
			for(int i=0;i<tab_col.length;i++) {
				String newValue=Cdate.getString(tab_col[i]);
				if(!olddate[0][i].equals(newValue)) {
					dbd.UpdateSQla("cms_position", "position_id='"+Pos_id+"'", tab_col[i], newValue);
					if(tab_col[i].equals("name") || tab_col[i].equals("type")) {
						dbd.UpdateSQla("cms_content", "position_id='"+Pos_id+"'", tab_col[i], newValue);
					}
					else {
						String siz=Cdate.getString("size_w")+"*"+Cdate.getString("size_h");
						dbd.UpdateSQla("cms_content", "position_id='"+Pos_id+"'", "size", siz);
					}
				}
			}
		} catch(JSONException e) {
			logger.error("数据内容不完整",e);
			throw new Exception("[info]412,数据内容不完整");
		}catch (Throwable e) {
			logger.error(e.toString());
			throw new Exception(e);
		}		
	}
	/**
	 * 函数说明：删除页面位置
	 * @param 	Pos_id		页面位置标签
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception	500,数据库故障
	 */
	void DelPosition(String Pos_id)throws Exception  {
		PropertyConfigurator.configure(logconf);	
		try {	
			int row=dbd.check("cms_position", "position_id", Pos_id);
			if(row>0) {
				dbd.DelSQl("cms_position", row, 1, 1);
				row=dbd.check("cms_content", "position_id", Pos_id);
				if(row>0) {
					dbd.DelSQl("cms_content", row, 1, 1);
				}
			}
		} catch (Throwable e) {
			logger.error(e.toString(),e);
			throw new Exception(e);
		}
	}	
	
	/**
	 * 函数说明：列出当前所有页面位置
	 * @param  channel_id		频道编号
	 * @param  filter	过滤器，由前端设定
	 * @param  page_count		分页显示时，每页显示的条数
	 * @param  page_num		分页显示时，显示的页码
	 * @return	  JSONArray格式字符串
	 * @throws Exception 500,数据库故障
	 */
	String ListPosition(String channel_id, String filter,String page_count,String page_num) throws Exception {
		PropertyConfigurator.configure(logconf);	
//		检查参数是否有效
		try {
			if(channel_id.equals(""))channel_id="*";
			if(filter.equals(""))filter="channel_id='"+channel_id+"'";	
			else if(filter.indexOf("like")>-1)filter="channel_id='"+channel_id+"' and "+filter.replace("*", "%");
			String filter1=filter+" order by name";
			filter=filter+" order by name limit "+Integer.parseInt(page_count)*(Integer.parseInt(page_num)-1)+","+page_count;
			int sum_num=dbd.checknum("cms_position", "id", "channel_id='"+channel_id+"' and "+filter1);
			String Colname="position_id,name,size_w,size_h,type";
			String[][] ul=dbd.readDB("cms_position", Colname, filter);
			JSONObject positionlist=new JSONObject();
			JSONArray jas= new JSONArray();
			int num=ul.length;
			if(!ul[0][0].equals("")) {
				for(int i=0;i<num;i++) {
					JSONObject jsb=new JSONObject();
					jsb.put("position_id", ul[i][0]);
					jsb.put("name", ul[i][1]);
					jsb.put("size_w", ul[i][2]);
					jsb.put("size_h", ul[i][3]);
					jsb.put("type", ul[i][4]);
					jas.put(jsb);
				}
			}			
			positionlist.put("total_num", sum_num);
			positionlist.put("positionlist", jas);
			positionlist.put("code", 200);
			return positionlist.toString();
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}		
	}
	
	/**
	 * 函数说明：创建一个新的新闻
	 * @param 	Rdata	新闻的数据JSON格式
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception 	404,用户名参数为空
	 * @throws 	Exception 	412,参数不正确
	 * @throws Exception 	500,数据库故障
	 */
	void AddNews(String Rdata) throws Exception {
		PropertyConfigurator.configure(logconf);		
		String[] Table_col= {"news_id","state","dtime","title","locate","summary","content","type","pic1","pic2","pic3","pic4"};
		String[] record= new String[Table_col.length];
		try {
			JSONObject pos=new JSONObject(Rdata);
			for(int i=2;i<Table_col.length;i++)record[i]=pos.getString(Table_col[i]);
			int row=dbd.check("cms_news","title", record[3]);
			if(row>0)throw new Exception("[info]412,新闻“"+record[3]+"”已存在");
			record[0]=sdf_sn.format(new Date());			
			record[1]="待发布";
			if(record[2].equals(""))record[2]="0001-01-01";
			for(int i=8;i<Table_col.length;i++) {
				if(!record[i].equals(""))record[i]=record[0]+"_"+record[i];
				else break;
			}
			dbd.AppendSQl("cms_news", Table_col, record, 1, 1);
		} catch(JSONException e) {
			logger.error("数据不完整"+e.toString());
			throw new Exception("[info]412,数据不完整,"+e.toString());
		}catch (Throwable e) {
			logger.error(e.toString());
			throw new Exception(e);
		}		
	}
	
	/**
	 * 函数说明：更新新闻数据
	 * @param 	news_id		新闻ID
	 * @param 	Rdata		要更新的新闻数据
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception 	404,新闻不存在
	 * @throws 	Exception 	412,参数不正确
	 * @throws 	Exception 	500,系统错误
	 */
	void UpdateNews(String news_id,String Rdata) throws Exception {
		PropertyConfigurator.configure(logconf);			
		String Colname="id,title,dtime,locate,summary,content,type,pic1,pic2,pic3,pic4";
		String[] tab_col= Colname.split(",");
		try {		
			JSONObject Cdate=new JSONObject(Rdata);
			String[][] olddate=dbd.readDB("cms_news", Colname, "news_id='"+news_id+"'");
			if(olddate[0][0].equals("")) throw new Exception("[info]404,新闻[ID: "+news_id+"]不存在");
			int tag=0;
			int row=Integer.parseInt(olddate[0][0]);
			for(int i=1;i<tab_col.length;i++) {
				String newValue=Cdate.getString(tab_col[i]);
				if(i==2) {
					olddate[0][i]=sdf1.format(sdf.parse(olddate[0][i]));
				}
				if(!olddate[0][i].equals(newValue)) {
					if(tab_col[i].indexOf("pic")>-1)	dbd.UpdateSQl("cms_news",row, tab_col[i], news_id+"_"+newValue);
					else dbd.UpdateSQl("cms_news", row, tab_col[i], newValue);
					tag=1;
				}
			}
			if(tag==1) {
				dbd.UpdateSQl("cms_news", row, "state", "待发布");
			}
		} catch(JSONException e) {
			logger.error("数据内容不完整",e);
			throw new Exception("[info]412,数据内容不完整");
		}catch (Throwable e) {
			logger.error(e.toString());
			throw new Exception(e);
		}		
	}
	/**
	 * 函数说明：删除新闻
	 * @param 	news_id		新闻标签
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception	500,数据库故障
	 */
	void DelNews(String news_id)throws Exception  {
		PropertyConfigurator.configure(logconf);	
		try {	
			int row=dbd.check("cms_news", "news_id", news_id);
			if(row>0) {
				dbd.DelSQl("cms_news", row, 1, 1);
			}
		} catch (Throwable e) {
			logger.error(e.toString(),e);
			throw new Exception(e);
		}
	}	
	
	/**
	 * 函数说明：列出当前所有新闻
	 * @param  filter	过滤器，由前端设定
	 * @param  page_count		分页显示时，每页显示的条数
	 * @param  page_num		分页显示时，显示的页码
	 * @return	  JSONArray格式字符串
	 * @throws Exception 500,数据库故障
	 */
	String ListNews(String filter,String page_count,String page_num) throws Exception {
		PropertyConfigurator.configure(logconf);	
//		检查参数是否有效
		try {
			if(filter.equals(""))filter="id>0";	
			else if(filter.indexOf("like")>-1)filter=filter.replace("*", "%");
			String filter1=filter+" order by dtime desc";
			filter=filter+" order by dtime desc limit "+Integer.parseInt(page_count)*(Integer.parseInt(page_num)-1)+","+page_count;
			
			int sum_num=dbd.checknum("cms_news", "id", filter1);
			String Colname="news_id,title,dtime,locate,summary,type,pic1,pic2,pic3,pic4,state";
			String cols[]=Colname.split(",");
			String[][] ul=dbd.readDB("cms_news", Colname, filter);
			JSONObject positionlist=new JSONObject();
			JSONArray jas= new JSONArray();
			int num=ul.length;
			if(!ul[0][0].equals("")) {
				for(int i=0;i<num;i++) {
					JSONObject jsb=new JSONObject();
					ul[i][2]=sdf1.format(sdf.parse(ul[i][2]));
					for(int k=0;k<cols.length;k++)jsb.put(cols[k], ul[i][k]);
					jas.put(jsb);
				}
			}			
			positionlist.put("total_num", sum_num);
			positionlist.put("newslist", jas);
			positionlist.put("code", 200);
			return positionlist.toString();
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}		
	}
	/**
	 * 函数说明：获取指定新闻
	 * @param  news_id	新闻编号
	 * @return	  JSONArray格式字符串
	 * @throws Exception 500,数据库故障
	 */
	String GetNews(String news_id) throws Exception {
		PropertyConfigurator.configure(logconf);	
//		检查参数是否有效
		try {
			String Colname="news_id,dtime,title,locate,summary,content,type,pic1,pic2,pic3,pic4";
			String cols[]=Colname.split(",");
			String[][] ul=dbd.readDB("cms_news", Colname, "news_id='"+news_id+"'");
			JSONObject news=new JSONObject();
			int num=ul.length;
			if(!ul[0][0].equals("")) {
				for(int i=0;i<num;i++) {
					for(int k=0;k<cols.length;k++)news.put(cols[k], ul[i][k]);
				}
			}			
			news.put("code", 200);
			return news.toString();
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}		
	}
	/**[Function] 	上传新闻图片
	 * @param news_id	新闻编号
	 * @param req			图片数据
	 * @return	 JSON格式字符串，message字段会给出上传结果
	 * @throws Exception 201, 文件上传成功，但存在重名文件
	 * @throws Exception 402, 上传文件格式不正确或上传失败
	 * @throws Exception 403, 上传文件容量过大
	 * @throws Exception 404, 项目或模块不存在 
	 */
	void UploadNewspic(String news_id, HttpServletRequest req) throws Exception{
		PropertyConfigurator.configure(logconf);
		try {
			if(news_id.equals("0")) {
				String[][] ul=dbd.readDB("cms_news", "news_id", "id>0 order by id desc");
				news_id=ul[0][0];
			}
	//		判断文件格式是否正确
			if(!ServletFileUpload.isMultipartContent(req))throw new Exception("[info]402, 上传的文件格式不正确！");		
	//		开始上传文件
			String tempPath = confpath+File.separator+"webapps"+File.separator+"CMS"+File.separator+"img"+File.separator+"temp";
			String filepath=confpath+File.separator+"webapps"+File.separator+"CMS"+File.separator+"img";
	
	//		1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();                     									
			factory.setRepository(new File(tempPath));
			factory.setSizeThreshold(1024*1024*50);	
			
//			2、创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			upload.setFileSizeMax(1024*1024*50);
			upload.setSizeMax(1024*1024*100);	      
			logger.info("上传路径确认，开始上传文件...");	
			
	//		3、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
	        List<FileItem> list = upload.parseRequest(req);
	//     	初始化重复文件列表
	        int a=1;
	        for(FileItem item : list){
	           	String filename = item.getName();
	            if(filename==null || filename.trim().equals("")) continue;                             
	            String fileExtName = filename.substring(filename.lastIndexOf(".")+1);  //得到上传文件的扩展名
	            String tfn=filepath +File.separator+news_id+"_"+a+"."+fileExtName;
	            File file = new File(tfn);
	            if(file.exists())file.delete();
	                
	           	InputStream in = item.getInputStream();		// 获取item中的上传文件的输入流
	           	FileOutputStream outa = new FileOutputStream(tfn);			//创建一个文件输出流
	           	byte buffer[] = new byte[1024];
	           	int len = 0;
	           	while((len=in.read(buffer))>0){
	           		outa.write(buffer, 0, len);
	            } 
	            in.close();  
	 	        outa.close();  
	 	        a++;
			}
		}catch (FileUploadBase.FileSizeLimitExceededException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("[info]403, 单个文件超出最大值！");
        }catch (FileUploadBase.SizeLimitExceededException e) {
        	logger.error(e.getMessage(), e);
			throw new Exception("[info]403, 上传文件的总的大小超出限制的最大值！");
        }catch (Throwable e) {
        	logger.error(e.getMessage(), e);
			throw new Exception("[info]402, 文件上传失败！");
        }
	}
	
	/**
	 * 函数说明：发布新闻
	 * @param 	news_id		新闻的ID
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception 	404,用户名参数为空
	 * @throws 	Exception 	412,参数不正确
	 * @throws Exception 	500,数据库故障
	 */
	void ReleaseNews(String news_id) throws Exception {
		PropertyConfigurator.configure(logconf);		
		try {
			String[][] news=dbd.readDB("cms_news", "id,state,pic1,pic2,pic3,pic4,dtime", "news_id='"+news_id+"'");
			if(news[0][0].equals(""))throw new Exception("[info]412,新闻[ID: "+news_id+"]不存在");
			if(news[0][1].equals("待发布")) {
				int row=Integer.parseInt(news[0][0]);
				if(!news[0][2].equals("")) {
					String filepath=confpath+File.separator+"webapps"+File.separator+"CMS"+File.separator+"img"+File.separator;
					String newfilepath=confpath+File.separator+"webapps"+File.separator+"safe-code"+File.separator+"img"+File.separator+"news"+File.separator;					
					String dirpath=confpath+File.separator+"webapps"+File.separator+"safe-code"+File.separator+"img"+File.separator+"news";
					File dir=new File(dirpath);
					if(!dir.exists())dir.mkdirs();
					else if(!dir.isDirectory())dir.mkdirs();
					
					for(int i=2;i<6;i++) {
						if(news[0][i].equals(""))break;
						String fn=filepath +news[0][i];
						String tfn=newfilepath +news[0][i];
						File sfile = new File(fn);
				        if(!sfile.exists())throw new Exception("[info]412,新闻[ID: "+news_id+"]的图片“+(i-1)+”不存在");
				        
						File file = new File(tfn);
				        if(file.exists())file.delete();				         
				        FileInputStream input=new FileInputStream(fn); 
		               	FileOutputStream outa = new FileOutputStream(tfn);			//创建一个文件输出流
		               
		               	int in=input.read();
		                while(in!=-1){
		                	outa.write(in);
		                    in=input.read();
		                }
		                outa.flush();
		                input.close();  
		 	            outa.close();  
					}
				}
				dbd.UpdateSQl("cms_news", row, "state", "已发布");
				if(news[0][6].equals(""))	dbd.UpdateSQl("cms_news", row, "dtime", sdf.format(new Date()));
			}
		} catch(JSONException e) {
			logger.error("数据不完整"+e.toString());
			throw new Exception("[info]412,数据不完整,"+e.toString());
		}catch (Throwable e) {
			logger.error(e.toString());
			throw new Exception(e);
		}		
	}
	
	/**[Function] 	添加内容
	 * @param Pos_id		位置编号
	 * @param policy		投放策略
	 * @param url			图片的落地页地址
	 * @param type			内容类型
	 * @param content		内容数据，包括文本或者图片名称
	 * @param req			图片数据
	 * @return	 JSON格式字符串，message字段会给出上传结果
	 * @throws Exception 201, 文件上传成功，但存在重名文件
	 * @throws Exception 402, 上传文件格式不正确或上传失败
	 * @throws Exception 403, 上传文件容量过大
	 * @throws Exception 404, 项目或模块不存在 
	 */
	void UpdateContent(String channel_id, String Pos_id, String policy, String url, String type, String content, HttpServletRequest req) throws Exception{
		PropertyConfigurator.configure(logconf);
		try {
//			更新内容
			dbd.UpdateSQla("cms_content", "position_id='"+Pos_id+"'", "policy", policy);
			dbd.UpdateSQla("cms_content", "position_id='"+Pos_id+"'", "state", "待发布");
			dbd.UpdateSQla("cms_content", "position_id='"+Pos_id+"'", "url", url);
			if(type.equals("文本"))dbd.UpdateSQla("cms_content", "position_id='"+Pos_id+"'", "content", content);
			else if(!content.equals(""))dbd.UpdateSQla("cms_content", "position_id='"+Pos_id+"'", "content", content);
					
			if(!type.equals("文本") && !content.equals("")) {
	//			判断文件格式是否正确
				if(!ServletFileUpload.isMultipartContent(req))throw new Exception("[info]402, 上传的文件格式不正确！");		
	//			开始上传文件
				String tempPath = confpath+File.separator+"webapps"+File.separator+"CMS"+File.separator+"img"+File.separator+"temp";
				String filepath=confpath+File.separator+"webapps"+File.separator+"CMS"+File.separator+"img"+File.separator+channel_id+File.separator;
	
	//			1、创建一个DiskFileItemFactory工厂
				DiskFileItemFactory factory = new DiskFileItemFactory();                     									
				factory.setRepository(new File(tempPath));
				factory.setSizeThreshold(1024*1024*50);	
				
	//			2、创建一个文件上传解析器
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				upload.setFileSizeMax(1024*1024*50);
				upload.setSizeMax(1024*1024*100);	      
				logger.info("上传路径确认，开始上传文件...");	
				
	//			3、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
	            List<FileItem> list = upload.parseRequest(req);
	//        	初始化重复文件列表
	            for(FileItem item : list){
	            	String filename = item.getName();
	                if(filename==null || filename.trim().equals("")) continue;                             
	                String fileExtName = filename.substring(filename.lastIndexOf(".")+1);  //得到上传文件的扩展名
	                String tfn=filepath + Pos_id+"."+fileExtName;
	                File file = new File(tfn);
	                if(file.exists())file.delete();
	                
	               	InputStream in = item.getInputStream();		// 获取item中的上传文件的输入流
	               	FileOutputStream outa = new FileOutputStream(tfn);			//创建一个文件输出流
	               	byte buffer[] = new byte[1024];
	               	int len = 0;
	               	while((len=in.read(buffer))>0){
	                   outa.write(buffer, 0, len);
	                } 
	               	in.close();  
	 	            outa.close();  
	            }
			}
		}catch (FileUploadBase.FileSizeLimitExceededException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("[info]403, 单个文件超出最大值！");
        }catch (FileUploadBase.SizeLimitExceededException e) {
        	logger.error(e.getMessage(), e);
			throw new Exception("[info]403, 上传文件的总的大小超出限制的最大值！");
        }catch (Throwable e) {
        	logger.error(e.getMessage(), e);
			throw new Exception("[info]402, 文件上传失败！");
        }
	}
	/**
	 * 函数说明：列出页面当前所有素材
	 * @param  channel_id		频道标签
	 * @param  filter	过滤器，由前端设定
	 * @param  page_count		分页显示时，每页显示的条数
	 * @param  page_num		分页显示时，显示的页码
	 * @return	  JSONArray格式字符串
	 * @throws Exception 500,数据库故障
	 */
	String ListContent(String channel_id,String filter,String page_count,String page_num) throws Exception {
		PropertyConfigurator.configure(logconf);	
//		检查参数是否有效
		try {
			if(channel_id.equals(""))channel_id="*";
			if(filter.equals(""))filter="channel_id='"+channel_id+"'";	
			else if(filter.indexOf("like")>-1)filter="channel_id='"+channel_id+"' and "+filter.replace("*", "%");
		
			filter=filter+" order by dtime desc limit "+Integer.parseInt(page_count)*(Integer.parseInt(page_num)-1)+","+page_count;
			int sum_num=dbd.checknum("cms_content", "id", "channel_id='"+channel_id+"'");

			String Colname="position_id,name,size,type,policy,state,dtime,url,content";
			String cols[]=Colname.split(",");
			String[][] ul=dbd.readDB("cms_content", Colname, filter);
			JSONObject positionlist=new JSONObject();
			JSONArray jas= new JSONArray();
			int num=ul.length;
			if(!ul[0][0].equals("")) {
				for(int i=0;i<num;i++) {
					JSONObject jsb=new JSONObject();
					if(ul[i][6].indexOf("0001-01-01")>-1)ul[i][6]="";
					else ul[i][6]=sdf.format(sdf.parse(ul[i][6]));
					for(int k=0;k<cols.length;k++)jsb.put(cols[k], ul[i][k]);
					jas.put(jsb);
				}
			}			
			positionlist.put("total_num", sum_num);
			positionlist.put("contentlist", jas);
			positionlist.put("code", 200);
			return positionlist.toString();
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}		
	}
	/**
	 * 函数说明：发布内容
	 * @param 	Position_id		页面位置标签
	 * @throws 	Exception 	401,操作权限不足
	 * @throws 	Exception 	404,文件不存在
	 * @throws 	Exception 	412,参数不正确
	 * @throws Exception 	500,数据库故障
	 */
	void ReleaseContent(String Position_id) throws Exception {
		PropertyConfigurator.configure(logconf);		
		try {
			String[][] state=dbd.readDB("cms_content", "state,id,type,content,channel_id,name", "position_id='"+Position_id+"'");
			if(state[0][0].equals(""))throw new Exception("[info]412,页面位置[ID: "+Position_id+"]不存在");
			if(state[0][0].equals("待发布")) {
//				拷贝图片
				if(state[0][2].equals("图片")) {
					File src = new File(confpath+File.separator+"webapps"+File.separator+"CMS"+File.separator+"img"+File.separator+""+state[0][4]+""+File.separator+state[0][3]);
					File aim = new File(confpath+File.separator+"webapps"+File.separator+"safe-code"+File.separator+"img"+File.separator+""+state[0][4]+""+File.separator+state[0][3]);
					
					String dirpath=confpath+File.separator+"webapps"+File.separator+"safe-code"+File.separator+"img"+File.separator+state[0][4];
					File dir=new File(dirpath);
					if(!dir.exists())dir.mkdirs();
					else if(!dir.isDirectory())dir.mkdirs();
					
					if(!src.isFile()){//不是文件或者为空 
						throw new Exception("[info]404,"+state[0][4]+"页"+state[0][5]+"的图片不存在");//自定义异常
					}
					if(aim.isFile())aim.delete();
						
					InputStream is = new FileInputStream(src);
					OutputStream os = new FileOutputStream(aim);
					
					byte[] bt = new byte[1024];
					int len = 0;
					while(-1!=(len=is.read(bt))){
						os.write(bt, 0, len);
					}
					os.flush();//强制刷出
					os.close();
					is.close();
				}			
				dbd.UpdateSQl("cms_content", Integer.parseInt(state[0][1]), "state", "已发布");
				dbd.UpdateSQl("cms_content", Integer.parseInt(state[0][1]), "dtime", sdf.format(new Date()));
			}
		} catch(JSONException e) {
			logger.error("数据不完整"+e.toString());
			throw new Exception("[info]412,数据不完整,"+e.toString());
		}catch (Throwable e) {
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