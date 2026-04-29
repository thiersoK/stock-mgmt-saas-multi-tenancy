package com.thierso.sass_app.controllers;

import com.thierso.sass_app.common.PageResponse;
import com.thierso.sass_app.requests.ProductRequest;
import com.thierso.sass_app.responses.ProductResponse;
import com.thierso.sass_app.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController // Déclare cette classe comme une API REST (renvoie du JSON par défaut)
@RequestMapping("/api/v1/products") // Définit l'URL de base pour toutes les méthodes de cette classe
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping // Intercepte les requêtes HTTP POST (Création)
    public ResponseEntity<Void> createProduct(
            @RequestBody // Récupère le JSON envoyé par le client et le transforme en objet Java
            @Valid // Déclenche la validation des champs (ex: @NotNull sur le nom de la catégorie)
            final ProductRequest request
    ) {
        this.productService.create(request); // Appelle la logique de création
        //return ResponseEntity.ok().build(); // Renvoie un code HTTP 200 (Succès)
        return ResponseEntity.status(HttpStatus.CREATED).build(); //Renvoie un code 201 Created
    }

    @PutMapping("/{product-id}") // Intercepte les requêtes HTTP PUT (Modification)
    public ResponseEntity<Void> updateProduct(
            @RequestBody
            @Valid
            final ProductRequest request, // Les nouvelles données
            @PathVariable("product-id")
            @NotNull(message = "Product ID cannot be null")
            final String id // L'ID extrait de l'URL ({category-id})
    ) {
        this.productService.update(id, request); // Appelle la mise à jour
        return ResponseEntity.accepted().build(); // Renvoie un code HTTP 202 (Accepté)
    }

    @GetMapping("/{product-id}") // Intercepte les requêtes GET pour un seul élément
    public ResponseEntity<ProductResponse> findProductById(
            @PathVariable("product-id")
            @NotNull(message = "Product ID cannot be null")
            final String id
    ) {
        // Retourne la catégorie trouvée avec un code HTTP 200
        return ResponseEntity.ok(this.productService.findById(id));
    }

    @GetMapping // Intercepte les requêtes GET sur la racine (/api/v1/categories)
    public ResponseEntity<PageResponse<ProductResponse>> findAllProducts(
            @RequestParam(name = "page", defaultValue = "0") final int page, // Paramètre ?page=X
            @RequestParam(name = "size", defaultValue = "10") final int size // Paramètre ?size=Y
    ) {
        // Appelle le service pour récupérer les données paginées
        return ResponseEntity.ok(this.productService.findAll(page, size));
    }

    @DeleteMapping("/{product-id}") // Intercepte les requêtes HTTP DELETE
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("category-id")
            @NotNull(message = "Product ID cannot be null")
            final String id
    ) {
        this.productService.delete(id); // Appelle la logique de suppression
        return ResponseEntity.noContent().build(); // Renvoie un code HTTP 204 (Pas de contenu)
    }
}
