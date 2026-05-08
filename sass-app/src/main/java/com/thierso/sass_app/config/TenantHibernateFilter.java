package com.thierso.sass_app.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


/**
 *
 *
 * FONCTIONNEMENT:
 *  1. TenantFilter (HTTP) a deja stocke le tenantId dans TenantContext
 *  2. Cet aspect intercepte tout appel a un Repository
 *  3. Il active le filtre Hibernate "tenantFilter" avec le tenantId courant
 *  4. Hibernate ajoute automatiquement WHERE tenant_id = :tenantId
 *
 * POURQUOI UN ASPECT ?
 *  Sans cet aspect, il faudrait activer le filtre manuellement dans chaque
 *  methode de service. L'aspect le fait automatiquement et de maniere transversale .
 *
 * ALTERNATIVE:
 *  On pourrait aussi utiliser un HandlerInterceptor ou un @EventListener.
 *  L'aspect est plus propre car il s'execute au plus proche de la couche donnees
 */
@Aspect
@Component
public class TenantHibernateFilter {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Intercepte tous les appels au methodes des services
     * dans le package com.thierso.sass_app.services
     *
     * execution(* com.thierso.sass_app.services.*.*(..))
     * * toute methode (*), de toute classe (*), avec tout argument (...)
     * * dans le package com.thierso.sass_app.services
     */
    //@Before("execution(* com.thierso.saas_app.repositories.*Repository.*(..))")
    @Before("execution(* com.thierso.sass_app.services.*.*(..))")
    public void activateTenantFilter() {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId != null) {
            final Session session = this.entityManager.unwrap(Session.class);

            // active le filtre et injecte le parametre tenanId
            session.enableFilter("tenantFilter")
                    .setParameter("tenantId", tenantId);
        }
    }
}
