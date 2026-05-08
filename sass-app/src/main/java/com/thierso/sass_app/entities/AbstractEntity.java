package com.thierso.sass_app.entities;


import com.thierso.sass_app.config.TenantContext;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Getter // Génère automatiquement les méthodes pour lire les champs (getId(), etc.)
@Setter // Génère automatiquement les méthodes pour modifier les champs (setId(), etc.)
@SuperBuilder // Permet d'utiliser le pattern Builder même avec l'héritage (indispensable ici)
@NoArgsConstructor // Génère un constructeur sans argument (requis par JPA/Hibernate)
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@MappedSuperclass // Indique que cette classe est un parent : ses colonnes seront dans les tables des enfants
@EntityListeners(AuditingEntityListener.class) // Active l'écouteur Spring Data pour remplir les dates/utilisateurs
@FilterDef(
        name = "tenantFilter",
        parameters = @ParamDef(name = "tenantId", type = String.class),
        defaultCondition = "tenant_id = :tenantId"
)
@Filter(name = "tenantFilter")
public class AbstractEntity {


    @Id // Définit la clé primaire
    @GeneratedValue(strategy = UUID) // Utilise un identifiant unique universel (UUID) au lieu d'un nombre (1, 2, 3...)
    @Column(name ="id", updatable = false, nullable = false) // Colonne non modifiable et obligatoire
    private String id;

    @CreatedDate // Remplit automatiquement la date au moment de l'insertion (INSERT)
    @Column(name ="created_at", updatable = false, nullable = false) // Colonne non modifiable
    private LocalDateTime createdAt;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @LastModifiedDate // Met à jour automatiquement la date à chaque modification (UPDATE)
    @Column(name ="update_at", insertable = false) // 'insertable=false' car inutile lors de la création
    private LocalDateTime updateAt;

    @CreatedBy // Enregistre l'utilisateur (username/ID) qui a créé l'enregistrement
    @Column(name ="created_by", updatable = false, nullable = false)
    private String createdBy;

    @LastModifiedBy // Enregistre l'utilisateur qui a fait la dernière modification
    @Column(name ="update_by", insertable = false)
    private String updatedBy;

    @Column(name ="deleted", nullable = false) // Utilisé pour le "Soft Delete" (ne pas supprimer physiquement la ligne)
    private Boolean deleted;

    @PrePersist // S'exécute juste avant que l'objet ne soit enregistré pour la première fois en base
    protected void onCreate() {
        // Initialisation par défaut du champ 'deleted' à FALSE si aucune valeur n'est fournie
        if (this.deleted == null) {
            this.deleted = Boolean.FALSE;
        }
        //todo: this has to be deleted once security is implemented
        if(this.createdBy == null) {
            this.createdBy = "SYSTEM";
        }

        if (this.tenantId == null) {
            this.tenantId = TenantContext.getCurrentTenant();
        }
    }


}
