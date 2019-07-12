package top.geomatics.gazetteer.service.address;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;
import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.MatcherResultRow;
import top.geomatics.gazetteer.model.user.User;
import top.geomatics.gazetteer.service.user.UserInformation;
import top.geomatics.gazetteer.utilities.database.excel.BatchDealExcel;
import top.geomatics.gazetteer.utilities.database.excel2gpkg.Excel2Geopackage;
import top.geomatics.gazetteer.utilities.database.shp2gpkg.Shapefile2Geopackage;

/**
 * <b>地名地址数据服务类</b><br>
 * 
 * @author whudyj
 */
@Api(value = "/data", tags = "地名地址数据服务")
@RestController
@RequestMapping("/data")
public class DataController {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(DataController.class);

	private static final String UP_PATH = "upload_file_path";
	private static final String DEFAULT_USERNAME = "user_admin";

	/**
	 * <b>上传数据文件</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为excel格式，文件上传到服务器上固定位置</i><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/upload?fileName=
	 * 
	 * @param file MultipartFile 请求参数，前台上传的文件
	 * @return String 返回JSON格式的上传结果
	 */
	@ApiOperation(value = "上传地址数据", notes = "上传地址数据")
	@PostMapping("/upload")
	public ResponseEntity<String> fileUpload(
			@ApiParam(value = "上传数据文件") @RequestParam(value = "fileName", required = true) MultipartFile file) {
		if (file.isEmpty()) {
			// 日志
			String logMsgString = Messages.getString("DataController.1"); //$NON-NLS-1$
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}
		// 服务器上存储的文件名
		UUID uuid = UUID.randomUUID();
		String fileName = uuid + Messages.getString("DataController.3"); //$NON-NLS-1$

		// 配置的存储路径
		String upload_file_path = ResourcesManager.getInstance().getValue(UP_PATH);
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
			return new ResponseEntity<>(logMsgString, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String logMsgString = String.format("上传文件失败！");
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.EXPECTATION_FAILED);
		}

	}

	/**
	 * <b>上传数据文件，并进行地址批量匹配处理</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为excel格式</i><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/upload/matcher
	 * 
	 * @param file MultipartFile 请求参数，上传的数据文件
	 * @return String 返回JSON格式的批量匹配处理结果
	 */
	@ApiOperation(value = "上传数据文件，并进行地址批量匹配处理", notes = "上传数据文件，并进行地址批量匹配处理")
	@PostMapping("/upload/matcher")
	public ResponseEntity<String> uploadMatcherFile(
			@ApiParam(value = "上传数据文件") @RequestParam("fileName") MultipartFile file) {
		if (file.isEmpty()) {
			// 日志
			String logMsgString = Messages.getString("DataController.7"); //$NON-NLS-1$
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}
		// 服务器上存储的文件名
		UUID uuid = UUID.randomUUID();
		String fileName = uuid + Messages.getString("DataController.9"); //$NON-NLS-1$

		// 配置的存储路径
		String upload_file_path = ResourcesManager.getInstance().getValue(UP_PATH);
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
			return new ResponseEntity<>(logMsgString, HttpStatus.EXPECTATION_FAILED);
		}
		// 批量匹配处理数据
		String fileName2 = uuid + Messages.getString("DataController.12"); //$NON-NLS-1$
		String destFilePath = upload_file_path + File.separator + fileName2;
		List<MatcherResultRow> allRows = BatchDealExcel.batchDealExcel(sourceFilePath, destFilePath);

		String responseString = ControllerUtils.getResponseBody6(allRows);
		return new ResponseEntity<>(responseString, HttpStatus.OK);
	}

	/**
	 * <b>下载数据文件</b><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/download/
	 * 
	 * @param fileName String 请求参数，服务器上需要下载的数据文件名
	 * @param response HttpServletResponse 服务器响应
	 */
	@ApiIgnore
	@ApiOperation(value = "下载数据", notes = "下载数据")
	@GetMapping("/download")
	public ResponseEntity<String> download(
			@ApiParam(value = "需要下载的数据文件名") @RequestParam(value = "fileName", required = true) String fileName,
			HttpServletResponse response) {
		// 配置的数据文件路径
		String folder = ResourcesManager.getInstance().getValue(UP_PATH);
		try (
				// jdk7新特性，可以直接写到try()括号里面，java会自动关闭
				InputStream inputStream = new FileInputStream(new File(folder, fileName));
				OutputStream outputStream = response.getOutputStream()) {
			// 指明为下载
			response.setContentType(Messages.getString("DataController.13")); //$NON-NLS-1$
			response.addHeader(Messages.getString("DataController.14"), //$NON-NLS-1$
					Messages.getString("DataController.15") + fileName); //$NON-NLS-1$

			// 把输入流copy到输出流
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();

		} catch (Exception e) {
			e.printStackTrace();
			String logMsgString = String.format("文件下载失败！");
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>("数据下载成功！", HttpStatus.OK);
	}

	/**
	 * <b>获得数据文件中的字段</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>数据文件格式为excel格式或shapefile格式<br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/data/fields?username=%26fileName= </i>
	 * 
	 * @param username String 请求参数，用户名
	 * @param file     String 请求参数，文件名
	 * @return String 返回JSON格式的结果
	 */
	@ApiOperation(value = "获得数据文件中的字段", notes = "获得数据文件中的字段")
	@GetMapping("/fields")
	public ResponseEntity<String> getFields(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "文件名") @RequestParam(value = "fileName", required = true) String fileName) {
		String upload_file_path = UserManager.getInstance().getUserInfo(username).getUploadPath();
		String ffn = upload_file_path + File.separator + fileName;
		File file = new File(ffn);

		if (!file.exists()) {
			// 日志
			String logMsgString = String.format("文件 %s 不存在", fileName);
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.EXPECTATION_FAILED);
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
			String body = JSON.toJSONString(fields);
			return new ResponseEntity<>(body, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("", HttpStatus.OK);
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
	 * @param username String 请求参数，用户名
	 * @return String 返回JSON格式的结果
	 */
	@ApiOperation(value = "列出已经上传的数据文件", notes = "列出已经上传的数据文件")
	@GetMapping("/source")
	public String getSource(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username) {
		String upload_file_path = UserManager.getInstance().getUserInfo(username).getUploadPath();
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
	 * @param username String 请求参数，用户名
	 * @return String 返回JSON格式的结果
	 */
	@ApiOperation(value = "列出可以下载的数据文件", notes = "列出可以下载的数据文件")
	@GetMapping("/target")
	public String getTarget(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username) {
		String download_file_path = UserManager.getInstance().getUserInfo(username).getDownloadPath();
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
	 * @param username String 请求参数，用户名
	 * @param fileName String 需要编辑的数据文件名
	 */
	@ApiOperation(value = "设置需要编辑的数据文件", notes = "设置需要编辑的数据文件")
	@PutMapping(value = "/settings")
	public ResponseEntity<String> setRevisionFile(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "需要编辑的数据文件名") @RequestParam(value = "fileName", required = true) String fileName) {
		// 文件目录
		String folder = UserManager.getInstance().getUserInfo(username).getDownloadPath();
		File file = new File(folder, fileName);
		if (!file.exists()) {
			// 日志
			String logMsgString = String.format("数据文件 %s 不存在，请重新设置！", fileName);
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}
		// 修改相应的配置文件
		String db_properties = UserManager.getInstance().getUserInfo(username).getDbProperties();

		Parameters db_params = new Parameters();
		File db_propertiesFile = new File(db_properties);

		FileBasedConfigurationBuilder<FileBasedConfiguration> db_builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
				PropertiesConfiguration.class)
						.configure(db_params.fileBased().setFile(db_propertiesFile).setEncoding("UTF-8"));
		db_builder.setAutoSave(true);
		try {
			Configuration db_config = db_builder.getConfiguration();

			String fpath = folder + File.separator + fileName;
			String value = String.format("jdbc:sqlite:%s", fpath);
			db_config.setProperty("url", value);
			// 更新内存中的用户信息
			User user2 = UserManager.getInstance().getUserInfo(username).getUser();
			UserManager.getInstance().addUser(user2);

		} catch (ConfigurationException e1) {
			e1.printStackTrace();
			// 日志
			String logMsgString = String.format("打开配置文件 %s 失败！", db_properties);
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>("设置数据文件成功!", HttpStatus.OK);

	}

	/**
	 * <b>导出数据</b><br>
	 * 
	 * <i>examples:<br>
	 * http://localhost:8083/data/export?fileName= </i>
	 * 
	 * @param username String 请求参数，用户名
	 * @param fileName String 需要下载的数据文件名
	 */
	@ApiOperation(value = "导出数据", notes = "导出数据")
	@GetMapping(value = "/export")
	public void export(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "需要下载的数据文件名") @RequestParam(value = "fileName", required = true) String fileName,
			HttpServletResponse response) {
		// 文件目录
		String folder = UserManager.getInstance().getUserInfo(username).getDownloadPath();
		InputStream inputStream = null;
		String zipFileName = fileName.replace(".gpkg", ".zip");
		File tf = new File(zipFileName);
		if (!tf.exists()) {
			ZipFileUtils.zipFiles(folder + File.separator + fileName, null, false,
					folder + File.separator + zipFileName);
		}

		try {
			inputStream = new FileInputStream(new File(folder, zipFileName));
			OutputStream outputStream = response.getOutputStream();
			// 指明为下载
			response.setContentType(Messages.getString("DataController.13")); //$NON-NLS-1$
			// 设置文件名
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));

			// 把输入流copy到输出流
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("导出数据文件 %s 失败！", zipFileName);
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
	 * @param username String 请求参数，用户名
	 * @param file     MultipartFile 请求参数，前台上传的文件
	 * @return 返回处理结果
	 */
	@ApiOperation(value = "导入数据文件", notes = "导入数据文件")
	@PostMapping("/import")
	public ResponseEntity<String> importData(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "上传文件") @RequestParam(value = "fileName", required = true) MultipartFile file) {
		if (file.isEmpty()) {
			// 日志
			String logMsgString = Messages.getString("DataController.7"); //$NON-NLS-1$
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}
		// 服务器上存储的文件名
		UserInformation userInfo = UserManager.getInstance().getUserInfo(username);
		if (null == userInfo) {
			// 日志
			String logMsgString = String.format("% 请先登录", username);
			logger.error(logMsgString);
			return new ResponseEntity<>(logMsgString, HttpStatus.NOT_FOUND);
		}
		String upload_file_path = userInfo.getUploadPath();
		String download_file_path = userInfo.getDownloadPath();
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
			List<String> unzippedFiles = ZipFileUtils.unzip(destFN, upload_file_path);
			for (String impString : unzippedFiles) {
				if (impString.contains("import.xml")) {
					// 转换到数据库
					dataImport(impString, upload_file_path, download_file_path);
				}
			}
		} else if (dfn.contains("import.xml")) {
			// 转换到数据库
			dataImport(dfn, upload_file_path, download_file_path);
		}

		// 修改用户配置文件

		return new ResponseEntity<>("数据上传成功!", HttpStatus.OK);
	}

	private boolean dataImport(String importXML, String upload_file_path, String download_file_path) {
		String destFN = upload_file_path + File.separator + importXML;
		File xmlFile = new File(destFN);
		if (!xmlFile.exists()) {
			return false;
		}
		Field[] fileds = AddressEditorRow.class.getDeclaredFields();

		Document document = load(destFN);
		Element root = document.getRootElement();
		List<Element> nodes = root.elements("DataFile");
		Iterator<Element> it = nodes.iterator();
		while (it.hasNext()) {
			Element elm = it.next();
			// 文件名和设置
			String fnString = "";
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
			Map<String, String> settings = new HashMap<String, String>();
			List<Element> sElements = elm.elements("Settings");
			if (sElements.size() > 0) {
				Element se = sElements.get(0);
				List<Element> se2 = se.elements("Fields");
				if (se2.size() > 0) {
					Element se3 = se2.get(0);
					List<Element> lse3 = se3.elements("Field");
					if (lse3.size() > 0) {
						Element se4 = lse3.get(0);
						for (int i = 0; i < fileds.length; i++) {
							Field fld = fileds[i];
							String fldName = fld.getName();
							List<Element> lse4 = se4.elements(fldName);
							if (lse4.size() > 0) {
								Element se5 = lse4.get(0);
								String orgin = se5.getText();
								if (!orgin.isEmpty()) {
									settings.put(orgin, fldName);
								}
							}

						}
						List<Element> lse4_g = se4.elements("build_geometry");

						if (lse4_g.size() > 0) {
							Element se5 = lse4_g.get(0);
							geometryString = se5.getText();
							if (!geometryString.isEmpty()) {
								// settings.put("buildGeometry", geometryString);
							}
						}

					}
				}

			}
			String fname = fnString.substring(0, fnString.lastIndexOf('.'));
			String ftype = fnString.substring(fnString.lastIndexOf('.') + 1, fnString.length());
			String sourceFN = upload_file_path + File.separator + fnString;
			String targetFN = download_file_path + File.separator + fname + ".gpkg";

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

}
