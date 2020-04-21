package org.zyx.service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.zyx.entity.WxParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SunShine on 2020/4/21.
 */
@Service
public class WxService {


    /**
     * 开发者通过检验signature对请求进行校验
     * 验证签名
     * @param wxParam
     * @return
     */
    public boolean check(WxParam wxParam){
        /**
         * 1）将token、timestamp、nonce三个参数进行字典序排序
         * 2）将三个参数字符串拼接成一个字符串进行sha1加密
         * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
         */
        //1.字典排序
        String strs[]=new String[]{wxParam.getToken(),wxParam.getTimestamp(),wxParam.getNonce()};
        Arrays.sort(strs);//字典排序
        System.out.println(strs);
        String encryption=strs[0]+strs[1]+strs[2];
        //2.进行sha1加密
        encryption =sha1(encryption);

        //加密后的字符串可与signature对比
        if(encryption.equals(wxParam.getSignature())){
            //表示该请求来自微信
            System.out.println("请求来自微信");
            return true;
        }


        return false;
    }


    public String sha1(String encryption){

        try {
            //获取一个加密对象
            MessageDigest md=MessageDigest.getInstance("sha1");//也可以填写md5加密
            //加密
            byte[] digest = md.digest(encryption.getBytes());

            char[] chars={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            StringBuffer sb=new StringBuffer();
            //处理加密结果
            //1.遍历字节数组,8字节,分别对前四位和后四位进行加密处理
            for (byte b : digest){
                //右移4位,前4个字节移动到后四位,按位&15(1111),去除前4位,方位0到15
                sb.append(chars [(b>>4)&15]);
                //处理后四位
                sb.append(chars[b&15]);
            }
            System.out.println(sb.toString());

            return sb.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return encryption;
    }

    /**
     * 解析xml数据包
     * @param request
     * @return
     * @throws IOException
     */
    public Map<String,String> parseRequest(HttpServletRequest request) throws IOException, DocumentException {

        Map<String,String> map=new HashMap<String,String>();

        InputStream is=request.getInputStream();
        //1.获取sax解析器对象
        SAXReader saxReader=new SAXReader();
        //2.读取输入流获取文档对象
        Document document = saxReader.read(is);
        //3.根据文档对象获取根节点
        Element root=document.getRootElement();
        //4.获取所有子节点
        List<Element> elements=root.elements();
        //5.遍历获取所有子节点
        for (Element e: elements) {
            map.put(e.getName(),e.getStringValue());
        }
        return map;
    }
}
