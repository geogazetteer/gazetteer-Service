/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <em>Zip文件工具类</em><br>
 * 
 * @author whudyj
 *
 */
public class ZipFileUtils {
	/**
	 * <em>压缩(ZIP)给定目录文件或者里面的文件</em><br>
	 * 
	 * @param inFile     给定的目录或文件
	 * @param extensions 需要压缩的文件扩展名 ，不指定为null
	 * @param recursive  是否遍历给定目录
	 * @param outPathStr 输出文件名
	 * @throws Exception
	 */
	public static void zipFiles(String inFile, String[] extensions, boolean recursive, String outPathStr) {
		Collection<File> filesToArchive = null;
		File file = Paths.get(inFile).toFile();
		if (file.isFile()) {
			filesToArchive = Arrays.asList(file);
		} else {
			filesToArchive = FileUtils.listFiles(file, extensions, recursive);
		}
		// 此处是避免压缩文件里面出现文件目录前面有系统分割符号
		String rootPath = file.getParent();
		if (!rootPath.endsWith(File.separator)) {
			rootPath += File.separator;
		}
		try {
			zip(outPathStr, filesToArchive, rootPath, null);
		} catch (IOException | ArchiveException e) {
			e.printStackTrace();
		}
	}

	private static void zip(String outPathStr, Collection<File> filesToArchive, String rootPath, String comment)
			throws FileNotFoundException, IOException, ArchiveException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(Paths.get(outPathStr).toFile()));
		try (ZipArchiveOutputStream o = (ZipArchiveOutputStream) new ArchiveStreamFactory()
				.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out)) {
			// TODO 使用 new
			// java.util.zip.ZipFile("c://ExamExam.zip").getComment();
			// 获取注释,直接o.setComment设置注释，默认是UTF-8编码的。但是window上打开是乱码，或许可以设置
			// 为GBK，打开才是正常的
			if (!StringUtils.isBlank(comment)) {
				// o.setEncoding(System.getProperty("sun.jnu.encoding"));
				o.setEncoding("GBK");
				o.setComment(comment);
			}

			for (File f : filesToArchive) {
				// 获取每个文件相对路径,作为在ZIP中路径
				ArchiveEntry entry = o.createArchiveEntry(f, f.getCanonicalPath().substring(rootPath.length()));
				o.putArchiveEntry(entry);
				if (f.isFile()) {
					try (InputStream i = Files.newInputStream(f.toPath())) {
						IOUtils.copy(i, o);
					}
				}
				o.closeArchiveEntry();
			}

			o.finish();
		}
		out.close();
	}

	/**
	 * <em>解压Zip文件</em>
	 * 
	 * @param zipFile 需要解压的zip文件位置
	 * @param destDir 解压的目标位置
	 */
	public static List<String> unzip(String zipFile, String destDir) {
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
