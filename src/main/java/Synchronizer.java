import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class Synchronizer {

    private static final String GOOGLE_SAVES_FOLDER = "Saves";
    
    private Drive service;
    private String savesLocalPath;
    private String zippedSavesLocalPath;
    private String gameName;
    
    
    Synchronizer(Drive service, String savesLocalPath) {
        this.service = service;
        this.savesLocalPath = savesLocalPath;
        gameName = savesLocalPath.substring(savesLocalPath.lastIndexOf("/") + 1);
        zippedSavesLocalPath = savesLocalPath + ".zip";
    }

    public void uploadSaves() throws IOException {
        // Zip saves
        ZipUtility.pack(savesLocalPath, zippedSavesLocalPath);
        
        // Find google saves folder
        FileList folderResult = service.files().list()
            .setQ("name = '" + GOOGLE_SAVES_FOLDER + "'")
            .setFields("files(id, name)")
            .execute();
        File folder = folderResult.getFiles().get(0);

        // Delete save file
        FileList result = service.files().list()
            .setQ("name = '" + gameName + ".zip' and '" + folder.getId() + "' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false")
            .setFields("files(id, name)")
            .execute();
        for (File file : result.getFiles()) {
            service.files().delete(file.getId()).execute();
        }

        // Upload local saves
        File fileMetadata = new File();
        fileMetadata.setName(gameName + ".zip");
        fileMetadata.setParents(Collections.singletonList(folder.getId()));
        java.io.File filePath = new java.io.File(zippedSavesLocalPath);
        FileContent mediaContent = new FileContent("application/zip", filePath);
        service.files().create(fileMetadata, mediaContent)
            .setFields("id, parents")
            .execute();
        
        // Delete temporary zip
        java.io.File zipFile = new java.io.File(zippedSavesLocalPath);
        zipFile.delete();
    }

    public void downloadSaves() throws IOException {

        // Find google saves folder
        FileList folderResult = service.files().list()
            .setQ("name = '" + GOOGLE_SAVES_FOLDER + "'")
            .setFields("files(id, name)")
            .execute();
        File folder = folderResult.getFiles().get(0);

        FileList result = service.files().list()
            .setQ("name = '" + gameName + ".zip' and '" + folder.getId() + "' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false")
            .setFields("files(id, name)")
            .execute();
        for (File file : result.getFiles()) {
            
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            service.files().get(file.getId())
                .executeMediaAndDownloadTo(byteArrayOutputStream);

            try(OutputStream outputStream = new FileOutputStream(zippedSavesLocalPath)) {
                byteArrayOutputStream.writeTo(outputStream);
            }

            // Delete old saves
            java.io.File oldSaves = new java.io.File(savesLocalPath);
            oldSaves.delete();

            ZipUtility.unpack(zippedSavesLocalPath, savesLocalPath);

            // Delete temporary zip
            java.io.File zipFile = new java.io.File(zippedSavesLocalPath);
            zipFile.delete();
        }
    }
}
