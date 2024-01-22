package com.binarfinalproject.rajawali.dto.reservation.request;

import com.binarfinalproject.rajawali.entity.Passenger.AgeType;
import com.binarfinalproject.rajawali.entity.Passenger.GenderType;
import com.binarfinalproject.rajawali.util.ValidateEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PassengerDto {
    @NotNull
    @ValidateEnum(targetClassType = GenderType.class, message = "gender type must be 'MAN' or 'WOMAN'")
    private String genderType;

    @NotBlank
    private String fullname;

    @NotNull
    @ValidateEnum(targetClassType = AgeType.class, message = "age type must be 'ADULT' or 'CHILD' or 'INFANT'")
    private String ageType;
}