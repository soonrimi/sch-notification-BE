package conconccc.schnofiticationbe.dto;

import conconccc.schnofiticationbe.entity.Notice;
import lombok.Getter;
import lombok.Setter;

public class NoticeDto {

    @Getter @Setter
    public static class CreateRequest {
        private String title;
        private String content;
        private String targetYear;
        private String targetDept;
        private String fileUrl;
    }

    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String content;
        private String targetYear;
        private String targetDept;
        private String fileUrl;
        private String source;
        private int viewCount;

        public Response(Notice n) {
            this.id = n.getId();
            this.title = n.getTitle();
            this.author = n.getAuthor();
            this.content = n.getContent();
            this.targetYear = n.getTargetYear();
            this.targetDept = n.getTargetDept();
            this.fileUrl = n.getFileUrl();
            this.source = n.getSource();
            this.viewCount = n.getViewCount();
        }
    }
}
