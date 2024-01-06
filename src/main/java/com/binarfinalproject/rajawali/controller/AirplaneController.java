package com.binarfinalproject.rajawali.controller;

import com.binarfinalproject.rajawali.dto.airplane.request.AirPlaneEditRequest;
import com.binarfinalproject.rajawali.dto.airplane.request.AirplaneRequest;
import com.binarfinalproject.rajawali.dto.airplane.response.AirplaneResponse;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.entity.Airport;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.AirplaneService;
import com.binarfinalproject.rajawali.util.ResponseMapper;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import jakarta.websocket.OnClose;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/v1/airplants")
public class AirplaneController {

    @Autowired
    AirplaneService airplaneService;

    @Autowired
    ModelMapper modelMapper;

    //create data
    @PostMapping
    public ResponseEntity<Object> createData(@Valid @RequestBody AirplaneRequest request){
        try {
            AirplaneResponse response = airplaneService.createData(request);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Successfully Create Data!",
                    response);
        } catch (Exception e){
            return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //get data
    @GetMapping("/{id}")
    public ResponseEntity<Object> getData(@PathVariable UUID id) throws ApiException {
        try {
            Airplane response = airplaneService.findById(id);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
                    "Airplane has successfully fatched",
                    modelMapper.map(response, AirplaneResponse.class)
                    );
        } catch (ApiException e){
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage()
            );
        } catch (Exception e){
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //update data
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateData(@PathVariable UUID id,
                                             @Valid @RequestBody AirPlaneEditRequest request) throws ApiException{
     try {
         AirplaneResponse response = airplaneService.updateData(id, request);
         return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Data has successfully edited!",
                 response);
     } catch (ApiException e){
         return ResponseMapper.generateResponseFailed(
                 e.getStatus(), e.getMessage());
     } catch (Exception e){
         return ResponseMapper.generateResponseFailed(
                 HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
     }
    }

    //delete data
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteData(@PathVariable UUID id) throws ApiException{
        try {
            AirplaneResponse response = airplaneService.deleteData(id);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Data has successfully deleted!",
                    response);
        } catch (ApiException e){
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e){
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //get All
    @GetMapping
    public ResponseEntity<Object> getAllData(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        try {
            if (page == null)
                page = 0;
            if (pageSize == null)
                pageSize = 10;

            Pageable paginationQueries = PageRequest.of(page, pageSize);
            Specification<Airplane> filterQueries = ((root, query, criteriaBuilder) -> {
                List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
                if (name != null && !name.isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" +
                                    name.toLowerCase() + "%"));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });

            Page<AirplaneResponse> response = airplaneService.getAllData(filterQueries, paginationQueries);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Data has successfully fatched!", response);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
