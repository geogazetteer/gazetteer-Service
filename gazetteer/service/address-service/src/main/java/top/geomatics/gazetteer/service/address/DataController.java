package top.geomatics.gazetteer.service.address;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;
import top.geomatics.gazetteer.config.ResourcesManager2;
import top.geomatics.gazetteer.model.MatcherResultRow;
import top.geomatics.gazetteer.utilities.database.excel.BatchDealExcel;
import top.geomatics.gazetteer.utilities.database.excel2gpkg.Excel2Geopackage;
import top.geomatics.gazetteer.utilities.database.shp2gpkg.Shapefile2Geopackage;

/**
 * <b>地址数据上传下载服务类</b><br>
 * 
 * @author whudyj
 */
@Api(value = "/data", tags = "地址数据上传下载")
@Controller
@RequestMapping("/data")
public class DataController {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(DataController.class);

	private String username = "user_admin";// 缺省用户
//	{
//		username = CurrentSession.getCurrentUserName();//当前登录用户
//	}

	private static final String EDIT_DB_PROPERTIES = "editor_db_properties_file";

	private ResourcesManager2 rm = new ResourcesManager2(username);
	// 文件上传路径
	private String upload_file_path = rm.getValue(Messages.getString("DataController.0")); //$NON-NLS-1$
	// 文件下载路径
	private String download_file_path = rm.getValue("download_file_path");

	/**
	 * <b>上传数据文件</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为excel格式</i><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/upload?fileName=
	 * 
	 * @param file MultipartFile 请求参数，前台上传的文件
	 * @return String 返回JSON格式的上传结果
	 */
	@ApiOperation(value = "上传地址数据", notes = "上传地址数据")
	@PostMapping("/upload")
	@ResponseBody
	public String fileUpload(@ApiParam(value = "前台上传的文件") @RequestParam("fileName") MultipartFile file) {
		if (file.isEmpty()) {
			// 日志
			String logMsgString = Messages.getString("DataController.1"); //$NON-NLS-1$
			logger.error(logMsgString);
			return ""; //$NON-NLS-1$
		}
		// 服务器上存储的文件名
		UUID uuid = UUID.randomUUID();
		String fileName = uuid + Messages.getString("DataController.3"); //$NON-NLS-1$

		String sourceFilePath = upload_file_path + File.separator + fileName;
		// 判断文件父目录是否存在，如果不存在，则创建目录
		File dest = new File(sourceFilePath);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdir();
		}

		try {
			// 保存文件
			file.transferTo(dest);
			String logMsgString = Messages.getString("DataController.4"); //$NON-NLS-1$
			logger.info(logMsgString);
			return logMsgString;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ""; //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ""; //$NON-NLS-1$
		}

	}

	/**
	 * <b>上传批量匹配处理数据文件</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为excel格式</i><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/upload/matcher
	 * 
	 * @param file MultipartFile 请求参数，前台上传的文件
	 * @return String 返回JSON格式的批量匹配处理结果
	 */
	@ApiOperation(value = "上传批量匹配处理数据", notes = "上传批量匹配处理数据")
	@PostMapping("/upload/matcher")
	@ResponseBody
	public String uploadMatcherFile(@ApiParam(value = "前台上传的文件") @RequestParam("fileName") MultipartFile file) {
		if (file.isEmpty()) {
			// 日志
			String logMsgString = Messages.getString("DataController.7"); //$NON-NLS-1$
			logger.error(logMsgString);
			return ""; //$NON-NLS-1$
		}
		// 服务器上存储的文件名
		UUID uuid = UUID.randomUUID();
		String fileName = uuid + Messages.getString("DataController.9"); //$NON-NLS-1$

		String sourceFilePath = upload_file_path + File.separator + fileName;
		File dest = new File(sourceFilePath);
		// 判断文件父目录是否存在，如果不存在，则创建目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdir();
		}

		try {
			// 保存文件
			file.transferTo(dest);
		} catch (Exception e) {
			e.printStackTrace();
			// 日志
			String logMsgString = Messages.getString("DataController.10"); //$NON-NLS-1$
			logger.error(logMsgString);
			return ""; //$NON-NLS-1$
		}
		// 批量匹配处理数据
		String fileName2 = uuid + Messages.getString("DataController.12"); //$NON-NLS-1$
		String destFilePath = upload_file_path + File.separator + fileName2;
		List<MatcherResultRow> allRows = BatchDealExcel.batchDealExcel(sourceFilePath, destFilePath);

		return ControllerUtils.getResponseBody6(allRows);
	}

	/**
	 * <b>下载数据文件</b><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/download/
	 * 
	 * @param id String 路径变量，服务器上下载文件名
	 */
	@ApiIgnore
	@ApiOperation(value = "下载数据", notes = "下载数据")
	@GetMapping("/download/{id}")
	public void download(@ApiParam(value = "下载文件名") @PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		// 文件地址
		String folder = upload_file_path;
		try (
				// jdk7新特性，可以直接写到try()括号里面，java会自动关闭
				InputStream inputStream = new FileInputStream(new File(folder, id));
				OutputStream outputStream = response.getOutputStream()) {
			// 指明为下载
			response.setContentType(Messages.getString("DataController.13")); //$NON-NLS-1$
			response.addHeader(Messages.getString("DataController.14"), Messages.getString("DataController.15") + id); // 设置文件名 //$NON-NLS-1$ //$NON-NLS-2$

			// 把输入流copy到输出流
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * long start = System.currentTimeMillis(); String path = upload_file_path +
		 * File.separator + id; File file = new File(path); file.mkdir();
		 * 
		 * String xlsfile = upload_file_path + File.separator + id + ".xlsx";
		 * System.out.println(xlsfile); String shppath = path + "/shp_" + id + ".shp";
		 * System.out.println(shppath); // excel转shp Excel2Shp.excel2Shp(xlsfile,
		 * shppath);
		 * 
		 * // 将生成的shp文件压缩 String sourcePath = upload_file_path + File.separator + id;
		 * String zipName = "shp_" + id; ShpZip.createCardImgZip(sourcePath, zipName);
		 * long end = System.currentTimeMillis(); System.out.println(end - start);
		 */

	}

	/**
	 * <b>获得数据文件中的字段</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为excel格式或shapefile格式<br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/fields?fileName= </i>
	 * 
	 * @param file String 请求参数，文件名
	 * @return String 返回JSON格式的结果
	 */
	@ApiOperation(value = "获得数据文件中的字段", notes = "获得数据文件中的字段")
	@GetMapping("/fields")
	@ResponseBody
	public String getFields(@ApiParam(value = "文件名") @RequestParam("fileName") String fileName) {
		String ffn = upload_file_path + File.separator + fileName;
		File file = new File(ffn);

		if (!file.exists()) {
			// 日志
			String logMsgString = String.format("文件 %s 不存在", fileName);
			logger.error(logMsgString);
			return "";
		}
		String ftype = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
		List<String> fields = null;
		if (ftype.compareToIgnoreCase("shp") == 0) {
			Shapefile2Geopackage s2g = new Shapefile2Geopackage(ffn);
			fields = s2g.getFields();
		} else if (ftype.compareToIgnoreCase("xlsx") == 0) {
			Excel2Geopackage x2g = new Excel2Geopackage(ffn);
			fields = x2g.getFields();
		}
		if (null != fields && fields.size() > 0) {
			return JSON.toJSONString(fields);
		} else {
			return "";
		}

	}

	/**
	 * <b>列出已经上传的数据文件</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为excel格式或shapefile格式<br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/source </i>
	 * 
	 * @return String 返回JSON格式的结果
	 */
	@ApiOperation(value = "列出已经上传的数据文件", notes = "列出已经上传的数据文件")
	@GetMapping("/source")
	@ResponseBody
	public String getSource() {
		File file = new File(upload_file_path);

		if (!file.exists()) {
			// 日志
			String logMsgString = String.format("文件路径 %s 不存在", upload_file_path);
			logger.error(logMsgString);
			return "";
		}
		List<String> fs = new ArrayList<String>();
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				String fileName = f.getName();
				String ftype = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
				if (ftype.compareToIgnoreCase("shp") == 0 || ftype.compareToIgnoreCase("xlsx") == 0) {
					fs.add(fileName);
				}
			}
			// 暂时不考虑子目录

		}
		if (fs.size() > 0) {
			return JSON.toJSONString(fs);
		} else {
			return "";
		}

	}

	/**
	 * <b>列出可以下载的数据文件</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为geopackage格式<br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/target </i>
	 * 
	 * @return String 返回JSON格式的结果
	 */
	@ApiOperation(value = "列出可以下载的数据文件", notes = "列出可以下载的数据文件")
	@GetMapping("/target")
	@ResponseBody
	public String getTarget() {
		File file = new File(download_file_path);

		if (!file.exists()) {
			// 日志
			String logMsgString = String.format("文件路径 %s 不存在", download_file_path);
			logger.error(logMsgString);
			return "";
		}
		List<String> fs = new ArrayList<String>();
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				String fileName = f.getName();
				String ftype = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
				if (ftype.compareToIgnoreCase("gpkg") == 0) {
					fs.add(fileName);
				}
			}
			// 暂时不考虑子目录

		}
		if (fs.size() > 0) {
			return JSON.toJSONString(fs);
		} else {
			return "";
		}

	}

	/**
	 * <b>设置需要编辑的数据文件</b><br>
	 * 
	 * <i>examples:<br>
	 * http://localhost:8083/data/settings?fileName= </i>
	 * 
	 * @param fileName String 需要编辑的数据文件名
	 */
	@ApiOperation(value = "设置需要编辑的数据文件", notes = "设置需要编辑的数据文件")
	@PutMapping(value = "/settings")
	public ResponseEntity<String> setRevisionFile(
			@ApiParam(value = "需要编辑的数据文件名") @RequestParam("fileName") String fileName) {
		// 文件目录
		String folder = download_file_path;
		File file = new File(folder, fileName);
		if (!file.exists()) {
			// 日志
			String logMsgString = String.format("数据文件 %s 不存在，请重新设置！", fileName);
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}
		// 修改相应的配置文件
		String db_properties = rm.getValue(EDIT_DB_PROPERTIES);
		Properties prop = new Properties();
		try {
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(db_properties), "UTF-8")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("配置文件 %s 不存在！", db_properties);
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("打开配置文件 %s 失败！", db_properties);
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}
		String value = "jdbc:sqlite:";
		value = value + folder + File.separator + fileName;
		prop.setProperty("url", value);

		File pf = new File(db_properties);

		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pf), "UTF-8"));
			prop.store(out, String.format("update database URL"));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("修改配置文件 %s 失败！", db_properties);
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>("设置数据文件成功!", HttpStatus.OK);

	}

	/**
	 * <b>导出数据</b><br>
	 * 
	 * <i>examples:<br>
	 * http://localhost:8083/data/export?fileName= </i>
	 * 
	 * @param fileName String 需要下载的数据文件名
	 */
	@ApiOperation(value = "导出数据", notes = "导出数据")
	@GetMapping(value = "/export")
	public void export(@ApiParam(value = "需要下载的数据文件名") @RequestParam("fileName") String fileName,
			HttpServletResponse response) {
		// 文件目录
		String folder = download_file_path;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(folder, fileName));
			OutputStream outputStream = response.getOutputStream();
			// 指明为下载
			response.setContentType(Messages.getString("DataController.13")); //$NON-NLS-1$
			// 设置文件名
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

			// 把输入流copy到输出流
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("导出数据文件 %s 失败！", fileName);
			logger.error(logMsgString);
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				inputStream = null;
			}
		}

	}

	/**
	 * <b>导入数据文件</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为excel格式或shapefile格式</i><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/import
	 * 
	 * @param file MultipartFile 请求参数，前台上传的文件
	 * @return 返回处理结果
	 */
	@ApiOperation(value = "导入数据文件", notes = "导入数据文件")
	@PostMapping("/import")
	public ResponseEntity<String> importData(@ApiParam(value = "上传文件") @RequestParam("fileName") MultipartFile file) {
		if (file.isEmpty()) {
			// 日志
			String logMsgString = Messages.getString("DataController.7"); //$NON-NLS-1$
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}
		// 服务器上存储的文件名
		String sfn = file.getOriginalFilename();
		File sf = new File(sfn);
		String dfn = sf.getName();
		String destFN = upload_file_path + File.separator + dfn;
		File dest = new File(destFN);
		// 判断文件父目录是否存在，如果不存在，则创建目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdir();
		}
		dest.setWritable(true);

		try {
			// 保存文件
			file.transferTo(dest);
		} catch (Exception e) {
			e.printStackTrace();
			// 日志
			String logMsgString = Messages.getString("DataController.10"); //$NON-NLS-1$
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.FORBIDDEN);
		}
		// 处理数据
		String ftype = dfn.substring(dfn.lastIndexOf('.') + 1, dfn.length());
		// 如果是压缩文件，需要解压缩
		if (ftype.compareToIgnoreCase("zip") == 0) {
			List<String> unzippedFiles = unzip(destFN, upload_file_path);
			for (String impString : unzippedFiles) {
				if (impString.contains("import.xml")) {
					// 转换到数据库
					dataImport(impString);
				}
			}
		} else if (dfn.contains("import.xml")) {
			// 转换到数据库
			dataImport(dfn);
		}

		// 修改用户配置文件

		return new ResponseEntity<>("数据上传成功!", HttpStatus.OK);
	}

	private boolean dataImport(String importXML) {
		String destFN = upload_file_path + File.separator + importXML;
		File xmlFile = new File(destFN);
		if (!xmlFile.exists()) {
			return false;
		}
		Document document = load(destFN);
		Element root = document.getRootElement();
		List<Element> nodes = root.elements("DataFile");
		Iterator<Element> it = nodes.iterator();
		while (it.hasNext()) {
			Element elm = it.next();
			// 文件名和设置
			String fnString = "";
			String originString = "";
			String XString = "";
			String YString = "";
			String geometryString = "";
			List<Element> fElements = elm.elements("FileName");
			if (fElements.size() > 0) {
				fnString = fElements.get(0).getText();
			} else {
				continue;
			}
			if (fnString.trim().isEmpty()) {
				continue;
			}
			List<Element> sElements = elm.elements("Settings");
			if (sElements.size() > 0) {
				Element se = sElements.get(0);
				List<Element> se2 = se.elements("Fields");
				if (se2.size() > 0) {
					Element se3 = se2.get(0);
					List<Element> lse3 = se3.elements("Field");
					if (lse3.size() > 0) {
						Element se4 = lse3.get(0);
						List<Element> lse4_o = se4.elements("origin_address");
						List<Element> lse4_x = se4.elements("longitude");
						List<Element> lse4_y = se4.elements("latitude");
						List<Element> lse4_g = se4.elements("build_geometry");
						if (lse4_o.size() > 0) {
							Element se5 = lse4_o.get(0);
							originString = se5.getText();
						}
						if (lse4_x.size() > 0) {
							Element se5 = lse4_x.get(0);
							XString = se5.getText();
						}
						if (lse4_y.size() > 0) {
							Element se5 = lse4_y.get(0);
							YString = se5.getText();
						}
						if (lse4_g.size() > 0) {
							Element se5 = lse4_g.get(0);
							geometryString = se5.getText();
						}
					}
				}

			}
			String fname = fnString.substring(0, fnString.lastIndexOf('.'));
			String ftype = fnString.substring(fnString.lastIndexOf('.') + 1, fnString.length());
			String sourceFN = upload_file_path + File.separator + fnString;
			String targetFN = download_file_path + File.separator + fname + ".gpkg";
			Map<String, String> settings = new HashMap<String, String>();
			if (!originString.isEmpty()) {
				settings.put(originString, "origin_address");
			}
			if (!XString.isEmpty()) {
				settings.put(XString, "longitude_");
			}
			if (!YString.isEmpty()) {
				settings.put(YString, "latitude_");
			}
			if (!geometryString.isEmpty()) {
				// settings.put("buildGeometry", geometryString);
			}
			if (ftype.compareToIgnoreCase("shp") == 0) {
				Shapefile2Geopackage s2g = new Shapefile2Geopackage(sourceFN, targetFN, settings);
				s2g.execute();
			} else if (ftype.compareToIgnoreCase("xlsx") == 0) {
				Excel2Geopackage s2g = new Excel2Geopackage(sourceFN, targetFN, settings);
				s2g.execute();
			}

		}
		return true;
	}

	private Document load(String filename) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(filename)); // 读取XML文件,获得document对象
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}

	/**
	 * 解压Zip文件
	 * 
	 * @param zipFile 需要解压的zip文件位置
	 * @param destDir 解压的目标位置
	 */
	private List<String> unzip(String zipFile, String destDir) {
		File f;
		List<String> fileNames = new ArrayList<String>();
		try (ArchiveInputStream i = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP,
				Files.newInputStream(Paths.get(zipFile)), "GBK")) {
			ArchiveEntry entry = null;
			while ((entry = i.getNextEntry()) != null) {
				if (!i.canReadEntryData(entry)) {
					continue;
				}
				fileNames.add(entry.getName());
				f = new File(destDir, entry.getName());
				if (entry.isDirectory()) {
					if (!f.isDirectory() && !f.mkdirs()) {
						throw new IOException("failed to create directory " + f);
					}
				} else {
					File parent = f.getParentFile();
					if (!parent.isDirectory() && !parent.mkdirs()) {
						throw new IOException("failed to create directory " + parent);
					}
					try (OutputStream o = Files.newOutputStream(f.toPath())) {
						IOUtils.copy(i, o);
					}
				}
			}
		} catch (IOException | ArchiveException e) {
			e.printStackTrace();
		}
		return fileNames;
	}

}
