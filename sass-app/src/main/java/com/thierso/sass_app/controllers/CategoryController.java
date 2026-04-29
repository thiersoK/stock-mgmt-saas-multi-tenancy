package com.thierso.sass_app.controllers; // Emplacement du fichier

// Importations des outils Spring Boot pour le Web et la Validation
import com.thierso.sass_app.common.PageResponse;
import com.thierso.sass_app.requests.CategoryRequest;
import com.thierso.sass_app.responses.CategoryResponse;
import com.thierso.sass_app.services.CategoryService;
import com.thierso.sass_app.services.impl.CategoryServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Déclare cette classe comme une API REST (renvoie du JSON par défaut)
@RequestMapping("/api/v1/categories") // Définit l'URL de base pour toutes les méthodes de cette classe
@RequiredArgsConstructor // Génère le constructeur pour l'injection automatique du service
public class CategoryController {

    // Injection du service qui contient la logique métier
    //private final CategoryServiceImpl categoryService;
    private final CategoryService categoryService;

    @PostMapping // Intercepte les requêtes HTTP POST (Création)
    public ResponseEntity<Void> createCategory(
            @RequestBody // Récupère le JSON envoyé par le client et le transforme en objet Java
            @Valid // Déclenche la validation des champs (ex: @NotNull sur le nom de la catégorie)
            final CategoryRequest request
    ) {
        this.categoryService.create(request); // Appelle la logique de création
        //return ResponseEntity.ok().build(); // Renvoie un code HTTP 200 (Succès)
        return ResponseEntity.status(HttpStatus.CREATED).build(); //Renvoie un code 201 Created
    }

    @PutMapping("/{category-id}") // Intercepte les requêtes HTTP PUT (Modification)
    public ResponseEntity<Void> updateCategory(
            @RequestBody @Valid final CategoryRequest request, // Les nouvelles données
            @PathVariable("category-id")
            @NotNull(message = "Category ID cannot be null")
            final String id // L'ID extrait de l'URL ({category-id})
    ) {
        this.categoryService.update(id, request); // Appelle la mise à jour
        return ResponseEntity.accepted().build(); // Renvoie un code HTTP 202 (Accepté)
    }

    @GetMapping("/{category-id}") // Intercepte les requêtes GET pour un seul élément
    public ResponseEntity<CategoryResponse> findCategoryById(
            @PathVariable("category-id")
            @NotNull(message = "Category ID cannot be null")
            final String id
    ) {
        // Retourne la catégorie trouvée avec un code HTTP 200
        return ResponseEntity.ok(this.categoryService.findById(id));
    }

    @GetMapping // Intercepte les requêtes GET sur la racine (/api/v1/categories)
    public ResponseEntity<PageResponse<CategoryResponse>> findAllCategories(
            @RequestParam(name = "page", defaultValue = "0") final int page, // Paramètre ?page=X
            @RequestParam(name = "size", defaultValue = "10") final int size // Paramètre ?size=Y
    ) {
        // Appelle le service pour récupérer les données paginées
        return ResponseEntity.ok(this.categoryService.findAll(page, size));
    }

    @DeleteMapping("/{category-id}") // Intercepte les requêtes HTTP DELETE
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("category-id")
            @NotNull(message = "Category ID cannot be null")
            final String id
    ) {
        this.categoryService.delete(id); // Appelle la logique de suppression
        return ResponseEntity.noContent().build(); // Renvoie un code HTTP 204 (Pas de contenu)
    }
}