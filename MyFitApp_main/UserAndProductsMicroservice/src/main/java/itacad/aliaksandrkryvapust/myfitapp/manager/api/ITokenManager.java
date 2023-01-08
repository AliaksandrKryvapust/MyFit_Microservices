package itacad.aliaksandrkryvapust.myfitapp.manager.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;

public interface ITokenManager {
    void saveToken(User user,String token);
    void validateToken(String token);
}
