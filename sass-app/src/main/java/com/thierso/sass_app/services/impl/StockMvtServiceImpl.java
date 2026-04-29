package com.thierso.sass_app.services.impl; // Localisation du fichier

// Importations des composants Spring, JPA, Lombok et tes propres classes (Mapper, DTO, Repository)
import com.thierso.sass_app.common.PageResponse;
import com.thierso.sass_app.entities.Product;
import com.thierso.sass_app.entities.StockMvt;
import com.thierso.sass_app.mapper.StockMvtMapper;
import com.thierso.sass_app.repositories.ProductRepository;
import com.thierso.sass_app.repositories.StockMvtRepository;
import com.thierso.sass_app.requests.StockMvtRequest;
import com.thierso.sass_app.responses.StockMvtResponse;
import com.thierso.sass_app.services.StockMvtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Enregistre cette classe comme un service Spring (géré par le conteneur)
@RequiredArgsConstructor // Génère un constructeur avec les champs 'final' (Injection de dépendances)
@Slf4j // Permet d'utiliser 'log' pour tracer les événements (debug, info, error)
public class StockMvtServiceImpl implements StockMvtService {

    // Dépendances injectées pour interagir avec la DB et transformer les données
    private final StockMvtRepository stockMvtRepository;
    private final ProductRepository productRepository;
    private final StockMvtMapper stockMvtMapper;

    @Override
    public void create(StockMvtRequest request) {
        // 1. Vérifie obligatoirement que le produit concerné existe bien en base
        checkIfProductExistById(request.getProductId());

        // 2. Convertit le DTO de la requête en entité pour la base de données
        final StockMvt entity = this.stockMvtMapper.toEntity(request);

        // 3. Sauvegarde le nouveau mouvement de stock
        this.stockMvtRepository.save(entity);
    }

    @Override
    public void update(String id, StockMvtRequest request) {
        // 1. Vérifie si le mouvement de stock à modifier existe réellement
        final Optional<StockMvt> stockMvt = this.stockMvtRepository.findById(id);
        if (stockMvt.isEmpty()) {
            log.debug("stockMvt does not exist");
            throw new RuntimeException("stockMvt does not exist");
        }

        // 2. Vérifie si le produit lié (peut-être un nouveau) existe bien
        checkIfProductExistById(request.getProductId());

        // 3. Transformation du DTO en entité
        final StockMvt stockMvtToUpdate = this.stockMvtMapper.toEntity(request);

        // 4. On fixe l'ID manuellement pour forcer Hibernate à faire un UPDATE (pas un INSERT)
        stockMvtToUpdate.setId(id);

        // 5. Enregistre les modifications
        this.stockMvtRepository.save(stockMvtToUpdate);
    }

    @Override
    public PageResponse<StockMvtResponse> findAll(int page, int size) {
        // Prépare la configuration de la pagination (numéro de page et taille)
        final PageRequest pageRequest = PageRequest.of(page, size);

        // Récupère les données paginées depuis la base de données
        final Page<StockMvt> stockMvts = this.stockMvtRepository.findAll(pageRequest);

        // Transforme la page d'entités en une page de DTO de réponse
        final Page<StockMvtResponse> stockMvtResponses = stockMvts.map(this.stockMvtMapper::toResponse);

        // Retourne le format de pagination personnalisé pour ton interface frontend
        return PageResponse.of(stockMvtResponses);
    }

    @Override
    public StockMvtResponse findById(String id) {
        return stockMvtRepository.findById(id) // Recherche le mouvement
                .map(this.stockMvtMapper::toResponse) // S'il existe, transforme-le en réponse
                // Sinon, lance une erreur 404 (EntityNotFound)
                .orElseThrow(() -> new EntityNotFoundException("stockMvt does not exist"));
    }

    @Override
    public void delete(String id) {
        // Vérifie que l'élément existe avant de tenter la suppression
        final StockMvt stockMvt = stockMvtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("stockMvt does not exist"));

        // Supprime l'entrée de la base de données
        this.stockMvtRepository.delete(stockMvt);
    }

    // Méthode privée pour garantir l'intégrité référentielle
    private void checkIfProductExistById(final String productId) {
        final Optional<Product> product = this.productRepository.findById(productId);
        if (product.isEmpty()) {
            log.debug("product does not exist");
            throw new EntityNotFoundException("product does not exist");
        }
    }

}
