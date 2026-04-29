package com.thierso.sass_app.services.impl; // Emplacement du fichier

// Importations des classes nécessaires (Entités, DTOs, Repositories, etc.)
import com.thierso.sass_app.common.PageResponse;
import com.thierso.sass_app.entities.Category;
import com.thierso.sass_app.mapper.CategoryMapper;
import com.thierso.sass_app.repositories.CategoryRepository;
import com.thierso.sass_app.requests.CategoryRequest;
import com.thierso.sass_app.responses.CategoryResponse;
import com.thierso.sass_app.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Déclare cette classe comme un Bean de service Spring (Logique métier)
@RequiredArgsConstructor // Lombok génère un constructeur avec les champs 'final' (Injection de dépendances)
@Slf4j // Permet d'utiliser 'log' pour écrire des messages dans la console
public class CategoryServiceImpl implements CategoryService {

    // Dépendances injectées automatiquement via le constructeur de Lombok
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /* Méthode : Créer une catégorie */

    @Override
    public void create(CategoryRequest request) {
        // Recherche si une catégorie avec le même nom existe déjà (insensible à la casse)
        final Optional<Category> category = categoryRepository.findByNameIgnoreCase(request.getName());
        if (category.isPresent()) {
            log.debug("Category already exists"); // Log pour le développeur
            throw new RuntimeException("Category already exists"); // Empêche la création de doublons
        }
        // Transforme le DTO (Request) en Entité pour la base de données
        final Category entity = this.categoryMapper.toEntity(request);
        // Sauvegarde l'entité en base
        this.categoryRepository.save(entity);
    }

    /* Méthode : Mettre à jour une catégorie */

    @Override
    public void update(String id, CategoryRequest request) {
        // Vérifie d'abord si la catégorie qu'on veut modifier existe bien en base
        final Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            log.debug("Category already exists"); // Note: le message devrait plutôt être "Category not found"
            throw new EntityNotFoundException("Category already exists");
        }

        // Vérifie que le nouveau nom n'est pas déjà pris par une autre catégorie
        checkIfCategoryAlreadyExistByName(request.getName());

        // Transforme la requête en entité
        final Category categoryToUpdate = this.categoryMapper.toEntity(request);
        // Force l'ID de l'URL sur l'entité pour que JPA fasse un UPDATE et non un INSERT
        categoryToUpdate.setId(id);
        this.categoryRepository.save(categoryToUpdate);
    }

    /* Méthodes de lecture (Find) */

    @Override
    public PageResponse<CategoryResponse> findAll(int page, int size) {
        // Prépare l'objet de pagination (quelle page, combien d'éléments)
        final PageRequest pageRequest = PageRequest.of(page, size);
        // Récupère une "Page" d'entités depuis la base de données
        final Page<Category> categories = this.categoryRepository.findAll(pageRequest);
        // Transforme chaque entité Category en CategoryResponse via un Mapper
        final Page<CategoryResponse> categoryResponses = categories.map(this.categoryMapper::toResponse);
        // Encapsule le résultat dans un objet personnalisé pour le frontend
        return PageResponse.of(categoryResponses);
    }

    @Override
    public CategoryResponse findById(String id) {
        return categoryRepository.findById(id) // Cherche par ID
                .map(this.categoryMapper::toResponse) // Si trouvé, transforme en Response
                // Si non trouvé, lance une exception gérée par Spring
                .orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
    }

    /* Suppression et Méthode utilitaire */

    @Override
    public void delete(String id) {
        // Vérifie l'existence avant de supprimer
        final Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
        // Supprime physiquement (ou logiquement si Soft Delete activé) l'entité
        this.categoryRepository.delete(category);
    }

    // Méthode interne pour éviter la duplication de code (utilisée dans create et update)
    void checkIfCategoryAlreadyExistByName(String categoryName) {
        final Optional<Category> category = categoryRepository.findByNameIgnoreCase(categoryName);
        if (category.isPresent()) {
            log.debug("Category already exists");
            throw new RuntimeException("Category already exists");
        }
    }
}
