import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

 
@WebServlet(name = "api",urlPatterns = "/api")
public class API extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String type=req.getParameter("type");
    	if(type.equals("1")) {
    		queryByProvince(req,resp);
    	}
    	if(type.equals("2")) {
    		queryByCity(req,resp);
    	}
    	
    }
    private void queryByCity(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
    	req.setCharacterEncoding("utf-8");
        List list=apiByCity(req,resp,req.getParameter("provinceName"));
        if(list.size()==0) {
        	resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("城市输入不正确，例子：连云港  作者QQ：2210074929");
        }
        String result="";
        for (Object str : list) {
            result=result+(String)str+"\n";
        }
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(result);
	}
    //按照单个城市查询
    public List apiByCity(HttpServletRequest req, HttpServletResponse resp,String key) throws IOException { 
        List jsonList=new ArrayList();
        Document doc = Jsoup.connect("http://127.0.0.1:2019/2019_nCov/2019-nCov?type=0").get();
        ChangeCharset changeCharset=new ChangeCharset();
        String temp1=changeCharset.toUTF_8(doc+"");
        temp1=temp1.replace("<html>","");
        temp1=temp1.replace("</html>","");
        temp1=temp1.replace("<body>","");
        temp1=temp1.replace("</body>","");
        temp1=temp1.replace("<head>","");
        temp1=temp1.replace("</head>","");
        temp1=temp1.trim();
        JSONArray jsonObject = JSON.parseArray(temp1);
        for (int i = 0; i < jsonObject.size(); i++) {
            JSONObject jo = jsonObject.getJSONObject(i);
            
            
            JSONArray cities = JSON.parseArray(jo.getString("cities"));
            for(int j = 0; j < cities.size(); j++) {
            	if(cities.getJSONObject(j).getString("cityName").equals(key)) {
            		JSONObject resultJson = cities.getJSONObject(j);
            		 String temp=resultJson.getString("cityName")+",确诊数量："
                           +resultJson.getString("confirmedCount")+",治愈数量："
                           +resultJson.getString("curedCount")+",死亡数量："
                           +resultJson.getString("deadCount");
            		 jsonList.add(temp);        		 
            	}
            }
        }
        return jsonList;

    }
    
    
    
	private void queryByProvince(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
    	req.setCharacterEncoding("utf-8");
        List list=api(req,resp,req.getParameter("provinceName"));
        if(list.size()==0) {
        	resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("省份输入不正确，例子：江苏省 （直辖市输入市即可，如：北京市，上海市）  作者QQ：2210074929");
        }
        String result="";
        for (Object str : list) {
            result=result+(String)str+"\n";
        }
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(result);
	}
	//按照省份查询
	public List api(HttpServletRequest req, HttpServletResponse resp,String key) throws IOException {
        List jsonList=new ArrayList();
        Document doc = Jsoup.connect("http://127.0.0.1:2019/2019_nCov/2019-nCov?type=0").get();
        ChangeCharset changeCharset=new ChangeCharset();
        String temp1=changeCharset.toUTF_8(doc+"");

        temp1=temp1.replace("<html>","");
        temp1=temp1.replace("</html>","");
        temp1=temp1.replace("<body>","");
        temp1=temp1.replace("</body>","");
        temp1=temp1.replace("<head>","");
        temp1=temp1.replace("</head>","");
        temp1=temp1.trim();
        JSONArray jsonObject = JSON.parseArray(temp1);
        for (int i = 0; i < jsonObject.size(); i++) {
            JSONObject jo = jsonObject.getJSONObject(i);
            String str=jo.getString("provinceName");
            if (str.equals(key)){
                JSONArray cities = JSON.parseArray(jo.getString("cities"));
                for (int j = 0; j < cities.size(); j++) {
                    JSONObject resultJson = cities.getJSONObject(j);
                    String temp=resultJson.getString("cityName")+",确诊数量："
                            +resultJson.getString("confirmedCount")+",治愈数量："
                            +resultJson.getString("curedCount")+",死亡数量："
                            +resultJson.getString("deadCount");
                    System.out.println(temp+"121212");
                    jsonList.add(temp);
                }
            }
        }
        return jsonList;

    }

}
