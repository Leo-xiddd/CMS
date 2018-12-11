package servlet;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import main.*;
/**
 * Servlet implementation class ServCMS
 */
@WebServlet(description = "servlet ServCMS for safe-code service", urlPatterns = { "/servlet/ServCMS" })
public class ServCMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String logconf=System.getProperty("user.dir").replace(File.separator+"bin", "")+File.separator+"conf"+File.separator+"CMS"+File.separator+"logconf.properties";
	Logger logger = Logger.getLogger(ServCMS.class.getName());
	CMS api = new CMS();
	
    public ServCMS() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String[]> para;
		String ReqUrl="";
		String RespVau="";
//		获取请求的API和附带参数
		request.setCharacterEncoding("UTF-8"); 
		para=request.getParameterMap();
		ReqUrl=request.getRequestURI();
		int div=ReqUrl.lastIndexOf("/")+1;
		ReqUrl=ReqUrl.substring(div);
		
//		将API和参数发送到API接口函数处理
		try {
			RespVau=api.DoAPI(ReqUrl,para,"",request);
		}catch(Exception e) {
			e.printStackTrace();
		}	
		
//		将API接口函数返回的结果发送给客户端
		response.setContentType("text/html;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		out.println(RespVau);
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String[]> para;
		String ReqUrl="";
		String message = "";
		String body="";
		
//		获取请求的API和附带参数		
		request.setCharacterEncoding("UTF-8"); 
		para=request.getParameterMap();
		ReqUrl=request.getRequestURI();
		int div=ReqUrl.lastIndexOf("/")+1;
		ReqUrl=ReqUrl.substring(div);
		
//		1、判断提交上来的数据是否是上传表单的数据
		if(!ReqUrl.equals("UpdateContent") && !ReqUrl.equals("UploadNewspic")){
			BufferedReader br;
			String readline="";			
			br=request.getReader();
			try {
			       while ((readline = br.readLine()) != null) {
			    	   body += readline;
			       }
			       br.close();
			 } catch (IOException e) {
				 logger.error("IOException: " + e.getStackTrace());
				 message="{\"code\"=500,\"message\"="+e+"}";
			 }								
		}
//		将API和参数发送到API接口函数处理	
		if(message.equals("")){
			try {
				message=api.DoAPI(ReqUrl,para,body,request);
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}		
//		将API接口函数返回的结果发送给客户端	
		response.setContentType("text/html;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		out.println(message);
		out.close();
	}
}
