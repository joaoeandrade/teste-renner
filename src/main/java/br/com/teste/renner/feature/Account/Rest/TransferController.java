package br.com.teste.renner.feature.Account.Rest;

import br.com.teste.renner.feature.Account.TransferRequest;
import br.com.teste.renner.feature.Account.service.Impl.TransferServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfers")
@Tag(name = "Transfers", description = "Operações de transferência entre contas")
public class TransferController {

    private final TransferServiceImpl transferService;



    @PostMapping
    @Operation(
            summary = "Realiza transferência entre contas",
            description = "Transfere um valor de uma conta de origem para uma conta de destino"
    )
    @ApiResponse(responseCode = "201", description = "Transferência realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo insuficiente")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request) {
        transferService.transfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}