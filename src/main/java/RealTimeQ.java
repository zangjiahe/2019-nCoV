import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
@WebServlet(name = "2019-nCov",urlPatterns = "/2019-nCov")
public class RealTimeQ extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String method=req.getParameter("type");
        if (method.equals("0")){
            DIY(req, resp);
        }
        if (method.equals("1")){
            query(req, resp);
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List list=queryRealTime(req.getParameter("provinceName"));
        String result="";
        for (Object str : list) {
            result=result+(String)str+"\n";
        }
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(result);


    }
    public List queryRealTime(String key){
        List jsonList=new ArrayList();
        String json=getJson();
        if (json.equals("信息获取失败！请联系作者QQ：2210074929")){
            jsonList.add(json);
            return jsonList;
        }
        JSONArray jsonObject = JSON.parseArray(json);
        for (int i = 0; i < jsonObject.size(); i++) {
            JSONObject jo = jsonObject.getJSONObject(i);
            String str=jo.getString("provinceName");
            if (str.equals(key)){
                System.out.println(key);
                JSONArray cities = JSON.parseArray(jo.getString("cities"));
                for (Iterator iterator = cities.iterator(); iterator.hasNext();) {
                    JSONObject resultJson = (JSONObject) iterator.next();
                    String temp=resultJson.getString("cityName")+",确诊数量："
                            +resultJson.getString("confirmedCount")+",治愈数量："
                            +resultJson.getString("curedCount")+",死亡数量："
                            +resultJson.getString("deadCount");
                    jsonList.add(temp);
                }
            }else{
                System.out.println("无此城市，请输入省全称，如：江苏省");
                String temp="无此城市，请输入省全称，如：江苏省";
                jsonList.add(temp);
                return jsonList;
            }
        }


        return jsonList;
    }

    public void DIY(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/json;charset=utf-8");
        resp.getWriter().write(getJson());
    }
    //处理返回结果
    public  String getJson(){
        String json="null";

        //初始化json为网页全代码
        json=getHTML();
        if (json.equals("")||json==null){
            json="信息获取失败！请联系作者QQ：2210074929";
            return json;
        }
        Pattern pattern = Pattern.compile("<script id=\"getAreaStat\">(.*?)</script>");
        Matcher matcher = pattern.matcher(json);
        if(matcher.find()){
            json=matcher.group(1);
            try {
                ChangeCharset changeCharset=new ChangeCharset();
                json=changeCharset.toUTF_8(json);
//                json= UTF82GBK(json);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        json=json.replace("try { window.getAreaStat = ","");
        json=json.replace("}catch(e){}","");
        return json;
    }
    //获取网页源代码
    public static String getHTML(){

        //1.确定url
        //String url = "https://picjumbo.com/free-stock-photos/love/";
        String url = "https://3g.dxy.cn/newh5/view/pneumonia";
        //2.设置请求方式
        HttpGet hg = new HttpGet(url);
       // hg.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
        //3.创建发送对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //4.发送请求,获取响应对象
        //CloseableHttpResponse里面封装了响应头响应体响应行
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(hg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html="";
        //5.解析响应对象
        if (response.getStatusLine().getStatusCode() == 200) {
            //获取响应体
            HttpEntity entity = response.getEntity();
            try {
                //解码响应体（根据网页的编码解码）
                html = EntityUtils.toString(entity,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //关闭发送对象
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return html;
    }
    //utf-8转gbk
    public static String UTF82GBK(String data) throws UnsupportedEncodingException {
        System.out.println("Default Charset=" + Charset.defaultCharset());
        //思路：先转为Unicode，然后转为GBK
        String utf8 = new String(data.getBytes( "UTF-8"));
        //等同于:
//        String utf8 = new String(t.getBytes( "UTF-8"),Charset.defaultCharset());

        System.out.println(utf8);
        String unicode = new String(utf8.getBytes(),"UTF-8");
        //等同于:
//        String unicode = new String(utf8.getBytes(Charset.defaultCharset()),"UTF-8");
        System.out.println(unicode);
        String gbk = new String(unicode.getBytes("GBK"));
        //等同于:
//        String gbk = new String(unicode.getBytes("GBK"),Charset.defaultCharset());

        return utf8;
    }
}
