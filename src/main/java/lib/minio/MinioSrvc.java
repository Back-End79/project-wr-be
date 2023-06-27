package lib.minio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lib.minio.configuration.property.MinioProp;
import lib.minio.exception.MinioServiceDownloadException;
import lib.minio.exception.MinioServiceUploadException;
import lib.i18n.utility.MessageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MinioSrvc {

  private static final Long DEFAULT_EXPIRY = TimeUnit.HOURS.toSeconds(1);

  private final MinioClient minio;
  private final MinioProp prop;

  private final MessageUtil message;

  private static String bMsg(String bucket) {
    return "bucket " + bucket;
  }

  private static String bfMsg(String bucket, String filename) {
    return bMsg(bucket) + " of file " + filename;
  }

  private String getLink(String bucket, String filename, Long expiry) {
    try {
      return minio.getPresignedObjectUrl(
          GetPresignedObjectUrlArgs.builder()
              .method(Method.GET)
              .bucket(bucket)
              .object(filename)
              .expiry(Math.toIntExact(expiry), TimeUnit.SECONDS)
              .build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
        | InvalidResponseException | NoSuchAlgorithmException | XmlParserException | ServerException
        | IllegalArgumentException | IOException e) {
      log.error(message.get(prop.getGetErrorMessage(), bfMsg(bucket, filename)) + ": " + e.getLocalizedMessage(), e);
      throw new MinioServiceDownloadException(
          message.get(prop.getGetErrorMessage(), bfMsg(bucket, filename)), e);
    }
  }

  @Data
  public static class ListItem {
    private String objectName;
    private Long size;
    private boolean dir;
    private String versionId;

    @JsonIgnore
    private Item item;

    public ListItem(Item item) {
      this.objectName = item.objectName();
      this.size = item.size();
      this.dir = item.isDir();
      this.versionId = item.versionId();
      this.item = item;
    }
  }

  public List<Object> getList(String bucket) {
    List<Result<Item>> results = new ArrayList<>();
    minio.listObjects(
        ListObjectsArgs.builder()
            .bucket(bucket)
            .recursive(true)
            .build())
        .forEach(results::add);
    return results.stream().map(t -> {
      try {
        return new ListItem(t.get());
      } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
          | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
          | XmlParserException | IOException e) {
        log.error(message.get(prop.getGetErrorMessage(), bMsg(bucket)) + ": " + e.getLocalizedMessage(), e);
        return null;
      }
    }).collect(Collectors.toList());
  }

  public void view(HttpServletResponse response, String bucket, String filename, Long expiry) {
    try {
      response.sendRedirect(this.getLink(bucket, filename, expiry));
    } catch (IOException e) {
      log.error(message.get(prop.getGetErrorMessage(), bfMsg(bucket, filename)) + ": " + e.getLocalizedMessage(), e);
      throw new MinioServiceDownloadException(
          message.get(prop.getGetErrorMessage(), bfMsg(bucket, filename)), e);
    }
  }

  public void view(HttpServletResponse response, String bucket, String filename) {
    this.view(response, bucket, filename, DEFAULT_EXPIRY);
  }

  @Data
  @Builder
  public static class UploadOption {
    private String filename;
  }

  public ObjectWriteResponse upload(MultipartFile file, String bucket, Function<MultipartFile, UploadOption> modifier) {
    UploadOption opt = modifier.apply(file);
    try {
      return minio.putObject(
          PutObjectArgs.builder()
              .bucket(bucket)
              .object(opt.filename)
              .stream(file.getInputStream(), file.getSize(), -1)
              .contentType(file.getContentType())
              .build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
        | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
        | IllegalArgumentException | IOException e) {
      log.error(message.get(prop.getPostErrorMessage(), bfMsg(bucket, opt.filename)) + ": " + e.getLocalizedMessage(),
          e);
      throw new MinioServiceUploadException(
          message.get(prop.getPostErrorMessage(), bucket, opt.filename), e);
    }
  }

  public ObjectWriteResponse upload(MultipartFile file, String bucket) {
    return this.upload(file, bucket,
        o -> UploadOption.builder()
            .filename(bucket+"_"
                + o.getOriginalFilename().replace(" ", "_"))
            .build());
  }

}
