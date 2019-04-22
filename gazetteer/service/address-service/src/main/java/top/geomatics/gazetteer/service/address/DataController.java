package top.geomatics.gazetteer.service.address;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;
import top.geomatics.gazetteer.model.MatcherResultRow;
import top.geomatics.gazetteer.utilities.database.excel.BatchDealExcel;

//数据导入导出
@Api(value = "/data", tags = "地址数据上传下载")
@Controller
@RequestMapping("/data")
public class DataController {

	/*
	 * 获取file.html页面
	 */
//    @RequestMapping("/file")
//    public String file(){
//        return "file";
//    }
	@ApiIgnore
	@ApiOperation(value = "上传地址数据", notes = "上传地址数据")
	@PostMapping("/fileUpload")
	@ResponseBody
	public String fileUpload(@RequestParam("fileName") MultipartFile file) {
		if (file.isEmpty()) {
			return "false";
		}
		// 获取文件名到保存到服务器
		String fileName = file.getOriginalFilename();
		int size = (int) file.getSize();
		System.out.println(fileName + "-->" + size);
		// 文件上传路径
		String path = "D:/data/upload";
		File dest = new File(path + "/" + fileName);
		if (!dest.getParentFile().exists()) { // 判断文件父目录是否存在
			dest.getParentFile().mkdir();
		}

		try {

			file.transferTo(dest); // 保存文件
			String fileName2 = "test2.xls";
			BatchDealExcel.batchDealExcel(path + "/" + fileName, path + "/" + fileName2);
			return "true";

		} catch (IllegalStateException e) {
			e.printStackTrace();
			return "false";
		} catch (IOException e) {
			e.printStackTrace();
			return "false";
		}

	}

	@ApiOperation(value = "上传批量匹配处理数据", notes = "上传批量匹配处理数据")
	@PostMapping("/upload/matcher")
	@ResponseBody
	public String uploadMatcherFile(@RequestParam("fileName") MultipartFile file) {
		if (file.isEmpty()) {
			// 日志
		}
		// 服务器上存储的文件名
		UUID uuid = UUID.randomUUID();
		String fileName = uuid + ".xlsx";
		// 文件上传路径
		String path = "D:/data/upload";
		String sourceFilePath = path + File.separator + fileName;
		File dest = new File(sourceFilePath);
		if (!dest.getParentFile().exists()) { // 判断文件父目录是否存在
			dest.getParentFile().mkdir();
		}

		try {
			// 保存文件
			file.transferTo(dest);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileName2 = uuid + "_result.xlsx";
		String destFilePath = path + File.separator + fileName2;
		List<MatcherResultRow> allRows = BatchDealExcel.batchDealExcel(sourceFilePath, destFilePath);

		return ControllerUtils.getResponseBody6(allRows);
	}

	@ApiOperation(value = "下载地址数据", notes = "下载地址数据")
	@GetMapping("/download/{id}") // id为文件名
	public void download(@ApiParam(value = "测试，文件名输入test") @PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		// 文件地址
		String folder = "D:/data/upload";
		try (
				// jdk7新特性，可以直接写到try()括号里面，java会自动关闭
				InputStream inputStream = new FileInputStream(new File(folder, id + ".csv"));
				OutputStream outputStream = response.getOutputStream()) {
			// 指明为下载
			response.setContentType("application/x-download");
			String fileName = "test.csv";
			response.addHeader("Content-Disposition", "attachment;fileName=" + fileName); // 设置文件名

			// 把输入流copy到输出流
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
