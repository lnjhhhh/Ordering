package cn.com.scitc.admin;

import cn.com.scitc.dao.FoodDao;
import cn.com.scitc.model.Food;
import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FoodEditServlet",urlPatterns = "/filterAdmin/foodEdit")
public class FoodEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletConfig servletConfig;

    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //新建一个SmartUpload对象
            SmartUpload su = new SmartUpload();
            //上传初始化
            su.initialize(servletConfig, request, response);
            //限制每个上传文件的最大长度
            su.setMaxFileSize(1000000);
            //限制总上传数据的长度
//			su.setTotalMaxFileSize(200000);
            //设定允许上传的文件（通过扩展名限制），仅允许jpg，png文件
//			su.setAllowedFilesList("jpg,png");
            //设定禁止上传的文件（通过扩展名限制），禁止上传带有exe,bat,jsp,htm,html扩展名的文件和没有扩展名的文件
            su.setDeniedFilesList("exe,bat,jsp,htm,html");
            //上传文件
            su.upload();
            su.save("/img/");
            //获取上传的文件操作
            Files files = su.getFiles();
            //获取单个文件
            File singleFile = files.getFile(0);
            //获取上传文件的扩展名
            String fileType = singleFile.getFileExt();
            //设置上传文件的扩展名
            String[] type = {"JPG","jpg"};
            // 判断上传文件的类型是否正确
            int place = java.util.Arrays.binarySearch(type, fileType);
            //判断文件扩展名是否正确
            if (place != -1){
                //判断该文件是否被选择
                if (!singleFile.isMissing()){
//					String picSize = String.valueOf(singleFile.getSize());

                    //以系统时间作为上传文件名称，设置上传完整路径
                    String fileName = String.valueOf(System.currentTimeMillis());
                    String filedir =  fileName + "." + singleFile.getFileExt();
//					String smalldir = "samllImages/" + fileName + "." + singleFile.getFileExt();


//					String sql = "INSERT INTO image(image) VALUES(" + fileName + ")";
                    request.setCharacterEncoding("gbk");
                    //执行上传操作
                    singleFile.saveAs("/img/" + filedir, File.SAVEAS_VIRTUAL);
                    System.out.println("上传至： " + filedir);
                    String id = su.getRequest().getParameter("id");
                    Integer ids = Integer.parseInt(id);
                    String f_name = su.getRequest().getParameter("f_name");
                     String price = su.getRequest().getParameter("price");
                    String f_content = su.getRequest().getParameter("f_content");

                    Food food = new FoodDao().searchById(ids);
                    food.setF_name(f_name);
                    food.setF_content(f_content);
                    Integer jg = Integer.parseInt(price);
                    food.setPrice(jg);
                    food.setF_image(fileName);
                    FoodDao dao = new FoodDao();
                    dao.saveFood(food);
                    System.out.println("FoodName: " + food.getF_name());
                    System.out.println("menuPrice: " + food.getPrice());
                    System.out.println("menuNotice: " + food.getF_content());
                    System.out.println(food.getF_image());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<script language='javascript'>alert('修改成功');window.location.href='editFood';</script>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
        try{
            Integer id = Integer.parseInt(request.getParameter("id"));
            Food food = new FoodDao().searchById(id);
            food.setF_image(request.getParameter("f_image"));
            System.out.println(food.getF_image());
            food.setF_name(request.getParameter("f_name"));
            food.setPrice(Integer.parseInt(request.getParameter("price")));
            food.setF_content(request.getParameter("f_content"));
            new FoodDao().saveFood(food);
            System.out.println("更新成功");
        }catch (Exception er){
            er.printStackTrace();
        }
    */
    }
}
