package conconccc.schnofiticationbe.dto;

import conconccc.schnofiticationbe.entity.BasePost;
import lombok.Getter;
import lombok.Setter;

public class BasePostDto {

    @Getter @Setter
    public static class CreateRequest {
        protected String title;
        protected String content;
    }

    @Getter @Setter
    public static class UpdateRequest {
        protected String title;
        protected String content;
    }

    @Getter
    public static class Response {
        protected Long id;
        protected String title;
        protected String content;
        protected String createdAt;

        public Response(BasePost post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdAt = post.getCreatedAt() != null
                    ? post.getCreatedAt().toString()
                    : null;
        }
    }
}
