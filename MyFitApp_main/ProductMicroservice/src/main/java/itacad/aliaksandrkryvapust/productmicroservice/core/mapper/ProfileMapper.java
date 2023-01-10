package itacad.aliaksandrkryvapust.productmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProfileDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProfileDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.UserMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProfileMapper {
    private final UserMapper userMapper;

    public ProfileMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Profile inputMapping(ProfileDtoInput dtoInput, MyUserDetails userDetails) {
        User user = this.userMapper.inputMapping(userDetails);
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

    public PageDtoOutput<ProfileDtoOutput> outputPageMapping(Page<Profile> record) {
        List<ProfileDtoOutput> outputs = record.getContent().stream().map(this::outputMapping).collect(Collectors.toList());
        return PageDtoOutput.<ProfileDtoOutput>builder()
                .number(record.getNumber() + 1)
                .size(record.getSize())
                .totalPages(record.getTotalPages())
                .totalElements(record.getTotalElements())
                .first(record.isFirst())
                .numberOfElements(record.getNumberOfElements())
                .last(record.isLast())
                .content(outputs)
                .build();
    }
}
