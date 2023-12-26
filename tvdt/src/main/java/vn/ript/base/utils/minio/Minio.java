package vn.ript.base.utils.minio;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.minio.BucketExistsArgs;
import io.minio.DownloadObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import vn.ript.base.utils.Utils;

public class Minio {

    MinioClient minioClient;

    public Minio() {
        this.minioClient = MinioClient.builder()
                .endpoint(Utils.SS_MINIO_HOST, Utils.SS_MINIO_PORT, Utils.SS_MINIO_SECURE)
                .credentials(Utils.SS_MINIO_ACCESS_KEY, Utils.SS_MINIO_SECRET_KEY).build();
    }

    public Boolean isBucketExisted(String bucket_name) {
        Boolean found = null;
        try {
            found = this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket_name).build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
        return found;
    }

    public void createBucketIfNotExist(String bucket_name) {
        Boolean found = this.isBucketExisted(bucket_name);
        if (!found) {
            try {
                this.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket_name).build());
            } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                    | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                    | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Bucket already exists.");
        }
    }

    public void deleteBucketIfExist(String bucket_name) {
        Boolean found = this.isBucketExisted(bucket_name);
        if (!found) {
            System.out.println("Bucket is not exists.");
        } else {
            try {
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket_name).build());
            } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                    | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                    | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Bucket> listBuckets() {
        List<Bucket> bucketList = new ArrayList<>();
        try {
            bucketList = minioClient.listBuckets();
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IOException e) {
            e.printStackTrace();
        }
        return bucketList;
    }

    public InputStream getObject(String bucket_name, String object_name) {
        InputStream stream = null;
        try {
            stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket_name)
                            .object(object_name)
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    public void downloadObject(String bucket_name, String object_name, String path) {
        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucket_name)
                            .object(object_name)
                            .filename(path)
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadObject(String bucket_name, String object_name, String path) {
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucket_name)
                            .object(object_name)
                            .filename(path)
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeObject(String bucket_name, String object_name) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket_name)
                            .object(object_name)
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeObjects(String bucket_name, List<String> object_names) {
        try {
            List<DeleteObject> deleteObjects = new LinkedList<>();
            for (String object_name : object_names) {
                deleteObjects.add(new DeleteObject(object_name));
            }
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucket_name)
                            .objects(deleteObjects)
                            .build());
            for (Result<DeleteError> result : results) {
                DeleteError error;
                error = result.get();
                System.out.println(
                        "Error in deleting object " + error.objectName() + "; " + error.message());
            }
        } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
                | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
                | XmlParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
