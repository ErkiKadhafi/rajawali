package com.binarfinalproject.rajawali.dto.reservation.request;

import com.binarfinalproject.rajawali.entity.Passenger.AgeType;
import com.binarfinalproject.rajawali.entity.Passenger.GenderType;
import com.binarfinalproject.rajawali.util.ValidateEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PassengerDto {
    @NotBlank
    private String seatId;

    @NotNull
    @ValidateEnum(targetClassType = GenderType.class, message = "gender type must be 'MAN' or 'WOMAN'")
    private String genderType;

    @NotNull
    @ValidateEnum(targetClassType = AgeType.class, message = "age type must be 'ADULT' or 'CHILD' or 'INFANT'")
    private String ageType;

    @NotBlank
    private String fullname;

    @NotBlank
    @Size(min = 16, message = "must has 16 characters")
    @Size(max = 16, message = "must has 16 characters")
    private String idCardNumber;
}