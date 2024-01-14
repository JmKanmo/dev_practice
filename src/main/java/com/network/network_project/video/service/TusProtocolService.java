package com.network.network_project.video.service;

import com.network.network_project.video.config.TusProtocolConfig;
import com.network.network_project.video.util.tus.TusUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.upload.UploadInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TusProtocolService {
    private final TusProtocolConfig tusProtocolConfig;
    private final TusFileUploadService tusFileUploadService;

    public String tusUpload(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 업로드
            tusFileUploadService.process(request, response);

            // 현재 업로드 정보
            UploadInfo uploadInfo = tusFileUploadService.getUploadInfo(request.getRequestURI());

            // 완료 된 경우 파일 저장
            if (uploadInfo != null && !uploadInfo.isUploadInProgress()) {
                // 파일 저장
                Map<String, String> videoMetaData = uploadInfo.getMetadata();
                String result = createFile(tusFileUploadService.getUploadedBytes(request.getRequestURI()), videoMetaData);
                log.info("[TusProtocolService:tusUpload] file created, save path:" + result);
                // 임시 파일 삭제
                tusFileUploadService.deleteUpload(request.getRequestURI());
                return "success";
            }
        } catch (Exception e) {
            log.error("exception was occurred. message={}", e.getMessage(), e);
            return "error";
        }
        return "ok";
    }

    // 파일 업로드 (날짜별 디렉토리 하위에 저장)
    private String createFile(InputStream is, String filename) throws Exception {
        LocalDate today = LocalDate.now();
        String uploadedPath = tusProtocolConfig.getSaveDirectory() + "/" + today;
        String vodName = getVodName(filename);
        File file = new File(uploadedPath, vodName);

        FileUtils.copyInputStreamToFile(is, file);
        return String.format(
                TusUtil.URL
                , tusProtocolConfig.getServerProtocol()
                , tusProtocolConfig.getServerAddress()
                , tusProtocolConfig.getUploadType()
                , today
                , vodName);
    }

    // 브라우저 측에서 넘겨받은 경로에 파일 저장
    private String createFile(InputStream is, Map<String, String> videoMetaData) throws Exception {
        String date = videoMetaData.get("date");
        String savePath = tusProtocolConfig.getSaveDirectory() + "/" + date;
        String vodName = videoMetaData.get("vodName");
        File file = new File(savePath, vodName);

        FileUtils.copyInputStreamToFile(is, file);
        return savePath + "/" + vodName;
    }

    // 파일 이름은 랜덤 UUID 사용
    private String getVodName(String filename) {
        String[] split = filename.split("\\.");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid + "." + split[split.length - 1];
    }
}
