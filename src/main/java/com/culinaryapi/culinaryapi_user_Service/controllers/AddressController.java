package com.culinaryapi.culinaryapi_user_Service.controllers;

import com.culinaryapi.culinaryapi_user_Service.dtos.AddressDto;
import com.culinaryapi.culinaryapi_user_Service.model.AddressModel;
import com.culinaryapi.culinaryapi_user_Service.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Criar endereço", description = "Cria um novo endereço para o usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PostMapping("/")
    public ResponseEntity<Object> createAddress(@RequestBody @Validated AddressDto addressDto) {
        return addressService.createAddress(addressDto);
    }

    @Operation(summary = "Desativar endereço", description = "Desativa um endereço específico do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço desativado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PutMapping("/{addressId}/deactivate")
    public ResponseEntity<Object> deactivateAddress(@PathVariable(value = "addressId") UUID addressId) {
        return addressService.deactivateAddress(addressId);
    }

    @Operation(summary = "Atualizar endereço", description = "Atualiza um endereço específico do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PutMapping("/{addressId}")
    public ResponseEntity<Object> updateAddress(@PathVariable(value = "addressId") UUID addressId,
                                                @RequestBody @Validated AddressDto addressDto) {
        return addressService.updateAddress(addressId, addressDto);
    }

    @Operation(summary = "Listar endereços do usuário", description = "Obtém todos os endereços associados a um usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereços listados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Page<AddressModel>> getUserAddresses(@PathVariable UUID userId, Pageable pageable) {
        return addressService.getUserAddresses(userId, pageable);
    }
}
