package itacad.aliaksandrkryvapust.auditmicroservice.repository.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class User{
    private UUID id;
    private String email;
    private EUserRole role;
}