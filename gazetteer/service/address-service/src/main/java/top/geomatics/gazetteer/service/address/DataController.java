package top.geomatics.gazetteer.service.address;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.utilities.database.BatchDealExcel;

//数据导入导出
@Api(value = "/data", tags = "地址数据上传下载")
@Controller
@RequestMapping("/data")
public class DataController {
	
	/*获取file.html页面
     */
//    @RequestMapping("/file")
//    public String file(){
//        return "file";
//    }
	
    @ApiOperation(value = "上传地址数据", notes = "上传地址数据")
    @PostMapping("/fileUpload")
    @ResponseBody 
    public String fileUpload(@RequestParam("fileName") MultipartFile file){
        if(file.isEmpty()){
            return "false";
        }
        //获取文件名到保存到服务器
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);
        //文件上传路径
        String path = "D:/data/upload" ;
        File dest = new File(path + "/" + fileName);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
       
        
        try {
        	
            file.transferTo(dest); //保存文件
            String fileName2="test2.xls";
            BatchDealExcel.batchDealExcel(path + "/" + fileName,path + "/" + fileName2);
            return "true";
            
            
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
        
    }
    
    @ApiOperation(value = "下载地址数据", notes = "下载地址数据")
    @GetMapping("/download/{id}") //id为文件名
    public void download(@ApiParam(value = "测试，文件名输入test")
    		@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
    	//文件地址
    	String folder = "D:/data/upload";
        try (
          //jdk7新特性，可以直接写到try()括号里面，java会自动关闭
         InputStream inputStream = new FileInputStream(new File(folder, id + ".csv"));
         OutputStream outputStream = response.getOutputStream()
        ) {
            //指明为下载
            response.setContentType("application/x-download");
            String fileName = "test.csv";
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);   // 设置文件名

            //把输入流copy到输出流
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }	

}
