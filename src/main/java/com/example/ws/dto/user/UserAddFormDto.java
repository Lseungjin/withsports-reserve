package com.example.ws.dto.user;




import com.example.ws.domain.value.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;


import java.util.List;

/**
 * 회원 가입에 사용할 폼
 */
@Data
public class UserAddFormDto {

    @NotNull(message = "이메일을 입력 해 주세요")
    private String email;

    private String password;

    @NotEmpty(message = "이름을 입력 해 주세요")
    private String name;


    @NotNull(message = "나이를 입력 해 주세요")
    private Integer age;

    @NotEmpty(message = "주소를 입력 해 주세요")
    private String address;

    @NotEmpty(message = "상세 주소를 입력 해 주세요")
    private String detailAddress;

}
