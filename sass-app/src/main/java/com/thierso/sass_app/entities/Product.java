// Définit l'emplacement de la classe dans l'arborescence du projet
package com.thierso.sass_app.entities;

// Imports des annotations JPA pour la persistance
import jakarta.persistence.*;
// Imports Lombok pour réduire le code répétitif (Boilerplate)
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// Import pour la précision monétaire (obligatoire pour les prix)
import java.math.BigDecimal;
import java.util.List;

@Getter // Génère automatiquement tous les getters (ex: getNom())
@Setter // Génère automatiquement tous les setters (ex: setNom())
@AllArgsConstructor // Génère un constructeur avec tous les champs (y compris ceux de AbstractEntity grâce à SuperBuilder)
@NoArgsConstructor // Génère un constructeur vide sans argument (requis par JPA/Hibernate)
@SuperBuilder // Permet de construire des objets Product en incluant les champs hérités de AbstractEntity
@Entity // Déclare cette classe comme une entité JPA qui sera mappée en base de données
@Table(name = "product") // Précise le nom exact de la table dans la base PostgreSQL
public class Product extends AbstractEntity { // "extends" permet de récupérer l'ID, les dates d'audit et le soft delete

    // Définit la colonne 'name' ; elle est obligatoire (nullable = false)
    @Column(name = "name", nullable = false)
    private String name;

    // Définit la référence ; obligatoire et unique (deux produits ne peuvent avoir la même)
    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    // Utilise le type 'TEXT' en SQL au lieu de 'VARCHAR' pour permettre des descriptions très longues
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Seuil d'alerte pour le stock ; obligatoire (ex: envoyer une notif s'il en reste 5)
    @Column(name = "alert_threshold", nullable = false)
    private Integer alertThreshold;

    // Prix du produit ; on utilise BigDecimal pour éviter les erreurs d'arrondi des nombres flottants
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne // Plusieurs produits peuvent appartenir à une seule catégorie
    @JoinColumn(name = "category_id") // Crée la clé étrangère 'category_id' dans la table product
    private Category category;

    @OneToMany(mappedBy = "product") // Un produit peut avoir plusieurs mouvements de stock
    private List<StockMvt> stockMouvements;
}