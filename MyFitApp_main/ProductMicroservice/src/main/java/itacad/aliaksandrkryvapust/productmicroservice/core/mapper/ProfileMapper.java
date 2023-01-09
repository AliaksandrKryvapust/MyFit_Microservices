package itacad.aliaksandrkryvapust.productmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProfileDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.TokenValidationDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProfileDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.UserMapper;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProfileMapper {
    private final UserMapper userMapper;

    public ProfileMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Profile inputMapping(ProfileDtoInput dtoInput, TokenValidationDto tokenValidationDto) {
        User user = this.userMapper.inputMapping(tokenValidationDto);
        return Profile.builder()
                .height(dtoInput.getHeight())
                .weight(dtoInput.getWeight())
                .dtBirthday(dtoInput.getDtBirthday())
                .target(dtoInput.getTarget())
                .activityType(dtoInput.getActivityType())
                .sex(dtoInput.getSex())
                .user(user).build();
    }

    public ProfileDtoOutput outputMapping(Profile profile){
        UserDtoOutput dtoOutput = this.userMapper.outputMapping(profile.getUser());
        return ProfileDtoOutput.builder()
                .id(profile.getId())
                .height(profile.getHeight())
                .weight(profile.getWeight())
                .dtBirthday(profile.getDtBirthday())
                .target(profile.getTarget())
                .activityType(profile.getActivityType())
                .sex(profile.getSex())
                .user(dtoOutput)
                .dtCreate(profile.getDtCreate())
                .dtUpdate(profile.getDtUpdate()).build();
    }
}
