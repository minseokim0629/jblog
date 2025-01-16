package jblog.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	private static final String SAVE_PATH = "/jblog-uploads";
	private static final String URL = "/assets/upload-images";

	public String restore(MultipartFile file) throws RuntimeException {
		try {
			File uploadDirectory = new File(SAVE_PATH);

			if (!uploadDirectory.exists() && !uploadDirectory.mkdir()) {
				return null;
			}

			if (file.isEmpty()) {
				return null;
			}

			String originFileName = Optional.ofNullable(file.getOriginalFilename()).orElse("");
			String extName = originFileName.substring(originFileName.lastIndexOf(".") + 1);
			String saveFileName = generateFileName(extName);

			byte[] data = file.getBytes();

			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
			os.write(data);
			os.close();

			return URL + "/" + saveFileName;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String generateFileName(String extName) {
		Calendar calendar = Calendar.getInstance();
		return "" + calendar.get(calendar.YEAR) + calendar.get(calendar.MONTH) + calendar.get(calendar.DATE)
				+ calendar.get(calendar.HOUR) + calendar.get(calendar.MINUTE) + calendar.get(calendar.SECOND)
				+ calendar.get(calendar.MILLISECOND) + ("." + extName);
	}
}
