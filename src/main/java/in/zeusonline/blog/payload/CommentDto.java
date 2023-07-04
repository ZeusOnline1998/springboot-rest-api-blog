package in.zeusonline.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class CommentDto {

    private long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Enter a valid email")
    private String email;

    @NotEmpty(message = "Comment cannot be empty")
    private String body;
}
