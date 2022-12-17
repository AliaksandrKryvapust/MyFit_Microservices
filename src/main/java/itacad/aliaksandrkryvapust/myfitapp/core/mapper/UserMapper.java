package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

//@Component
//@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
//public class UserMapper {
//
//    public User inputMapping(UserDtoInput userDtoInput) {
//        return User.builder().username(userDtoInput.getUsername())
//                .password(userDtoInput.getPassword())
//                .email(userDtoInput.getEmail())
//                .height(userDtoInput.getHeight())
//                .weight(userDtoInput.getWeight())
//                .age(userDtoInput.getAge()).build();
//    }
//
//    public UserDtoOutput outputMapping(User user) {
//        return UserDtoOutput.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .build();
//    }
//}
