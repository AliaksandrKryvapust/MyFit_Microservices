package aliaksandrkryvapust.reportmicroservice.repository.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class File {
    @Enumerated(EnumType.STRING)
    private EFileType fileType;
    private String contentType;
    private String fileName;
    private String url;
    private String fileKey;

    @Override
    public String toString() {
        return "File{" +
                "fileType=" + fileType +
                ", contentType='" + contentType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                ", fileKey='" + fileKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        if (getFileType() != file.getFileType()) return false;
        if (!getContentType().equals(file.getContentType())) return false;
        if (!getFileName().equals(file.getFileName())) return false;
        if (!getUrl().equals(file.getUrl())) return false;
        return getFileKey().equals(file.getFileKey());
    }

    @Override
    public int hashCode() {
        int result = getFileType().hashCode();
        result = 31 * result + getContentType().hashCode();
        result = 31 * result + getFileName().hashCode();
        result = 31 * result + getUrl().hashCode();
        result = 31 * result + getFileKey().hashCode();
        return result;
    }
}
