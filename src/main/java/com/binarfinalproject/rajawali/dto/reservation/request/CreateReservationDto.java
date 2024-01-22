package com.binarfinalproject.rajawali.dto.reservation.request;

import java.util.List;

import com.binarfinalproject.rajawali.entity.Passenger.GenderType;
import com.binarfinalproject.rajawali.entity.Seat.ClassType;
import com.binarfinalproject.rajawali.util.ValidateEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReservationDto {
    @NotNull
    @ValidateEnum(targetClassType = ClassType.class, message = "class type must be 'ECONOMY' or 'BUSINESS' or 'FIRST'")
    private String classType;

    @NotNull
    @ValidateEnum(targetClassType = GenderType.class, message = "gender type must be 'MAN' or 'WOMAN'")
    private String genderType;

    @NotBlank
    private String fullname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotNull
    @NotEmpty(message = "passenger list cannot be empty.")
    private List<@Valid PassengerDto> passengerList;

    @NotNull
    @NotEmpty(message = "flight detail list cannot be empty.")
    private List<@Valid FlightDetailsDto> flightDetailList;
}
