package org.zyx.controller;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.zyx.entity.WxParam;
import org.zyx.service.WxService;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by SunShine on 2020/4/21.
 */
@Controller
public class WxHandler {

    @Autowired
    private WxService wxService;

    @Value("${token}")
    private String token;

    @GetMapping("/")
    public void index(WxParam wxParam, Model model, HttpServletResponse response){

        wxParam.setToken(token);//基于微信发送请求的token
        System.out.println(wxParam);

        if(wxService.check(wxParam)){
            System.out.println("验证成功");

            //原样返回echostr
            try {
                //返回该数据后就完成了配置
                PrintWriter out=response.getWriter();
                out.print(wxParam.getEchostr());
                out.flush();
                out.close();

                System.out.println("接入成功");

            } catch (IOException e) {
                e.printStackTrace();
            }


        }else{
            System.out.println("验证失败");
        }

    }

    @PostMapping("/")
    public void getMsg(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException {
        System.out.println("接收到post");

        /**
             <xml><ToUserName><![CDATA[gh_cb165d6894a2]]></ToUserName>
             <FromUserName><![CDATA[oLlGc0qvZLtXmJek8eV50LF4xu2k]]></FromUserName>
             <CreateTime>1587459265</CreateTime>
             <MsgType><![CDATA[text]]></MsgType>
             <Content><![CDATA[测试测试]]></Content>
             <MsgId>22726951286407020</MsgId>
             </xml>
         */
        //解析xml消息包(引入dom4j依赖)
        //处理消息和事件推送
        Map<String,String> msg=wxService.parseRequest(request);
        System.out.println(msg);


        //消息包测试
//        ServletInputStream inputStream=request.getInputStream();
//        byte[] bytes=new byte[1024];
//        int len=0;
//        StringBuffer sb=new StringBuffer();
//        while((len=inputStream.read(bytes))!=-1){
//            sb.append(new String(bytes,0,len));
//        }
//        System.out.println(sb.toString());

    }



}
