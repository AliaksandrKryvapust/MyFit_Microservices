package itacad.aliaksandrkryvapust.usermicroservice.event;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;

@Builder
@Getter
public class EmailVerificationEvent extends ApplicationEvent {
    private final @NonNull User user;

    public EmailVerificationEvent(@NonNull User user) {
        super(user);
        this.user = user;
    }
}
