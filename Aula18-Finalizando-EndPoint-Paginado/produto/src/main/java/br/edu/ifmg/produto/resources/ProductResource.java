package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.dtos.ProductListDTO;
import br.edu.ifmg.produto.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/product")
@Tag(name="Product", description = "Controller/Resource for product")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping(produces = "application/json")
    @Operation(
            description = "Get all products",
            summary = "List all registered products",
            responses = {
                    @ApiResponse(description = "ok", responseCode = "200"),
            }
    )
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        Page<ProductDTO> products = productService.findAll(pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Get a product",
            summary = "List only one produdct",
            responses = {
                    @ApiResponse(description = "ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),

            }
    )
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO product = productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value = "/paged", produces = "application/json")
    @Operation(
            description = "Get all products",
            summary = "List all registered products",
            responses = {
                    @ApiResponse(description = "ok", responseCode = "200"),
            }
    )
    public ResponseEntity<Page<ProductListDTO>> findAllPaged(
            Pageable pageable,
            @RequestParam(value = "categoryId", defaultValue = "0") String categoryId,
            @RequestParam(value = "name", defaultValue = "") String name) {

        Page<ProductListDTO> products = productService.findAllPaged(name, categoryId, pageable);
        return ResponseEntity.ok().body(products);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
        dto = productService.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Update a product",
            summary = "Change the values of a registered product",
            responses = {
                    @ApiResponse(description = "ok", responseCode = "200"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404"),

            }
    )

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<ProductDTO> update(@Valid @PathVariable Long id, @RequestBody ProductDTO dto) {
        dto = productService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            description = "Delete a product",
            summary = "Delete a registered product",
            responses = {
                    @ApiResponse(description = "ok", responseCode = "200"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404"),

            }
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}