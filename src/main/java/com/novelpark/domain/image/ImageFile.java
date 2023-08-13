package com.novelpark.domain.image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.novelpark.exception.BadRequestException;
import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.InternalServerException;

public class ImageFile {

	private static final Pattern VALIDATE_EXTENSION = Pattern.compile("^(png|jpg|jpeg|gif)$");

	private final String originalFilename;
	private final String contentType;
	private final byte[] imageBytes;
	private final long fileSize;

	public ImageFile(String originalFilename, String contentType, byte[] imageBytes, long fileSize) {
		this.originalFilename = originalFilename;
		this.contentType = contentType;
		this.imageBytes = imageBytes;
		this.fileSize = fileSize;
	}

	public String getRandomName() {
		return new StringBuilder().append(UUID.randomUUID())
			.append(".")
			.append(StringUtils.getFilenameExtension(originalFilename))
			.toString();
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(imageBytes);
	}

	public long getFileSize() {
		return fileSize;
	}

	public static ImageFile from(MultipartFile multipartFile) {
		validateFileExtension(multipartFile);
		try {
			return new ImageFile(
				multipartFile.getOriginalFilename(),
				multipartFile.getContentType(),
				multipartFile.getBytes(),
				multipartFile.getSize()
			);
		} catch (IOException ex) {
			throw new InternalServerException(ErrorCode.INVALID_IMAGE);
		}
	}

	private static void validateFileExtension(final MultipartFile multipartFile) {
		String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
		if (extension == null || !VALIDATE_EXTENSION.matcher(extension).matches()) {
			throw new BadRequestException(ErrorCode.INVALID_FILE_EXTENSION);
		}
	}
}
