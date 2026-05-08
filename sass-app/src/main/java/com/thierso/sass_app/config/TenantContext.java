package com.thierso.sass_app.config;

/**
 * TenantContext - stocke l'identifiant du tenant courant dans un ThreadLocal
 *
 * Chaque requete HTTP est traitee par un thread dedie;
 *
 * Le threadLocal garantit que le tenant_id est isolé par thread,
 * meme en cas de requete simultanée de tenants differents
 *
 * Flux:
 * 1.TenantFilter extrait le tenant_id de la requete HTTP
 * 2.TenantFilter appelle TenantContext.setCurrentContext(tenant_id)
 * 3.Le code metier ( service, repository ) accede au tenant via TenantContext.getCurrentTenant()
 * 4.TenantFilter appelle TenantContext.clear() après la reponse (nettoyage)
 */

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * Definit l'identifiant du tenant pour le thread courant.
     */
    public static void setCurrentTenant(final String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    /**
     * Recupere l'identifiant du tenant pour le thread courant
     */
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Nettoie le tenant du thread courant
     * IMPORTANT: doit appele dans un bloc finally
     * pour eviter les fuites de memoire (memory leak)
     * et les fuites de données entre requetes HTTP
     */

    public static void clear(){
        CURRENT_TENANT.remove();
    }
}

