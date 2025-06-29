package library.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class GridFsService {

    private final MongoTemplate mongoTemplate;
    private final GridFSBucket gridFSBucket;

    @Autowired
    public GridFsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb(), "bookImages");
    }

    public String storeFile(MultipartFile file) throws IOException {
        GridFSUploadOptions options = new GridFSUploadOptions()
                .metadata(new org.bson.Document("filename", file.getOriginalFilename()));
        try (InputStream inputStream = file.getInputStream()) {
            ObjectId fileId = gridFSBucket.uploadFromStream(file.getOriginalFilename(), inputStream, options);
            return fileId.toHexString();
        }
    }

    public void downloadFile(String fileId, OutputStream outputStream) throws IOException {
        gridFSBucket.downloadToStream(new ObjectId(fileId), outputStream);
    }

    public void deleteFile(String fileId) {
        gridFSBucket.delete(new ObjectId(fileId));
    }
}