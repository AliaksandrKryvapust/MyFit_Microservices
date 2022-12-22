package itacad.aliaksandrkryvapust.myfitapp.repository.entity;

public enum Role {
    USER("user"), ADMIN("admin"), DEFAULT("");
    private final String str;

    Role(String str) {
        this.str = str;
    }

    public static Role getByStr(String str) {
        for (Role value : values()) {
            if (value.str.equalsIgnoreCase(str)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
