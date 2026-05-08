package com.thierso.sass_app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "stock_mvts")
@Filter(name = "tenantFilter")
public class StockMvt extends AbstractEntity{

    @Column(name = "type_mvt", nullable = false) // Colonne obligatoire
    @Enumerated(EnumType.STRING) // Stocke le nom de l'énumération en texte (ex: "ENTREE") au lieu d'un chiffre
    private TypeMvt typeMvt;

    @Column(name = "quantity", nullable = false) // Quantité mouvementée, obligatoire
    private Integer quantity;

    @Column(name = "date_mvt", nullable = false) // Date précise du mouvement
    private LocalDate dateMvt;

    // "columnDefinition = TEXT" permet de stocker des chaînes de caractères très longues (plus de 255 caractères)
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @ManyToOne // Plusieurs mouvements de stock peuvent concerner un seul et même produit
    @JoinColumn(name = "product_id") // Crée une clé étrangère (FK) nommée 'product_id' dans la table stock_mvt
    private Product product;

}
