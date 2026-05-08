package com.thierso.sass_app.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Tenant - Itercepte chaque requete HTTP pour identifiier le trnznt.
 *
 * Ce filtre est un point d'entree du mecanisme multi-tenant.
 * Il s'execute avant tous les controleurs et services
 *
 * Strategie d'identification du tenant ( par ordre de priorite )
 * 1. Header "X-Tenant-ID"
 * 2. (Optionnel) Sous-domaine: alpha.stockapp.com -> "alpha"
 *
 * Si aucun tenant n'est identifie -> reponse 400 BAD REQUEST
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String tenantId = resolveTenant(request);
        if (tenantId == null || tenantId.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\": \"Tenant ID is missing in the request header, please add the header X-Tenant-ID\"}"
            );
            return;
        }
        try {

            //Stocker le tenant dans le ThreadLocal
            TenantContext.setCurrentTenant(tenantId);
            // Continuer la chaine de filtre -> controller -> service
            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            // CRITIQUE: toujours nettoyer le ThreadLocal après la requete
            // sans ce clear() le tenant pourrait fuiter vers la requete suivante
            // si le thread est reutilise par le pool de threads du serveur
            TenantContext.clear();
        }
        //filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveTenant(final HttpServletRequest request) {
        final String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId != null && !tenantId.isBlank()) {
            return tenantId.trim().toLowerCase();
        }
        return null;
    }
}

//package com.thierso.sass_app.config;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class TenantFilter implements Filter {
//
//    private static final String TENANT_HEADER = "X-Tenant-ID";
//
//    // Liste des prefixes d'URL qui ne nécessitent pas de Tenant ID
//    private static final List<String> EXCLUDED_URLS = Arrays.asList(
//            "/swagger-ui",
//            "/v3/api-docs",
//            "/swagger-resources",
//            "/webjars",
//            "/favicon.ico",
//            "/health",   // Utile pour Docker/Kubernetes
//            "/info"
//    );
//
//    @Override
//    public void doFilter(
//            ServletRequest servletRequest,
//            ServletResponse servletResponse,
//            FilterChain filterChain) throws IOException, ServletException {
//
//        final HttpServletRequest request = (HttpServletRequest) servletRequest;
//        final HttpServletResponse response = (HttpServletResponse) servletResponse;
//        final String path = request.getRequestURI();
//
//        // 1. Vérifier si l'URL est dans la liste des exclusions
//        boolean isExcluded = EXCLUDED_URLS.stream().anyMatch(path::startsWith);
//
//        if (isExcluded) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//
//        // 2. Logique de résolution du Tenant
//        final String tenantId = resolveTenant(request);
//
//        if (tenantId == null || tenantId.isBlank()) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(
//                    "{\"error\": \"Tenant ID is missing. Please add the header: " + TENANT_HEADER + "\"}"
//            );
//            return;
//        }
//
//        try {
//            // 3. Stockage et exécution
//            TenantContext.setCurrentTenant(tenantId);
//            filterChain.doFilter(servletRequest, servletResponse);
//        } finally {
//            // 4. Nettoyage crucial
//            TenantContext.clear();
//        }
//    }
//
//    private String resolveTenant(final HttpServletRequest request) {
//        final String tenantId = request.getHeader(TENANT_HEADER);
//        if (tenantId != null && !tenantId.isBlank()) {
//            return tenantId.trim().toLowerCase();
//        }
//        return null;
//    }
//}
