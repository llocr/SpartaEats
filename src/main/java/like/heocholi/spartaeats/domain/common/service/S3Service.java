package like.heocholi.spartaeats.domain.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.FileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3Client s3Client;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	/**
	 * 파일 업로드
	 * @param multipartFile
	 * @param dirName
	 * @return 업로드된 파일 URL
	 * @throws IOException
	 */
	public String upload(MultipartFile multipartFile, String dirName) throws IOException {
		File uploadFile = convert(multipartFile)
			.orElseThrow(() -> new FileException(ErrorType.FILE_UPLOAD_ERROR));
		
		return upload(uploadFile, dirName);
	}
	
	/**
	 * 파일 업로드
	 * @param uploadFile
	 * @param dirName
	 * @return 업로드된 파일 URL
	 */
	private String upload(File uploadFile, String dirName) {
		String fileName = dirName + "/" + UUID.randomUUID();
		String uploadImageUrl = putS3(uploadFile, fileName);
		
		removeNewFile(uploadFile);
		return uploadImageUrl;
	}
	
	/**
	 * S3에 파일 업로드
	 * @param uploadFile
	 * @param fileName
	 * @return 업로드된 파일 URL
	 */
	private String putS3(File uploadFile, String fileName) {
		s3Client.putObject(
			new PutObjectRequest(bucket, fileName, uploadFile)
		);
		
		return s3Client.getUrl(bucket, fileName).toString();
	}
	
	/**
	 * 파일 삭제
	 * @param targetFile
	 */
	private void removeNewFile(File targetFile) {
		String name = targetFile.getName();
		
		if (targetFile.delete()) {
			log.info(name + "파일이 삭제되었습니다.");
		} else {
			log.info(name + "파일이 삭제되지 못했습니다.");
		}
	}
	
	/**
	 * MultipartFile을 File로 변환
	 * @param multipartFile
	 * @return Optional<File>
	 * @throws IOException
	 */
	public Optional<File> convert(MultipartFile multipartFile) throws IOException {
		File convertFile = new File(multipartFile.getOriginalFilename());
		
		if (convertFile.createNewFile()){ // 해당 경로에 파일이 없을 경우, 새 파일 생성
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(multipartFile.getBytes());
			}
			return Optional.of(convertFile);
		}
		
		return Optional.empty();
	}
	
	/**
	 * S3에 저장된 프로필 이미지 삭제
	 * @param filePath
	 */
	public void deleteProfileImage(String filePath) {
		String fileName = extractFileNameFromURL(filePath);
		
		try {
			if (s3Client.doesObjectExist(bucket, fileName)) {
				s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
				log.info(fileName + " 파일이 S3에서 삭제되었습니다.");
			} else {
				log.warn(fileName + " 파일이 S3에 존재하지 않습니다.");
			}
		} catch (Exception e) {
			log.error("S3 파일 삭제 중 오류 발생: ", e);
			throw new FileException(ErrorType.FILE_DELETE_ERROR);
		}
	}
	
	/**
	 * URL에서 파일 이름 추출
	 * @param url
	 * @return 파일 이름
	 */
	public String extractFileNameFromURL (String url) {
		try {
			URL parsedUrl = new URL(url);
			String substring = parsedUrl.getPath().substring(1);
			log.info(substring);
			return substring;
		} catch (MalformedURLException e) {
			throw new FileException(ErrorType.INVALID_URL);
		}
	}
}
