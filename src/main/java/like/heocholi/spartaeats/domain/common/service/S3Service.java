package like.heocholi.spartaeats.domain.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
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
	
	public String upload(MultipartFile multipartFile, String dirName) throws IOException {
		File uploadFile = convert(multipartFile)
			.orElseThrow(() -> new FileException(ErrorType.FILE_UPLOAD_ERROR));
		
		return upload(uploadFile, dirName);
	}
	
	private String upload(File uploadFile, String dirName) {
		String fileName = dirName + "/" + uploadFile.getName();
		String uploadImageUrl = putS3(uploadFile, fileName);
		
		removeNewFile(uploadFile);
		return uploadImageUrl;
	}
	
	private String putS3(File uploadFile, String fileName) {
		s3Client.putObject(
			new PutObjectRequest(bucket, fileName, uploadFile)
		);
		
		return s3Client.getUrl(bucket, fileName).toString();
	}
	
	private void removeNewFile(File targetFile) {
		String name = targetFile.getName();
		
		if (targetFile.delete()) {
			log.info(name + "파일이 삭제되었습니다.");
		} else {
			log.info(name + "파일이 삭제되지 못했습니다.");
		}
	}
	
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
}
