package utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * 图片的Base64加解码
 * @author qzz
 */
public class Base64ImageUtil {

    /**
     * 远程服务器图片转Base64字符串
     * @param imageUrl
     * @return
     */
    public static String getImageBase64Str(String imageUrl){
        //将图片文件转化为字节数组字符串，并进行Base64编码处理
        byte[] data=null;
        InputStream in=null;
        ByteArrayOutputStream out=null;
        //1.读取图片字节数组
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            in = connection.getInputStream();
            out = new ByteArrayOutputStream();

            data=new byte[1024];
            int len = 0;
            while((len = in.read(data))!=-1){
                out.write(data,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //2.对字节数组进行Base64编码
        String base64Image = new String(Base64.getEncoder().encode(out.toByteArray()));
        //如果需要将得到的 base64 字符串展示在 html 的 img 标签中，需要在字符串中添加 data:image/jpg;base64, 前缀
        return base64Image;
    }

    /**
     * Base64字符串转图片
     * @param base64Image Base64字符串
     * @param imagePath   转换完之后的图片存储地址
     * @return
     */
    public static void getImageUrlFormBase64(String base64Image,String imagePath){
        if(base64Image==null){
            return;
        }
        OutputStream out=null;
        try {
            byte[] data=Base64.getDecoder().decode(base64Image);
            for(int i=0;i<data.length;i++){
                if(data[i]<0){
                    data[i]+=256;
                }
            }
            out=new FileOutputStream(imagePath);
            out.write(data);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //图片编码
        String base64Image=Base64ImageUtil.getImageBase64Str("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
        System.out.println("图片url转Base64字符串:"+base64Image);

        //图片解码
        Base64ImageUtil.getImageUrlFormBase64(base64Image,"F:\\image\\b1.png");
    }
}
