package itacad.aliaksandrkryvapust.myfitapp.event;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Builder
@Getter
public class EmailVerificationEvent extends ApplicationEvent {
    private final @NonNull String appUrl;
    private final @NonNull Locale locale;
    private final @NonNull User user;

    public EmailVerificationEvent(@NonNull String appUrl, @NonNull Locale locale, @NonNull User user) {
        super(user);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user;
    }
}
