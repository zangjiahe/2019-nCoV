# 2019-nCoV
2019-nCov-API，新型冠型肺炎疫情在线查询API，数据来源：丁香园，代码编写不交粗糙，没有进行代码优化，本程序可随意进行二次开发
应用技术：Java+Maven

需要修改API.java中的两处域名，本API支持两种查询
    一、支持按照省份(直辖市)查询 ：域名/2019_nCov/api?provinceName=省份（如果是直辖市 需要些市  如上海市）
    二、支持按照城市查询  ：域名/2019_nCov/api?provinceName=城市名称&type=2 
    三、支持自己DIY编写查询API：域名/2019_nCov/2019-nCov?type=0 
    
    参数：type：查询类型： 0 为DIY接口 GET请求得到的是最新的整个json格式接口，默认返回的有HTML的格式，如请求只需要json 
                          可将RealTimeQ.java中DIY方法中的resp.setContentType("text/json;charset=utf-8");更换为
                          resp.setContentType("application/json;charset=utf-8");即可。
                          1 为按照省份查询     
                          2 为按照城市查询
          provinceName：城市或省份名称
          两种方式 请求方式都为GET
          
          
          
    返回结果为json 但是没有按json的格式来写，有兴趣的可以自己写一写 无非就是字符串拼接而已
    有不明白的可以私聊我咨询 QQ2210074929
    
