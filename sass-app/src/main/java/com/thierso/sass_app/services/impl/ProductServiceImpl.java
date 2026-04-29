package com.thierso.sass_app.services.impl;

import com.thierso.sass_app.common.PageResponse;
import com.thierso.sass_app.entities.Category;
import com.thierso.sass_app.entities.Product;
import com.thierso.sass_app.mapper.ProductMapper;
import com.thierso.sass_app.repositories.CategoryRepository;
import com.thierso.sass_app.repositories.ProductRepository;
import com.thierso.sass_app.requests.ProductRequest;
import com.thierso.sass_app.responses.CategoryResponse;
import com.thierso.sass_app.responses.ProductResponse;
import com.thierso.sass_app.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    @Override
    public void create(ProductRequest request) {

        //Check if product already exist
        checkIfProductAlreadyExistByReference(request.getReference());

        //Check if category already exist
        checkIfCategoryExistById(request.getCategoryId());

        final Product entity = this.productMapper.toEntity(request);
        this.productRepository.save(entity);
    }

    @Override
    public void update(String id, ProductRequest request) {

        // 1. Vérifie si le produit que l'on veut modifier existe
        final Optional<Product> productExists = this.productRepository.findById(id);
        if (productExists.isEmpty()) {
            log.debug("Product does not exist");
            throw new RuntimeException("Product does not exist");
        }

        // 2. Vérifie si la nouvelle référence n'est pas déjà prise par un autre produit
        checkIfProductAlreadyExistByReference(request.getReference());


        // 3. Vérifie si la catégorie rattachée existe toujours
        checkIfCategoryExistById(request.getCategoryId());

        // Transforme la requête en entité, Mapping du DTO vers l'entité
        final Product productToUpdate = this.productMapper.toEntity(request);
        // Très important : on fixe l'ID pour que JPA fasse un "Update" et non un "INSERT"
        productToUpdate.setId(id);
        // Sauvegarde des modifications
        this.productRepository.save(productToUpdate);

    }

    @Override
    public PageResponse<ProductResponse> findAll(int page, int size) {
        // Prépare l'objet de pagination (quelle page, combien d'éléments)
        final PageRequest pageRequest = PageRequest.of(page, size);
        // Récupère une "Page" d'entités depuis la base de données
        final Page<Product> products = this.productRepository.findAll(pageRequest);
        // Transforme chaque entité Product en ProductResponse via un Mapper
        final Page<ProductResponse> productResponses = products.map(this.productMapper::toResponse);
        // Encapsule le résultat dans un objet personnalisé pour le frontend
        return PageResponse.of(productResponses);
    }

    @Override
    public ProductResponse findById(String id) {
        return productRepository.findById(id) // Cherche par ID
                .map(this.productMapper::toResponse) // Si trouvé, transforme en Response
                // Si non trouvé, lance une exception gérée par Spring
                .orElseThrow(() -> new EntityNotFoundException("product does not exist"));
    }

    @Override
    public void delete(String id) {
        // Vérifie l'existence avant de supprimer
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("product does not exist"));
        // Supprime physiquement (ou logiquement si Soft Delete activé) l'entité
        this.productRepository.delete(product);
    }

    // Vérifie l'unicité de la référence technique du produit
    private void checkIfProductAlreadyExistByReference(final String reference) {
        final Optional<Product> product = this.productRepository.findByReferenceIgnoreCase(reference);
        if (product.isPresent()) {
            log.debug("Product already exists");
            throw new RuntimeException("Product already exists");
        }
    }

    // Vérifie l'existence de la catégorie parente (contrainte d'intégrité)
    private void checkIfCategoryExistById(final String categoryId) {
        final Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            log.debug("category does not exist");
            throw new EntityNotFoundException("Category does not exist");
        }
    }
}
